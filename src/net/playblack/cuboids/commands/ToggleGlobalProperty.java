package net.playblack.cuboids.commands;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidE;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Toggle global properties
 * 
 * @author Chris
 * 
 */
public class ToggleGlobalProperty extends CBaseCommand {

    public ToggleGlobalProperty() {
        super("Toggle Global properties:" + ColorManager.Yellow
                + " /cmod toggle <property>", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        if (!(player.hasPermission("cToggleGlobals") || player
                .hasPermission("cIgnoreRestrictions"))) {
            MessageSystem.getInstance().failMessage(player, "permissionDenied");
            return;
        }

        CuboidE setting = Config.getInstance().getGlobalSettings();
        if (command[2].equalsIgnoreCase("pvp")) {
            if (setting.isAllowedPvp()) {
                setting.setAllowPvp(false);
                MessageSystem.getInstance().successMessage(player,
                        "globalPvpOff");
            } else {
                setting.setAllowPvp(true);
                MessageSystem.getInstance().successMessage(player,
                        "globalPvpOn");
            }
        }

        else if (command[2].equalsIgnoreCase("creeper")) {
            if (setting.isCreeperSecure()) {
                setting.setCreeperSecure(false);
                MessageSystem.getInstance().successMessage(player,
                        "globalCreeperOff");
            } else {
                setting.setCreeperSecure(true);
                MessageSystem.getInstance().successMessage(player,
                        "globalCreeperOn");
            }
        }

        else if (command[2].equalsIgnoreCase("sanctuary")) {
            if (setting.isSanctuary()) {
                setting.setSanctuary(false);
                MessageSystem.getInstance().successMessage(player,
                        "globalSanctuaryOff");
            } else {
                setting.setSanctuary(true);
                MessageSystem.getInstance().successMessage(player,
                        "globalSanctuaryOn");
            }
        }

        else if (command[2].equalsIgnoreCase("animalspawn")) {
            if (setting.sanctuarySpawnAnimals()) {
                setting.setSanctuarySpawnAnimals(false);
                MessageSystem.getInstance().successMessage(player,
                        "globalSanctuaryASOff");
            } else {
                setting.setSanctuarySpawnAnimals(true);
                MessageSystem.getInstance().successMessage(player,
                        "globalSanctuaryASOn");
            }
        }

        else if (command[2].equalsIgnoreCase("tnt")) {
            if (setting.isTntSecure()) {
                setting.setTntSecure(false);
                MessageSystem.getInstance().successMessage(player,
                        "globalTntOff");
            } else {
                setting.setTntSecure(true);
                MessageSystem.getInstance().successMessage(player,
                        "globalTntOn");
            }
        }

        else if (command[2].equalsIgnoreCase("protection")
                || command[2].equalsIgnoreCase("protect")) {
            if (setting.isProtected()) {
                setting.setProtection(false);
                MessageSystem.getInstance().successMessage(player,
                        "globalProtectionOff");
            } else {
                setting.setProtection(true);
                MessageSystem.getInstance().successMessage(player,
                        "globalProtectionOn");
            }
        }

        else if (command[2].equalsIgnoreCase("fire")
                || command[2].equalsIgnoreCase("firespread")) {
            if (setting.isBlockFireSpread()) {
                setting.setBlockFireSpread(false);
                MessageSystem.getInstance().successMessage(player,
                        "globalFirespreadOff");
            } else {
                setting.setBlockFireSpread(true);
                MessageSystem.getInstance().successMessage(player,
                        "globalFirespreadOn");
            }
        }

        else if (command[2].equalsIgnoreCase("lava")
                || command[2].equalsIgnoreCase("lavaflow")) {
            if (setting.isLavaControl()) {
                setting.setLavaControl(false);
                MessageSystem.getInstance().successMessage(player,
                        "globalLavaOff");
            } else {
                setting.setLavaControl(true);
                MessageSystem.getInstance().successMessage(player,
                        "globalLavaOn");
            }
        }

        else if (command[2].equalsIgnoreCase("water")
                || command[2].equalsIgnoreCase("waterflow")) {
            if (setting.isWaterControl()) {
                setting.setLavaControl(false);
                MessageSystem.getInstance().successMessage(player,
                        "globalWaterOff");
            } else {
                setting.setWaterControl(true);
                MessageSystem.getInstance().successMessage(player,
                        "globalWaterOn");
            }
        }

        else if (command[2].equalsIgnoreCase("physics")) {
            if (setting.isPhysicsDisabled()) {
                setting.setPhysics(false);
                MessageSystem.getInstance().successMessage(player,
                        "globalPhysicsControlOff");
            } else {
                setting.setPhysics(true);
                MessageSystem.getInstance().successMessage(player,
                        "globalPhysicsControlOn");
            }
        }

        else if (command[2].equalsIgnoreCase("enderman")
                || command[2].equalsIgnoreCase("endercontrol")) {
            if (setting.hasEnderControl()) {
                setting.setEnderControl(false);
                MessageSystem.getInstance().successMessage(player,
                        "globalEnderControlOff");
            } else {
                setting.setEnderControl(true);
                MessageSystem.getInstance().successMessage(player,
                        "globalEnderControlOn");
            }
        } else {
            MessageSystem.getInstance().failMessage(player,
                    "invalidGlobalProperty");
            return; // prevent config updating - not needed here
        }
        Config.getInstance().updateGlobalSettings(setting);
        RegionManager.getInstance().updateGlobalSettings();
    }
}
