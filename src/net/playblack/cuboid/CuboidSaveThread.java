package net.playblack.cuboid;

import net.playblack.cuboid.tree.CuboidTreeHandler;

/**
 * Periodically save cubodis to file and stuff
 * @author Chris
 *
 */
public class CuboidSaveThread implements Runnable {

	long delay;
	CuboidTreeHandler handler;
	public CuboidSaveThread(long delay, CuboidTreeHandler handler) {
		this.delay = delay;
		this.handler = handler;
	}
	
	@Override
	public void run() {
		System.out.println("Cuboids2: Saving ...");
		handler.save(false, false);
		System.out.println("Cuboids2: Next save in "+delay+" minutes");
	}

}
