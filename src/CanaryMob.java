import net.playblack.cuboids.gameinterface.CMob;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.mcutils.Vector;

/**
 * Implements a CanaryMod Mob
 * @author Chris
 *
 */
public class CanaryMob extends CMob {
    private Mob mob;
    public CanaryMob(OEntityLiving entity) {
        mob = new Mob(entity);
    }
    public CanaryMob(Mob entity) {
        mob = entity;
    }
    @Override
    public int getHealth() {
        return mob.getHealth();
    }

    @Override
    public void setHealth(int health) {
        mob.setHealth(health);
    }

    @Override
    public String getName() {
        return mob.getName();
    }

    @Override
    public CWorld getWorld() {
        return null;
    }

    @Override
    public Vector getPosition() {
        return new Vector(mob.getX(),mob.getY(), mob.getZ());
    }

    @Override
    public void setPosition(Vector v) {
        mob.setX(v.getX());
        mob.setY(v.getY());
        mob.setZ(v.getZ());
    }

    @Override
    public double getX() {
        return mob.getX();
    }

    @Override
    public double getY() {
        return mob.getY();
    }

    @Override
    public double getZ() {
        return mob.getZ();
    }

    @Override
    public void kill() {
        mob.setHealth(0);
    }

    @Override
    public void spawn() {
        mob.spawn();
    }

    @Override
    public void setX(double x) {
        mob.setX(x);

    }

    @Override
    public void setY(double y) {
        mob.setY(y);

    }

    @Override
    public void setZ(double z) {
        mob.setZ(z);

    }

}
