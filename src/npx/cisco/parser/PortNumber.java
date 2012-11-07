/**
 *
 * @author Luis Dias Costa
 */
package npx.cisco.parser;

public class PortNumber {
    
    protected String name;
    protected String proto;
    protected int number;
    protected int upToNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getUpToNumber() {
        return upToNumber;
    }

    public void setUpToNumber(int upToNumber) {
        this.upToNumber = upToNumber;
    }

}
