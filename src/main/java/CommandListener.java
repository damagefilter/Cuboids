import net.playblack.cuboids.InvalidPlayerException;
import net.playblack.cuboids.commands.CBaseCommand;
import net.playblack.cuboids.commands.Cbackup;
import net.playblack.cuboids.commands.Cbrush;
import net.playblack.cuboids.commands.Cceiling;
import net.playblack.cuboids.commands.Ccopy;
import net.playblack.cuboids.commands.Cdel;
import net.playblack.cuboids.commands.Cdiag;
import net.playblack.cuboids.commands.Cdisc;
import net.playblack.cuboids.commands.Cexpand;
import net.playblack.cuboids.commands.Cfill;
import net.playblack.cuboids.commands.Cfloor;
import net.playblack.cuboids.commands.Cinfo;
import net.playblack.cuboids.commands.CmodAdd;
import net.playblack.cuboids.commands.CmodAllowCommand;
import net.playblack.cuboids.commands.CmodAllowEntity;
import net.playblack.cuboids.commands.CmodAllowItem;
import net.playblack.cuboids.commands.CmodDisallowEntity;
import net.playblack.cuboids.commands.CmodInfo;
import net.playblack.cuboids.commands.CmodList;
import net.playblack.cuboids.commands.CmodListFlags;
import net.playblack.cuboids.commands.CmodLoad;
import net.playblack.cuboids.commands.CmodLoadFrom;
import net.playblack.cuboids.commands.CmodLoadPoints;
import net.playblack.cuboids.commands.CmodMessages;
import net.playblack.cuboids.commands.CmodMove;
import net.playblack.cuboids.commands.CmodParent;
import net.playblack.cuboids.commands.CmodPriority;
import net.playblack.cuboids.commands.CmodRemove;
import net.playblack.cuboids.commands.CmodRemoveFlag;
import net.playblack.cuboids.commands.CmodRename;
import net.playblack.cuboids.commands.CmodRestrictCommand;
import net.playblack.cuboids.commands.CmodRestrictItem;
import net.playblack.cuboids.commands.CmodSave;
import net.playblack.cuboids.commands.CmodSetFlag;
import net.playblack.cuboids.commands.CmodShowCmdBlacklist;
import net.playblack.cuboids.commands.CmodTpTo;
import net.playblack.cuboids.commands.Cmove;
import net.playblack.cuboids.commands.Cpaste;
import net.playblack.cuboids.commands.Cpyramid;
import net.playblack.cuboids.commands.Credo;
import net.playblack.cuboids.commands.Creplace;
import net.playblack.cuboids.commands.Crestore;
import net.playblack.cuboids.commands.Csphere;
import net.playblack.cuboids.commands.Cundo;
import net.playblack.cuboids.commands.Cwalls;
import net.playblack.cuboids.commands.Highprotect;
import net.playblack.cuboids.commands.Protect;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.selections.SelectionManager;

public class CommandListener extends PluginListener {
    @Override
    public boolean onCommand(Player player, String[] split) {
        SelectionManager.get().getPlayerSelection(player.getName());
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(player.getName());
        } catch (InvalidPlayerException e) {
            // fallback to manually get a player
            cplayer = new CanaryPlayer(player);
        }
        CBaseCommand command;

        if (split[0].equalsIgnoreCase("/cdel")) {
            command = new Cdel();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/cdiag")) {
            command = new Cdiag();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/cbrush")) {
            command = new Cbrush();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/ccopy")) {
            command = new Ccopy();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/cpaste")) {
            command = new Cpaste();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/cmove")) {
            command = new Cmove();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/cwalls")) {
            command = new Cwalls("/cwalls");
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/cfaces")) {
            command = new Cwalls("/cfaces");
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/cfill")) {
            command = new Cfill();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/creplace")) {
            command = new Creplace();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/csphere")) {
            command = new Csphere();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/cpyramid")) {
            command = new Cpyramid();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/ccircle")) {
            command = new Cdisc("/ccircle");
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/cdisc")) {
            command = new Cdisc("/cdisc");
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/cundo")) {
            command = new Cundo();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/credo")) {
            command = new Credo();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/cceiling")
                || split[0].equalsIgnoreCase("/cceil")) {
            command = new Cceiling();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/cfloor")) {
            command = new Cfloor();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/cexpand")) {
            command = new Cexpand();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/cinfo")) {
            command = new Cinfo();
            command.execute(cplayer, split);
            return true;
        }
        if (split[0].equalsIgnoreCase("/cbackup")) {
            command = new Cbackup();
            command.execute(cplayer, split);
            return true;
        }
        if (split[0].equalsIgnoreCase("/crestore")) {
            command = new Crestore();
            command.execute(cplayer, split);
            return true;
        }
        if (split.length == 2) {
            if (split[0].equalsIgnoreCase("/cmod")
                    && split[1].equalsIgnoreCase("list")) {
                command = new CmodList();
                command.execute(cplayer, split);
                return true;
            }
        }

        if (split.length > 2) {

            if (split[0].equalsIgnoreCase("/cmod")
                    && split[1].equalsIgnoreCase("list")) {
                command = new CmodList();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && (split[2].equalsIgnoreCase("add") || split[2]
                            .equalsIgnoreCase("create"))) {
                command = new CmodAdd();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && (split[2].equalsIgnoreCase("remove") || split[2]
                            .equalsIgnoreCase("delete"))) {
                command = new CmodRemove();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && split[2].equalsIgnoreCase("rename")) {
                command = new CmodRename();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && split[2].equalsIgnoreCase("loadpoints")) {
                command = new CmodLoadPoints();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && split[2].equalsIgnoreCase("restrictcommand")) {
                command = new CmodRestrictCommand();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && split[2].equalsIgnoreCase("allowcommand")) {
                command = new CmodAllowCommand();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && split[2].equalsIgnoreCase("allowitem")) {
                command = new CmodAllowItem();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && split[2].equalsIgnoreCase("restrictitem")) {
                command = new CmodRestrictItem();
                command.execute(cplayer, split);
                return true;
            }
            if (split[0].equalsIgnoreCase("/cmod")
                    && (split[2].equalsIgnoreCase("info") || split[2]
                            .equalsIgnoreCase("explain"))) {
                command = new CmodInfo();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && (split[2].equalsIgnoreCase("cmdblacklist") || split[2]
                            .equalsIgnoreCase("cmdlist"))) {
                command = new CmodShowCmdBlacklist();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && (split[2].equalsIgnoreCase("prio") || split[2]
                            .equalsIgnoreCase("priority"))) {
                command = new CmodPriority();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && split[2].equalsIgnoreCase("parent")) {
                command = new CmodParent();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && split[2].equalsIgnoreCase("allow")) {
                command = new CmodAllowEntity();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && split[2].equalsIgnoreCase("disallow")) {
                command = new CmodDisallowEntity();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && split[2].equalsIgnoreCase("tpto")) {
                command = new CmodTpTo();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && (split[2].equalsIgnoreCase("resize") || split[2]
                            .equalsIgnoreCase("move"))) {
                command = new CmodMove();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && (split[2].equalsIgnoreCase("welcome")
                            || split[2].equalsIgnoreCase("farewell") || split[2]
                                .equalsIgnoreCase("goodbye"))) {
                command = new CmodMessages(split[2]);
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && (split[2].equalsIgnoreCase("flag") && split[3].equalsIgnoreCase("set"))) {
                command = new CmodSetFlag();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && (split[2].equalsIgnoreCase("flag") && split[3].equalsIgnoreCase("remove"))) {
                command = new CmodRemoveFlag();
                command.execute(cplayer, split);
                return true;
            }

            if (split[0].equalsIgnoreCase("/cmod")
                    && ((split[1].equalsIgnoreCase("flag") || split[2].equalsIgnoreCase("flag"))
                            && (split[2].equalsIgnoreCase("list") || split[3].equalsIgnoreCase("list")))) {
                command = new CmodListFlags();
                command.execute(cplayer, split);
                return true;
            }
        }

        if (split[0].equalsIgnoreCase("/csave")) {
            command = new CmodSave();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/csave-all")) {
            command = new CmodSave();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/cload")) {
            command = new CmodLoad();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/cloadfrom")) {
            command = new CmodLoadFrom();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/protect")) {
            command = new Protect();
            command.execute(cplayer, split);
            return true;
        }

        if (split[0].equalsIgnoreCase("/highprotect")) {
            command = new Highprotect();
            command.execute(cplayer, split);
            return true;
        }
        return false;
    }
}
