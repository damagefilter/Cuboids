package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.datasource.CanaryDbData;
import net.playblack.cuboids.datasource.legacy.MysqlDataLegacy;
import net.playblack.cuboids.datasource.legacy.XmlDataLegacy;
import net.playblack.mcutils.ColorManager;

/**
 * Crossload cuboids from datasources
 * TODO: Put legacy loading into here!
 *
 * @author Chris
 */
public class CmodLoadFrom extends CBaseCommand {
    public CmodLoadFrom() {
        super("Load cuboids: " + ColorManager.Yellow + "/cmod loadfrom <mysql|xml>", 2);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }

        if (command[1].equalsIgnoreCase("mysql")) {
            MysqlDataLegacy ds = new MysqlDataLegacy(Config.get().getSqlConfig());
            ds.loadAll();
        }
        //Flatfile now does legacy loading
        else if (command[1].equalsIgnoreCase("xml")) {
            XmlDataLegacy ds = new XmlDataLegacy();
            ds.loadAll();
        }
        else {
            CanaryDbData ds = new CanaryDbData();
            ds.loadAll();
        }
        MessageSystem.successMessage(player, "cuboidLoadedAll");
    }
}
