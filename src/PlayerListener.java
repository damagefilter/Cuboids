import java.util.ArrayList;

import com.playblack.ToolBox;
import com.playblack.vector.Vector;


/**
 * Listens to player venets such as kick, ban, connect etc
 * @author Christoph Ksoll
 *
 */
public class PlayerListener extends PluginListener {
	private ToolBox toolBox = new ToolBox();
	private ArrayList<Integer> foodList = new ArrayList<Integer>(0);
	private boolean playerTeleported = false;
	public PlayerListener() {
		super();
		//NOTE: This is called foodList but this could also
		//contain general items that can be exluded from the
		//permissions checks in areas and stuff (onItemUSe hook)
		foodList.add(297);
		foodList.add(319);
		foodList.add(320);
		foodList.add(322);
		foodList.add(344);
		foodList.add(349);
		foodList.add(350);
		foodList.add(354);
		foodList.add(357);
		foodList.add(360);
		foodList.add(363);
		foodList.add(364);
		foodList.add(365);
		foodList.add(366);
		foodList.add(367);
	}
	
	@Override
	public void onDisconnect(Player player) {
		Vector v = new Vector(player.getX(), player.getY(), player.getZ());
		Vector to = new Vector(0,0,0);
		Cuboids2.cuboids.removePlayerWithin(player, v, to);
	}
	
	@Override
	public void onBan(Player mod, Player player, String reason) {
		Vector v = new Vector(player.getX(), player.getY(), player.getZ());
		Vector to = new Vector(0,0,0);
		Cuboids2.cuboids.removePlayerWithin(player, v, to);
	}
	
	@Override
	public void onKick(Player mod, Player player, String reason) {
		Vector v = new Vector(player.getX(), player.getY(), player.getZ());
		Vector to = new Vector(0,0,0);
		Cuboids2.cuboids.removePlayerWithin(player, v, to);
	}
	
	@Override
	public void onPlayerMove(Player player, Location from, Location to) {
		Vector vTo = toolBox.adjustWorldBlock(new Vector(to.x, to.y, to.z));		
		if(!Cuboids2.cuboids.canEnter(player,vTo)) {
			player.teleportTo(from);
		}
		Cuboids2.cuboids.addPlayerWithin(player, new Vector(player.getX(), player.getY(), player.getZ()), playerTeleported);	
		Cuboids2.cuboids.removePlayerWithin(player, toolBox.adjustWorldBlock(new Vector(from.x, from.y, from.z)), 
													vTo);
		playerTeleported = false;	
	}
	
	@Override
	public boolean onTeleport(Player player, Location from, Location to) {
		if (!player.getWorld().isChunkLoaded((int)to.x, (int)to.y, (int)to.z)) {
			player.getWorld().loadChunk((int)to.x, (int)to.y, (int)to.z);
        }
		//Vector vTo = toolBox.adjustWorldBlock(new Vector(to.x, to.y, to.z));
		Cuboids2.cuboids.addPlayerWithin(player, new Vector(player.getX(), player.getY(), player.getZ()), false);	
		Cuboids2.cuboids.removePlayerWithin(player, toolBox.adjustWorldBlock(new Vector(from.x, from.y, from.z)), 
											new Vector(to.x, to.y, to.z));
		playerTeleported = true;		
		return false; //allow tp	
	}
	
	
	
	@Override
	public boolean onDamage(PluginLoader.DamageType type, BaseEntity attacker, BaseEntity defender, int amount) {
		return Cuboids2.cuboids.canTakeDamage(type, attacker, defender);
	}
	
	@Override
	public boolean onItemUse(Player player, Block blockPlaced, Block blockClicked, Item item) {
		if(player.canUseCommand("/cIgnoreRestriction")) {
			return false; //allow
		}
		//25d == flint&steel
		if(item.getItemId() == 259) {
			return !Cuboids2.cuboids.canStartFire(player, blockClicked);
		}
		if(item.getItemId() == 326 || item.getItemId() == 327) {
			if(Cuboids2.cuboids.canPlaceBucket(player, blockClicked)) {
				//item.setItemId(325);
				return true;
			}
		}
		else {
			//If we're holding something we can eat, let em eat
			//NOTE: The list can contain any item
			if(item != null && foodList.contains(item.getItemId())) {
				return false;
			}
			if(blockClicked == null) {
				return !Cuboids2.cuboids.canModifyBlock(player, blockPlaced);
			}
			else {
				return !Cuboids2.cuboids.canModifyBlock(player, blockClicked);
			}
		}
		return false;
	}
}
