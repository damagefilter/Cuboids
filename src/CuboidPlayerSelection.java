
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.playblack.cuboid.CuboidData;
import com.playblack.cuboid.CuboidSelection;
import com.playblack.vector.Vector;

/**
 * This stores areas, origins and offsets for selections so multiple players can work on stuff at once.
 * Can't be packaged because it stores item data directly...
 * @author Chris
 *
 */
public class CuboidPlayerSelection extends CuboidSelection {

	private CuboidData original;
	private int sculptRadius=3;
	private int sculptType=0; //air
	private int sculptData=0; //data value if we need it
	
	public LinkedHashMap<Vector,Item[]> chestContents = new LinkedHashMap<Vector,Item[]>(10);
	public LinkedHashMap<Vector,ArrayList<String>> signText = new LinkedHashMap<Vector,ArrayList<String>>(10);
	
	public CuboidData getOriginal() {
		return original;
	}
	public void setOriginal(CuboidData original) {
		this.original = original;
	}

	
	public int getSculptRadius() {
		return sculptRadius;
	}

	public void setSculptRadius(int sculptRadius) {
		this.sculptRadius = sculptRadius;
	}

	public int getSculptType() {
		return sculptType;
	}

	public void setSculptType(int sculptType) {
		this.sculptType = sculptType;
	}

	public int getSculptData() {
		return sculptData;
	}

	public void setSculptData(int sculptData) {
		this.sculptData = sculptData;
	}
}
