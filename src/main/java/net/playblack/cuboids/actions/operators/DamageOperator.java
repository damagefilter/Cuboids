package net.playblack.cuboids.actions.operators;

import net.canarymod.api.entity.Entity;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.cuboids.regions.RegionManager;

public class DamageOperator {

    public boolean mobCanDoDamage(Location l) {
        Region r = RegionManager.get().getActiveRegion(l, false);
        return r.getProperty("mob-damage") != Status.DENY;
    }

    public boolean playerCanDoDamage(Location l) {
        Region r = RegionManager.get().getActiveRegion(l, false);
        if (r.getProperty("pvp-damage") == Status.DENY) {
            return false;
        }
        else if (r.getProperty("animal-damage") == Status.DENY) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if damage can NOT be done and hook needs to be canceled
     *
     * @param attacker
     * @param defender
     * @return
     */
    public boolean onDamage(Entity attacker, Entity defender) {
        if (attacker.isMob() || attacker.isAnimal()) {
            if (!mobCanDoDamage(defender.getLocation())) {
                return true;
            }
        }
        if (attacker.isPlayer()) {
            if (!playerCanDoDamage(defender.getLocation())) {
                return true;
            }
        }
        return false;
    }
}
