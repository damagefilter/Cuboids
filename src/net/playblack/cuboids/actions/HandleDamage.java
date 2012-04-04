package net.playblack.cuboids.actions;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.Vector;

public class HandleDamage {

    /**
     * Returns true if a mob can damage a player
     * @param attacker
     * @param defender
     * @return
     */
    public static boolean handleMobDamage(Vector position, CWorld world) {
        boolean sanctuary = RegionManager.getInstance()
                .getActiveCuboid(position, world.getName(), world.getDimension())
                .getCuboid()
                .isSanctuary();
        if(sanctuary) {
            return false;
        }
        return true;
    }
    
    /**
     * Return true if the attacker can dish out the damage, false otherwise
     * @param attacker
     * @param defender
     * @return
     */
    public static boolean handlePvpDamage(CPlayer attacker, CPlayer defender) {
        boolean pvpSecure = RegionManager.getInstance()
                .getActiveCuboid(defender.getPosition(), defender.getWorld().getName(), defender.getWorld().getDimension())
                .getCuboid()
                .isAllowedPvp();
        if(pvpSecure) {
            if(attacker.hasPermission("cIgnoreRestrictions")) {
                return true;
            }
            return false;
        }
        return true;
    }
}
