package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;

/**
 * Toggle an area property of a cuboid
 * @author Chris
 *
 */
public class ToggleAreaProperty extends CBaseCommand {

    public ToggleAreaProperty() {
        super("Toggle Area properties: /cmod <area> toggle <property>", 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        //cmod AREA toggle PROPERTY
        if(!parseCommand(player, command)) {
            return;
        }
        //now, mega über super if monster!!! :D
        if(command[3].equalsIgnoreCase("creeper")) {
            CuboidInterface.getInstance().toggleCreeperSecure(player, command[1]);
            return;
        }
        
        if(command[3].equalsIgnoreCase("farmland") || command[3].equalsIgnoreCase("farm")) {
            CuboidInterface.getInstance().toggleFarmland(player, command[1]);
            return;
        }
        
        if(command[3].equalsIgnoreCase("fire") || command[3].equalsIgnoreCase("firespread")) {
            CuboidInterface.getInstance().toggleFireSpread(player, command[1]);
            return;
        }
        
        if(command[3].equalsIgnoreCase("water") || command[3].equalsIgnoreCase("watercontrol")) {
            CuboidInterface.getInstance().toggleFlowControl(player, command[1], false);
            return;
        }
        
        if(command[3].equalsIgnoreCase("lava") || command[3].equalsIgnoreCase("lavacontrol")) {
            CuboidInterface.getInstance().toggleFlowControl(player, command[1], true);
            return;
        }
        
        if(command[3].equalsIgnoreCase("freebuild") || command[3].equalsIgnoreCase("creative")) {
            CuboidInterface.getInstance().toggleFreebuild(player, command[1]);
            return;
        }
        
        if(command[3].equalsIgnoreCase("heal") || command[3].equalsIgnoreCase("healing")) {
            CuboidInterface.getInstance().toggleHealing(player, command[1]);
            return;
        }
        
        if(command[3].equalsIgnoreCase("hmob") || command[3].equalsIgnoreCase("hmobs")) {
            CuboidInterface.getInstance().toggleHmobs(player, command[1]);
            return;
        }
        
        if(command[3].equalsIgnoreCase("protection") || command[3].equalsIgnoreCase("protect")) {
            CuboidInterface.getInstance().toggleProtection(player, command[1]);
            return;
        }
        
        if(command[3].equalsIgnoreCase("pvp")) {
            CuboidInterface.getInstance().togglePvp(player, command[1]);
            return;
        }
        
        if(command[3].equalsIgnoreCase("restriction") || command[3].equalsIgnoreCase("restrict")) {
            CuboidInterface.getInstance().toggleRestriction(player, command[1]);
            return;
        }
        
        if(command[3].equalsIgnoreCase("sanctuary")) {
            CuboidInterface.getInstance().toggleSanctuary(player, command[1]);
            return;
        }
        
        if(command[3].equalsIgnoreCase("sanctuary-animalspawn") || command[3].equalsIgnoreCase("animalspawn")) {
            CuboidInterface.getInstance().toggleSanctuaryAnimalSpawn(player, command[1]);
            return;
        }
        
        if(command[3].equalsIgnoreCase("tnt")) {
            CuboidInterface.getInstance().toggleTntSecure(player, command[1]);
            return;
        }
    }
}
