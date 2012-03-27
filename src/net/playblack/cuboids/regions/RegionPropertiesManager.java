package net.playblack.cuboids.regions;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CMob;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.IBaseEntity;
import net.playblack.mcutils.EventLogger;
import net.playblack.mcutils.Vector;

/**
 * RegionPropertiesManager contains helpers to check if a person or block is inside a region
 * and what properties that region may got from a cuboidnode.
 * This manager only checks for properties, it does not change the state of anything.
 * @author Chris
 *
 */
public class RegionPropertiesManager {
    RegionManager regions;
    ScheduledExecutorService threadManager = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    EventLogger log;
    Object lock;
    
    public RegionPropertiesManager(RegionManager rm, EventLogger log) {
        regions = rm;
        this.log = log;
    }
    
    /**
     * Check if a player is allowed to do damage to other players
     * @param player
     * @return true if we can attack, false otherwise
     */
    public boolean canAttack(IBaseEntity attacker, IBaseEntity defender) {
        CuboidE cube = regions.getActiveCuboid(defender.getPosition(), defender.getWorld().getName()).getCuboid();
        //PVP allowed. alright.
        if(cube.isAllowedPvp()) {
            return true;
        }
        if(defender instanceof CMob){
            //We can ALWAYS hurt animals etc - sounds evil
            return true;
        }
        //PVP is not allowed, check for permissions if the attacker can ignore that fact
        else {
            if((attacker instanceof CPlayer) && (defender instanceof CPlayer)) {
                CPlayer pAttacker = (CPlayer)attacker;
                if(!pAttacker.hasPermission("cIgnoreRestrictions")) {
                    return true;
                }
            }
            return false;
        }
    }
    
    /**
     * Check if the thing at the given position is fireproof.
     * @param position
     * @param world
     * @return True if fireproof, false otherwise
     */
    public boolean isFireProof(Vector position, String world) {
        CuboidE cube = regions.getActiveCuboid(position, world).getCuboid();
        return cube.isBlockFireSpread();
    }
    
    /**
     * Check if a position is creeper secure.
     * TODO: Radius check!
     * @param position
     * @param world
     * @return
     */
    public boolean isCreeperSecure(Vector position, String world) {
        CuboidE cube = regions.getActiveCuboid(position, world).getCuboid();
        return cube.isCreeperSecure();
    }
    
    /**
     * Check if this position is restricted
     * @param position
     * @param world
     * @return
     */
    public boolean isRestricted(Vector position, String world) {
        CuboidE cube = regions.getActiveCuboid(position, world).getCuboid();
        return cube.isRestricted();
    }
    
    /**
     * Check if region is a farmland area
     * @param position
     * @param world
     * @return
     */
    public boolean isFarmland(Vector position, String world) {
        CuboidE cube = regions.getActiveCuboid(position, world).getCuboid();
        return cube.isFarmland();
    }
    
    /**
     * Check if region is a creative-mode area
     * @param position
     * @param world
     * @return
     */
    public boolean isFreebuild(Vector position, String world) {
        CuboidE cube = regions.getActiveCuboid(position, world).getCuboid();
        return cube.isFreeBuild();
    }
    
    /**
     * Check if region is a sanctuary
     * @param position
     * @param world
     * @return
     */
    public boolean isSanctuary(Vector position, String world) {
        CuboidE cube = regions.getActiveCuboid(position, world).getCuboid();
        return cube.isSanctuary();
    }
    
    /**
     * Check if region is a sanctuary that can spawn animals!
     * @param position
     * @param world
     * @return
     */
    public boolean isSanctuaryWithAnimals(Vector position, String world) {
        CuboidE cube = regions.getActiveCuboid(position, world).getCuboid();
        return cube.sanctuarySpawnAnimals();
        }
        
    
    /**
     * Check if this position is in some way tnt secured
     * @param position
     * @param world
     * @return
     */
    public boolean isTntSecure(Vector position, String world) {
        CuboidE cube = regions.getActiveCuboid(position, world).getCuboid();
        return cube.isTntSecure();
    }
    
    /**
     * Check if the block that wants to flow is in a flow-controlled region
     * @param position
     * @param block
     * @param world
     * @return
     */
    public boolean hasFlowControl(Vector position, CBlock block, String world) {
        //Lava blocks
        if(block.getType()== 10 || block.getType() == 11) {
            return regions.getActiveCuboid(position, world).getCuboid().isLavaControl();
        }
        //Water blocks
        if(block.getType() == 8 || block.getType() == 9) {
            return regions.getActiveCuboid(position, world).getCuboid().isWaterControl();
        }
        return false; //That should actually never happen
    }
    
    public boolean canModifyBlock(CPlayer player, CBlock block) {
        CuboidE cube = regions.getActiveCuboid(player.getPosition(), player.getWorld().getName()).getCuboid();
        if((player.hasPermission("cIgnoreRestrictions") || player.hasPermission("cAreaMod")) || (!cube.isProtected())) {
            //No further checks required
            return true;
        }
        //cube must be protected at this point
        return cube.playerIsAllowed(player.getName(), player.getGroups());
    }
}
