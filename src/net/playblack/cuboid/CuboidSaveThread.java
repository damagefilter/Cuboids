package net.playblack.cuboid;

import java.util.Timer;
import java.util.TimerTask;

import net.playblack.cuboid.tree.CuboidTreeHandler;

/**
 * Periodically save cubodis to file and stuff
 * @author Chris
 *
 */
public class CuboidSaveThread extends TimerTask {

	long delay;
	CuboidTreeHandler handler;
	static Timer timer = new Timer();
	public CuboidSaveThread(long delay, CuboidTreeHandler handler) {
		this.delay = delay;
		this.handler = handler;
	}
	
	@Override
	public void run() {
		System.out.println("Cuboids2: Saving ...");
		handler.save(false, false);
		System.out.println("Cuboids2: Next save in "+delay+" seconds");
		timer.schedule(new CuboidSaveThread(delay, handler), delay);
		
		
	}

}
