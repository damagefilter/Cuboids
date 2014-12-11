package net.playblack.cuboids.actions.operators;

/**
* Created by Foamy on 11.12.2014.
*/
public enum ExplosionType {
    TNT(1),
    CREEPER(2),
    GHAST_FIREBALL(3),
    WITHER_SKULL(4);

    private int myId;

    ExplosionType(int id) {
        myId = id;
    }

    public static ExplosionType fromId(int id) {
        switch (id) {
            case 1:
                return TNT;
            case 2:
                return CREEPER;
            case 3:
                return GHAST_FIREBALL;
            case 4:
                return WITHER_SKULL;
            default:
                return TNT;
        }
    }

    public static int toId(ExplosionType t) {
        switch (t) {
            case CREEPER:
                return 2;
            case GHAST_FIREBALL:
                return 3;
            case TNT:
                return 1;
            case WITHER_SKULL:
                return 4;
            default:
                return -1;
        }
    }

    public int getId() {
        return myId;
    }
}
