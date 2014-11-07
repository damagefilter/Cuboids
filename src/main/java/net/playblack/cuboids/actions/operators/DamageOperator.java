package net.playblack.cuboids.actions.operators;

import net.playblack.cuboids.actions.ActionHandler;
import net.playblack.cuboids.actions.ActionListener;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.EntityDamageEvent;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.CLocation;

public class DamageOperator implements ActionListener {

    public boolean mobCanDoDamage(CLocation l) {
        Region r = RegionManager.get().getActiveRegion(l, false);
        return r.getProperty("mob-damage") != Status.DENY;
    }

    public boolean playerCanDoDamage(CLocation l) {
        Region r = RegionManager.get().getActiveRegion(l, false);
        if (r.getProperty("pvp-damage") == Status.DENY) {
            return false;
        }
        else if (r.getProperty("animal-damage") == Status.DENY) {
            return false;
        }
        return true;
    }

    @ActionHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getAttacker().isMob() || event.getAttacker().isAnimal()) {
            if (!mobCanDoDamage(new CLocation(event.getDefender().getLocation()))) {
                event.cancel();
            }
        }
        if (event.getAttacker().isPlayer()) {
            if (!playerCanDoDamage(new CLocation(event.getDefender().getLocation()))) {
                event.cancel();
            }
        }
    }

    static {
        ActionManager.registerActionListener("Cuboids", new DamageOperator());
    }
}
