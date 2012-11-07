package npx.cisco.handlers;

import npx.cisco.parser.CiscoConfig;
import java.util.ArrayList;

/**
 *
 * @author Luis Dias Costa
 */
public class CfgHandler_Hostname extends AbstractCfgHandler {

    public CfgHandler_Hostname() {
        super();
        setCommand("hostname");
    }

    @Override
    public CfgHandlerContext call(CfgHandlerContext context, CiscoConfig config) {
        super.call(context, config);


        if (config.getConfig().size() > 1) {
            logger.warn("odd hostname config.");
            return null;
        }

        // configuration section validation is ok. it is now more safe to 
        // continue the actual parsing for this section and create the proper
        // model
        return parseConfig();
    }

    @Override
    public CfgHandlerContext parseConfig() {

        ArrayList<String> configs = new ArrayList<String>(config.getConfig());

        if (configs.size()==0) {
            logger.trace("hostname config section is null. ignoring config");
            return null;
        }
        if (configs.size() > 1) {
            logger.trace("hostname config section has the wrong size. ignoring config");
            return null;
        }

        String hostname = configs.get(0);
        context.getLogicalNetworkElem().setHostname(hostname);

        logger.debug("hostname = " + hostname);

        return context;
    }
}
