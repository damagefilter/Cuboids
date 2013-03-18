package net.playblack.cuboids.datasource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.exceptions.DeserializeException;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.mcutils.EventLogger;
import net.playblack.mcutils.ToolBox;
import net.playblack.mcutils.Vector;
import net.visualillusionsent.utils.SystemUtils;

/**
 * XmlData extends BaseData and represents the data layer for retrieving
 * Regions from xml files.
 * 
 * @author Chris
 * 
 */
public class XmlData implements BaseData {

    private Object lock = new Object();
    private EventLogger log;
    /** Used to serialize the XML data into a bytestream */
    private XMLOutputter xmlSerializer = new XMLOutputter(Format.getPrettyFormat().setExpandEmptyElements(true).setOmitDeclaration(true).setOmitEncoding(true).setLineSeparator(SystemUtils.LINE_SEP));
    private SAXBuilder regionBuilder = new SAXBuilder();
    private HashMap<String,ArrayList<Region>> loadedRegions = new HashMap<String,ArrayList<Region>>();
    public XmlData(EventLogger log) {
        this.log = log;
    }

    @Override
    public void saveRegion(Region node) {
        try {
            writeFile(regionToDom(node));
        } catch (IOException e) {
            log.logMessage(e.getMessage(), "WARNING");
        }
    }

    @Override
    public void saveAll(ArrayList<Region> treeList, boolean silent, boolean force) {
        ArrayList<Document> regionFiles = new ArrayList<Document>();
        synchronized(lock) {
            try {
                for(Region r : treeList) {
                    regionFiles.add(regionToDom(r));
                    for(Region reg : r.getChildsDeep(new ArrayList<Region>())) {
                        writeFile(regionToDom(reg));
                    }
                }
            }
            catch(IOException e) {
                log.logMessage(e.getMessage(), "WARNING");
            }
            
        }
    }

    @Override
    public void loadAll() {
        
        RegionManager regionMan = RegionManager.get();
        loadedRegions.put("root", new ArrayList<Region>());
        File regionFolder = new File(Config.get().getBasePath() + "regions/");
        if(!regionFolder.exists()) {
            regionFolder.mkdirs();
        }
        int counter = 0;
        //Load all files sorted by parents.
        //Parentless regions get sorted into "root"
        for(File file : regionFolder.listFiles()) {
            if (file.getName().toLowerCase().endsWith("xml")) {
                try {
                    Document rdoc = regionBuilder.build(file);
                    Element meta = rdoc.getRootElement().getChild("meta");
                    String parentName = meta.getChildText("parent");
                    Region r = domToRegion(rdoc, false);
                    if(r != null) {
                        if(parentName == null || parentName.isEmpty()) {
                            loadedRegions.get("root").add(r);
                        }
                        else {
                            if(loadedRegions.get(parentName) == null) {
                                loadedRegions.put(parentName, new ArrayList<Region>());
                            }
                            loadedRegions.get(parentName).add(r);
                        }
                    }
                } 
                catch (JDOMException e) {
                    log.logMessage(e.getMessage(), "SEVERE");
                } 
                catch (IOException e) {
                    log.logMessage(e.getMessage(), "SEVERE");
                }
            }
            counter++;
        }
        
        //Sort out parents and stuff.
        for(String key : loadedRegions.keySet()) {
            //Root has no parents to sort out
            if(!key.equals("root")) {
                for(Region r : loadedRegions.get(key)) {
                    Region parent = findByName(key);
                    if(parent == null) {
                        log.logMessage("Cannot find parent " + key + ". Dropping region " + r.getName(), "SEVERE");
                        continue; //Drop the region
                    }
                    r.setParent(parent);
                }
            }
        }
        
        //Now that we have all the parents sorted out, we can just add all nodes under "root" to the regionmanager
        
        for(Region root : loadedRegions.get("root")) {
            regionMan.addRoot(root);
        }
        EventLogger.getInstance().logMessage("Loaded " + counter + " regions", "INFO");
    }
    
    /**
     * Get a region from the given list with the given name
     * @param name
     * @return
     */
    private Region findByName(String name) {
        for(String key : loadedRegions.keySet()) {
            for(Region r : loadedRegions.get(key)) {
                if(r.getName().equals(name)) {
                    return r;
                }
            }
        }
        return null;
    }

    @Override
    public void loadRegion(String name, String world, int dimension) {
        String path = Config.get().getBasePath() + "regions/" + world + "_" + name + "_" + dimension + ".xml";
        File f = new File(path);
        try {
            Document rdoc = regionBuilder.build(f);
            Region r = domToRegion(rdoc, true);
            Region old = RegionManager.get().getRegionByName(name, world, dimension);
            if(old != null) {
                RegionManager.get().removeRegion(old);
            }
            RegionManager.get().addRegion(r);
        } catch (JDOMException e) {
            log.logMessage(e.getMessage(), "SEVERE");
        } catch (IOException e) {
            log.logMessage(e.getMessage(), "SEVERE");
        }
    }

    @Override
    public void deleteRegion(Region node) {
        String path = Config.get().getBasePath() + "regions/" + node.getWorld() + "_" + node.getName() + "_" + node.getDimension() + ".xml";
        File file = new File(path);
        file.delete();
    }
    
    private void writeFile(Document xmlDoc) throws IOException {
        Element meta = xmlDoc.getRootElement().getChild("meta");
        FileWriter writer = new FileWriter(Config.get().getBasePath() + "regions/" + meta.getChildText("world") + "_" + meta.getChildText("name") + "_" + meta.getChildText("dimension") + ".xml");
        xmlSerializer.output(xmlDoc, writer);
    }
    
    private Document regionToDom(Region r) {
        Document data = new Document(new Element("region"));
        Element regionElement = data.getRootElement();
        Element meta = new Element("meta");
        
        meta.addContent(new Element("welcome").setText(r.getWelcome()));
        meta.addContent(new Element("farewell").setText(r.getFarewell()));
        meta.addContent(new Element("name").setText(r.getName()));
        if(r.hasParent()) {
            meta.addContent(new Element("parent").setText(r.getParent().getName()));
        }
        meta.addContent(new Element("priority").setText(""+r.getPriority()));
        meta.addContent(new Element("world").setText(r.getWorld()));
        meta.addContent(new Element("dimension").setText(""+r.getDimension()));
        meta.addContent(new Element("origin").setText(r.getOrigin().serialize().toString()));
        meta.addContent(new Element("offset").setText(r.getOffset().serialize().toString()));
        meta.addContent(new Element("players").setText(r.getPlayerList()));
        meta.addContent(new Element("groups").setText(r.getGroupList()));
        regionElement.addContent(meta);
        Element properties = new Element("properties");
        regionElement.addContent(properties);
        HashMap<String, Status> props = r.getAllProperties();
        for(String key : props.keySet()) {
            properties.addContent(new Element(key).setText(props.get(key).name()));
        }
        return data;
    }
    
    private Region domToRegion(Document doc, boolean lookupParent) {
        Region newRegion = new Region();
        Element root = doc.getRootElement();
        Element meta = root.getChild("meta");
        Element properties = root.getChild("properties");
        
        newRegion.setName(meta.getChildText("name"));
        newRegion.setPriority(Integer.parseInt(meta.getChildText("priority")));
        newRegion.setDimension(Integer.parseInt(meta.getChildText("dimension")));
        newRegion.setWorld(meta.getChildText("world"));
        newRegion.setWelcome(ToolBox.stringToNull(meta.getChildText("welcome")));
        newRegion.setFarewell(ToolBox.stringToNull(meta.getChildText("farewell")));
        newRegion.addPlayer(meta.getChildText("players"));
        newRegion.addGroup(meta.getChildText("groups"));
        try {
            newRegion.setOrigin(Vector.deserialize(meta.getChildText("origin")));
            newRegion.setOffset(Vector.deserialize(meta.getChildText("offset")));
        } catch (DeserializeException e) {
            log.logMessage(e.getMessage() + " - dropping region!", "WARNING");
            return null;
        }
        for(Element prop : properties.getChildren()) {
            newRegion.setProperty(prop.getName(), Status.fromString(prop.getText()));
        }
        if(lookupParent) {
            if(meta.getChildText("parent") != null) {
                newRegion.setParent(RegionManager.get().getPossibleParent(newRegion));
            }
        }
        return newRegion;
    }
}
