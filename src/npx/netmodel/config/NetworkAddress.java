package npx.netmodel.config;

/**
 * 
 * @author Luis Dias Costa
 */
public class NetworkAddress extends Config {

    private String address;
    protected String standbyAddress;

    private String mask;
    /** some logical interfaces have also names */
    private String name;
    private NetworkInterface networkInterface;
    private PassiveConfig passiveConfig;

    
    /**
     * Get the value of standbyAddress
     *
     * @return the value of standbyAddress
     */
    public String getStandbyAddress() {
        return standbyAddress;
    }

    /**
     * Set the value of standbyAddress
     *
     * @param standbyAddress new value of standbyAddress
     */
    public void setStandbyAddress(String standbyAddress) {
        this.standbyAddress = standbyAddress;
    }
    
    
    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NetworkInterface getNetworkInterface() {
        return networkInterface;
    }

    public void setNetworkInterface(NetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
    }

    public void setPassiveConfig(PassiveConfig passiveConfig) {
        this.passiveConfig = passiveConfig;
    }

    public PassiveConfig getPassiveConfig() {
        return passiveConfig;
    }
    
    

    @Override
    public String toString() {
        return "NetworkAddress{" + "address=" + address + ", standbyAddress=" + standbyAddress + ", mask=" + mask + ", name=" + name + ", networkInterface=" + networkInterface + ", passiveConfig=" + passiveConfig + '}';
    }

   
}
