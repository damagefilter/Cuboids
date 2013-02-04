package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Save a single or all cuboids
 * 
 * @author Chris
 * 
 */
public class Csave extends CBaseCommand {

    private boolean saveAll = false;

    public Csave(boolean saveAll) {
        super(
                "Save cuboid(s): " + ColorManager.Yellow + "/csave" + saveAll != null
                        && (saveAll) ? "-all" : " <area>", 1, 2);
        this.saveAll = saveAll;
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        if (saveAll) {
            CuboidInterface.get().saveAll(player);
        } else {
            CuboidInterface.get().saveCuboid(player, command[1]);
        }
    }
}
