package npx.cisco.handlers;

import npx.cisco.parser.CiscoConfig;
import java.util.ArrayList;
import org.apache.log4j.Level;
import npx.netmodel.config.NetworkAddress;
import npx.netmodel.config.NetworkInterface;

/**
 *
 * @author Luis Dias Costa
 */
public class CfgHandler_Interface extends AbstractCfgHandler {

    public CfgHandler_Interface() {
        super();
        setCommand("interface");
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

        String interfaceNameOrAlias = configs.get(0);

        NetworkInterface ni = new NetworkInterface();
        NetworkAddress na = new NetworkAddress();
        switch (context.getCiscoType()) {
            case CfgHandlerContext.CISCO_IOS:
            case CfgHandlerContext.CISCO_FWSM:
                // IP address subconfig parsing (used in IOS and new FWSM) may have an address configured or not 
                CiscoConfig ipConfig = config.getSubCommand("ip address");
                ni.setName(interfaceNameOrAlias);
                context.addNetworkInterface(ni);
                if (ipConfig == null) {
                    // try without 'address' (older version of IOS?)
                    logger.log(Level.DEBUG, "interface name1,2 = " + interfaceNameOrAlias);
                    return context;
                }
                //else
                ArrayList<String> ipConfigs = new ArrayList<String>(ipConfig.getCommandConfig());
                if (ipConfigs.size()==0) {
                    logger.log(Level.ERROR, "unexpected null subconfig");
                    return context;
                }

                String address = ipConfigs.get(0);
                String mask = ipConfigs.get(1);

                na.setName(interfaceNameOrAlias);
                na.setAddress(address);
                na.setMask(mask);
                na.setNetworkInterface(ni);
                ni.setNetworkAddress(na);

                context.addNetworkAddress(na);

                logger.log(Level.DEBUG, "interface name1,2 = " + interfaceNameOrAlias + " with address " + address + "/" + mask);
                break;
            case CfgHandlerContext.CISCO_FWSM_OLD:
                // in this particular case, nameif comes first so interfaces may exist already
                ni = context.getNetworkInterfaceByAlias(interfaceNameOrAlias);

                if (ni == null) {
                    ni = new NetworkInterface();
                    ni.setName(interfaceNameOrAlias);
                    context.addNetworkInterface(ni);
                } //else -> do nothing
                else {
                    logger.debug("network inteface already created ");
                }

                logger.log(Level.DEBUG, "interface name3 = " + interfaceNameOrAlias);
                break;

            case CfgHandlerContext.CISCO_PIX:
                // with these types os OSes, only the inteface is know, not the address (it may be present in other part of the configuration)
                ni.setName(interfaceNameOrAlias);
                context.addNetworkInterface(ni);
                logger.log(Level.DEBUG, "interface name4 = " + interfaceNameOrAlias);
                break;

            default:
                logger.warn("Unknown Cisco OS type " + context.getCiscoType());
                break;
        }
        return context;
    }
}
