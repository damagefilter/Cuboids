package net.playblack.cuboids.commands;


import net.playblack.cuboids.Config;
import net.playblack.cuboids.RegionFlagRegister;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Backup an area
 *
 * @author Chris
 */
public class CmodListFlags extends CBaseCommand {

    public CmodListFlags() {
        super("List available Cuboid flags: " + ColorManager.Yellow + "/cmod listflags [area]", 1, 2);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        //Just list all available flags
        if (command.length == 1) {
            String[] flags = RegionFlagRegister.getRegisteredFlags();
            StringBuilder out = new StringBuilder();
            int num = 0;
            for (String fl : flags) {
                if (num == 3) {
                    out.append(";");
                    num = 0;
                }
                else {
                    out.append(fl).append(", ");
                    num++;
                }
            }
            String[] lines = out.toString().split(";");
            for (String str : lines) {
                player.sendMessage(ColorManager.Yellow + str);
            }
        }
        else {
            Region r = RegionManager.get()
                                    .getRegionByName(command[1], player.getWorld().getName(), player.getWorld()
                                                                                                    .getDimension());
            if (r == null) {
                r = Config.get().getGlobalSettings();
            }
            player.sendMessage(ColorManager.LightGray + "---- " + r.getName() + " ----");
            String[] lines = r.getFlagList().split(";");
            for (String str : lines) {
                player.sendMessage(str);
            }
        }
    }
}
