
public class CuboidHookDispatcher implements PluginInterface {
	CuboidHookBase hook = null;
	/**
	 * Get my name, dude!
	 */
	public String getName() {
		return "CuboidAPI";
	}
	
	/**
	 * Get the amout of my parameters, dude!
	 */
	public int getNumParameters() {
		return 3;
	}
	
	public String checkParameters(Object[] os) {
		if(os.length < 3 || os.length > 5) {
			return "Invalid amount of parameters";
		}
		return null;
	}

	
	/**
	 * Index 0: Player or String (mode)
	 * Index 1: Block or String (areaname) (mode)
	 * Index 3: hashMap or String (entity) (arenaname) (mode)
	 * Index 4: String(areaname)
	 * Index 5: String(entity)
	 * Player, Block, &lt areaname &gt, &lt player/group &gt, &lt Area Flag List(highly optional) &gt, MODE
	 */
	public Object run(Object[] os) {
		if(os.length == 2 || os.length == 3) {
			if(os[0] instanceof Player) {
				hook = new CuboidHookChecks();
			}
			else {
				hook = new CuboidHookAreaAction();
			}
			
		}
		if(os.length == 4) {
			if(os[os.length-1] instanceof String) {
				String test = (String)os[os.length-1];
				//Precheck the mode as action and flag hooks have the same amount of args sometimes ..
				if(test.equalsIgnoreCase("AREA_GET_FLAG")) {
					hook = new CuboidHookAreaFlags();
				}
				else {
					hook = new CuboidHookAreaAction();
				}
			}
		}
		if(os.length == 5) {
			hook = new CuboidHookAreaFlags();
		}
		
		//EXECUTE
		if(hook != null) {
			return hook.run(os);
		}
		return null;
	}	
}
