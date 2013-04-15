package net.playblack.cuboids.commands;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.datasource.FlatfileDataLegacy;
import net.playblack.cuboids.datasource.MysqlDataLegacy;
import net.playblack.cuboids.datasource.XmlData;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.mcutils.ColorManager;

/**
 * Crossload cuboids from datasources
 * TODO: Put legacy loading into here!
 * @author Chris
 *
 */
public class CmodLoadFrom extends CBaseCommand {
    public CmodLoadFrom() {
        super("Load cuboids: " + ColorManager.Yellow + "/cmod loadfrom <mysql|flatfile|xml>", 2);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }

        if (command[1].equalsIgnoreCase("mysql")) {
            MysqlDataLegacy ds = new MysqlDataLegacy(Config.get().getSqlConfig());
            ds.loadAll();
        }
        //Flatfile now does legacy loading
        else if(command[1].equalsIgnoreCase("flatfile")) {
            FlatfileDataLegacy ds = new FlatfileDataLegacy();
            ds.loadAll();
        }
        else {
            XmlData ds = new XmlData();
            ds.loadAll();
        }
        MessageSystem.successMessage(player, "cuboidLoadedAll");
    }
}
