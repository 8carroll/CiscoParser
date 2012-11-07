package npx.netmodel.render;

import java.util.ArrayList;
import java.util.List;
import npx.netmodel.config.NetworkAddress;
import npx.netmodel.config.PassiveConfig;

/**
 *
 * @author Luis Dias Costa <luis@theincrediblemachine.org>
 */
public class StringRenderer extends Renderer {

    public StringRenderer(List<PassiveConfig> configList) {
        super(configList);
    }

    @Override
    public Object getResults() {

        String out="";
        
        for (PassiveConfig cfg : getConfigList()) {

            out+=("host:"
                    + cfg.getLogicalNetworkElem().getHostname() + "\n");
            out+=("vendor:"
                    + cfg.getLogicalNetworkElem().getVendor() + "\n");
            out+=("os:"
                    + cfg.getLogicalNetworkElem().getOperatingSystem() + " "
                    + cfg.getLogicalNetworkElem().getVersion() + "\n");

            if (cfg != null) {
                ArrayList<NetworkAddress> addrs =
                        cfg.getNetworkAddress();
                if (addrs != null) {
                    for (NetworkAddress addr : addrs) {
                        out+=("address:"
                                + addr.getAddress() + "/" + addr.getMask() + "; interface:"
                                + addr.getNetworkInterface().getName() + "\n");
                    }
                }

            }
            
            out+="--\n";
        }
        
        return out;
    }
}
