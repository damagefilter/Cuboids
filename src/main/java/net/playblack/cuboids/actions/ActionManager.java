package net.playblack.cuboids.actions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.cuboids.exceptions.InvalidActionHandlerException;
import net.playblack.mcutils.Debug;
import net.playblack.mcutils.ToolBox;

/**
 * This class handles firing action events all over the place,
 * invoking listening actions if required. You may also register your thing here
 * @author chris
 *
 */
public class ActionManager {
    HashMap<Class<? extends CuboidEvent>, List<RegisteredAction>> actions;

    private static ActionManager instance;

    private ActionManager() {
        actions = new HashMap<Class<? extends CuboidEvent>, List<RegisteredAction>>();
    }

    public static void fireEvent(CuboidEvent event) {
        List<RegisteredAction> receivers = ActionManager.instance.actions.get(event.getClass().asSubclass(CuboidEvent.class));
        for(RegisteredAction action : receivers) {
            action.execute(event);
        }
    }

    /**
     * Register your {@link ActionListener} here to make it available for Cuboid2's event system
     * @param listener
     * @return
     * @throws InvalidActionHandlerException if the signature of an actionhandler annotated method is incorrect
     */
    public static void registerActionListener(String owner, ActionListener listener) throws InvalidActionHandlerException {
        //Make a new instance if there is none
        if(ActionManager.instance == null) {
            ActionManager.instance = new ActionManager();
        }
        //Here comes fancy-pancy reflection magic
        //Note: All the final stuff right here is to make sure we can use the data
        //in the inline declaration for ActionExecutor.
        //Props and thx and Kudos to the Bukkit folks for I took some pages out of their book (JavaPluginLoader)

        Method[] allMethods = ToolBox.safeMergeArrays(listener.getClass().getMethods(), listener.getClass().getDeclaredMethods(), new Method[1]);
        //First check the public methods for Actionhandler annotations
        for(final Method m : allMethods) {
            final ActionHandler handler = m.getAnnotation(ActionHandler.class);
            if(handler == null) { continue; } //not an action handling method, bye
            //Check if the new method has correct number of parameters (1)
            if(m.getParameterTypes().length != 1) {
                throw new InvalidActionHandlerException(owner + " tried to register action handler with invalid signature! Wrong num parameters for " + m.getName());
            }
            //If we have 1 parameter, check if it is of the correct type
            final Class<?> eventClass = m.getParameterTypes()[0];
            if(!CuboidEvent.class.isAssignableFrom(eventClass)) {
                throw new InvalidActionHandlerException(owner + " tried to register action handler with invalid signature! Wrong parameter type for " + m.getName());
            }
            //Okay, we're cool. Lets try to register that thing!
            //Make sure we have a working set for registered actions before adding it.
            instance.registerEventType(eventClass.asSubclass(CuboidEvent.class));

            ActionExecutor executor = new ActionExecutor() {

                @Override
                public void execute(ActionListener action, CuboidEvent event) {
                    if(eventClass.isAssignableFrom(event.getClass())) {
                        try {
                            m.invoke(action, event);
                        }
                        catch (IllegalArgumentException e) {
                            Debug.logStack(e);
                        }
                        catch (IllegalAccessException e) {
                            Debug.logStack(e);
                        }
                        catch (InvocationTargetException e) {
                            Debug.logStack(e);
                        }
                    }
                }
            };
            instance.addRegisteredAction(eventClass.asSubclass(CuboidEvent.class), new RegisteredAction(listener, handler.priority(), executor, owner));
        }
    }

    private void addRegisteredAction(Class<? extends CuboidEvent> eventClass, RegisteredAction action) {
        actions.get(eventClass).add(action);
        Collections.sort(actions.get(eventClass), new RegisteredActionsComparator());
    }

    /**
     * Check if the event is already registered. If not, it will make a new entry in the HashMap.
     * @param cls
     */
    private void registerEventType(Class<? extends CuboidEvent> cls) {
        if(!actions.containsKey(cls)) {
            actions.put(cls, new ArrayList<RegisteredAction>());
        }
    }

    private class RegisteredActionsComparator implements Comparator<RegisteredAction> {

        @Override
        public int compare(RegisteredAction o1, RegisteredAction o2) {
            return Integer.valueOf(o1.getPriority().ordinal()).compareTo(o2.getPriority().ordinal());
        }

    }
}
