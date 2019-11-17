/**
 * This class represents opponent battlefield model
 * It stores attack point and status of opponent battlefield
 * @author Bowen Yan
 * @version May,2008
 */
public class OpponentBattleGrid {
	/*initialize constant*/
	public static final int SUB_FIRE_ORI_WEST = 0;
	public static final int SUB_FIRE_ORI_EAST = 1;
	public static final String SHIP_FIRE = "shipfire";
	public static final String SUB_FIRE = "subfire";
	public static final String CANNOT_FIRE = "cannotfire";
	public static final String SHIP_FIRE_HIT = "shipfirehit";
	public static final String SUB_FIRE_HIT = "subfirehit";
	public static final String SHIP_FIRE_MISS = "shipfiremiss";
	public static final String SUB_FIRE_MISS = "subfiremiss";
	public static final String SHIP_FIRE_SUNK = "shipfiresunk";
	public static final String SUB_FIRE_SUNK = "subfiresunk";
	
	public static final int COORDINATE_EMPTY = 0;
	public static final int COORDINATE_FIRE = 1;
	public static final int COORDINATE_HIT = 2;
	public static final int COORDINATE_SUNK = 3;
	public static final int COORDINATE_MISS = 4;
	
	private int row, column, lastFireX, lastFireY;
	private String lastFireType;
	private int[][] grid;
	private OpponentBattleGridChangeSupport listeners = new OpponentBattleGridChangeSupport();
	
	/**
	 * Constructor for objects of class OpponentBattleGrid
	 * @param row
	 * @param column
	 */
	public OpponentBattleGrid(int row, int column) {
		this.row = row;
		this.column = column;
		lastFireX = lastFireY = -1;  //initialize the position of last fire
		grid = new int[column][row];
		// do not define as IShip[row][column], 
		// because when use (x,y) to find in grid,
		// x -> column, y -> row
	}
	
	/**
	 * upgrade the status of grid
	 */
	public void hit() {
		if(lastFireType != null) {
			if(lastFireType.equals(SHIP_FIRE)) {
				grid[lastFireX][lastFireY] = COORDINATE_HIT;
				//inform listeners that Shipfire hit with coordinate
				fireListener(SHIP_FIRE_HIT, new int[]{lastFireX, lastFireY});
			} else if(lastFireType.equals(SUB_FIRE)) {
				//inform listeners that Subfire hit without coordinate
				fireListener(SUB_FIRE_HIT);
			}
		}
		clearLastFireSign();  //clear the information of last fire
	}

	/**
	 * upgrade the status of grid
	 */
	public void miss() {
		if(lastFireType != null) {
			if(lastFireType.equals(SHIP_FIRE)) {
				grid[lastFireX][lastFireY] = COORDINATE_MISS;
				//inform listeners that Shipfire miss with coordinate
				fireListener(SHIP_FIRE_MISS, new int[]{lastFireX, lastFireY});
			} else if(lastFireType.equals(SUB_FIRE)) {
				//inform listeners that Subfire miss without coordinate
				fireListener(SUB_FIRE_MISS);
			}
		}
		clearLastFireSign();   //clear the information of last fire
	}
	
	/**
	 * estimate whether the fire is valid or not
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean canFire(int x, int y) {
		return testFireIsValidate(x, y);
	}
	
	/**
	 * estimate whether the fire is valid or not
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean testFireIsValidate(int x, int y) {
		//estimate whether it slops over or not
		if(x < 0 || x >= column || y < 0 || y >= row) return false;
		//estimate whether the position has been fired or not
		if(grid[x][y] != COORDINATE_EMPTY) return false;
		else return true;
	}

	/**
	 * shipfire from player to opponent
	 * @param x
	 * @param y
	 */
	public void shipFire(int x, int y) {
		if(testFireIsValidate(x, y)) {
			grid[x][y] = COORDINATE_FIRE;
			lastFireX = x;  //record the coordinate of attack
			lastFireY = y;  //record the coordinate of attack
			lastFireType = SHIP_FIRE;   //record the the attack type
			//inform listeners that the player shipfire a position
			fireListener(SHIP_FIRE, new int[]{x, y});
		} else {
			fireListener(CANNOT_FIRE, new int[]{x, y});
		}
	}

	/**
	 * subfire a torpedo from player to opponent
	 * @param y
	 */
	public void subFire(int y) {
		if(y >= 0 && y < column) {
			//inform listeners that the player subfire a torpedo
			fireListener(SUB_FIRE, y);
			lastFireType = SUB_FIRE;
		} else {
			fireListener(CANNOT_FIRE, y);
		}
	}

	/**
	 * upgrade the status of grid
	 * @param type
	 */
	public void sunk(String type) {
		if(lastFireType != null) {
			if(lastFireType.equals(SHIP_FIRE)) {
				grid[lastFireX][lastFireY] = COORDINATE_SUNK;
				fireListener(SHIP_FIRE_SUNK, new Object[]{lastFireX, lastFireY, type});
			} else if(lastFireType.equals(SUB_FIRE)) {
				fireListener(SUB_FIRE_SUNK, type);
			}
		}
		clearLastFireSign();   //clear the information of last fire
	}
	
	/**
	 * get the status of grid
	 * @return
	 */
	public int[][] getGridState() {
		return grid;
	}
	
	/**
	 * clear the information of last fire
	 */
	private void clearLastFireSign() {
		lastFireX = -1;
		lastFireY = -1;
		lastFireType = null;
	}
	
	/**
	 * add the listener
	 * @param l
	 */
	public void addListener(OpponentBattleGridListener l) {
		listeners.addListener(l);
	}
	
	/**
	 * remove the listener
	 * @param l
	 */
	public void removeListener(OpponentBattleGridListener l) {
		listeners.removeListener(l);
	}
	
	/**
	 * inform listeners without parameters
	 * @param event
	 */
	private void fireListener(String event) {
		listeners.fireUpdate(event, null);
	}
	
	/**
	 * inform listeners with parameters
	 * @param event
	 * @param arg
	 */
	private void fireListener(String event, Object arg) {
		listeners.fireUpdate(event, arg);
	}

}
