package net.playblack.cuboids.actions;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.Vector;

public class HandleDamage {

    /**
     * Returns true if a mob can damage a player
     * @param attacker
     * @param defender
     * @return
     */
    public static boolean handleMobDamage(Vector position, CWorld world) {
        if(CuboidInterface.getInstance().isSanctuary(position, world)) {
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
        if(!CuboidInterface.getInstance().isPvpEnabled(defender.getPosition(), defender.getWorld())) {
            if(attacker.hasPermission("cIgnoreRestrictions")) {
                return true;
            }
            return false;
        }
        return true;
    }
}
