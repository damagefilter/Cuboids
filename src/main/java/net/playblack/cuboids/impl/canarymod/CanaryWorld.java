package net.playblack.cuboids.impl.canarymod;

import java.util.ArrayList;

import net.canarymod.Canary;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.world.DimensionType;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.Chest;
import net.canarymod.api.world.blocks.DoubleChest;
import net.canarymod.api.world.blocks.Sign;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.blocks.CItem;
import net.playblack.cuboids.blocks.ChestBlock;
import net.playblack.cuboids.blocks.SignBlock;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.mcutils.Vector;


public class CanaryWorld extends CWorld {
    private World world;
    private ArrayList<CBlock> blockCache = new ArrayList<CBlock>(20);

    public CanaryWorld(World world) {
        this.world = world;
    }

    @Override
    public String getName() {
        return world.getName();
    }

    @Override
    public int getDimension() {
        return world.getType().getId();
    }

    //Needs to get removed...
    @Override
    public int getId() {
        return world.getType().getId();
    }

    @Override
    public CBlock getBlockAt(Vector position) {
        return getBlockAt(position.getBlockX(), position.getBlockY(), position.getBlockZ());
    }

    @Override
    protected CBlock getBlockAt(int x, int y, int z) {
        Block b = world.getBlockAt(x, y, z);
        // Hey, are we a chest?
        if (b.getTypeId() == 54) {
            Chest chest = (Chest) b.getWorld().getOnlyComplexBlock(b);
            if (chest != null) {
                ChestBlock bc = new ChestBlock();
                if (chest.hasAttachedChest()) {
                    DoubleChest dchest = chest.getDoubleChest();
                    bc.putItemList(itemsToArrayList(dchest.getContents()));
                    bc.setData((byte) dchest.getBlock().getData());
                } else {
                    bc.putItemList(itemsToArrayList(chest.getContents()));
                    bc.setData((byte) chest.getBlock().getData());
                }
                return bc;
            }
            return recycleBlock((short) 54, (byte) 0);// new CBlock(54, 0);
                                                      // //fallback, empty chest
        }

        // Or maybe ... a sign?
        else if (b.getTypeId() == 63) {
            Sign sign = (Sign) b.getWorld().getComplexBlock(b);
            // 4 lines per sign, eh?
            String[] text = new String[4];
            text[0] = sign.getTextOnLine(0);
            text[1] = sign.getTextOnLine(1);
            text[2] = sign.getTextOnLine(2);
            text[3] = sign.getTextOnLine(3);
            SignBlock block = new SignBlock(text);
            block.setData((byte) sign.getBlock().getData());
            return block;
        }
        // or are we a simple world block?
        else {
            // return new CBlock((short)b.getType(), (byte)b.getData());
            return recycleBlock((short) b.getTypeId(), (byte) b.getData());
        }
    }

    @Override
    public boolean isChunkLoaded(Vector position) {
        return world.isChunkLoaded(position.getBlockX(), position.getBlockZ());
    }

    @Override
    public boolean isChunkLoaded(int x, int y, int z) {
        return world.isChunkLoaded(x, y, z);
    }

    @Override
    public void loadChunk(Vector position) {
        loadChunk(position.getBlockX(), position.getBlockY(), position.getBlockZ());
    }

    @Override
    public void loadChunk(int x, int y, int z) {
        world.loadChunk(x, z);

    }

    @Override
    public long getTime() {
        return world.getRawTime();
    }

    @Override
    public int getHighestBlock(int x, int z) {
        return world.getHighestBlockAt(x, z);
    }

    @Override
    public void setBlockAt(short type, byte data, Vector v) {
        if (BlockType.fromId(type) == null) { // Invalid Block!
            return;
        }

        world.setBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ(), type);
        if (getBlockAt(v).getData() != data) {
            world.setDataAt(v.getBlockX(), v.getBlockY(), v.getBlockZ(), data);
        }

    }

    @Override
    public void setBlockAt(short type, Vector v) {
        setBlockAt(type, (byte) 0, v);
    }

    @Override
    public void setBlockAt(Vector v, CBlock block) {
        setBlockAt(block.getType(), block.getData(), v);
        Block test = world.getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ());
        if (block instanceof ChestBlock) {
            ChestBlock c = (ChestBlock) block;
            Chest chest = (Chest) world.getOnlyComplexBlock(test);
            if (chest != null) {
                if (chest.hasAttachedChest()) {
                    try {
                        DoubleChest dchest = chest.getDoubleChest();
                        dchest.getBlock().setData(c.getData());
                        dchest.clearContents(); // aha! nifty trick to avoid
                                                // item duping in chests while
                                                // recovering :3
                        dchest.setContents(itemsToArray(c.getItemList()));
                        dchest.update();
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                } else {
                    try {
                        chest.getBlock().setData(c.getData());
                        chest.setContents(itemsToArray(c.getItemList()));
                        chest.update();
                    } catch (ArrayIndexOutOfBoundsException f) {

                    }
                }
            }
        }

        if (block instanceof SignBlock) {
            SignBlock c = (SignBlock) block;
            try {
                Sign sign = (Sign) world.getComplexBlock(test);
                for (int i = 0; i < c.getSignTextArray().length; i++) {
                    sign.setTextOnLine(c.getTextOnLine(i), i);
                }
                sign.getBlock().setData(c.getData());
                sign.update();
            } catch (ClassCastException e) {

            }
        }

    }

    @Override
    public void setBlockData(byte data, Vector v) {
        world.setDataAt(v.getBlockX(), v.getBlockY(), v.getBlockZ(), data);
    }

    @Override
    public String dimensionFromId(int dim) {
        return DimensionType.fromId(dim).getName();
    }

    @Override
    public String getFilePrefix() {
        return world.getFqName();
    }

    @Override
    public boolean isCanaryModWorld() {
        return true;
    }

    @Override
    public boolean isBukkitWorld() {
        return false;
    }

    public World getHandle() {
        return world;
    }

    /**
    * Convert Canary Item list to Cuboids2 Item list
    */
   private ArrayList<CItem> itemsToArrayList(Item[] items) {
       if (items == null || items.length == 0) {
           return new ArrayList<CItem>(0);
       }

       ArrayList<CItem> newItems = new ArrayList<CItem>(items.length);
       for (Item i : items) {
           if (i != null) {
               newItems.add(new CItem(i.getId(), i.getDamage(), i.getAmount(), i.getSlot()));
           }
       }
       return newItems;
   }

   /**
    * Turn items to array
    *
    * @param items
    * @return
    */
   private Item[] itemsToArray(ArrayList<CItem> items) {
       if (items.isEmpty() || items.size() == 0) {
           return new Item[1];
       }
       Item[] nItem = new Item[items.size()];
       for (int i = 0; i < nItem.length; i++) {
           Item it = Canary.factory().getItemFactory().newItem(items.get(i).getId());

           if (items.size() > i) {
               it.setDamage(items.get(i).getData());
               it.setAmount(items.get(i).getAmount());
               it.setSlot(items.get(i).getSlot());
               nItem[i] = it;
           }

       }
       return nItem;
   }

   /**
    * Recycle a block instance to reduce load on the GC
    *
    * @param type
    * @param data
    * @return
    */
   private CBlock recycleBlock(short type, byte data) {
       for (CBlock block : blockCache) {
           if (block.equals(type, data)) {
               return block;
           }
       }
       if (blockCache.size() > 50) {
           while (blockCache.size() > 45) {
               blockCache.remove(0);
           }
       }
       CBlock toRet = new CBlock(type, data);
       blockCache.add(toRet);
       return toRet;
   }

}
