package net.playblack.cuboids.blocks;

public class SignBlock extends CBlock {

    String[] signText = new String[4];

    public SignBlock() {
        data = (byte) 0;
        type = (short) 63;
    }

    public SignBlock(String[] text) {
        data = (byte) 0;
        type = (short) 63;
        signText = text;
    }

    public String getTextOnLine(int line) {
        if (line > signText.length) {
            return null;
        } else {
            return signText[line];
        }
    }

    public void setTextOnLine(String text, int line) {
        if (line > 4) {
            return;
        }
        signText[line] = text;
    }

    public void setText(String[] text) {
        if (text.length > 4) {
            return;
        }
        signText = text;
    }

    public String[] getSignTextArray() {
        return signText;
    }
}
