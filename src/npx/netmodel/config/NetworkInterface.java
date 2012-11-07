package npx.netmodel.config;

/**
 * 
 * @author Luis Dias Costa
 */
public class NetworkInterface extends Config {

    private String name;
    protected String alias;
    private PassiveConfig passiveConfig;
    private NetworkAddress networkAddress;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public PassiveConfig getPassiveConfig() {
        return passiveConfig;
    }

    public void setPassiveConfig(PassiveConfig passiveConfig) {
        this.passiveConfig = passiveConfig;
    }

    public NetworkAddress getNetworkAddress() {
        return networkAddress;
    }

    public void setNetworkAddress(NetworkAddress networkAddress) {
        this.networkAddress = networkAddress;
    }
    
    

    @Override
    public String toString() {
        return "NetworkInterface{" + "name=" + name + ", alias=" + alias + ", passiveConfig=" + passiveConfig + '}';
    }

}
