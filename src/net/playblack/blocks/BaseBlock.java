package net.playblack.blocks;

import net.playblack.exceptions.DeserializeException;

public class BaseBlock {

	protected byte data;
	protected short type;
	
	public BaseBlock(short type, byte data) {
	    this.data = data;
	    this.type = type;
	}
	
	//Just some dirty thing, I should probably not do that 
	public BaseBlock() {
    }
	
	/**
	 * Set the Item/Block Type of this block.
	 * @param type
	 */
	public void setType(Number type) {
		this.type = type.shortValue();
	}
	
	/**
	 * Get the current Item/Block Type of this block.
	 * @return Number The Item/Block Type
	 */
	public Number getType() {
		return type;
	}
	
	/**
	 * Set the Data or Damage value of this block.
	 * @param data
	 */
	public void setData(Number data) {
		this.data = data.byteValue();
	}
	
	/**
	 * Get the current damage/data value of this block.
	 * @return byte The current damage/data value
	 */
	public Number getData() {
		return data;
	}
	
	public boolean equals(Object block) {
		if(!(block instanceof BaseBlock)) {
			return false;
		}
		else {
			BaseBlock b = (BaseBlock)block;
			if(b.getType() == this.getType() && b.getData() == this.getData()) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	public boolean equalsSlack(Object block) {
		if(!(block instanceof BaseBlock)) {
			return false;
		}
		else {
			BaseBlock b = (BaseBlock)block;
			if(b.getType() == this.getType()) {
				return true;
			}
			else {
				return false;
			}
		}
	}
    
	/**
     * Serialize this Block into a StringBuilder. This returns [type,data]
     * @return
     */
    public StringBuilder serialize() {
        return new StringBuilder().append("[")
                .append(Short.toString(type)).append(",")
                .append(Byte.toString(data)).append("]");
    }
    
    public String toString() {
        return new StringBuilder().append("[")
                .append(Short.toString(type)).append(",")
                .append(Byte.toString(data)).append("]").toString();
    }
    
    public static BaseBlock deserialize(String serialized) throws DeserializeException {
        serialized = serialized.replace("[", "").replace("]", "");
        BaseBlock tr = null;
        String[] values = serialized.split(",");
        if(values.length != 2) {
            throw new DeserializeException("Could not deserialize BaseBlock object. Invalid serialized data!", serialized);
        }
        short type = Short.parseShort(values[0]);
        byte data = Byte.parseByte(values[1]);
        if(type == 54) {
            tr = new ChestBlock();
            tr.setData(data);
        }
        else if(type == 63) {
            tr = new SignBlock();
            tr.setData(data);
        }
        else {
            tr = new BaseBlock(type, data);
        }
        return tr;
    }
}
