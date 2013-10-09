package net.playblack.cuboids.actions.events;

/**
 * Base class for a cuboids event
 *
 * @author chris
 */
public abstract class CuboidEvent {
    /**
     * Get the name of this event (the class name, specifically)
     *
     * @return String the name
     */
    public String getName() {
        return getClass().getSimpleName();
    }
}
