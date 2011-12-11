import java.util.ArrayList;
import java.io.Serializable;

@SuppressWarnings("serial")
public class CuboidD implements Serializable {

    String name = "noname";
    int[] coords = new int[6];	//	int[]{firstX, firstY, firstZ, secondX, secondY, secondZ}
    boolean protection = false;
    boolean restricted = false;
    boolean trespassing = false;
    boolean PvP = true;
    boolean heal = false;
    boolean creeper = true;
    boolean sanctuary = false;
    ArrayList<String> allowedPlayers = new ArrayList<String>();
    String welcomeMessage = null;
    String farewellMessage = null;
    String warning = null;
    String world = "normal";
    ArrayList<String> disallowedCommands = new ArrayList<String>();
    boolean changed = true;

    public CuboidD() {
    }

    public boolean contains(String worldType, int X, int Y, int Z) {
        if (world.equalsIgnoreCase(worldType) && 
        		
        		X >= coords[0] &&  X <= coords[3] 
        	 && Z >= coords[2] && Z <= coords[5] 
        	 && Y >= coords[1] && Y <= coords[4]) {
            return true;
        }
        return false;
    }

    public boolean contains(String worldType, Location l) {
        return contains(worldType, (int) l.x, (int) l.y, (int) l.z);
    }

    public boolean isAllowed(Player player) {
        String playerName = player.getName().toLowerCase();
        for (String allowedPlayer : allowedPlayers) {
            if (allowedPlayer.equalsIgnoreCase(playerName) || allowedPlayer.equalsIgnoreCase("o:" + playerName)) {
                return true;
            }
            if (allowedPlayer.startsWith("g:") && player.isInGroup(allowedPlayer.substring(2))) {
                return true;
            }
        }
        return false;
    }

    public boolean isAllowed(String command) {
        for (String disallowed : disallowedCommands) {
            if (command.equalsIgnoreCase(disallowed)) {
                return false;
            }
        }
        return true;
    }

    public boolean isOwner(Player player) {
        String playerName = "o:" + player.getName();
        for (String allowedPlayer : allowedPlayers) {
            if (allowedPlayer.equalsIgnoreCase(playerName)) {
                return true;
            }
        }
        return false;
    }

    public void allowPlayer(String playerName) {
        boolean done = false;
        boolean newIsOwner = false;
        if (playerName.startsWith("o:")) {
            playerName = playerName.substring(2);
            newIsOwner = true;
        }
        for (int j = 0; j < this.allowedPlayers.size() && !done; j++) {
            String allowedPlayer = this.allowedPlayers.get(j);
            // if the new player already is in the list as simple allowed
            if (allowedPlayer.equalsIgnoreCase(playerName)) {
                // we switch him to owner if needed
                if (newIsOwner) {
                    this.allowedPlayers.set(j, "o:" + playerName);
                }
                // we've found the guy, no need to add him again.
                done = true;
            }
            // if the new player already is an owner, no need to go further
            if (allowedPlayer.equalsIgnoreCase("o:" + playerName)) {
                done = true;
            }
        }
        // If the player wasn't found, we add him.
        if (!done) {
            this.allowedPlayers.add(((newIsOwner) ? "o:" : "") + playerName);
        }
        changed = true;
    }

    public void disallowPlayer(String playerName) {
        for (int pl_search = 0; pl_search <= this.allowedPlayers.size() - 1; pl_search++) {
            if (this.allowedPlayers.get(pl_search).equalsIgnoreCase(playerName)) {
                this.allowedPlayers.remove(pl_search);
            }
        }
        changed = true;
    }

    public void disallowCommand(String command) {
        if (!disallowedCommands.contains(command)) {
            disallowedCommands.add(command);
        }
        changed = true;
    }

    public void allowCommand(String command) {
        disallowedCommands.remove(command);
        changed = true;
    }

    public void playerEnters(Player player) {
        if (this.welcomeMessage != null) {
            player.sendMessage(Colors.Yellow + this.welcomeMessage);
        }
    }

    public void playerLeaves(Player player) {
        if (this.farewellMessage != null) {
            player.sendMessage(Colors.Yellow + this.farewellMessage);
        }
    }

    public void printInfos(Player player, boolean players, boolean commands) {
        player.sendMessage(Colors.Yellow + "----    " + this.name + "    ----");
        player.sendMessage(Colors.Yellow + "World : " + Colors.White + this.world);
        String flags = "";
        boolean noflag = true;
        if (this.protection) {
            flags += " protection";
            noflag = false;
        }
        if (this.restricted) {
            flags += " restricted";
            noflag = false;
        }
        if (!this.PvP) {
            flags += " no-PvP";
            noflag = false;
        }
        if (this.heal) {
            flags += " heal";
            noflag = false;
        }
        if (!this.creeper) {
            flags += " creeper-free";
            noflag = false;
        }
        if (this.sanctuary) {
            flags += " sanctuary";
            noflag = false;
        }
        if (this.welcomeMessage != null) {
            flags += " welcome";
            noflag = false;
        }
        if (this.farewellMessage != null) {
            flags += " farewell";
            noflag = false;
        }
        if (noflag) {
            flags += " <none>";
        }
        player.sendMessage(Colors.Yellow + "Flags :" + Colors.White + flags);
        printAllowedPlayers(player);
        if (players) {
            printPresentPlayers(player);
        }
        if (commands) {
            printDisallowedCommands(player);
        }
    }

    public void printAllowedPlayers(Player player) {
        if (this.allowedPlayers.size() == 0) {
            player.sendMessage(Colors.Yellow + "Allowed players : " + Colors.White + "<list is empty>");
            return;
        }
        String list = "";
        for (String playerName : this.allowedPlayers) {
            list += " " + playerName;
        }
        player.sendMessage(Colors.Yellow + "Allowed players :" + Colors.White + list);
    }

    public void printPresentPlayers(Player player) {
        String list = "";
        for (Player p : etc.getServer().getPlayerList()) {
            if (this.contains(player.getWorld().toString().toLowerCase(), (int) p.getX(), (int) p.getY(), (int) p.getZ())) {
                list += " " + p.getName();
            }
        }
        if (list.length() < 2) {
            player.sendMessage(Colors.Yellow + "Present players : " + Colors.White + "<list is empty>");
        } else {
            player.sendMessage(Colors.Yellow + "Present players :" + Colors.White + list);
        }
    }

    public void printDisallowedCommands(Player player) {
        if (this.disallowedCommands.size() == 0) {
            player.sendMessage(Colors.Yellow + "Disallowed commands : " + Colors.White + "<list is empty>");
            return;
        }
        String list = "";
        for (String command : this.disallowedCommands) {
            list += " " + command;
        }
        player.sendMessage(Colors.Yellow + "Disallowed commands :" + Colors.White + list);
    }
}
