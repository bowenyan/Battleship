/**
 * Ship factory
 * @author bowen Yan
 * @version May,2008
 *
 */
public class ShipFactory {
	/*initialize constant*/
	private static final ShipFactory factory = new ShipFactory();
	//other classes cannot instant class ShipFactory
	private ShipFactory(){}
	
	/**
	 * return an instance
	 * @return
	 */
	public static ShipFactory getInstance() {
		return factory;
	}
	
	/**
	 * get the type of ships
	 * @param type
	 * @return type or null
	 */
	public IShip getShip(String type) {
		if("BATTLESHIP".equals(type)) {
			return new BattleShipModel();
		} else if ("DESTROYERS".equals(type)) {
			return new DestroyersModel();
		} else if ("SUBMARINES".equals(type)) {
			return new SubmarinesModel();
		} else {
			return null;
		}
	}
}
