/**
 * It stores the basic information about Submarines
 * This class inherits a superclass AbstractShip
 * @author Bowen Yan
 * @version May, 2008
 *
 */
public class SubmarinesModel extends AbstractShip {
	private static final int SPACE = 2;  //the length of Submarines

	public SubmarinesModel() {
		space = SPACE;
		//initialize the length of array status
		status = new int[SPACE];
	}
	
	/**
	 * get spaces of Submarines
	 */
	public int getSpace() {
		return SPACE;
	}

	/**
	 * get the type name of Submarines
	 */
	public String getType() {
		return "SUBMARINES";
	}
}
