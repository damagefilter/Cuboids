
import java.util.HashMap;

import net.playblack.cuboids.blocks.CItem;
import net.playblack.cuboids.gameinterface.CInventory;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.mcutils.Debug;
import net.playblack.mcutils.Vector;

public class CanaryPlayer extends CPlayer {
    private Player player;
    private CanaryWorld world;
    private HashMap<Integer, CInventory> inventories = new HashMap<Integer, CInventory>();
    
    public CanaryPlayer(Player p) {
        this.player = p;
        world = new CanaryWorld(p.getWorld());
    }

    @Override
    public int getHealth() {
        return player.getHealth();
    }

    @Override
    public void setHealth(int health) {
        player.setHealth(health);

    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public CWorld getWorld() {
        // Player has switched worlds
        if (player.getWorld().getName() != player.getWorld().getName()) {
            this.world = new CanaryWorld(player.getWorld());
        } else if (world.getDimension() != player.getWorld().getType().getId()) {
            this.world = new CanaryWorld(player.getWorld());
        }
        return world;
    }

    @Override
    public Vector getPosition() {
        return new Vector(player.getX(), player.getY(), player.getZ());
    }

    @Override
    public void setPosition(Vector v) {
        player.teleportTo(v.getX(), v.getY(), v.getZ(), player.getRotation(),
                player.getPitch());
    }

    @Override
    public double getX() {
        return player.getX();
    }

    @Override
    public double getY() {
        return player.getY();
    }

    @Override
    public double getZ() {
        return player.getZ();
    }

    @Override
    public void sendMessage(String message) {
        if (message.length() <= 0 || message.equalsIgnoreCase("null")) {
            return;
        }
        player.sendMessage(message);

    }

    @Override
    public void notify(String message) {
        player.notify(message);

    }

    @Override
    public boolean hasPermission(String permission) {
        return player.canUseCommand("/" + permission);
    }

    @Override
    public CItem getItemInHand() {
        Item i = player.getItemStackInHand();
        if (i == null) {
            return new CItem(0, (short) 0, 0);
        }
        return new CItem(i.getItemId(), (short) i.getDamage(), i.getSlot());
    }

    @Override
    public String[] getGroups() {
        return player.getGroups();
    }

    @Override
    public void setGameMode(int creative) {
        setInventoryForMode(getCurrentInventory(), getGameMode());
        player.setCreativeMode(creative);
        setInventory(getInventory(creative));
        
    }

    @Override
    public boolean isInCreativeMode() {
        return player.getCreativeMode() == 1;
    }

    @Override
    public void clearInventory() {
        player.getInventory().clearContents();
    }

    @Override
    public CInventory getInventory(int mode) {
        if(!inventories.containsKey(mode)) {
            inventories.put(mode, new CanaryInventory());
        }
        return inventories.get(mode);
    }

    @Override
    public void setInventory(CInventory items) {
        if (items == null) {
            player.getInventory().clearContents();
            return;
        }
        try {
            if(items.hasItems()) {
                ((CanaryInventory)items).setThisContents();
            }
            else {
                player.getInventory().clearContents();
            }
            
        } catch (ArrayIndexOutOfBoundsException e) {
            Debug.logStack(e);
        }
    }
    
    @Override
    public void setInventoryForMode(CInventory inv, int mode) {
        inventories.put(mode, inv);
    }

    @Override
    public void teleportTo(Vector v) {
        Location location = new Location(player.getWorld(), v.getX(), v.getY(),
                v.getZ(), player.getRotation(), player.getPitch());
        if (!this.world.isChunkLoaded(v)) {
            this.world.loadChunk(v);
        }
        player.teleportTo(location);

    }

    public String toString() {
        return new StringBuilder().append("Wrapped player: ")
                .append(player.getName()).append("\n").append("Wrapper: ")
                .append(this.getClass().getSimpleName()).toString();
    }

    @Override
    public net.playblack.mcutils.Location getLocation() {
        return new net.playblack.mcutils.Location((int) getX(), (int) getY(), (int) getZ(),
                getWorld().getName());
    }

    @Override
    public boolean isAdmin() {
        return player.isAdmin();
    }

    @Override
    public CInventory getCurrentInventory() {
        return new CanaryInventory(player.getInventory());
    }

    @Override
    public double getPitch() {
        return player.getPitch();
    }

    @Override
    public double getRotation() {
        return player.getRotation();
    }
    
    @Override
    public boolean isMob() {
        return player.isMob();
    }

    @Override
    public boolean isAnimal() {
        return player.isAnimal();
    }
    
    @Override
    public boolean isPlayer() {
        return player.isPlayer();
    }

    @Override
    public int getGameMode() {
        return player.getCreativeMode();
    }

}
