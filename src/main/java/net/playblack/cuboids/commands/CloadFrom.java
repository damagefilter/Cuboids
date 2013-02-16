package net.playblack.cuboids.commands;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.datasource.XmlData;
import net.playblack.cuboids.datasource.MysqlDataLegacy;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.EventLogger;

/**
 * Crossload cuboids from datasources
 * 
 * @author Chris
 * 
 */
public class CloadFrom extends CBaseCommand {
    public CloadFrom() {
        super("Load cuboids: " + ColorManager.Yellow
                + "/cloadfrom <mysql/flatfile>", 2);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        if (!player.hasPermission("cIgnoreRestrictions")) {
            MessageSystem.getInstance().failMessage(player, "permissionDenied");
        }
        if (command[1].equalsIgnoreCase("mysql")) {
            MysqlDataLegacy ds = new MysqlDataLegacy(Config.get().getSqlConfig(),
                    EventLogger.getInstance());
            ds.loadAll();
        } else {
            XmlData ds = new XmlData(EventLogger.getInstance());
            ds.loadAll();
        }
        MessageSystem.getInstance().successMessage(player, "cuboidLoadedAll");
    }
}
