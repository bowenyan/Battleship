/**
 * A controller controls the information of the opponent's battlefield model
 * @author Bowen Yan
 * @version May, 2008
 *
 */
import java.util.List;

public class FireOpponentController {
	private OpponentBattleGrid opponentBattleGrid;
	private BattleGrid battleGrid;
	
	public FireOpponentController(OpponentBattleGrid opponentBattleGrid, BattleGrid battleGrid) {
		this.opponentBattleGrid = opponentBattleGrid;
		this.battleGrid = battleGrid;
	}

	/**
	 * shipfire from player to opponent
	 * @param x
	 * @param y
	 */
	public void shipFire(int x, int y) {
		opponentBattleGrid.shipFire(x, y);
	}

	/**
	 * subfire a torpedo from player to opponent
	 * @param y
	 */
	public void subFire(int y) {
		opponentBattleGrid.subFire(y);
	}

	/**
	 * upgrade the status of grid
	 */
	public void hit() {
		opponentBattleGrid.hit();
	}

	/**
	 * upgrade the status of grid
	 */
	public void miss() {
		opponentBattleGrid.miss();
	}

	/**
	 * upgrade the status of grid
	 * @param type
	 */
	public void sunk(String type) {
		opponentBattleGrid.sunk(type);
	}
	
	/**
	 * get the ship from (x,y) coordinate
	 * @param x
	 * @param y
	 * @return
	 */
	public IShip getMyShip(int x, int y) {
		return battleGrid.getShip(x, y);
	}

	/**
	 * estimate whether the player can shipfire opponent or not
	 * @return
	 */
	public boolean canShipFire() {
		List<IShip> ships = battleGrid.getShips();
		for(IShip ship : ships) {
			//at least one ship exists
			if(!ship.getType().equals("SUBMARINES")
			&& !ship.isSunk()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * estimate whether the player can fire a torpedo or not
	 * @return
	 */
	public boolean canSubFire() {
		List<IShip> ships = battleGrid.getShips();
		for(IShip ship : ships) {
			//at least one submarine exists
			if(ship.getType().equals("SUBMARINES")
			|| !ship.isSunk()) {
				return true;
			}
		}
		return false;
	}
	
	
}
