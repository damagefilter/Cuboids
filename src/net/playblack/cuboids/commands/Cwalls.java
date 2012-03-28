package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.blockoperators.WallsGenerator;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;

/**
 * Create walls along a cuboid selection
 * @author Chris
 *
 */
public class Cwalls extends BaseCommand {

    private boolean onlyWalls;
    
    public Cwalls(String variant) {
        super("Create walls/faces: "+variant+" <wall>:[data] [floor]:[data] [ceiling]:[data]", 2, 4);
        if(variant.equalsIgnoreCase("/cwalls")) {
            onlyWalls = true;
        }
        else {
            onlyWalls = false;
        }
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
        //Check for the proper permissions
        MessageSystem ms = MessageSystem.getInstance();
        if(!player.hasPermission("cIgnoreRestrictions")) {
            if(!player.hasPermission("cWorldMod")) {
                ms.failMessage(player, "permissionDenied");
                return;
            }
        }
        
        //create a new template block
        CBlock walls = CBlock.parseBlock(command[1]);
        CBlock floor = null;
        CBlock ceiling = null;
        if(walls == null) {
            ms.failMessage(player, "invalidBlock");
            return;
        }
        if(command.length == 3) {
            floor = CBlock.parseBlock(command[2]);
        }
        else if(command.length == 4) {
            floor = CBlock.parseBlock(command[2]);
            ceiling = CBlock.parseBlock(command[3]);
        }
        else {
            floor = walls;
            ceiling = walls;
        }
        
        //prepare the selection
        CuboidSelection template = SelectionManager.getInstance().getPlayerSelection(player.getName());
        if(!template.getBlockList().isEmpty()) {
            template.clearBlocks();
        }
        
        //Create the block generator
        WallsGenerator gen = new WallsGenerator(template, player.getWorld());
        gen.setWallMaterial(walls);
        gen.setCeilingMaterial(ceiling);
        gen.setFloorMaterial(floor);
        gen.setWallsOnly(onlyWalls);
        
        if(gen.execute(player, true)) {
            if(onlyWalls) {
                ms.successMessage(player, "wallsBuild");
            }
            else {
                ms.successMessage(player, "facesBuild");
            }
        }
        else {
            ms.failMessage(player, "selectionIncomplete");
            ms.failMessage(player, "wallsNotBuild");
        }
        return;
    }
}
