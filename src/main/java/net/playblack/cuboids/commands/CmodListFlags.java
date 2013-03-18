package net.playblack.cuboids.commands;


import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.RegionFlagRegister;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Backup an area
 * 
 * @author Chris
 * 
 */
public class CmodListFlags extends CBaseCommand {

    public CmodListFlags() {
        super("Set a flag in the given area: " + ColorManager.Yellow + "/cmod [region] flag list", 3, 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        if (!player.hasPermission("cIgnoreRestrictions")) {
            if (!player.hasPermission("cflags")) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }
        //Just list all available flags
        if(command[1].equals("flag")) {
            String[] flags = RegionFlagRegister.getRegisteredFlags();
            StringBuilder out = new StringBuilder();
            int num = 0;
            for(String fl : flags) {
                if(num == 3) {
                    out.append(";");
                    num = 0;
                }
                else {
                    out.append(fl).append(", ");
                    num++;
                }
            }
            String[] lines = out.toString().split(";");
            for(String str : lines) {
                player.sendMessage(ColorManager.Yellow + str);
            }
        }
        
        else {
            if(command.length > 3) {
                Region r = RegionManager.get().getRegionByName(command[1], player.getWorld().getName(), player.getWorld().getDimension());
                if(r == null) {
                    if(command[1].equalsIgnoreCase("global")) {
                        r = Config.get().getGlobalSettings();
                    }
                    else {
                        MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
                        return;
                    }
                }
                player.sendMessage(ColorManager.LightGray + "---- " + r.getName() + " ----");
                String[] lines = r.getFlagList().split(";");
                for(String str : lines) {
                    player.sendMessage(str);
                }
            }
        }
    }
}
