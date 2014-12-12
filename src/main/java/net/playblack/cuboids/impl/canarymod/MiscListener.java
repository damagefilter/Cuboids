package net.playblack.cuboids.impl.canarymod;


import net.canarymod.hook.HookHandler;
import net.canarymod.hook.command.PlayerCommandHook;
import net.canarymod.hook.entity.EntitySpawnHook;
import net.canarymod.plugin.PluginListener;
import net.playblack.cuboids.actions.operators.MiscOperator;
import net.playblack.cuboids.regions.CuboidInterface;

public class MiscListener implements PluginListener {

    public MiscOperator miscOperator = new MiscOperator();

    @HookHandler
    public void onMobSpawn(EntitySpawnHook hook) {
        if (miscOperator.onMobSpawn(hook.getEntity())) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onCommand(PlayerCommandHook hook) {
        if (hook.getPlayer().isAdmin()) {
            return;
        }
        String[] split = hook.getCommand();
        if (CuboidInterface.get().commandIsRestricted(hook.getPlayer(), split[0])) {
            hook.setCanceled();
        }
    }
}
