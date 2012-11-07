package npx.netmodel.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Luis Dias Costa
 */
public class LogicalNetworkElem extends Config {

    private String hostname;
    private String vendor;
    private String operatingSystem;
    private String version;
    private List<PassiveConfig> passiveConfigs;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public List<PassiveConfig> getPassiveConfigs() {
        return passiveConfigs;
    }

    public void setPassiveConfigs(List<PassiveConfig> passiveConfigs) {
        this.passiveConfigs = passiveConfigs;
    }

    public void addPassiveConfig(PassiveConfig passiveConfig) {
        if (this.passiveConfigs == null) {
            this.passiveConfigs = new ArrayList<PassiveConfig>();
        }
        this.passiveConfigs.add(passiveConfig);
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    

    @Override
    public String toString() {
        return "LogicalNetworkElem{" + "hostname=" + hostname + ", vendor=" + vendor + ", operatingSystem=" + operatingSystem + ", version=" + version + ", passiveConfigs=" + passiveConfigs + '}';
    }



}
