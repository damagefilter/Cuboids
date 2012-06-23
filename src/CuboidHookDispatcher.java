import net.playblack.cuboids.InvalidPlayerException;
import net.playblack.cuboids.exceptions.InvalidApiHookException;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.hookapi.AreaActionHook;
import net.playblack.cuboids.hookapi.AreaChecksHook;
import net.playblack.cuboids.hookapi.AreaFlagsHook;
import net.playblack.cuboids.hookapi.CuboidHook;
import net.playblack.mcutils.EventLogger;
import net.playblack.mcutils.Vector;


public class CuboidHookDispatcher implements PluginInterface {

    @Override
    public String checkParameters(Object[] arg0) {
        return null;
    }

    @Override
    public String getName() {
        return "CuboidAPI";
    }

    @Override
    public int getNumParameters() {
        return 0;
    }

    @Override
    public Object run(Object[] args) {
        try {
            return execute(args);
        } catch (InvalidApiHookException e) {
            EventLogger.getInstance().logMessage(e.getMessage(), "WARNING");
            e.printStackTrace();
            return null;
        } catch (InvalidPlayerException e) {
            EventLogger.getInstance().logMessage(e.getMessage(), "WARNING");
            e.printStackTrace();
            return null;
        }
    }
    public Object execute(Object[] args) throws InvalidApiHookException, InvalidPlayerException {
        if(!(args[0] instanceof String)) {
            return null;
        }
        String mode = (String) args[0];
        Object[] converted = null;
        CuboidHook hook = null;
        if(mode.equalsIgnoreCase("AREA_GET_OWNERS")) {
            hook = new AreaActionHook();
            World w = (World)args[1];
            converted = new Object[]{mode, CServer.getServer().getWorld(w.getName(), w.getType().getId()), (String)args[2]};
        }
        else if(mode.equalsIgnoreCase("AREA_GET_PLAYERLIST") || mode.equalsIgnoreCase("AREA_GET_GROUPLIST")) {
            
            hook = new AreaActionHook();
            World w = (World)args[1];
            converted = new Object[]{mode, CServer.getServer().getWorld(w.getName(), w.getType().getId()), (String)args[2]};
        }
        
        else if(mode.equalsIgnoreCase("AREA_ADD_PLAYER") || mode.equalsIgnoreCase("AREA_ADD_GROUP")) {
            hook = new AreaActionHook();
            World w = (World)args[1];
            converted = new Object[]{mode, CServer.getServer().getWorld(w.getName(), w.getType().getId()), (String)args[2], (String)args[3]};
        }
        
        else if(mode.equalsIgnoreCase("AREA_REMOVE_PLAYER") || mode.equalsIgnoreCase("AREA_REMOVE_GROUP")) {
            hook = new AreaActionHook();
            World w = (World)args[1];
            converted = new Object[]{mode, CServer.getServer().getWorld(w.getName(), w.getType().getId()), (String)args[2], (String)args[3]};
        }
        else if(mode.equalsIgnoreCase("CAN_MODIFY")) {
            hook = new AreaChecksHook();
            CPlayer player = CServer.getServer().getPlayer((String)args[1]);
            Block b = (Block)args[2];
            Vector v = new Vector(b.getX(),b.getY(), b.getZ());
            converted = new Object[]{mode, player, v};
        }
        else if(mode.equalsIgnoreCase("AREA_GET_NAME_LOCAL")) {
            hook = new AreaChecksHook();
            CPlayer player = CServer.getServer().getPlayer((String)args[1]);
            converted = new Object[]{mode, player};
        }
        else if(mode.equalsIgnoreCase("AREA_GET_NAME_REMOTE")) {
            hook = new AreaChecksHook();
            CPlayer player = CServer.getServer().getPlayer((String)args[1]);
            Block b = (Block)args[2];
            Vector v = new Vector(b.getX(),b.getY(), b.getZ());
            converted = new Object[]{mode, player, v};
        }
        else if(mode.equalsIgnoreCase("AREA_GET_FLAG")) {
            hook = new AreaFlagsHook();
            World w = (World)args[1];
            CWorld world = CServer.getServer().getWorld(w.getName(), w.getType().getId());
            converted = new Object[] {mode, world, (String)args[2], (String)args[3]};
        }
        else if(mode.equalsIgnoreCase("AREA_SET_FLAG")) {
            hook = new AreaFlagsHook();
            World w = (World)args[1];
            CWorld world = CServer.getServer().getWorld(w.getName(), w.getType().getId());
            converted = new Object[] {mode, world, (String)args[2], (String)args[3], (Boolean)args[4]};
        }
        else {
            throw new InvalidApiHookException("Catched an invalid hook call, check this! Hook: "+mode);
        }
        return hook.run(converted);
    }

}
