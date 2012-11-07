package npx.cisco.handlers;

import java.util.ArrayList;
import npx.netmodel.config.LogicalNetworkElem;
import npx.netmodel.config.NetworkAddress;
import npx.netmodel.config.NetworkInterface;
import npx.netmodel.config.PassiveConfig;

/**
 * Context for a parser session. Every common information that is reused 
 * in different sections being parsed is part of this context. e.g the hostname
 * a config belongs to belongs to a context. The list of vlans belongs also to
 * the context since it can be used by other parts of the configuration.
 * This means that the order in which the configuration handlers are called
 * is important since parts of the configuration may required prior sections to 
 * be parsed.
 * 
 * @author Luis Dias Costa
 */
public class CfgHandlerContext {

    public static final int CISCO_UNKOWN = 0;
    public static final int CISCO_IOS = 1;
    public static final int CISCO_FWSM = 2;
    public static final int CISCO_FWSM_OLD = 3;
    public static final int CISCO_PIX = 4;
    private int ciscoType;
    private LogicalNetworkElem lne;
    private PassiveConfig passiveConfig;
    private ArrayList<NetworkAddress> networkAddresses;
    private ArrayList<NetworkInterface> networkInterfaces;

    public CfgHandlerContext() {
        this.lne = new LogicalNetworkElem();
        this.ciscoType = CISCO_UNKOWN;
        this.passiveConfig = new PassiveConfig();
    }

    public void addNetworkAddress(NetworkAddress na) {
        if (this.networkAddresses == null) {
            networkAddresses = new ArrayList<NetworkAddress>();
        }
        networkAddresses.add(na);
        na.setPassiveConfig(passiveConfig);
        this.passiveConfig.addConfig(na);
    }

    public void addNetworkInterface(NetworkInterface ni) {
        if (this.networkInterfaces == null) {
            networkInterfaces = new ArrayList<NetworkInterface>();
        }
        networkInterfaces.add(ni);
        ni.setPassiveConfig(passiveConfig);
        this.passiveConfig.addConfig(ni);
    }

    /* finds the first network address by interface name */
    public NetworkAddress getNetworkAddress(String interfaceName) {
        for (NetworkAddress networkAddress : networkAddresses) {
            if (networkAddress.getNetworkInterface().getName().equals(interfaceName)) {
                return networkAddress;
            }
        }
        return null;
    }

    public NetworkInterface getNetworkInterfaceByName(String interfaceName) {
        if (networkInterfaces == null || interfaceName == null) {
            return null;
        }
        for (NetworkInterface networkInterface : networkInterfaces) {
            if (networkInterface.getName() != null) {
                if (networkInterface.getName().equals(interfaceName)) {
                    return networkInterface;
                }
            }
        }
        return null;
    }

    public NetworkInterface getNetworkInterfaceByAlias(String interfaceAlias) {
        if (networkInterfaces == null || interfaceAlias == null) {
            return null;
        }
        for (NetworkInterface networkInterface : networkInterfaces) {
            if (networkInterface.getAlias() != null) {
                if (networkInterface.getAlias().equals(interfaceAlias)) {
                    return networkInterface;
                }
            }
        }
        return null;
    }

    void setLogicalNetworkElem(LogicalNetworkElem lne) {
        this.lne = lne;
        this.passiveConfig.addConfig(lne);
        this.lne.addPassiveConfig(passiveConfig);
    }

    void setCiscoType(int type) {
        this.ciscoType = type;
    }

    int getCiscoType() {
        return this.ciscoType;
    }

    public PassiveConfig getPassiveConfig() {
        return this.passiveConfig;
    }

    LogicalNetworkElem getLogicalNetworkElem() {
        return this.lne;
    }
}
