package npx.netmodel.config;

/**
 *
 * @author Luis Dias Costa
 */
public class AccessList extends Config {
    

    /** acl number */
    String id;
    /** acl number */
    String name;
    /** Can be 'any' */
    String srcAddress = null;
    /** */
    String srcMask = null;
    /** */
    String upToSrcAddress = null;
    /** */
    String upToSrcMask = null;
    /** destination address. Can be 'any' */
    String dstAddress = null;
    /** destination mask */
    String dstMask = null;
    /** dst address range upper limit */
    String upToDstAddress = null;
    /** dst mask range upper limit */
    String upToDstMask = null;
    /** permit, allow, deny, reject, remark (i.e. a comment) */
    String action = null;
    /** description, remark, comment */
    String description = null;
    /** protocol (tcp, ip, udp, gre, icmp, ... IANA proto numbers */
    String proto;
    /** port (name or number). For names, it can be a IANA port name, predefined 
     * by the firewall/router software or specified by the user. Can be 'any' */
    String port = null;
    /***/
    String upToPort = null;
    
    /**logical operators */
    String operator=null;
    boolean negatedSource=false;
    boolean negatedDestination=false;
    boolean negatedPort=false;
    

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDstAddress() {
        return dstAddress;
    }

    public void setDstAddress(String dstAddress) {
        this.dstAddress = dstAddress;
    }

    public String getDstMask() {
        return dstMask;
    }

    public void setDstMask(String dstMask) {
        this.dstMask = dstMask;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public String getSrcAddress() {
        return srcAddress;
    }

    public void setSrcAddress(String srcAddress) {
        this.srcAddress = srcAddress;
    }

    public String getSrcMask() {
        return srcMask;
    }

    public void setSrcMask(String srcMask) {
        this.srcMask = srcMask;
    }

    public String getUpToDstAddress() {
        return upToDstAddress;
    }

    public void setUpToDstAddress(String upToDstAddress) {
        this.upToDstAddress = upToDstAddress;
    }

    public String getUpToDstMask() {
        return upToDstMask;
    }

    public void setUpToDstMask(String upToDstMask) {
        this.upToDstMask = upToDstMask;
    }

    public String getUpToPort() {
        return upToPort;
    }

    public void setUpToPort(String upToPort) {
        this.upToPort = upToPort;
    }

    public String getUpToSrcAddress() {
        return upToSrcAddress;
    }

    public void setUpToSrcAddress(String upToSrcAddress) {
        this.upToSrcAddress = upToSrcAddress;
    }

    public String getUpToSrcMask() {
        return upToSrcMask;
    }

    public void setUpToSrcMask(String upToSrcMask) {
        this.upToSrcMask = upToSrcMask;
    }

    public boolean isNegatedDestination() {
        return negatedDestination;
    }

    public void setNegatedDestination(boolean negatedDestination) {
        this.negatedDestination = negatedDestination;
    }

    public boolean isNegatedPort() {
        return negatedPort;
    }

    public void setNegatedPort(boolean negatedPort) {
        this.negatedPort = negatedPort;
    }

    public boolean isNegatedSource() {
        return negatedSource;
    }

    public void setNegatedSource(boolean negatedSource) {
        this.negatedSource = negatedSource;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "AccessList{" + "id=" + id + ", name=" + name + ", srcAddress=" + srcAddress + ", srcMask=" + srcMask + ", upToSrcAddress=" + upToSrcAddress + ", upToSrcMask=" + upToSrcMask + ", dstAddress=" + dstAddress + ", dstMask=" + dstMask + ", upToDstAddress=" + upToDstAddress + ", upToDstMask=" + upToDstMask + ", action=" + action + ", description=" + description + ", proto=" + proto + ", port=" + port + ", upToPort=" + upToPort + ", operator=" + operator + ", negatedSource=" + negatedSource + ", negatedDestination=" + negatedDestination + ", negatedPort=" + negatedPort + '}';
    }
    
    
}
