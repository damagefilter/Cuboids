package net.playblack.blocks;

public class SignBlock extends BaseBlock {

	String[] signText = new String[4];
	
	public SignBlock() {
		data=(byte)0;
		type=(short)63;
	}
	
	public SignBlock(String[] text) {
		data=(byte)0;
		type=(short)63;
		signText = text;
	}
	
	@Override
	public void setType(Number type) {
		return; //Can't set type of static sign

	}

	@Override
	public Short getType() {
		return type;
	}

	@Override
	public void setData(Number data) {
		this.data = data.byteValue();
	}

	@Override
	public Byte getData() {
		return data;
	}
	
	public String getTextOnLine(int line) {
		if(line > signText.length) {
			return null;
		}
		else {
			return signText[line];
		}
	}
	
	public void setTextOnLine(String text, int line) {
	    if(line > 4) {
	        return;
	    }
	    signText[line] = text;
	}
	
	public void setText(String[] text) {
	    if(text.length > 4) {
	        return;
	    }
	    signText = text;
	}
	
	public String[] getSignTextArray() {
		return signText;
	}

}
