package net.playblack.cuboids.actions;

import net.playblack.cuboids.actions.events.CuboidEvent;

/**
 * This executes the action event for an executable plugin listener
 *
 * @author chris
 */
public abstract class ActionExecutor {
    public abstract void execute(ActionListener action, CuboidEvent event);
}
