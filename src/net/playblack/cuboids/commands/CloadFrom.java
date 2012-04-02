package net.playblack.cuboids.commands;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.datasource.FlatfileData;
import net.playblack.cuboids.datasource.MysqlData;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.EventLogger;

/**
 * Crossload cuboids from datasources
 * @author Chris
 *
 */
public class CloadFrom extends BaseCommand {
    public CloadFrom() {
        super("Load cuboids: /cloadfrom <mysql/flatfile>", 2);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
        if(!player.hasPermission("cIgnoreRestrictions")) {
            MessageSystem.getInstance().failMessage(player, "permissionDenied");
        }
        if(command[1].equalsIgnoreCase("mysql")) {
            MysqlData ds = new MysqlData(Config.getInstance().getSqlConfig(), EventLogger.getInstance());
            ds.loadAll(RegionManager.getInstance());
        }
        else {
            FlatfileData ds = new FlatfileData(EventLogger.getInstance());
            ds.loadAll(RegionManager.getInstance());
        }
        MessageSystem.getInstance().successMessage(player, "cuboidLoadedAll");
    }
}
