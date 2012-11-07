package npx.cisco.handlers;

import npx.cisco.parser.CiscoConfig;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Luis Dias Costa
 */
public abstract class AbstractCfgHandler implements CfgHandler {

    /** Cisco IOS command name this handler handles */
    private String command = null;
    /** Cisco IOS config to work with */
    protected CiscoConfig config = null;
    protected CfgHandlerContext context = null;

    @Override
    public CfgHandlerContext call(CfgHandlerContext context, CiscoConfig config) {

        this.config = config;
        this.context = context;

        if (command == null) {
            logger.log(Level.ERROR, "command not set");
            return null;
        }

        return context;

    }

    @Override
    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String getCommand() {
        return command;
    }

    protected Logger logger = Logger.getLogger(this.getClass());

    @Override
    public abstract CfgHandlerContext parseConfig();
}
