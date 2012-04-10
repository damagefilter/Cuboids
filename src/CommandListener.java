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
            command = new Credo();
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
        
        if(split[0].equalsIgnoreCase("/cexpand")) {
            command = new Cexpand();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/cinfo")) {
            command = new Cinfo();
            command.execute(cplayer, split);
            return true;
        }
        if(split.length == 2) {
            if(split[0].equalsIgnoreCase("/cmod") && split[1].equalsIgnoreCase("list")) {
                command = new CmodList();
                command.execute(cplayer, split);
                return true;
            }
        }
        
        if(split.length > 2) {
            if(split[0].equalsIgnoreCase("/cmod") && split[1].equalsIgnoreCase("toggle")) {
                command = new ToggleGlobalProperty();
                command.execute(cplayer, split);
                return true;
            }
            
            if(split[0].equalsIgnoreCase("/cmod") && split[2].equalsIgnoreCase("toggle")) {
                command = new ToggleAreaProperty();
                command.execute(cplayer, split);
                return true;
            }
            
            if(split[0].equalsIgnoreCase("/cmod") && (split[2].equalsIgnoreCase("add") || split[2].equalsIgnoreCase("create"))) {
                command = new CmodAdd();
                command.execute(cplayer, split);
                return true;
            }
            
            if(split[0].equalsIgnoreCase("/cmod") && (split[2].equalsIgnoreCase("remove") || split[2].equalsIgnoreCase("delete"))) {
                command = new CmodRemove();
                command.execute(cplayer, split);
                return true;
            }
            
            if(split[0].equalsIgnoreCase("/cmod") && split[2].equalsIgnoreCase("backup")) {
                command = new Cbackup();
                command.execute(cplayer, split);
                return true;
            }
            
            if(split[0].equalsIgnoreCase("/cmod") && split[2].equalsIgnoreCase("restore")) {
                command = new Crestore();
                command.execute(cplayer, split);
                return true;
            }
            
            if(split[0].equalsIgnoreCase("/cmod") && split[2].equalsIgnoreCase("restrictcommand")) {
                command = new CmodRestrictCommand();
                command.execute(cplayer, split);
                return true;
            }
            
            if(split[0].equalsIgnoreCase("/cmod") && split[2].equalsIgnoreCase("allowcommand")) {
                command = new CmodAllowCommand();
                command.execute(cplayer, split);
                return true;
            }
            
            if(split[0].equalsIgnoreCase("/cmod") && split[2].equalsIgnoreCase("allowitem")) {
                command = new CmodAllowItem();
                command.execute(cplayer, split);
                return true;
            }
            
            if(split[0].equalsIgnoreCase("/cmod") && split[2].equalsIgnoreCase("restrictitem")) {
                command = new CmodRestrictItem();
                command.execute(cplayer, split);
                return true;
            }
            if(split[0].equalsIgnoreCase("/cmod") && (split[2].equalsIgnoreCase("info") || split[2].equalsIgnoreCase("explain"))) {
                command = new CmodInfo();
                command.execute(cplayer, split);
                return true;
            }
            
            if(split[0].equalsIgnoreCase("/cmod") && (split[2].equalsIgnoreCase("cmdblacklist") || split[2].equalsIgnoreCase("cmdlist"))) {
                command = new CmodShowCmdBlacklist();
                command.execute(cplayer, split);
                return true;
            }
            
            if(split[0].equalsIgnoreCase("/cmod") && (split[2].equalsIgnoreCase("prio") || split[2].equalsIgnoreCase("priority"))) {
                command = new CmodPriority();
                command.execute(cplayer, split);
                return true;
            }
            
            if(split[0].equalsIgnoreCase("/cmod") && split[2].equalsIgnoreCase("parent")) {
                command = new CmodParent();
                command.execute(cplayer, split);
                return true;
            }
            
            if(split[0].equalsIgnoreCase("/cmod") && split[2].equalsIgnoreCase("allow")) {
                command = new CmodAllowEntity();
                command.execute(cplayer, split);
                return true;
            }
            
            if(split[0].equalsIgnoreCase("/cmod") && split[2].equalsIgnoreCase("disallow")) {
                command = new CmodDisallowEntity();
                command.execute(cplayer, split);
                return true;
            }
            
            if(split[0].equalsIgnoreCase("/cmod") && (split[2].equalsIgnoreCase("resize") || split[2].equalsIgnoreCase("move"))) {
                command = new CmodMove();
                command.execute(cplayer, split);
                return true;
            }
            
            if(split[0].equalsIgnoreCase("/cmod") && (split[2].equalsIgnoreCase("welcome") || split[2].equalsIgnoreCase("farewell") || split[2].equalsIgnoreCase("goodbye"))) {
                command = new CmodMessages(split[2]);
                command.execute(cplayer, split);
                return true;
            }
        }
        
        if(split[0].equalsIgnoreCase("/csave")) {
            command = new Csave(false);
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/csave-all")) {
            command = new Csave(true);
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/cload")) {
            command = new Cload();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/cloadfrom")) {
            command = new CloadFrom();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/protect")) {
            command = new Protect();
            command.execute(cplayer, split);
            return true;
        }
        
        if(split[0].equalsIgnoreCase("/highprotect")) {
            command = new Highprotect();
            command.execute(cplayer, split);
            return true;
        }
        
        return false;
    }
}
