package net.playblack.cuboids.actions;

import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;

public class RegisteredAction {
    private ActionListener listener;
    private ActionHandler.Priority priority;
    private ActionExecutor executor;
    private String owner;
    
    /**
     * Construct a new Registered action.
     * @param listener
     * @param priority
     * @param executor
     * @param owner
     */
    public RegisteredAction(ActionListener listener, ActionHandler.Priority priority, ActionExecutor executor, String owner) {
        this.listener = listener;
        this.priority = priority;
        this.executor = executor;
        this.owner = owner;
    }
    
    /**
     * Get this actions owner
     * @return
     */
    public String getOwner() {
        return owner;
    }
    
    /**
     * get this actions priority for sorting purposes
     * @return
     */
    public ActionHandler.Priority getPriority() {
        return priority;
    }
    
    /**
     * Call the given event on the registered executor
     * @param event
     */
    public void execute(CuboidEvent event) {
        if(event instanceof Cancellable) {
            if(((Cancellable)event).isCancelled()) {
                return;
            }
        }
        executor.execute(listener, event);
    }
    
}
