package net.playblack.cuboids.impl.canarymod;

import net.canarymod.Canary;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.plugin.Plugin;
import net.playblack.cuboids.Bootstrapper;
import net.playblack.cuboids.Config.Implementation;
import net.playblack.cuboids.CuboidFLoader;
import net.playblack.cuboids.converters.Loader;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.impl.canarymod.commands.CmodCommands;
import net.playblack.cuboids.impl.canarymod.commands.MiscCommands;
import net.playblack.cuboids.impl.canarymod.commands.SelectionEditingCommands;
import net.playblack.cuboids.impl.canarymod.commands.WorldEditingCommands;
import net.playblack.mcutils.Debug;
import net.playblack.mcutils.Location;

public class Cuboids extends Plugin {

    public static Location toLocalLocation(net.canarymod.api.world.position.Location loc) {
        return new Location(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), CServer.getServer()
                                                                                      .getWorld(loc.getWorldName(), loc.getType()
                                                                                                                       .getId()));
    }

    @Override
    public boolean enable() {
        Debug.overrideLogger(getLogman());
        new Bootstrapper(new CanaryServer(), new Loader[]{new CuboidFLoader()}, Implementation.CANARY).bootstrap();
        try {
            Canary.commands().registerCommands(new CmodCommands(), this, false);
            Canary.commands().registerCommands(new MiscCommands(), this, false);
            Canary.commands().registerCommands(new SelectionEditingCommands(), this, false);
            Canary.commands().registerCommands(new WorldEditingCommands(), this, false);
        }
        catch (CommandDependencyException e) {
            Debug.logStack(e);
            return false;
        }
        Canary.hooks().registerListener(new BlockListener(), this);
        Canary.hooks().registerListener(new MiscListener(), this);
        Canary.hooks().registerListener(new PlayerListener(), this);
        return true;
    }

    @Override
    public void disable() {
        Canary.commands().unregisterCommands(this);
    }

}
