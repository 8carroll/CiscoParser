package npx.cisco.handlers;

import java.util.ArrayList;
import npx.cisco.parser.CiscoConfig;
import npx.netmodel.config.NetworkAddress;
import npx.netmodel.config.NetworkInterface;
import npx.utilities.StringHelper;
import org.apache.log4j.Level;

/**
 * FWSM and IOS ip address handler
 *
 * @author Luis Dias Costa
 */
public class CfgHandler_IPAddress extends AbstractCfgHandler {

    public CfgHandler_IPAddress() {
        super();
        setCommand("ip address");
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

        switch (context.getCiscoType()) {

            case CfgHandlerContext.CISCO_FWSM:
                logger.log(Level.WARN, "unexpected root command found 'ip address' for a IOS config");
                break;
            case CfgHandlerContext.CISCO_IOS:
            case CfgHandlerContext.CISCO_FWSM_OLD:
            case CfgHandlerContext.CISCO_PIX:
                logger.log(Level.DEBUG, "ip address (fwsm old, pix) if= " + interfaceNameOrAlias + ": ip=" + configs);
                NetworkInterface ni = context.getNetworkInterfaceByAlias(interfaceNameOrAlias);

                if (ni == null) {
                    logger.error("unable to find interface name " + interfaceNameOrAlias + " for ip address.");
                } else {
                    NetworkAddress na = new NetworkAddress();
                    na.setNetworkInterface(ni);
                    ni.setNetworkAddress(na);
                    
                    if (configs.size() > 1) {
                        String address = configs.get(1);
                        if (!StringHelper.emptyOrNull(address)) {
                            na.setAddress(address);
                        }
                    }
                    if (configs.size() > 2) {
                        String mask = configs.get(2);
                        if (!StringHelper.emptyOrNull(mask)) {
                            na.setMask(mask);
                        }
                    }
                    if (configs.size() > 3) {
                        String standbyStmt = configs.get(3);
                        if (!StringHelper.emptyOrNull(standbyStmt) && standbyStmt.equals("standby")) {
                            String standbyAddress = configs.get(4);
                            if (!StringHelper.emptyOrNull(standbyStmt)) {
                                na.setStandbyAddress(standbyAddress);
                            }
                        }
                    }
                    
                    context.addNetworkAddress(na);
                    logger.debug("network address=" + na);
                }
                break;
            default:
                logger.warn("Unknown Cisco OS type " + context.getCiscoType());
                break;
        }
        return context;
    }
}
