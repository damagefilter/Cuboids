package net.playblack.cuboids.help;

import java.util.ArrayList;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.mcutils.ColorManager;

public class CommandHelper {
    private static CommandHelper instance;

    private ArrayList<HelpContext> help;

    private CommandHelper() {
        help = new ArrayList<HelpContext>();
    }

    public static CommandHelper get() {
        if (instance == null) {
            instance = new CommandHelper();
        }
        return instance;
    }

    /**
     * Add a new help context
     * 
     * @param toolTip
     * @param taxonomy
     */
    public void addHelp(String toolTip, String[] taxonomy) {
        help.add(new HelpContext(toolTip, taxonomy));
    }

    /**
     * Add new Help context with no taxonomy. A help context without taxonomy
     * will always be displayed
     * 
     * @param toolTip
     */
    public void addHelp(String toolTip) {
        help.add(new HelpContext(toolTip, new String[] {}));
    }

    public void displayHelp(CPlayer player, int page, String[] terms) {

        int perPage = 10, maxPages = 0, amount = 0;
        ArrayList<HelpContext> toDisplay = new ArrayList<HelpContext>();

        for (HelpContext hc : help) {
            if (hc.searchFor(terms)) {
                toDisplay.add(hc);
            }
        }

        // Following is all taken from CuboidPlugin
        // Because I suck at making paging
        if (toDisplay == null || toDisplay.isEmpty()) {
            MessageSystem.customMessage(player, ColorManager.LightGray,
                    "No help content found, sorry.");
            return;
        }
        maxPages = (int) Math.ceil(toDisplay.size() / perPage);
        if ((toDisplay.size() % perPage) > 0) {
            maxPages++;
        }
        if (page > maxPages) {
            page = 1;
        }
        amount = (page - 1) * perPage;
        MessageSystem.customMessage(player, ColorManager.LightGreen,
                "Help for your search term(s), Page " + page + " of "
                        + maxPages);
        for (int i = amount; i < (amount + perPage); i++) {
            if (toDisplay.size() <= i) {
                break;
            }
            MessageSystem.customMessage(player, ColorManager.Rose, toDisplay
                    .get(i).getToolTip());
        }
    }
}
