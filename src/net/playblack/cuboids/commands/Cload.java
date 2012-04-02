package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;

/**
 * Load a single cuboid
 * @author Chris
 *
 */
public class Cload extends BaseCommand {
    public Cload() {
        super("Load a cuboid: /cload <area>", 2);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
        CuboidInterface.getInstance().loadCuboid(player, command[1]);
    }
}
