package npx.cisco.handlers;

import npx.cisco.parser.CiscoConfig;
import java.util.ArrayList;
import org.apache.log4j.Level;
import npx.netmodel.config.NetworkInterface;

/**
 * FWSM and IOS ip address handler
 * 
 * @author Luis Dias Costa
 */
public class CfgHandler_nameif extends AbstractCfgHandler {

    public CfgHandler_nameif() {
        super();
        setCommand("nameif");
    }

    @Override
    public CfgHandlerContext call(CfgHandlerContext context, CiscoConfig config) {
        super.call(context, config);
        return parseConfig();
    }

    @Override
    public CfgHandlerContext parseConfig() {

        ArrayList<String> configs = new ArrayList<String>(config.getConfig());

        if (configs.size()==0) {
            logger.log(Level.ERROR, "unexpected null config");
            return null;
        }

        String interfaceName = configs.get(0);
        String interfaceAlias = configs.get(1);
        NetworkInterface ni = context.getNetworkInterfaceByName(interfaceName);

        switch (context.getCiscoType()) {
            case CfgHandlerContext.CISCO_IOS:
                logger.warn("unexpected nameif as root command");
                break;
            case CfgHandlerContext.CISCO_FWSM:
                logger.warn("unexpected nameif as root command");
                break;
            case CfgHandlerContext.CISCO_FWSM_OLD:
                logger.debug("nameif3 -> " + configs);

                if (ni != null) {
                    ni.setAlias(interfaceAlias);
                } else {
                    ni = new NetworkInterface();
                    ni.setName(interfaceName);
                    ni.setAlias(interfaceAlias);
                    context.addNetworkInterface(ni);
                }

                break;
            case CfgHandlerContext.CISCO_PIX:
                logger.debug("nameif4 -> " + configs);
                ni.setAlias(interfaceAlias);
                break;
            default:
                logger.warn("Unknown Cisco OS type " + context.getCiscoType());
                break;
        }
        return context;
    }
}
