package net.playblack.cuboids;

public class InvalidPlayerException extends Exception {
    private static final long serialVersionUID = -3968794790965756594L;

    public InvalidPlayerException(String msg) {
        super(msg);
    }
}
