/**
 * This class represents my battlefield model
 * It stores information of my battlefield
 * @author Bowen Yan
 * @version May,2008
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BattleGrid {
	/*initialize constant*/
	public static final int FIRETYPE_SHIPFIRE = 0;
	public static final int FIRETYPE_SUBFIRE = 1;
	
	public static final String CANNOT_SET_HERE = "cannotsethere";
	public static final String CANNOT_USE_SHIPFIRE = "cannotuseshipfire";
	public static final String CANNOT_USE_SUBFIRE = "cannotusesubfire";
	public static final String SHIP_SUNK = "shipsunk";
	public static final String FIRE_MISS = "firemiss";
	public static final String FIRE_HIT = "firehit";
	public static final String ILOSE = "ilose";
	public static final String IWIN = "iwin";
	
	public static final String ADD_SHIP = "addship";
	
	public static final int COORDINATE_EMPTY = 0;
	public static final int COORDINATE_HIT = 1;
	public static final int COORDINATE_SUNK = 2;
	public static final int COORDINATE_MISS = 3;
	
	private static final int ADD = 0;
	private static final int REMOVE = 1;
	private int row;
	private int column;
	private boolean isWinner;
	private List<IShip> ships;  //the list of ships
	private IShip[][] grid;     //store a matrix for all the ships
	private int[][] status;     //store the status of every grid
	private BattleGridChangeSupport listeners = new BattleGridChangeSupport();
	
	/**
	 * Constructor for objects of class BattleGrid
	 * @param row
	 * @param column
	 */
	public BattleGrid(int row, int column) {
		this.row = row;
		this.column = column;
		grid = new IShip[column][row];
		status = new int[column][row];
		// do not define as IShip[row][column], 
		// because when use (x,y) to find in grid,
		// x -> column, y -> row
	}

	/**
	 * get the column of my battlefield
	 * @return
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * get the row of my battlefield
	 * @return
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * get the status of every grid on my battlefield
	 * @return
	 */
	public int[][] getStatus() {
		return status;
	}

	/**
	 * get the collection of ships
	 * @return
	 */
	public List<IShip> getShips() {
		//if the list of ships is empty, return empty list
		if(ships == null) return Collections.emptyList();
		return ships;
	}

	/**
	 * get the ship from (x,y) coordinate
	 * @param x
	 * @param y
	 * @return
	 */
	public IShip getShip(int x, int y) {
		//if slop over return null
		if(x< 0 || x > column - 1 || y < 0 || y > row - 1) {
			return null;
		}
		return grid[x][y];
	}

	/**
	 * public method
	 * estimate whether the ship can be placed on my battlefield or not
	 * invoke private method
	 * @param ship
	 * @return
	 */
	public boolean canSet(IShip ship) {
		return testSetIsValidate(ship);
	}
	
	/**
	 * private method
	 * estimate whether the ship can be placed on my battlefield or not
	 * @param ship
	 * @return
	 */
	private boolean testSetIsValidate(IShip ship) {
		int x = ship.getX();
		int y = ship.getY();
		int space = ship.getSpace();
		//estimate whether it slops over or not
		if(x < 0 || x >= column || y < 0 || y >= row
		|| ship.getOrientation() == IShip.HORIZONTAL && x + space > column 
		|| ship.getOrientation() == IShip.VERTICAL && y + space > row) {
			return false;
		} else {
			//if the orientation is horizontal
			if(ship.getOrientation() == IShip.HORIZONTAL) {
				for(int i = 0; i < space; i++) {
					//estimate whether the coordinate is empty or not
					if(grid[x + i][y] != null) {
						return false;
					}
				}   //if the orientation is vertical
			} else if(ship.getOrientation() == IShip.VERTICAL) {
				for(int i = 0; i < space; i++) {
					//estimate whether the coordinate is empty or not
					if(grid[x][y + i] != null) {
						return false;
					}
				}
			}
			return true;  //can place
		}
	}

	/**
	 * add a new ship
	 * @param ship
	 */
	public void addShip(IShip ship) {
		if(ships == null) ships = new ArrayList<IShip>();
		if(ship != null) {
			if(!testSetIsValidate(ship)) {
				//the ship cannot be placed on the position
				//inform listeners (view)
				fireListener(CANNOT_SET_HERE);
				return;
			}
			//add a new ship into list
			ships.add(ship);
			//inform listeners the ship has been added
			fireListener(ADD_SHIP);
			//upgrade the matrix of ships
			upgradeBattleGrid(ship, ADD);
		}
	}

	/**
	 * remove a ship
	 * @param x
	 * @param y
	 */
	public void removeShip(int x, int y) {
		if(x< 0 || x > column - 1 || y < 0 || y > row - 1) {
			return;
		}
		IShip ship = grid[x][y];
		ships.remove(ship);
		upgradeBattleGrid(ship, REMOVE);
	}
	
	/**
	 * upgrade the matrix of ships
	 * @param ship
	 * @param type
	 */
	private void upgradeBattleGrid(IShip ship, int type) {
		int x = ship.getX();
		int y = ship.getY();
		int space = ship.getSpace();
		
		if(ADD == type) {
			if(ship.getOrientation() == IShip.HORIZONTAL) {
				for(int i = 0; i < space; i++) {
					grid[x + i][y] = ship;   //store the variable reference of ship into corresponding grids
				}
			} else if(ship.getOrientation() == IShip.VERTICAL) {
				for(int i = 0; i < space; i++) {
					grid[x][y + i] = ship;   //store the variable reference of ship into corresponding grids
				}
			}
		} else if(REMOVE == type) {  //remove a ship
			if(ship.getOrientation() == IShip.HORIZONTAL) {  
				for(int i = 0; i < space; i++) {
					grid[x + i][y] = null;     //remove the variable reference of ship into corresponding grids
				}
			} else if(ship.getOrientation() == IShip.VERTICAL) {
				for(int i = 0; i < space; i++) {
					grid[x][y + i] = null;      //remove the variable reference of ship into corresponding grids
				}
			}
		}
	}

	/**
	 * shipfire to the appointed position from opponent to player
	 * @param x
	 * @param y
	 */
	public void shipFire(int x, int y) {
		IShip ship = grid[x][y];
		if(ship == null || ship.isSunk()) {
			//this position is empty or the ship has already been sunk
			status[x][y] = COORDINATE_MISS;
			//inform listeners that the fire missed
			fireListener(FIRE_MISS);
		} else {
			//if the ship has been destroyed
			if(ship.destroyXY(x, y)) {
				//set the status to be HIT
				status[x][y] = COORDINATE_HIT;
				//inform listeners which type of ship has been hit
				//if every component of ship has been destroyed, the ship is sunk
				fireListener(FIRE_HIT, ship.getType());
				if(ship.isSunk()) {
					//upgrade the status of the latest ship, which has been sunk
					setGridStatusForSunk(ship);
					//inform listeners which type of ship has been sunk
					fireListener(SHIP_SUNK, ship.getType());
					//check whether all of ships have been sunk
					checkIsLost();
				}
			} else {
				status[x][y] = COORDINATE_MISS;
				fireListener(FIRE_MISS);
			}
		}
	}
	
	/**
	 * upgrade the status of the latest ship, which has been sunk
	 * @param ship
	 */
	private void setGridStatusForSunk(IShip ship) {
		int x = ship.getX();
		int y = ship.getY();
		int space = ship.getSpace();
		if(ship.getOrientation() == IShip.HORIZONTAL) {  //if the orientation is horizontal
			for(int i = 0; i < space; i ++) {
				status[x + i][y] = COORDINATE_SUNK;
			}
		} else if(ship.getOrientation() == IShip.VERTICAL) {  //if the orientation is vertical
			for(int i = 0; i < space; i ++) {
				status[x][y + i] = COORDINATE_SUNK;
			}
		}
	}

	/**
	 * fire a torpedo from opponent to player
	 * @param y
	 */
	public void subFire(int y) {
		subFireRecursion(0, y); //y is the orientation of row
	}
	
	/**
	 * use recursion to test subfire
	 * @param x
	 * @param y
	 */
	private void subFireRecursion(int x, int y) {
		if(x > column - 1) {
			//make sure that do not cause array out of bound
			fireListener(FIRE_MISS);
			return;
		}
		IShip ship = grid[x][y];
		//this position is empty or the ship has already been sunk
		if(ship == null || ship.isSunk()) {
			subFireRecursion(x + 1, y);
		} else {
			if(ship.destroyXY(x, y)) {
				//set the status to be HIT
				status[x][y] = COORDINATE_HIT;
				//inform listeners which type of ship has been hit
				fireListener(FIRE_HIT, ship.getType());
				if(ship.isSunk()) {
					setGridStatusForSunk(ship);
					//inform listeners which type of ship has been sunk
					fireListener(SHIP_SUNK, ship.getType());
					//check whether all of ships have been sunk
					checkIsLost();
				}
				return;
			} else {
				// because the ship is not sunk, so if destoryXY 
				// return false, it means this coordinate is the 
				// ship destroy body
				status[x][y] = COORDINATE_MISS;
				fireListener(FIRE_MISS);
				return;
			}
		}
	}
	
	/**
	 * check whether all of ships have been sunk or not
	 */
	private void checkIsLost() {
		int i = 0;
		for(;i < ships.size();i++) {
			if(!ships.get(i).isSunk()) break;
		}
		if(i == ships.size()) {
			//inform listeners ILOSE
			fireListener(ILOSE);
		}
	}
	
	/**
	 * set winner and loser
	 * @param win
	 */
	public void setWinner(boolean win) {
		this.isWinner = win;
		//inform listeners
		if(win == false) fireListener(ILOSE);
		else fireListener(IWIN);
	}
	
	/**
	 * estimate whether I win or not
	 * @return
	 */
	public boolean isWinner() {
		return isWinner;
	}

	/**
	 * add the listener
	 * @param l
	 */
	public void addListener(BattleGridListener l) {
        listeners.addListener(l);
    }

	/**
	 * remove the listener
	 * @param l
	 */
	public void removeListener(BattleGridListener l) {
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
