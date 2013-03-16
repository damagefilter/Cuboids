import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.gameinterface.IBaseEntity;
import net.playblack.mcutils.Location;
import net.playblack.mcutils.Vector;

//NOTE: This whole base entity thing inheritance is really bad executed and should be fixed some day!
public class CanaryBaseEntity implements IBaseEntity {
    BaseEntity ent;
    CanaryWorld w;
    public CanaryBaseEntity(BaseEntity ent) {
        this.ent = ent;
        w = new CanaryWorld(ent.getWorld());
    }
    @Override
    public int getHealth() {
        return 0;
    }

    @Override
    public void setHealth(int health) { }

    @Override
    public String getName() {
        return ent.getName();
    }

    @Override
    public CWorld getWorld() {
        if(w.getHandle() != ent.getWorld()) {
            w = new CanaryWorld(ent.getWorld());
        }
        return w;
    }

    @Override
    public Vector getPosition() {
        return new Vector(ent.getX(), ent.getY(), ent.getZ());
    }

    @Override
    public Location getLocation() {
        return new Location(ent.getX(), ent.getY(), ent.getZ(), w.getDimension(), w.getName());
    }

    @Override
    public void setPosition(Vector v) {
        ent.setX(v.getX());
        ent.setY(v.getY());
        ent.setZ(v.getZ());
    }

    @Override
    public double getX() {
        return ent.getX();
    }

    @Override
    public double getY() {
        return ent.getY();
    }

    @Override
    public double getZ() {
        return ent.getZ();
    }

    @Override
    public double getPitch() {
        return ent.getPitch();
    }

    @Override
    public double getRotation() {
        return ent.getRotation();
    }
    @Override
    public boolean isPlayer() {
        return ent.isPlayer();
    }
    @Override
    public boolean isMob() {
        return ent.isMob();
    }
    @Override
    public boolean isAnimal() {
        return ent.isAnimal();
    }

}
