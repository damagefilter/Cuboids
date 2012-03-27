package net.playblack.mcutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sends log messages to the server log. This also creates Cuboid Area logs
 * for Griefer monitoring and stuff. (Once it's implemented that is)
 * TODO: monitored flag for areas
 * @author chris
 *
 */
public class EventLogger {
	private Logger log;
	private HashMap<String,AreaMessages> arealog = new HashMap<String,AreaMessages>();
	private static EventLogger instance = null;
	
	/**
	 * Construc the logger with a reference to thr minecraft logger
	 * @param log
	 */
	private EventLogger(Logger log) {
		this.log = log;
	}
	
	public static EventLogger getInstance() {
	    if(instance == null) {
	        instance = new EventLogger(Logger.getLogger("Minecraft"));
	    }
	    return instance;
	}
	
	/**
	 * Directly send a message to the server log.
	 * @param message
	 * @param level the log level, can use INFO, WARNING and SEVERE
	 */
	public void logMessage(String message, String level) {
		if(level.equalsIgnoreCase("INFO")) {
			//log.info(message);
			log.log(Level.INFO, message);
		}
		else if(level.equalsIgnoreCase("WARNING")) {
			log.log(Level.WARNING, message);
			//log.warning(message);
		}
		else if(level.equalsIgnoreCase("SEVERE")) {
			//log.severe(message);
			log.log(Level.SEVERE, message);
		}
	}
	/**
	 * Log an event that has happened inside an area.
	 * @param area
	 * @param message
	 */
	public void logAreaEvent(String area, String message) {
		if(arealog.get(area) == null) {
			arealog.put(area, new AreaMessages());
		}
		arealog.get(area).logMessage(message);
	}
	
	public String makeTimestamp() {
		return null;
	}
	//TODO: implement area logging
	private class AreaMessages {
		private ArrayList<String> messages = new ArrayList<String>();
		
		public void logMessage(String message) {
			messages.add(message);
		}
		
		public String toString() {
			StringBuilder out = new StringBuilder();
			for(String m : messages) {
				out.append(m).append("\n");
			}
			return out.toString();
		}
	}
}
