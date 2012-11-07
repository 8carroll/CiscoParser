package npx.cisco.parser;

import java.io.*;
import java.util.*;
import npx.cisco.handlers.*;
import npx.netmodel.config.PassiveConfig;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @TODO the parser is working in its most basic mode, allowing some parts of
 * the command tree to be parsed by user provided handlers. I need to refactor
 * the Cisco IOS Command handler and its yaml loader, cleaning up the code from
 * the handler... see details in this class directly.
 *
 * @author Luis Dias Costa
 */
public class CiscoParser {

    private String iosFileFilter = ".ios";
    // Signals a multi/single-line config (PIX and others) may be configured using single-line configuration commands
    private boolean singleLine = false;
    // Section handler registry (used by 'parser' method) (sorted by insert order! IMPORTANT)
    private LinkedHashMap<String, CfgHandler> sectionHandlers;
    // handler of the valid and ignored cisco commands' trees
    private CiscoCommandHandler ciscoCommandHandler;
    /**
     * configuration filename
     */
//    private String filename;

    public CiscoParser() {
        this.ciscoCommandHandler = new CiscoCommandHandler();
        InputStream is1 = getClass().getResourceAsStream("cisco.yaml");
        ciscoCommandHandler.loadCommandsFromYaml(is1);
        InputStream is2 = getClass().getResourceAsStream("port-numbers.yaml");
        ciscoCommandHandler.loadPortNumbersFromYaml(is2);
    }

    public CiscoParser(CiscoCommandHandler ciscoCommandHandler) {
        this.ciscoCommandHandler = ciscoCommandHandler;
    }

    public PassiveConfig parse(String filename) {

        if (!nullOrEmpty(filename)) {
            try {
                File f = new File(filename);
                FileInputStream fis;
                fis = new FileInputStream(f);
                DataInputStream din = new DataInputStream(fis);
                return parse(new BufferedReader(
                        new InputStreamReader(din)));
            } catch (FileNotFoundException ex) {
                logger.log(Level.FATAL, ex.getMessage());
            }
        }
        return null;

    }

    public PassiveConfig parse(BufferedReader br) {


        // check this instance config
        if (ciscoCommandHandler == null) {
            logger.log(Level.FATAL, "Configuration error, cisco command handler was not properly loaded.");
        }

        // holds all the parsed configuration lines in CiscoConfig instances
        CiscoConfig curRootConfig = null; // holds the current 'root' configuration section e.g. 'interface ethernet0' root=interface
        /**
         * parsed and sorted according to cisco command
         */
        TreeMap<String, ArrayList<CiscoConfig>> sortedConfigMap =
                new TreeMap<String, ArrayList<CiscoConfig>>();

        try {
            String line; // line being parsed from configuration file
            //String previousCommand)=null; // last parsed token parsed i.e. first token from last line being parsed
            String previousRootCommand = null; // last parsed token parsed i.e. first token from last line being parsed

            // signals an ignored configuration portion
            boolean ignore = true;

            while ((line = br.readLine()) != null) {

                /*
                 * get the top root command or subcommand candidate from the
                 * list of valid commands it is a candidate since some
                 * root-commands may repeat themselves as sub-commands. eg:
                 * 'access' is a root command but may also appear as a
                 * subcommand of 'policy-map'
                 */
                String commandCandidate = ciscoCommandHandler.getCommandString(line);
                logger.log(Level.TRACE, "Current configuration line=" + line + ", command candidate=" + commandCandidate + ", previous root=" + previousRootCommand);

                if (!handledAsSpecialCase(br, line)
                        && hasContent(line)
                        && !nullOrEmpty(commandCandidate)) {
                    // current configuration line is not special (ie. does not require a specific parsing expression
                    // the command line string has parsable content and
                    // commandCandidate is a valid string


                    // now, check if the current command candidate is in fact a
                    // subcommand of the root command found in the previous line
                    //logger.log(Level.TRACE,"prev.root.cmd=" + previousRootCommand + " prev.cmd=" + previousCommand + " cmd.cand=" + commandCandidate);
                    if (!singleLine && ciscoCommandHandler.isSubCommandOfRootCommand(previousRootCommand, commandCandidate)) {
                        // found a subcommand of a root command
                        // create a new config for the just found root command and save it

                        if (!ignore) {
                            // current top cmd is not in the ignore list
                            if (curRootConfig != null) {
                                logger.log(Level.TRACE, "adding subcommand " + commandCandidate + " to " + curRootConfig.getCommandName());
                                Collection<String> cfg = ciscoCommandHandler.getCommmandConfiguration(commandCandidate, line);
                                CiscoConfig subCmdCfg = new CiscoConfig(commandCandidate, cfg);
                                curRootConfig.addSubCmdCfg(subCmdCfg);
                            } else {
                                logger.log(Level.ERROR, "Found a subcommand at a wrong parsing stage. Possible bug. Please contact administrator.");
                            }
                        } else {
                            logger.log(Level.TRACE, "\tignoring: " + line);
                        }
                        //previousCommand = commandCandidate;
                    } else {


                        // check if the command candidate is a root command (it can get here if with a subcommand is provided incorrectly by the configuration)
                        String rootCmdCandidate = ciscoCommandHandler.getRootCommandString(line);
                        if (!nullOrEmpty(rootCmdCandidate)) {
                            // found a fresh root command (rootCmdCandidate should be the same as commandCandidate)

                            // check if the root cmd is to be ignored
                            if (ignored(rootCmdCandidate)) {
                                ignore = true;
                                logger.log(Level.TRACE, "ignoring: " + line);
                            } else {
                                ignore = false;

                                // add a previously parsed configuration
                                if (curRootConfig != null) {
                                    addToSortedConfig(sortedConfigMap, curRootConfig);
                                }

                                // create a new config for the just found root command
                                Collection<String> cfg = ciscoCommandHandler.getCommmandConfiguration(commandCandidate, line);
                                curRootConfig = new CiscoConfig(commandCandidate, cfg);


                            }
                            // the candidate command is in fact a root command so,
                            // save it for the next parsing cycle
                            //previousCommand = commandCandidate;
                            previousRootCommand = rootCmdCandidate;
                        } else {
                            logger.log(Level.TRACE, "Ignoring configuration line: '" + line + "'. Unexpected command '" + commandCandidate + "' found at his stage.");
                        }
                    }
                } else {
                    logger.log(Level.TRACE, "Ignoring configuration line: '" + line + "'.");
                }

            } // end while
        } catch (IOException ex) {
            logger.log(Level.FATAL, null, ex);
        }
        // add a previously parsed configuration from the last line

        if (curRootConfig != null) {
            addToSortedConfig(sortedConfigMap, curRootConfig);
        }

        //logger.log(Level.TRACE, "parsed config (from sortedConfigMap)=" + sortedConfigMap);

        return handleParsedSections(sortedConfigMap);
    }

    private PassiveConfig handleParsedSections(TreeMap<String, ArrayList<CiscoConfig>> sortedConfig) {
        CfgHandlerContext context = new CfgHandlerContext();


        // parsing complete, call proper section handlers (if any)

        if (sortedConfig == null) {
            logger.log(Level.FATAL, "Configuration error: no configuration to parse");
        } else {
            if (sectionHandlers == null) {
                logger.log(Level.WARN, "Configuration warning: section handlers not provided. Using default (DEMO) handlers.");
                // config specific configuration section handling 
                // (eg: handle interface config sections) 
                // order matters!!!
                sectionHandlers = new LinkedHashMap<String, CfgHandler>();

                sectionHandlers.put("version", new CfgHandler_CiscoType()); // for ios 
                sectionHandlers.put("FWSM Version", new CfgHandler_CiscoType()); // for FWSMs (new and old)
                sectionHandlers.put("PIX Version", new CfgHandler_CiscoType()); // for PIXes

                sectionHandlers.put("hostname", new CfgHandler_Hostname());
                sectionHandlers.put("interface", new CfgHandler_Interface());
                sectionHandlers.put("nameif", new CfgHandler_nameif());
                sectionHandlers.put("ip address", new CfgHandler_IPAddress()); // for FWSM and PIX
                sectionHandlers.put("access-list", new CfgHandler_accesslist());
                //sectionHandlers.put("nat-control", new CfgHandler_nat());
                //sectionHandlers.put("nat", new CfgHandler_nat());
            }

            logger.log(Level.TRACE, "############################## Calling handlers: ");

            Set<String> commandHandlerkeys = sectionHandlers.keySet();  // keys for registered handlers

            // create a context for the host having its config parsed
            context = new CfgHandlerContext();

            // call handlers for each config section according to provided order
            for (String handlerKey : commandHandlerkeys) {
                logger.log(Level.TRACE, "calling handler for " + handlerKey);

                ArrayList<CiscoConfig> configs = sortedConfig.get(handlerKey);

                if (configs != null) {
                    for (CiscoConfig cfg : configs) {
                        if (cfg.getCommandName().equalsIgnoreCase(handlerKey)) {
                            CfgHandler sh = sectionHandlers.get(handlerKey);

                            context = sh.call(context, cfg);
                        }
                    }
                }
            }
        }


        return context.getPassiveConfig();

    }

    private void addToSortedConfig(TreeMap<String, ArrayList<CiscoConfig>> sortedConfig, CiscoConfig config) {
        String key = config.getCommandName();
        if (config == null || key == null || config.getConfig() == null) {
            logger.log(Level.WARN, "got a null config while trying to insert to sorted config");
            return;
        }
        if (sortedConfig == null) {
            sortedConfig = new TreeMap<String, ArrayList<CiscoConfig>>();
        }

        if (sortedConfig.containsKey(key)) {
            ArrayList<CiscoConfig> cfg = sortedConfig.get(key);
            if (cfg == null) {
                cfg = new ArrayList<CiscoConfig>();
            }
            cfg.add(config);
            sortedConfig.put(key, cfg);
        } else {
            ArrayList<CiscoConfig> cfg = new ArrayList<CiscoConfig>();
            cfg.add(config);
            sortedConfig.put(key, cfg);
        }
    }

    private boolean handledAsSpecialCase(BufferedReader br, String line) {

        if (line == null || line.isEmpty()) {
            return false;
        }

        // get current command candidate
        String curCandidate = ciscoCommandHandler.getRootCommandString(line);

        if (line.matches("^PIX Version.*") || line.matches("^FWSM Version 2\\.3.*")) {
            // try to check the Cisco OS version in order to change how the parser works
            // (most are multiline configs but PIX (and others?) have single-line configs
            // defaults to multiline...
            logger.log(Level.DEBUG, "found a PIX or old FWSM OS, going single line parser");
            singleLine = true;
        } else if (line.matches("^FWSM Version.*") || line.matches("^version.*")) {
            // try to check the Cisco OS version in order to change how the parser works
            // (most are multiline configs but PIX (and others?) have single-line configs
            // defaults to multiline...
            singleLine = false;
        } else if (line.matches("^Cryptochecksum:.*")) {
            // special treatment for cryptosum config line (its different from the others...)
            line.replaceAll("^Cryptochecksum:", "");
            logger.log(Level.TRACE, "Special case: Cryptochecksum");
            return true;

        } else if (curCandidate == null
                && line.matches("^\\!.*$")) {// comment line
			/*
             * some commands start with '!' so we check for null to verify first
             * if the apparent comment is in fact a commmand, when it is, the
             * next if branch parses it just like any other command
             */

            logger.log(Level.TRACE, "Special case: comment");
            return true;
        } else if (curCandidate != null
                && curCandidate.matches("^banner.*")) {
            // consume the banner i.e. ignore it @TODO change this if you need the banner contents
            ciscoCommandHandler.consumeBanner(br, line);
            return true;
        }

        return false;
    }

    public void setSectionHandlers(LinkedHashMap<String, CfgHandler> sectionHandlers) {
        this.sectionHandlers = sectionHandlers;
    }

    public void addSectionHandler(String s, CfgHandler c) {
        if (this.sectionHandlers == null) {
            sectionHandlers = new LinkedHashMap<String, CfgHandler>();
        }

        sectionHandlers.put(s, c);
    }

    /*
     * checks if a string has content to parse. i.e. checks if string has more
     * than spaces and tabs, etc.
     */
    private boolean hasContent(String line) {
        if (line == null || line.isEmpty()) {
            return false;
        }
        return !line.matches("^\\s*$");
    }

    /**
     * checks if current command candidate is to be ignored
     */
    private boolean ignored(String commandCandidate) {
        return ciscoCommandHandler.isIgnored(commandCandidate);
    }

    private boolean nullOrEmpty(String s) {
        return (s == null ? true : s.isEmpty());
    }

    public void setFileFilter(String fileFilter) {
        this.iosFileFilter = fileFilter;
    }


    class IOSFileFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return (name.endsWith(iosFileFilter));
        }
    }

    public ArrayList<PassiveConfig> parseDir(String dataDir, String iosFileFilter) {
        ArrayList<PassiveConfig> configs = new ArrayList<PassiveConfig>();

        // parse the configuration files
        File dir = new File(dataDir);

        FilenameFilter filter = new IOSFileFilter();

        String[] ls = dir.list(filter);

        for (String file : ls) {
            if (file != null) {
                long past = System.currentTimeMillis();

                logger.log(Level.TRACE, "--");
                logger.log(Level.TRACE, "About to parse: " + dir + "/" + file);

                configs.add(parse(dir + "/" + file));

                long present = System.currentTimeMillis();
                logger.log(Level.TRACE, "Parsed "
                        + dir + "/" + file
                        + " configuration file in "
                        + Long.toString(present - past) + "ms");

            }
        }

        logger.log(Level.DEBUG, "Done.");
        return configs;
    }
    private final Logger logger = Logger.getLogger(this.getClass());
}
