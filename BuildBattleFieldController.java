/**
 * The class is a controller,
 * which is used to modify my battlefield in initial view 
 * @author Bowen Yan
 * @version May, 2008
 *
 */
public class BuildBattleFieldController {
	private BattleGrid battleGrid;
	private ShipFactory factory;   //ship factory
	
	/**
	 * Constructor for objects of class BuildBattleFieldController
	 * @param battleGrid
	 */
	public BuildBattleFieldController(BattleGrid battleGrid) {
		factory = ShipFactory.getInstance();  //instance ship factory
		this.battleGrid = battleGrid;
	}
	
	/**
	 * add a new ship
	 * @param type
	 * @param x
	 * @param y
	 * @param ori
	 */
	public void addShip(String type, int x, int y, int ori) {
		IShip ship = factory.getShip(type);  //get the type of ships

		if(ship != null) {
			ship.setX(x);
			ship.setY(y);
			ship.setOrientation(ori);
			battleGrid.addShip(ship);  //modify the battleGrid model
		}
	}

	/**
	 * get the ship from (x,y) coordinate
	 * @param x
	 * @param y
	 * @return
	 */
	public IShip getShip(int x, int y) {
		return battleGrid.getShip(x, y);
	}

	/**
	 * remove a ship
	 * @param x
	 * @param y
	 */
	public void removeShip(int x, int y) {
		battleGrid.removeShip(x, y);
	}

	/**
	 * estimate whether the ship can be placed on my battlefield or not
	 * @param type
	 * @param x
	 * @param y
	 * @param ori
	 * @return
	 */
	public boolean canSet(String type, int x, int y, int ori) {
		IShip ship = factory.getShip(type);
		
		if(ship != null) {
			ship.setX(x);
			ship.setY(y);
			ship.setOrientation(ori);
			return battleGrid.canSet(ship);
		}
		
		return false;
	}

}
