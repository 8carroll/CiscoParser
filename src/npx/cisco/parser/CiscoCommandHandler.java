package npx.cisco.parser;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

/**
 * Handles the tree of Cisco Commands composed by objects of class CiscoCommand.
 *
 * @see npx.cisco.parser.CiscoCommand
 * 
 * @author Luis Dias Costa
 */
public class CiscoCommandHandler {

    // cisco token trees
    private CiscoCommand commands;
    private CiscoCommand ignoredCommands;
    private PortNumbers portNumbers;

    /**
     * Check if token is one of the loaded parent commands. A root command
     * appears first i.e. in the first column of the provided commands file.
     * Other commands are children i.e. they are subcommands of the root token.
     * e.g. interface ethernet1/0 ip address 10.10.1.1 255.255.255.0
     *
     *
     * 'interface' is the top-most parent token i.e. child of [root]; 'ip
     * address' is a child of 'interface'
     */
    private boolean isRootCommand(String candidate) {
        return isRootCommand(candidate, commands) || isRootCommand(candidate, ignoredCommands)  ;
    }

    private boolean isRootCommand(String candidate, CiscoCommand cmdTree) {

        if (cmdTree == null) {
            logger.error("command list is null");
            return false;
        }

        // check if current CiscoToken is the root
        if (cmdTree.getCommandName().compareTo("[root]") != 0) {
            return false;
        }

        return cmdTree.containsChild(candidate);
    }

    /**
     * Checks if the provided candidate is a root command to be ignored.
     */
    public boolean isIgnored(String candidate) {
        return isRootCommand(candidate, ignoredCommands);
    }

    /**
     * Return a root command string if there is a match with the pre-defined ios
     * command list. a command may have several string tokens e.g. 'ip vrf'.
     * this method returns the longest match for a command from the provided
     * StringTokenizer. there is a fixed limit (see code) on the number of
     * tokens that a command may have. (to improve speed...)
     */
    public String getRootCommandString(String configLine) {

        if (commands == null || ignoredCommands == null) {
            logger.warn("Unable to get top level command. No token list was provided.");
            return null;
        }

        StringTokenizer st = new StringTokenizer(configLine);

        if (st.countTokens() == 0) {
            return null;
        }

        String t = "";
        // top-level command candidate
        String candidate = null;
        int i = 0;

        while (st.hasMoreTokens() && i < 5) { // try longer commands, e.g 'ip vrf', etc
            t += st.nextToken();

            // save all the matches. the last one is the longest match
            if (isRootCommand(t)) {
                candidate = t;
            }

            t += " "; // restore the space between tokens in the original line string
            i++;
        }

        return candidate;
    }

    /**
     * Returns a command string if there is a match with the pre-defined ios
     * command list. a command may have several string tokens e.g. 'ip vrf'.
     * this method returns the longest match for a command from the provided
     * StringTokenizer. there is a fixed limit (see code) on the number of
     * tokens that a command may have. (to improve speed...)
     */
    public String getCommandString(String configLine) {

        if (commands == null) {
            logger.warn("Unable to get command. No command list was provided.");
            return null;
        }

        StringTokenizer st = new StringTokenizer(configLine);

        if (st.countTokens() == 0) {
            return null;
        }

        String t = "";
        // top-level command candidate
        String candidate = null;
        int i = 0;

        while (st.hasMoreTokens() && i < 5) { // try longer commands, e.g 'ip vrf', etc
            t += st.nextToken();

            // save all the matches. the last one is the longest match
            if (existsCommand(t)) {
                candidate = t;
            }

            t += " "; // restore the space between tokens in the original line string
            i++;
        }

        return candidate;
    }

    /*
     * get config tokens from provided command OR subcommand
     */
    public ArrayList<String> getCommmandConfiguration(String command, String cfgLine) {

        if (command == null || cfgLine == null) {
            return null;
        }

        ArrayList<String> list = new ArrayList<String>();

        StringTokenizer cfgTokens = new StringTokenizer(cfgLine);

        if (cfgTokens.countTokens() < 2) { // subcommand itself alone
            return null;
        }

        /*
         * get to the config tokens, ignoring the command that repeats itself in
         * cfgLine e.g 'ip route' somestring bla bla bla
         */


        StringTokenizer cmdTokens = new StringTokenizer(command);

        // the command in cfgLine has to be bigger to have a config in it
        if (cfgTokens.countTokens() < cmdTokens.countTokens()) {
            return null;
        }

        // compare only the command part and check if they are the same
        int tks = cmdTokens.countTokens();
        for (int i = 0; i < tks; i++) {

            String cmd = cmdTokens.nextToken();
            String cfg = cfgTokens.nextToken();

            if (!cmd.equalsIgnoreCase(cfg)) {
                return null;
            }
        }

        // the rest of the tokens are the configuration itself
        while (cfgTokens.hasMoreTokens()) {
            list.add(cfgTokens.nextToken());
        }

        return list;

    }

    /**
     * Checks if subCommand is a descendant of the rootCommand
     *
     * @param subCommand
     * @param rootCommand
     */
    public boolean isSubCommandOfRootCommand(String rootCommand, String subCommand) {

        if (nullOrEmpty(rootCommand) || nullOrEmpty(subCommand)) {
            return false;
        }

        String rootCmdStr = getRootCommandString(rootCommand);

        CiscoCommand parent = commands.getRootCommand(rootCmdStr);
        if (parent == null) {
            parent = ignoredCommands.getRootCommand(rootCmdStr);
        }
        if (parent != null) {
            // the provided rootCommand String is in fact a root command
            return parent.containsChild(subCommand);
        }

        return false;
    }

    public void setIgnoredCommands(CiscoCommand ignoredCommands) {
        this.ignoredCommands = ignoredCommands;
    }

    public void setCommands(CiscoCommand commands) {
        this.commands = commands;
    }

    private boolean nullOrEmpty(String s) {
        return (s == null ? true : s.isEmpty());
    }
    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * check if command exists in commands and ignored commands list
     */
    public boolean existsCommand(String t) {
        return existsCommand(commands.getChildren(), t) || existsCommand(ignoredCommands.getChildren(), t);
    }

    private boolean existsCommand(ArrayList<CiscoCommand> children, String t) {

        if (children == null || children.isEmpty()) {
            return false;
        }

        for (int i = 0; i < children.size(); i++) {

            CiscoCommand child = children.get(i);

            if (child.getCommandName().equals(t)) {
                return true;
            }

            ArrayList<CiscoCommand> subchildren = child.getChildren();

            if (subchildren != null && !subchildren.isEmpty()) {
                if (existsCommand(subchildren, t)) {
                    return true;
                }
            }

        }

        return false;
    }

    public void consumeBanner(BufferedReader br, String curCfgLine) {

        logger.trace("Consuming a banner");

        if (br == null) {
            return;
        }
        if (curCfgLine == null || curCfgLine.isEmpty()) {
            return;
        } // banner [other string] <delimeter> string <delimeter>
        // the current line was already read before (curCfgLine)
        StringTokenizer tz = new StringTokenizer(curCfgLine);
        if (tz.countTokens() == 0) {
            return;
        }
        String banner = tz.nextToken();
        if (!banner.equalsIgnoreCase("banner")) {
            return;
        }
        if (!tz.hasMoreTokens()) {
            return;
        }
        String bannerType = tz.nextToken();
        String delimiter = "";
        if (!tz.hasMoreTokens()) {
            return;
        }
        if (bannerType.equalsIgnoreCase("exec") || bannerType.equalsIgnoreCase("incoming")
                || bannerType.equalsIgnoreCase("login") || bannerType.equalsIgnoreCase("motd")
                || bannerType.equalsIgnoreCase("slip-ppp")) {
            delimiter = tz.nextToken();
        } else {
            // this is a "simple" banner command, i.e no bannerType is specified, so this token is
            // actually the delimiter of the following string
            delimiter = bannerType;
        }
        // cosume all the tokens from the tokenizer, trying to find the end of the string
        while (tz.hasMoreTokens()) {
            if (tz.nextToken().equalsIgnoreCase(delimiter)) {
                // done consuming!
                return;
            }
        }


        try {
            // here we have consumed all the tokens from the current line, next we will consume
            // char-by-char to find the end of the banner string (if it is multiline)
            int ch = 0;
            boolean done = false;
            String token = "";
            // read char-by-char until reaching the eof or the other delimiter was found
            for (ch = br.read(); ch != -1 && !done; ch = br.read()) {
                char c = (char) ch;

                if (c == ' ' || c == '\n' || c == '\t' || c == '\r') { // a space, newline etc.
                    if (token.equalsIgnoreCase(delimiter)) {
                        return;
                    }
                    token = "";
                } else {
                    token += c;
                }
            }
        } catch (IOException ex) {
            logger.error("IO Exception while consuming banner");
        }
    }

    public void loadCommandsFromYaml(String filename) {
        final Yaml yaml = new Yaml();
        Reader reader = null;
        try {
            reader = new FileReader(filename);

            ArrayList<Object> docs = new ArrayList<Object>();
            for (Object data : yaml.loadAll(reader)) {
                docs.add(data);
            }

            if (docs.size() > 2) {
                logger.error("Only 2 documents are allowed in yaml file!");
                return;
            }

            commands = (CiscoCommand) docs.get(0);
            if (docs.size() == 2) {
                ignoredCommands = (CiscoCommand) docs.get(1);
            }

        } catch (FileNotFoundException ex) {
            logger.error(ex);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (final IOException ex) {
                    logger.error(ex);
                }
            }
        }
    }

    public void loadCommandsFromYaml(InputStream is) {
        final Yaml yaml = new Yaml();

        ArrayList<Object> docs = new ArrayList<Object>();
        for (Object data : yaml.loadAll(is)) {
            docs.add(data);
        }

        if (docs.size() > 2) {
            logger.error("Only 2 documents are allowed in yaml file!");
            return;
        }

        commands = (CiscoCommand) docs.get(0);
        if (docs.size() == 2) {
            ignoredCommands = (CiscoCommand) docs.get(1);
        }

    }

    public void loadPortNumbersFromYaml(String filename) {
        final Yaml yaml = new Yaml();
        Reader reader = null;
        try {
            reader = new FileReader(filename);

            ArrayList<Object> docs = new ArrayList<Object>();
            for (Object data : yaml.loadAll(reader)) {
                docs.add(data);
            }

            if (docs.size() > 1) {
                logger.error("Only 1 documents is allowed in yaml file!");
                return;
            }

            portNumbers = (PortNumbers) docs.get(0);

        } catch (FileNotFoundException ex) {
            logger.error(ex);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (final IOException ex) {
                    logger.error(ex);
                }
            }
        }
    }

    public void loadPortNumbersFromYaml(InputStream is) {
        final Yaml yaml = new Yaml();
        ArrayList<Object> docs = new ArrayList<Object>();
        for (Object data : yaml.loadAll(is)) {
            docs.add(data);
        }

        if (docs.size() > 1) {
            logger.error("Only 1 document is allowed in this yaml file!");
            return;
        }

        portNumbers = (PortNumbers) docs.get(0);

    }
}
