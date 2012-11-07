package npx.cisco.parser;

import java.util.ArrayList;

/**
 *
 * @author Luis Dias Costa
 */
public class PortNumbers {

    private ArrayList<PortNumber> ports;

    public ArrayList<PortNumber> getPorts() {
        return ports;
    }

    public void setPorts(ArrayList<PortNumber> ports) {
        this.ports = ports;
    }

    /** returns first port number searching by its name */
    public PortNumber getPortNumberByName(String name) {
        for (PortNumber portNumber : ports) {
            if (portNumber.getName().equals(name)) {
                return portNumber;
            }
        }
        return null;
    }
}
