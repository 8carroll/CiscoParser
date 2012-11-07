package npx.cisco.handlers;

import npx.cisco.parser.CiscoConfig;
import java.util.ArrayList;
import org.apache.log4j.Level;

/**
 * 
 * 
 * @author Luis Dias Costa
 */
public class CfgHandler_nat extends AbstractCfgHandler {

    public CfgHandler_nat() {
        super();
        setCommand("nat");
    }

    @Override
    public CfgHandlerContext call(CfgHandlerContext context, CiscoConfig config) {
        super.call(context, config);
        return parseConfig();
    }

    @Override
    public CfgHandlerContext parseConfig() {

        ArrayList<String> configs = new ArrayList<String>(config.getConfig());
        logger.trace(config.getCommandName() + " handler + " + configs);

        if (configs.size()==0) {
            logger.log(Level.ERROR, "unexpected null config");
            return null;
        }

        switch (context.getCiscoType()) {
            case CfgHandlerContext.CISCO_IOS:
                logger.trace("nat format for cisco ios");
                break;
            case CfgHandlerContext.CISCO_FWSM:
                logger.trace("nat format for cisco fwsm");
                break;
            case CfgHandlerContext.CISCO_FWSM_OLD:
                logger.trace("nat format for cisco fwsm old");
                break;
            case CfgHandlerContext.CISCO_PIX:
                logger.trace("nat format for cisco pix");
                break;
            default:
                logger.warn("Unknown Cisco OS type " + context.getCiscoType());
                break;
        }

        return context;
    }
}

/*
10.162.2.104.ios:FWSM Version 3.1(12) <context>
10.162.2.104.ios:nat (PTNET) 19 10.169.16.0 255.255.254.0
10.162.2.104.ios:nat (vcontact-fe) 1 10.162.100.72 255.255.255.255
10.162.2.104.ios:nat (vcontact-fe) 1 10.162.100.75 255.255.255.255
10.162.2.104.ios:nat (vcontact-fe) 1 10.162.100.77 255.255.255.255
10.162.2.104.ios:nat (vcontact-fe) 1 10.162.100.80 255.255.255.255
10.162.2.104.ios:nat (ptlocal-fe) 1 10.162.100.118 255.255.255.255
10.162.2.104.ios:nat (vcontact-ecall) 1 10.162.100.176 255.255.255.240
10.162.2.106.ios:FWSM Version 3.1(12) <context>
10.162.2.108.ios:FWSM Version 3.1(12) <context>
10.162.2.11.ios:version 12.2
10.162.2.11.ios:access-list 1 permit 10.161.2.0 0.0.0.255
10.162.2.11.ios:access-list 1 permit 10.162.2.0 0.0.0.255
10.162.2.11.ios:access-list 1 permit 10.162.23.67
10.162.2.11.ios:access-list 1 permit 10.163.2.0 0.0.0.255
10.162.2.11.ios:access-list 1 permit 10.165.2.0 0.0.0.255
10.162.2.11.ios:access-list 1 permit 10.166.2.0 0.0.0.255
10.162.2.11.ios:access-list 1 permit 10.167.2.0 0.0.0.255
10.162.2.11.ios:access-list 1 permit 10.167.50.15
10.162.2.11.ios:access-list 1 permit 10.168.2.0 0.0.0.255
10.162.2.11.ios:access-list 1 permit 10.169.16.0 0.0.0.255
10.162.2.11.ios:access-list 1 permit 172.18.1.0 0.0.0.255
10.162.2.11.ios:access-list 50 permit 10.162.23.67
10.162.2.11.ios:access-list 50 permit 172.18.1.82
10.162.2.11.ios:access-list 95 permit 10.162.7.35

*/