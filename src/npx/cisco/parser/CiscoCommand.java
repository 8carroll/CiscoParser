package npx.cisco.parser;

import java.util.ArrayList;

/**
 *
 * 
 * @author Luis Dias Costa
 */
public class CiscoCommand {

    private String commandName;
    private CiscoCommand parent;
    private ArrayList<CiscoCommand> children;

    public CiscoCommand() {
    }

    public CiscoCommand(String s) {
        this.commandName = s;
    }

    public void setChildren(ArrayList<CiscoCommand> children) {
        this.children = children;

        for (int i = 0; i < children.size(); i++) {
            this.children.get(i).setParent(this);
        }
    }

    public ArrayList<CiscoCommand> getChildren() {
        return children;
    }

    public String getParentTokenName() {
        return parent.getCommandName();
    }

    public void setParent(CiscoCommand parent) {
        this.parent = parent;
        //    this.parent.setChild(this);
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String name) {
        this.commandName = name;
    }

    public boolean hasChildren() {
        return (this.children != null);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.commandName != null ? this.commandName.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return toString("");
    }

    public String toString(String tabs) {
        String r = tabs + "Command = " + (commandName == null ? "null" : commandName) + "";

        if (this.hasChildren()) {
            r += ", Children = {\n";
            for (int i = 0; i < children.size(); i++) {
                r += children.get(i).toString(tabs + "\t");
            }
            r += tabs + "}";
        }

        return r + "\n";
    }

    public boolean containsChild(String candidate) {

        if (children == null) {
            return false;
        }

        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getCommandName().equals(candidate)) {
                return true;
            }
        }

        return false;
    }

    /** gets a parent command. ie. a child of [root] */
    public CiscoCommand getRootCommand(String candidate) {

        if (children == null) {
            return null;
        }

        if (parent == null && this.getCommandName().equals("[root]")) {
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i).getCommandName().equals(candidate)) {
                    return children.get(i);
                }
            }
        }

        return null;
    }
}
