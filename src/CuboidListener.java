
/**
 * Listens for misc events
 * @author Chris
 *
 */
public class CuboidListener extends PluginListener{
	
	@Override
	public boolean onMobSpawn(Mob mob) {
		//log.info("Mob spawned! "+mob.getName());
		return Cuboids2.cuboids.sanctuarySpawnsMobs(mob);
	}
	
	@Override
	public PluginLoader.HookResult canPlayerUseCommand(Player player, String command) {
		if(player.isAdmin()) {
			//Cuboids2.eventLog.logMessage("player is admin", "INFO");
			return PluginLoader.HookResult.DEFAULT_ACTION;
		}
		//Cuboids2.eventLog.logMessage("player is NOT admin", "INFO");
		String[] split = command.split(" ");
		if(Cuboids2.cuboids.commandIsRestricted(player, split[0])) {
			//Cuboids2.eventLog.logMessage("Command is restricted!", "INFO");
			player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("commandDenied"));
			return PluginLoader.HookResult.PREVENT_ACTION;
		}
		else {
			return PluginLoader.HookResult.DEFAULT_ACTION;
		}
	}
	/**
	 * called from main to save data on plugin disable
	 */
	public void saveCuboidData() {
		Cuboids2.treeHandler.save(false, false);
	}
	
	@Override
	public boolean onFlow(Block blockFrom, Block blockTo) {
		/**
		 * True if there shall not be a flow, false for default
		 */
		return Cuboids2.cuboids.canFlow(blockFrom);
	}
	
	@Override
	public boolean onMobTarget(Player player, LivingEntity mob) {
		return Cuboids2.cuboids.isHmob(player);
	}
	
	
}
