package net.playblack.cuboids.blocks;

import net.playblack.mcutils.ToolBox;

/**
 * CBlock is a block that is somewhere in the world.
 * It doesn't know which world it is set in or where,
 * it only knows what it is.
 * @author chris
 *
 */
public class CBlock {
    protected short type = 0;
    protected byte data = 0;
    
    /**
     * Default CTOR
     */
    public CBlock() {
        type = 0;
        data=0;
    }
    
    /**
     * Contruct Block with type and data
     * @param type
     * @param data
     */
    public CBlock(int type, int data) {
        this.type = (short)type;
        this.data = (byte)data;
    }
    
    /**
     * Construct a block with its type only
     * @param type
     */
    public CBlock(int type) {
        this.type = (short)type;
    }
    /**
     * Set this blocks type
     * @param type
     */
    public void setType(int type) {
        this.type = (short) type;
    }
    
    /**
     * get this blocks type
     * @return
     */
    public short getType() {
        return type;
    }
    
    /**
     * set this blocks data
     * @param data
     */
    public void setData(int data) {
        this.data = (byte) data;
    }
    
    /**
     * get this blocks data
     * @return
     */
    public byte getData() {
        return data;
    }
    
    /**
     * Check if type and data are equal
     * @param b
     * @return
     */
    public boolean equals(CBlock b) {
        if((b.getType() == getType()) && (b.getData() == getData())){
            return true;
        }
        return false;
    }
    
    /**
     * Only check if the type is equal
     * @param b
     * @return
     */
    public boolean equalsSlack(CBlock b) {
        if((b.getType() == getType())){
            return true;
        }
        return false;
    }
    
    /**
     * Parse a new block from a string.
     * Syntax: BlockId:Data or only blockId
     * @param input
     * @return
     */
    public static CBlock parseBlock(String input) {
        String[] split = input.split(":");
        short type = 0;
        byte data = 0;
        if(split.length > 1) {
            type = ToolBox.convertType(split[0]);
            data = ToolBox.convertData(split[1]);
        }
        else {
            type = ToolBox.convertType(split[0]);
        }
        if((type == -1) || (data == -1)) {
            return null;
        }
        return new CBlock(type, data);
    }
    
}
