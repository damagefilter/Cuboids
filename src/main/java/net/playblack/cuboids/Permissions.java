package net.playblack.cuboids;

/**
 * Static class containing all permissions
 */
public class Permissions {
    public static final String ADMIN = "cuboids.super.admin";
    public static final String BYPASS$GAMEMODE= "cuboids.super.bypass.gamemode";
    public static final String EDIT$WORLD = "cuboids.super.edit.world"; // worldmod


    public static final String BRUSH$USE = "cuboids.brush.use"; // cbrush
    public static final String BRUSH$EDIT = "cuboids.brush.edit"; // cbrush

    public static final String BACKUPAREA = "cuboids.backup"; // cbackup


    public static final String SELECTION$CREATE = "cuboids.selection.create"; // cselect
    public static final String SELECTION$DELETEOWN = "cuboids.selection.delete.own";
    public static final String SELECTION$DELETEANY = "cuboids.selection.delete.any";

    public static final String REGION$EDIT$ANY = "cuboids.region.editany"; // areamod
    public static final String REGION$FLAGS = "cuboids.region.flags"; // base for flag permissions (followed by flag name)
    public static final String REGION$TELEPORT = "cuboids.region.teleport"; // cteleport
    public static final String REGION$CREATE = "cuboids.region.create";
    public static final String REGION$META = "cuboids.region.meta";
}
