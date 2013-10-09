package net.playblack.cuboids.actions.events;

/**
 * Marks and event cancellable.
 * YES, IN EUROPE IT'S WRITTEN LIKE THAT! :P
 *
 * @author chris
 */
public interface Cancellable {
    /**
     * Check if this event is already cancelled
     *
     * @return
     */
    public boolean isCancelled();

    /**
     * Cancel this event
     */
    public void cancel();

}
