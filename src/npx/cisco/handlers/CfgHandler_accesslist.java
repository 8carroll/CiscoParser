package npx.cisco.handlers;

import npx.cisco.parser.CiscoConfig;
import java.util.ArrayList;
import npx.netmodel.config.AccessList;
import npx.utilities.IntHelper;
import npx.utilities.StringHelper;
import npx.utilities.ipv4Helper;

/**
 * FWSM and IOS ip address handler
 * 
 * @author Luis Dias Costa
 */
public class CfgHandler_accesslist extends AbstractCfgHandler {

    private int curToken = 0;

    public CfgHandler_accesslist() {
        super();
        setCommand("access-list");
    }

    @Override
    public CfgHandlerContext call(CfgHandlerContext context, CiscoConfig config) {
        super.call(context, config);
        return parseConfig();
    }

    @Override
    public CfgHandlerContext parseConfig() {

        ArrayList<String> configs = new ArrayList<String>(config.getConfig());
        
        if (configs.size() == 0) {
            logger.debug("acl config section is null. ignoring config");
            return null;
        }
        
        
        logger.trace("access-list handler: " + configs);

        AccessList acl = new AccessList();

        switch (context.getCiscoType()) {
            case CfgHandlerContext.CISCO_IOS:
            case CfgHandlerContext.CISCO_FWSM:
            case CfgHandlerContext.CISCO_FWSM_OLD:
            case CfgHandlerContext.CISCO_PIX:

                // there are (at least) 3 major types of ACLs, standard, 'long' and extended (which is similar to long(?))

                // lets assume a number for the acl first:
                curToken = 0;
                String token = cfOk(configs);

                if (tkOk(token)) {
                    //
                    // 1) get the number or name of the acl
                    //
                    if (IntHelper.isNumber(configs.get(curToken))) {
                        // acl number
                        logger.trace("acl #" + configs.get(curToken));
                        acl.setId(configs.get(0));
                    } else {
                        // deny-flow-max or alert-interval or acl-name string

                        if (token.equals("deny-flow-max")) {
                            // @TODO handle this case...
                        } else if (token.equals("alert-interval")) {
                            // @TODO handle this case...
                        } else {
                            acl.setName(configs.get(curToken));
                        }
                    }
                    //
                    // 2) several options now: remark, extended, permit or deny
                    //
                    curToken++;
                    token = cfOk(configs);
                    if (tkOk(token)) {
                        if (token.equals("remark")) {
                            // special case - treated first. acl ends here
                            acl.setAction("remark");
                            String remark = "";
                            for (int i = 2; i < configs.size(); i++) {
                                remark += configs.get(i);
                            }

                            acl.setDescription(remark);
                            // acl ends here

                        } else if (token.equals("extended")) {


                            logger.trace("going extended1");

                            curToken++;
                            token = cfOk(configs);
                            if (token.equals("permit") || token.equals("deny")) {
                                acl = parseACL(configs, acl); // n == position at permit|deny token
                            }

                        } else if (token.equals("permit") || configs.get(curToken).equals("deny")) {

                            acl = parseACL(configs, acl); // n == position at permit|deny token

                        } else {
                            logger.warn("unexpected acl command found in access-list "
                                    + configs + ", for a cisco type " + context.getCiscoType());
                        }
                    }

                    if (acl == null) {
                        logger.error("error parsing acl: " + configs);
                    }

                    logger.trace("access-list parsed: " + acl.toString());
                    logger.trace("--");
                }

                break;
            default:
                logger.warn("Unknown Cisco OS type " + context.getCiscoType());
                break;
        }



        return context;
    }

    private AccessList parseACL(ArrayList<String> configs, AccessList acl) {

        if (acl == null) {
            return null;
        }
        if (configs == null || configs.isEmpty() || curToken < 0) {
            return acl;
        }

        String token = cfOk(configs);
        if (token.equals("permit") || token.equals("deny")) {

            acl.setAction(token);

            curToken++;
            token = cfOk(configs);

            if (tkOk(token)) {

                if (token.equals("any")) {
                    // STANDARD ACL 

                    acl.setSrcAddress("any");

                } else if (ipv4Helper.isIPv4(token)) {
                    // STANDARD ACL 
                    acl.setSrcAddress(token);
                    curToken++;
                    token = cfOk(configs);
                    if (tkOk(token) && ipv4Helper.isIPv4(token)) {
                        // this acl has a mask also
                        acl.setSrcMask(token);
                    }
                    // ends here
                } else {
                    // EXTENDED ACL
                    return parseExtendedACL(configs, acl);
                }

            }


        }

        return acl;
    }

    private AccessList parseExtendedACL(ArrayList<String> configs, AccessList acl) {

        // next, protocol (udp, tcp, icmp have specific options
        String token = cfOk(configs);
        if (tkOk(token)) {
            acl.setProto(token);
            curToken++;
            if (token.equals("icmp")) {
                return parseICMPAcl(configs, acl);
            } else if (token.equals("tdp")) {
                return parseTCPAcl(configs, acl);
            } else if (token.equals("udp")) {
                return parseUDPAcl(configs, acl);
            } else {
                return parseOtherPROTOAcl(configs, acl);
            }
        }

        return acl;
    }

    private AccessList parseICMPAcl(ArrayList<String> configs, AccessList acl) {
        //source
        acl = parseExtSrcFromAcl(configs, acl);
        // destination
        curToken++;
        acl = parseExtDstFromAcl(configs, acl);

        String token = cfOk(configs);
        if (tkOk(token)) {
        }
        return acl;
    }

    private AccessList parseTCPAcl(ArrayList<String> configs, AccessList acl) {
        //source
        acl = parseExtSrcFromAcl(configs, acl);
        // destination
        curToken++;
        acl = parseExtDstFromAcl(configs, acl);

        curToken++;
        String token = cfOk(configs);
        if (tkOk(token)) {
            acl = parsePort(configs, acl);
        }
        return acl;
    }

    private AccessList parseUDPAcl(ArrayList<String> configs, AccessList acl) {
        //source
        acl = parseExtSrcFromAcl(configs, acl);
        // destination
        curToken++;
        acl = parseExtDstFromAcl(configs, acl);


        curToken++;
        String token = cfOk(configs);
        logger.debug("cur token (udp) " + token);
        if (tkOk(token)) {
            acl = parsePort(configs, acl);
        }
        return acl;
    }

    private AccessList parseOtherPROTOAcl(ArrayList<String> configs, AccessList acl) {
        //source
        acl = parseExtSrcFromAcl(configs, acl);
        // destination
        curToken++;
        acl = parseExtDstFromAcl(configs, acl);

        curToken++;
        String token = cfOk(configs);
        if (tkOk(token)) {
        }
        return acl;
    }

    private AccessList parseExtSrcFromAcl(ArrayList<String> configs, AccessList acl) {
        String token = cfOk(configs);
        if (tkOk(token)) {

            // source
            if (token.equals("any")) {
                acl.setSrcAddress(token);
            } else if (token.equals("host")) {
                // host followed by dotted-ip address
                curToken++;
                token = cfOk(configs);
                if (tkOk(token)) {
                    acl.setSrcAddress(token);
                    acl.setSrcMask("255.255.255.255");
                }
            } else {
                // source source-wildcard (dotted-ip address format)
                acl.setSrcAddress(token);
                curToken++;
                token = cfOk(configs);
                if (tkOk(token)) {
                    acl.setSrcMask(token);
                }
            }
        }
        return acl;
    }

    private AccessList parseExtDstFromAcl(ArrayList<String> configs, AccessList acl) {
        String token = cfOk(configs);
        if (tkOk(token)) {

            // source
            if (token.equals("any")) {
                acl.setDstAddress(token);
            } else if (token.equals("host")) {
                // host followed by dotted-ip address
                curToken++;
                token = cfOk(configs);
                if (tkOk(token)) {
                    acl.setDstAddress(token);
                    acl.setDstMask("255.255.255.255");
                }
            } else {
                // source source-wildcard (dotted-ip address format)
                acl.setDstAddress(token);
                curToken++;
                token = cfOk(configs);
                if (tkOk(token)) {
                    acl.setDstMask(token);
                }
            }
        }
        return acl;
    }

    private AccessList parsePort(ArrayList<String> configs, AccessList acl) {

        // [ {gt | lt | neq | eq } port  | range fromPort toPort ]

        String token = cfOk(configs);
        logger.debug("token = " + token);
        curToken++;
        String token2 = cfOk(configs);
        logger.debug("token2= " + token);

        if (tkOk(token) && tkOk(token2)) {
            if (token.equals("gt")) {
                acl.setOperator(">");
                acl.setPort(token2);
            } else if (token.equals("lt")) {
                acl.setOperator("<");
                acl.setPort(token2);
            } else if (token.equals("neq")) {
                acl.setOperator("!=");
                acl.setPort(token2);
            } else if (token.equals("eq")) {
                acl.setOperator("=");
                acl.setPort(token2);
            } else if (token.equals("range")) {
                curToken++;
                String token3 = cfOk(configs);

                if (tkOk(token3)) {
                    acl.setPort(token2);
                    acl.setUpToPort(token3);
                }
            } else {
                logger.warn("port config uses unknown acl operation: " + config);
            }
        } else {
            logger.warn("port config not found in acl: " + config);
        }


        return acl;
    }

    /* checks if the next config token is valid i.e exists and is parsable */
    private boolean tkOk(String token) {
        return !StringHelper.emptyOrNull(token);
    }

    private String cfOk(ArrayList<String> configs) {
        if (configs != null) {
            if (curToken < configs.size()) {
                return configs.get(curToken);
            }
        }

        return null;
    }
}
