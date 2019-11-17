/**
 * It stores the basic information about Destroyers
 * This class inherits a superclass AbstractShip
 * @author Bowen Yan
 * @version May, 2008
 *
 */
public class DestroyersModel extends AbstractShip {
	private static final int SPACE = 3;  //then length of Destroyers

	public DestroyersModel() {
		space = SPACE;
		//initialize the length of array status
		status = new int[SPACE];
	}
	
	/**
	 * get spaces of Destroyers
	 */
	public int getSpace() {
		return SPACE;
	}

	/**
	 * get the type name of Destroyers
	 */
	public String getType() {
		return "DESTROYERS";
	}
}