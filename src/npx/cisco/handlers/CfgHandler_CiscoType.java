package npx.cisco.handlers;

import npx.cisco.parser.CiscoConfig;
import java.util.ArrayList;
import npx.netmodel.config.LogicalNetworkElem;

/**
 *
 * @author Luis Dias Costa
 */
public class CfgHandler_CiscoType extends AbstractCfgHandler {

    public CfgHandler_CiscoType() {
        super();
        setCommand("version");
    }

    @Override
    public CfgHandlerContext call(CfgHandlerContext context, CiscoConfig config) {
        logger.trace("-------------------------------------");
        super.call(context, config);

        return parseConfig();
    }

    @Override
    public CfgHandlerContext parseConfig() {

        ArrayList<String> configs = new ArrayList<String>(config.getConfig());

        if (configs.size() == 0) {
            logger.debug("version config section is null. ignoring config");
            return null;
        }

        String cmd = config.getCommandName();
        String c0 = configs.get(0);

        LogicalNetworkElem lne = new LogicalNetworkElem();
        lne.setOperatingSystem(cmd);
        lne.setVersion(c0);
        lne.setVendor("Cisco");
        context.setLogicalNetworkElem(lne);

        if (cmd.equals("FWSM Version")) {
            if (c0.startsWith("2.")) {
                context.setCiscoType(CfgHandlerContext.CISCO_FWSM_OLD);
                logger.debug("cisco type=CISCO_FWSM_OLD");
            } else if (c0.startsWith("3.")) {
                context.setCiscoType(CfgHandlerContext.CISCO_FWSM);
                logger.debug("cisco type=CISCO_FWSM");
            } else {
                context.setCiscoType(CfgHandlerContext.CISCO_UNKOWN);
                logger.debug("cisco type=CISCO_UNKNOWN");

            }
        } else if (cmd.equals("PIX Version") && c0.startsWith("6.")) {
            context.setCiscoType(CfgHandlerContext.CISCO_PIX);
            logger.debug("cisco type=CISCO_PIX");

        } else if (cmd.equals("version") && c0.startsWith("12.")) {
            context.setCiscoType(CfgHandlerContext.CISCO_IOS);
            logger.debug("cisco type=CISCO_IOS");

        } else {
            context.setCiscoType(CfgHandlerContext.CISCO_UNKOWN);
            logger.debug("cisco type=CISCO_UNKNOWN");

        }



        return context;
    }
}
