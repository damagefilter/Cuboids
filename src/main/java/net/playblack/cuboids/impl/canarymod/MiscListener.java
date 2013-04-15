package net.playblack.cuboids.impl.canarymod;


import net.canarymod.api.entity.living.monster.EntityMob;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.command.PlayerCommandHook;
import net.canarymod.hook.entity.EntitySpawnHook;
import net.canarymod.plugin.PluginListener;
import net.playblack.cuboids.InvalidPlayerException;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.EntitySpawnEvent;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.CuboidInterface;
public class MiscListener implements PluginListener {

    @HookHandler
    public void onMobSpawn(EntitySpawnHook hook) {
        EntitySpawnEvent event = null;
        if(hook.getEntity() instanceof EntityMob) {
            event = new EntitySpawnEvent(new CanaryMob(hook.getEntity()));
        }
        else {
            event = new EntitySpawnEvent(new CanaryBaseEntity(hook.getEntity()));
        }
        ActionManager.fireEvent(event);
        if(event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onCommand(PlayerCommandHook hook) {
        if (hook.getPlayer().isAdmin()) {
            return;
        }
        String[] split = hook.getCommand();
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(hook.getPlayer().getName());
        } catch (InvalidPlayerException e) {
            cplayer = new CanaryPlayer(hook.getPlayer());
        }
        if (CuboidInterface.get().commandIsRestricted(cplayer, split[0])) {
            hook.setCanceled();
        }
    }
}
