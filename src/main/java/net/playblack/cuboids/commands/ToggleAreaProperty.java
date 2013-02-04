package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Toggle an area property of a cuboid
 * 
 * @author Chris
 * 
 */
public class ToggleAreaProperty extends CBaseCommand {

    public ToggleAreaProperty() {
        super("Toggle Area properties:" + ColorManager.Yellow
                + " /cmod <area> toggle <property>", 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        // cmod AREA toggle PROPERTY
        if (!parseCommand(player, command)) {
            return;
        }
        // now, mega über super if monster!!! :D
        if (command[3].equalsIgnoreCase("creeper")) {
            CuboidInterface.get().toggleCreeperSecure(player,
                    command[1]);
            return;
        }

        else if (command[3].equalsIgnoreCase("farmland")
                || command[3].equalsIgnoreCase("farm")) {
            CuboidInterface.get().toggleFarmland(player, command[1]);
            return;
        }

        else if (command[3].equalsIgnoreCase("fire")
                || command[3].equalsIgnoreCase("firespread")) {
            CuboidInterface.get().toggleFireSpread(player, command[1]);
            return;
        }

        else if (command[3].equalsIgnoreCase("water")
                || command[3].equalsIgnoreCase("watercontrol")) {
            CuboidInterface.get().toggleFlowControl(player, command[1],
                    false);
            return;
        }

        else if (command[3].equalsIgnoreCase("lava")
                || command[3].equalsIgnoreCase("lavacontrol")) {
            CuboidInterface.get().toggleFlowControl(player, command[1],
                    true);
            return;
        }

        else if (command[3].equalsIgnoreCase("freebuild")
                || command[3].equalsIgnoreCase("creative")) {
            CuboidInterface.get().toggleFreebuild(player, command[1]);
            return;
        }

        else if (command[3].equalsIgnoreCase("heal")
                || command[3].equalsIgnoreCase("healing")) {
            CuboidInterface.get().toggleHealing(player, command[1]);
            return;
        }

        else if (command[3].equalsIgnoreCase("hmob")
                || command[3].equalsIgnoreCase("hmobs")) {
            CuboidInterface.get().toggleHmobs(player, command[1]);
            return;
        }

        else if (command[3].equalsIgnoreCase("protection")
                || command[3].equalsIgnoreCase("protect")) {
            CuboidInterface.get().toggleProtection(player, command[1]);
            return;
        }

        else if (command[3].equalsIgnoreCase("pvp")) {
            CuboidInterface.get().togglePvp(player, command[1]);
            return;
        }

        else if (command[3].equalsIgnoreCase("restriction")
                || command[3].equalsIgnoreCase("restrict")) {
            CuboidInterface.get().toggleRestriction(player, command[1]);
            return;
        }

        else if (command[3].equalsIgnoreCase("sanctuary")) {
            CuboidInterface.get().toggleSanctuary(player, command[1]);
            return;
        }

        else if (command[3].equalsIgnoreCase("sanctuary-animalspawn")
                || command[3].equalsIgnoreCase("animalspawn")) {
            CuboidInterface.get().toggleSanctuaryAnimalSpawn(player,
                    command[1]);
            return;
        }

        else if (command[3].equalsIgnoreCase("tnt")) {
            CuboidInterface.get().toggleTntSecure(player, command[1]);
            return;
        }

        else if (command[3].equalsIgnoreCase("physics")) {
            CuboidInterface.get().togglePhysics(player, command[1]);
            return;
        }

        else if (command[3].equalsIgnoreCase("enderman")
                || command[3].equalsIgnoreCase("endercontrol")) {
            CuboidInterface.get()
                    .toggleEnderControl(player, command[1]);
            return;
        } else {
            MessageSystem.getInstance().failMessage(player,
                    "invalidAreaProperty");
            return;
        }
    }
}
