package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Vector3D;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.generators.GenericGenerator;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.Debug;

/**
 * Copy a selection into the clipboard
 *
 * @author Chris
 */
public class Ccopy extends CBaseCommand {

    public Ccopy() {
        super("Copy a selection into your clipboard: " + ColorManager.Yellow + " /ccopy", 1);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        SelectionManager selectionManager = SelectionManager.get();
        CuboidSelection sel = selectionManager.getPlayerSelection(player.getName());
        GenericGenerator gen = new GenericGenerator(sel, player.getWorld());
        try {
            sel = gen.getWorldContent();
        }
        catch (BlockEditLimitExceededException e) {
            Debug.logWarning(e.getMessage());
            MessageSystem.customFailMessage(player, e.getMessage());
            e.printStackTrace();
        }
        catch (SelectionIncompleteException e) {
            MessageSystem.failMessage(player, "selectionIncomplete");
        }
        sel.setOrigin(new Vector3D(player.getPosition()));
        // gen.
        SessionManager.get().setClipboard(player.getName(), sel);
        MessageSystem.successMessage(player, "copiedToClipboard");
    }
}
