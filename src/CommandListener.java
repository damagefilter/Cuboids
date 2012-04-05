import net.playblack.cuboids.commands.CBaseCommand;
import net.playblack.cuboids.commands.*;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.selections.SelectionManager;


public class CommandListener extends PluginListener {
    @Override
    public boolean onCommand(Player player, String[] split) {
        SelectionManager.getInstance().getPlayerSelection(player.getName());
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
        CBaseCommand command;
        
        if(split[0].equalsIgnoreCase("/cdel")) {
            command = new Cdel();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/cbrush")) {
            command = new Cbrush();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/ccopy")) {
            command = new Ccopy();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/cpaste")) {
            command = new Cpaste();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/cmove")) {
            command = new Cmove();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/cwalls")) {
            command = new Cwalls("/cwalls");
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/cfaces")) {
            command = new Cwalls("/cfaces");
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/cfill")) {
            command = new Cfill();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/creplace")) {
            command = new Creplace();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/csphere")) {
            command = new Csphere();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/cpyramid")) {
            command = new Cpyramid();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/ccircle")) {
            command = new Cdisc("/ccircle");
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/cdisc")) {
            command = new Cdisc("/cdisc");
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/cundo")) {
            command = new Cundo();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/credo")) {
            command = new Cundo();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/cceiling") || split[0].equalsIgnoreCase("/cceil")) {
            command = new Cceiling();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/cfloor")) {
            command = new Cfloor();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/cmod")) {
            
        }
        return false;
    }
}
