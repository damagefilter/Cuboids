package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;

/**
 * A base command. Totally not took the idea from CanaryMods BaseCommand :P
 *
 * @author Chris
 *
 */
public abstract class CBaseCommand {
    protected String toolTip;
    protected int minParams;
    protected int maxParams;

    /**
     * Construct a command with toolTip, minParams and maxparams
     *
     * @param toolTip
     * @param minParams
     * @param maxParams
     */
    public CBaseCommand(String toolTip, int minParams, int maxParams) {
        this.toolTip = toolTip;
        this.minParams = minParams;
        this.maxParams = maxParams;
    }

    /**
     * Construct a command with tooltip and the minimum required number of
     * commands
     *
     * @param toolTip
     * @param minParams
     */
    public CBaseCommand(String toolTip, int minParams) {
        this.toolTip = toolTip;
        this.minParams = minParams;
        this.maxParams = -1;
    }

    protected boolean parseCommand(CPlayer player, String[] command) {
        if ((command.length < minParams)
                && ((command.length > maxParams) || (maxParams != -1))) {
            player.notify(toolTip);
            return false;
        } else {
            return true;
        }
    }

    public String getToolTip() {
        return toolTip;
    }

    public int getMinparams() {
        return minParams;
    }

    public int getMaxParams() {
        return maxParams;
    }

    public abstract void execute(CPlayer player, String[] command);

}
