package net.playblack.cuboids.commands;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidE;
import net.playblack.cuboids.regions.RegionManager;

/**
 * Toggle global properties
 * @author Chris
 *
 */
public class ToggleGlobalProperty extends BaseCommand {

    public ToggleGlobalProperty() {
        super("Toggle Global properties: /cmod toggle <property>", 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
        if(!(player.hasPermission("cToggleGlobals") || player.hasPermission("cIgnoreRestrictions"))) {
            MessageSystem.getInstance().failMessage(player, "permissionDenied");
            return;
        }
        
        CuboidE setting = Config.getInstance().getGlobalSettings();
        if(command[2].equalsIgnoreCase("pvp")) {
            if(setting.isAllowedPvp()) {
                setting.setAllowPvp(false);
                MessageSystem.getInstance().successMessage(player, "globalPvpOff");
            } else {
                setting.setAllowPvp(true);
                MessageSystem.getInstance().successMessage(player, "globalPvpOn");
            }
        }
        
        if(command[2].equalsIgnoreCase("creeper")) {
            if(setting.isCreeperSecure()) {
                setting.setCreeperSecure(false);
                MessageSystem.getInstance().successMessage(player, "globalCreeperOff");
            } else {
                setting.setCreeperSecure(true);
                MessageSystem.getInstance().successMessage(player, "globalCreeperOn");
            }
        }
        
        if(command[2].equalsIgnoreCase("sanctuary")) {
            if(setting.isSanctuary()) {
                setting.setSanctuary(false);
                MessageSystem.getInstance().successMessage(player, "globalSanctuaryOff");
            } else {
                setting.setSanctuary(true);
                MessageSystem.getInstance().successMessage(player, "globalSanctuaryOn");
            }
        }
        
        if(command[2].equalsIgnoreCase("animalspawn")) {
            if(setting.sanctuarySpawnAnimals()) {
                setting.setSanctuarySpawnAnimals(false);
                MessageSystem.getInstance().successMessage(player, "globalSanctuaryASOff");
            } else {
                setting.setSanctuarySpawnAnimals(true);
                MessageSystem.getInstance().successMessage(player, "globalSanctuaryASOn");
            }
        }
        
        if(command[2].equalsIgnoreCase("tnt")) {
            if(setting.isTntSecure()) {
                setting.setTntSecure(false);
                MessageSystem.getInstance().successMessage(player, "globalTntOff");
            } else {
                setting.setTntSecure(true);
                MessageSystem.getInstance().successMessage(player, "globalTntOn");
            }
        }
        
        if(command[2].equalsIgnoreCase("protection") || command[2].equalsIgnoreCase("protect")) {
            if(setting.isProtected()) {
                setting.setProtection(false);
                MessageSystem.getInstance().successMessage(player, "globalProtectionOff");
            } else {
                setting.setProtection(true);
                MessageSystem.getInstance().successMessage(player, "globalProtectionOn");
            }
        }
        if(command[2].equalsIgnoreCase("lava") || command[2].equalsIgnoreCase("lavaflow")) {
            if(setting.isLavaControl()) {
                setting.setLavaControl(false);
                MessageSystem.getInstance().successMessage(player, "globalLavaOff");
            } else {
                setting.setLavaControl(true);
                MessageSystem.getInstance().successMessage(player, "globalLavaOn");
            }
        }
        
        if(command[2].equalsIgnoreCase("water") || command[2].equalsIgnoreCase("waterflow")) {
            if(setting.isWaterControl()) {
                setting.setLavaControl(false);
                MessageSystem.getInstance().successMessage(player, "globalWaterOff");
            } else {
                setting.setWaterControl(true);
                MessageSystem.getInstance().successMessage(player, "globalWaterOn");
            }
        }
        Config.getInstance().updateGlobalSettings(setting);
        RegionManager.getInstance().updateGlobalSettings();
    }
}
