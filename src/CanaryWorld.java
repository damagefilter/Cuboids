import java.util.ArrayList;

import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.blocks.CItem;
import net.playblack.cuboids.blocks.ChestBlock;
import net.playblack.cuboids.blocks.SignBlock;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.mcutils.Vector;


public class CanaryWorld extends CWorld {

    private World world;
    
    public CanaryWorld(World w) {
        world = w;
    }
    @Override
    public String getFqName() {
        //TODO: put FQ Name here once the stuff is clear!
        return world.getType().name();
    }

    @Override
    public int getId() {
        return world.getType().getId();
    }

    @Override
    public CBlock getBlockAt(Vector position) {
        return getBlockAt(position.getBlockX(), position.getBlockY(), position.getBlockZ());
    }

    @Override
    public CBlock getBlockAt(int x, int y, int z) {
        Block b = world.getBlockAt(x, y, z);
        //Hey, are we a chest?
        if(b.getType() == 54) {
            Chest chest = (Chest)b.getWorld().getOnlyComplexBlock(b);
            if(chest != null) {
                ChestBlock bc = new ChestBlock();
                if(chest.findAttachedChest() != null) {
                    DoubleChest dchest = chest.findAttachedChest();
                    bc.putItemList(itemsToArrayList(dchest.getContents()));
                    bc.setData((byte)dchest.getBlock().getData());
                }
                else {
                    bc.putItemList(itemsToArrayList(chest.getContents()));
                    bc.setData((byte)chest.getBlock().getData());
                }
                return bc;
            }
            return new CBlock(54, 0); //fallback, empty chest
        }
        
        //Or maybe ... a sign?
        else if(b.getType() == 63) {
            Sign sign = (Sign)b.getWorld().getComplexBlock(b);
            //4 lines per sign, eh?
            String[] text = new String[4];
            text[0] = sign.getText(0);
            text[1] = sign.getText(1);
            text[2] = sign.getText(2);
            text[3] = sign.getText(3);
            SignBlock block = new SignBlock(text);
            block.setData((byte)sign.getBlock().getData());
            return block;
        }
        //or are we a simple world block?
        else {
            return new CBlock((short)b.getType(), (byte)b.getData());
        }
    }

    @Override
    public boolean isChunkLoaded(Vector position) {
        return isChunkLoaded(position.getBlockX(), position.getBlockY(), position.getBlockZ());
    }

    @Override
    public boolean isChunkLoaded(int x, int y, int z) {
        return world.isChunkLoaded(x, z);
    }

    @Override
    public void loadChunk(Vector position) {
        loadChunk(position.getBlockX(),position.getBlockY(), position.getBlockZ());

    }

    @Override
    public void loadChunk(int x, int y, int z) {
        world.loadChunk(x, y, z);

    }

    @Override
    public long getTime() {
        return world.getRelativeTime();
    }

    @Override
    public int getHighestBlock(int x, int z) {
        return world.getHighestBlockY(x, z);
    }

    @Override
    public void setBlockAt(short type, byte data, Vector v) {
        world.setBlockAt(type, v.getBlockX(), v.getBlockY(), v.getBlockZ());
        if(getBlockAt(v).getData() != data) {
            world.setBlockData(v.getBlockX(), v.getBlockY(), v.getBlockZ(), data);
        }
    }

    @Override
    public void setBlockAt(short type, Vector v) {
        setBlockAt(type, (byte)0, v);

    }

    @Override
    public void setBlockAt(Vector v, CBlock block) {
        setBlockAt(block.getType(), block.getData(), v);
        Block test = world.getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ());
        if(block instanceof ChestBlock) {
            ChestBlock c = (ChestBlock)block;
            Chest chest = (Chest)world.getOnlyComplexBlock(test);
            if(chest != null) {
                if(chest.findAttachedChest() != null) {
                    try {
                        DoubleChest dchest = chest.findAttachedChest();
                        dchest.getBlock().setData(c.getData());
                        dchest.clearContents(); //aha! nifty trick to avoid item duping in chests while recovering :3
                        dchest.setContents(itemsToArray(c.getItemList()));
                        dchest.update();
                    }
                    catch(ArrayIndexOutOfBoundsException e) {
                        
                    }
                }
                else {
                    try {   
                        chest.getBlock().setData(c.getData());
                        chest.setContents(itemsToArray(c.getItemList()));
                        chest.update();
                    }
                    catch(ArrayIndexOutOfBoundsException f) {
                        
                    }
                }
            }
        }
        
        if(block instanceof SignBlock) {
            SignBlock c = (SignBlock)block;
            try {
                Sign sign = (Sign)world.getComplexBlock(test);
                for(int i = 0; i < c.getSignTextArray().length;i++) {
                    sign.setText(i, c.getTextOnLine(i));
                }
                sign.getBlock().setData(c.getData());
                sign.update();
            }
            catch(ClassCastException e) {
                
            }
        }

    }

    @Override
    public void setBlockData(byte data, Vector v) {
        world.setBlockData(v.getBlockX(), v.getBlockY(), v.getBlockZ(), data);

    }
    
    /**
     * Convert Canary Item list to Cuboids2 Item list
     */
    private ArrayList<CItem> itemsToArrayList(Item[] items) {
        if(items == null || items.length == 0) {
            return new ArrayList<CItem>(0);
        }
        
        ArrayList<CItem> newItems = new ArrayList<CItem>(items.length);
        for(Item i: items) {
            if(i != null) {
                newItems.add(new CItem(i.getItemId(), i.getDamage(), i.getAmount(), i.getSlot()));
            }   
        }
        return newItems;
    }
    
    /**
     * Turn items to array
     * @param items
     * @return
     */
    private Item[] itemsToArray(ArrayList<CItem> items) {
        if(items.isEmpty() || items.size() == 0) {
            return new Item[1];
        }
        Item[] nItem = new Item[items.size()];
        for(int i = 0; i < nItem.length; i++) {
            Item it = new Item();

            if(items.size() > i) {
                it.setItemId(items.get(i).getId());
                it.setDamage(items.get(i).getData());
                it.setAmount(items.get(i).getAmount());
                it.setSlot(items.get(i).getSlot());
                nItem[i] = it;
            }

        }
        return nItem;
    }

}
