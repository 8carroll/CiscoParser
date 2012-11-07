package npx.netmodel.config;

import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 * 
 * @author Luis Dias Costa
 */
public class PassiveConfig {

    private ArrayList<Config> configs;

    public PassiveConfig() {
    }

    public ArrayList<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(ArrayList<Config> configs) {
        this.configs = configs;
    }
    
    

    public void addConfig(Config c) {
        if (configs == null) {
            configs = new ArrayList<Config>();
        }
        configs.add(c);
    }

    public LogicalNetworkElem getLogicalNetworkElem() {
        for (Config cfg : configs) {
            if (cfg instanceof LogicalNetworkElem) {
                return (LogicalNetworkElem) cfg;
            }
        }

        return null;
    }


    public ArrayList<NetworkInterface> getNetworkInterfaces() {

        if (configs == null) {
            return null;
        }

        ArrayList<NetworkInterface> tmp = new ArrayList<NetworkInterface>();
        for (Config cfg : configs) {
            if (cfg instanceof NetworkInterface) {
                tmp.add((NetworkInterface) cfg);
            }
        }

        if (tmp.isEmpty()) {
            return null;
        }

        return tmp;
    }

    public ArrayList<NetworkAddress> getNetworkAddress() {

        if (configs == null) {
            return null;
        }

        ArrayList<NetworkAddress> tmp = new ArrayList<NetworkAddress>();
        for (Config cfg : configs) {
            if (cfg instanceof NetworkAddress) {
                tmp.add((NetworkAddress) cfg);
            }
        }

        if (tmp.isEmpty()) {
            return null;
        }

        return tmp;
    }
    
        public ArrayList<AccessList> getAccessList() {

        if (configs == null) {
            return null;
        }

        ArrayList<AccessList> tmp = new ArrayList<AccessList>();
        for (Config cfg : configs) {
            if (cfg instanceof AccessList) {
                tmp.add((AccessList) cfg);
            }
        }

        if (tmp.isEmpty()) {
            return null;
        }

        return tmp;
    }


    public void log() {
        if (configs != null) {
            for (Config config : configs) {
                logger.debug(config);
            }
        } else {
             logger.debug("no config available to log.");
        }
    }
    private final Logger logger = Logger.getLogger(this.getClass());
}
