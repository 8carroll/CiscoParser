package npx.cisco.handlers;

import npx.cisco.parser.CiscoConfig;


/**
 *
 * @author Luis Dias Costa
 */
public interface CfgHandler {
    public CfgHandlerContext call(CfgHandlerContext context, CiscoConfig config);
    public void setCommand(String command);
    public String getCommand();
    public CfgHandlerContext parseConfig();
}
