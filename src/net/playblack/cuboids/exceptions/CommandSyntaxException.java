package net.playblack.cuboids.exceptions;

public class CommandSyntaxException extends Exception {

    private String toolTip;
    private static final long serialVersionUID = 1L;
    
    public CommandSyntaxException(String message, String toolTip) {
    	super(message);
    	this.toolTip = toolTip;
    }
    
    public String getToolTip() {
    	return toolTip;
    }

}
