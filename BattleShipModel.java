/**
 * It stores the basic information about Battleships
 * This class inherits a superclass AbstractShip
 * @author Bowen Yan
 * @version May, 2008
 *
 */
public class BattleShipModel extends AbstractShip {
	private static final int SPACE = 4;   //the length of Battleships

	public BattleShipModel() {
		space = SPACE;
		//initialize the length of array status
		status = new int[SPACE];
	}
	
	/**
	 * get spaces of Battleships
	 */
	public int getSpace() {
		return SPACE;
	}

	/**
	 * get the type name of Battleships
	 */
	public String getType() {
		return "BATTLESHIP";
	}
}