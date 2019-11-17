/**
 * A controller controls the information of the player's battlefield model
 * @author Bowen Yan
 * @version May, 2008
 *
 */
public class FireMeController {
	private BattleGrid battleGrid;
	
	public FireMeController(BattleGrid battleGrid) {
		this.battleGrid = battleGrid;
	}

	/**
	 * shipfire to the appointed position from opponent
	 * @param x
	 * @param y
	 */
	public void shipFire(int x, int y) {
		battleGrid.shipFire(x, y);		
	}

	/**
	 * fire a torpedo from opponent 
	 * @param y
	 */
	public void subFire(int y) {
		battleGrid.subFire(y);
	}
	
	/**
	 * the player wins
	 */
	public void win() {
		battleGrid.setWinner(true);
	}
	
	/**
	 * the player loses
	 */
	public void lose() {
		battleGrid.setWinner(false);
	}
}
