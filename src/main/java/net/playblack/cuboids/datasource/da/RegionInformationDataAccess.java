package net.playblack.cuboids.datasource.da;

import net.canarymod.database.DataAccess;
import net.playblack.cuboids.exceptions.DeserializeException;
import net.playblack.cuboids.exceptions.SerializerException;
import net.playblack.cuboids.regions.Region;
import net.playblack.mcutils.Debug;
import net.playblack.mcutils.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RegionInformationDataAccess extends DataAccess {

    public RegionInformationDataAccess() {
        super("cuboids_regions");
    }

    private Region generatedRegion;

    // Basics
    public String name;
    public String parent;
    public String world;
    public int dimension;
    public int priority;

    // Location
    public String origin; // Vector
    public String offset; // Vector

    public List<String> players;
    public List<String> groups;

    public List<String> commands;
    public List<String> items;

    // Formatted in name:value
    public List<String> flags; // I'm so sorry but I think there is no other way around it.

    /**
     * Returns the Region representation for this DataAccess object.
     * Beware: It has yet to be parented. Parenting can only be done once all regions are loaded
     * @return
     */
    public Region getRegion() {
        if (generatedRegion != null) {
            return generatedRegion;
        }
        Region r = new Region();
        r.setName(this.name);
        r.setWorld(this.world);
        r.setDimension(this.dimension);
        r.setPriority(this.priority);
        try {
            r.setOrigin(Vector.deserialize(this.origin));
            r.setOffset(Vector.deserialize(this.offset));
        }
        catch (DeserializeException e) {
            Debug.logError("Failed to deserialize a region from database. Dropping it: " + name);
            return null;
        }
        if (players != null) {
            r.addPlayer(players.toArray(new String[players.size()]));
        }
        if (groups != null) {
            r.addGroup(groups.toArray(new String[groups.size()]));
        }
        if (commands != null) {
            r.addRestrictedCommand(commands.toArray(new String[commands.size()]));
        }

        if (items != null) {
            r.addRestrictedItem(items.toArray(new String[items.size()]));
        }

        for (String flag : this.flags) {
            String[] split = flag.split(":");
            r.setProperty(split[0], Region.Status.fromString(split[1]));
        }
        generatedRegion = r;
        return generatedRegion;
    }

    public static RegionInformationDataAccess toDataAccess(Region r) {
        RegionInformationDataAccess da = new RegionInformationDataAccess();
        da.commands = r.getRestrictedCommands();
        da.dimension = r.getDimension();
        da.flags = new ArrayList<String>();
        Map<String, Region.Status> props = r.getAllProperties();
        for (String key : props.keySet()) {
            da.flags.add(key + ":" + Region.Status.toString(props.get(key)));
        }
        da.groups = r.getGroups();
        da.items = r.getRestrictedItems();
        da.name = r.getName();
        da.offset = r.getOffset().serialize().toString();
        da.origin = r.getOrigin().serialize().toString();
        Region parent = r.getParent();
        if(parent != null) {
            da.parent = parent.getName();
        }
        da.players = r.getPlayers();
        da.priority = r.getPriority();
        da.world = r.getWorld();
        return da;
    }


    @Override
    public DataAccess getInstance() {
        return new RegionInformationDataAccess();
    }
}
