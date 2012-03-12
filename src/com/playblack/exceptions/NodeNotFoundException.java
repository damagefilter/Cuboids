package com.playblack.exceptions;

import com.playblack.mcutils.Vector;

public class NodeNotFoundException extends Exception {

	private String scope;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8549331417887764646L;
	
	public NodeNotFoundException(String message, Vector position) {
		super(message);
		this.scope = position.toString();
	}
	
	public String getBoundary() {
		return scope;
	}
	
	public String getPlayerMessage() {
	    return "Node not found at "+scope;
	}
}
