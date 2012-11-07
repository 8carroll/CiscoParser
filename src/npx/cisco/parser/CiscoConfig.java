package npx.cisco.parser;

import java.util.ArrayList;
import java.util.Collection;

/**
 * class to hold parsed commands and sub-commands from a cisco device
 * 
 * @author Luis Dias Costa
 */
public class CiscoConfig {

    /** Cisco IOS command e.g. 'interface' in 'interface ethernet1/0' configuration line */
    private String command; // cisco ios command or subcommand name i.e. 'interface' in this example
    /** Collection: Command configurations e.g. 'address', '10.1.1.10','255.255.255.0' in 'ip address 10.1.1.10 255.255.255.0' configuration line */
    private Collection<String> configuration; // collection helds a configuration line, with the exception of the first command or subcommand which is held in 'commandName'
    /** Recursive subcommands of the same class type */
    private Collection<CiscoConfig> subCommands = null; // collection of configuration lines or 'commands', null by default
    /** configuration line type i.e. if it is a comment, a conventional line, etc. note that some
     * special comments have also configurations... so an handler may be used on comments
     */
    private int commandType = CISCO_IOS_CONFIG;
    /** a conventional command, which may be composed of several sub-commands */
    public static final int CISCO_IOS_CONFIG = 0;
    /** a conventional command, which may be composed of several unknown sub-commands */
    public static final int CISCO_IOS_UNKNOWN_CONFIG = 10;
    /** special configuration line with a cisco IOS comment */
    public static final int CISCO_IOS_COMMENT = 20;
    /** special configuration line */
    public static final int CISCO_IOS_CRYPTOCHECKSUM = 30;
    /** 'no' followed by an IOS command i.e. a configuration negation */
    public static final int CISCO_IOS_CONFIG_NEGATION = 40;

    public CiscoConfig(String commandName, Collection<String> commandConfig) {
        this.command = commandName;
        this.configuration = commandConfig;
    }

    public void addSubCmdCfg(CiscoConfig subCommand) {
        if (subCommands == null) {
            subCommands = new ArrayList<CiscoConfig>();
        }

        subCommands.add(subCommand);
    }

    public CiscoConfig addSubCmdCfg(String subCommandName, Collection<String> subCommandConfig) {
        if (subCommands == null) {
            subCommands = new ArrayList<CiscoConfig>();
        }

        CiscoConfig subCommand = new CiscoConfig(subCommandName, subCommandConfig);

        subCommands.add(subCommand);

        return subCommand;
    }

    public boolean hasSubCommand() {
        return (subCommands == null) ? false : true;
    }

    /* @TODO refactor to return a collection (some subcommands may repeat themselves!)
     * refactor to return a collection (some subcommands may repeat themselves!)
     * for now, we only have a level of subcommands!!!! But this will be changed!!!
     */
    public CiscoConfig getSubCommand(String name) {

        if (subCommands == null) {
            return null;
        }

        for (CiscoConfig command : subCommands) {
//            System.out.println("command name=" + command.getCommandName() + " searching for=" + name);
            if (command.getCommandName().equalsIgnoreCase(name)) {
                return command;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return toStr("");
    }

    private String toStr(String tabs) {
        String out = "";

        out += tabs + "command name=" + command + " cfg=";

        if (configuration != null) {
            for (String cfg : configuration) {
                if (cfg == null) {
                    out += "(null) ";
                } else {
                    out += cfg + " ";
                }
            }
            out += "\n";
        } else {
            out += "(null)\n";
        }

        if (subCommands != null) {
            out += toStr(subCommands, tabs + "\t");
        }

        return out;
    }

    private String toStr(Collection<CiscoConfig> ss, String tabs) {
        String out = "";

        for (CiscoConfig s : ss) {
            out += s.toStr(tabs);
        }

        return out;
    }

    /**
     * sets the configuration type 
     * @param type CISCO_IOS_CONFIG, CISCO_IOS_UNKNOWN_CONFIG, CISCO_IOS_COMMENT, CISCO_IOS_CRYPTOCHECKSUM, CISCO_IOS_CONFIG_NEGATION
     */
    public void setType(int type) {
        this.commandType = type;
    }

    public int getType() {
        return this.commandType;
    }

    public String getCommandName() {
        return command;
    }


    public Collection<String> getCommandConfig() {
        return configuration;
    }

    /** alias for getCommandConfig */
    public Collection<String> getConfig() {
        return configuration;
    }
}
