import java.util.Timer;
import java.util.TimerTask;

import com.playblack.cuboid.CuboidE;


/**
 * Is not a thread but still ... does a heal job :D
 * Base Code is taken form CuboidPlugin but was modified
 * to utilize the powers of the new CuboidE
 * @author Chris
 *
 */
public class CuboidHealThread extends TimerTask {
	 	Player player;
	    CuboidE cube;
	    int healPower;
	    long healDelay;
	    static Timer timer = new Timer();

	    public CuboidHealThread(Player player, CuboidE cube, int healPower, long healDelay) {
	        this.player = player;
	        this.cube = cube;
	        this.healPower = healPower;
	        this.healDelay = healDelay;
	    }

	    public void run() {
	 	        if (this.cube.playerIsWithin(player.getName())) {
	 	        	
	 	            if (player.getHealth() > 0) {
	 	            	
	 	                player.setHealth(player.getHealth() + this.healPower);
	 	            }
	 	            if (player.getHealth() < 20) {
	 	                timer.schedule(new CuboidHealThread(player, this.cube, healPower, healDelay), healDelay);
	 	            }
	 	        }
	    }
}
