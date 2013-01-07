import net.playblack.cuboids.blocks.CItem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.mcutils.Vector;

public class CanaryPlayer extends CPlayer {
    Player player;
    CanaryWorld world;

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
    public void setCreative(int creative) {
        player.setCreativeMode(creative);
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
    public CItem[] getInventory() {
        Item[] canaryItems = player.getInventory().getContents();
        CItem[] items = new CItem[canaryItems.length];
        for (int i = 0; i < canaryItems.length; i++) {
            if (canaryItems[i] == null) {
                continue;
            }
            items[i] = new CItem(canaryItems[i].getItemId(),
                    canaryItems[i].getDamage(), canaryItems[i].getAmount(),
                    canaryItems[i].getSlot());
        }
        return items;
    }

    @Override
    public void setInventory(CItem[] items) {
        if (items == null) {
            player.getInventory().clearContents();
            return;
        }
        Item[] canaryItems = new Item[items.length];
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                continue;
            }
            canaryItems[i] = new Item(items[i].getId(), items[i].getAmount(),
                    items[i].getSlot(), items[i].getData());
        }
        try {
            player.getInventory().setContents(canaryItems);
        } catch (ArrayIndexOutOfBoundsException e) {
            // might happen because of derp
        }
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

}
