package net.playblack.cuboids.actions;

import net.playblack.cuboids.gameinterface.CMob;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.Vector;

public class MiscHandler {

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
    
    /**
     * Return true if mob can spawn, false otherwise
     * @param mob
     * @return
     */
    public static boolean canSpawn(CMob mob) {
        if(mob.isMob()) {
            return !CuboidInterface.getInstance().isSanctuary(mob.getPosition(), mob.getWorld());
        }
        else if(mob.isAnimal()) {
            return CuboidInterface.getInstance().sanctuarySpawnsAnimals(mob);
        }
        else {
            return true;
        }
    }
}
