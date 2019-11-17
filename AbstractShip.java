/**
 * A superclass for Battleships, Submarines and Destroyers
 * It implements some universal methods for ships (E.g get orientation etc.)
 * Inherits IShip interface
 * @author Bowen Yan
 * @version May, 2008
 *
 */
public abstract class AbstractShip implements IShip {
	protected int ori = HORIZONTAL;   //initialize orientation of ships
	protected int x;       //x axis
	protected int y;       //y axis
	protected int space = 0;      //the length of ships
	protected int[] status = new int[0];   //the status of every space of ships
	protected boolean destroy;         //estimate whether the ship is sunk or not

	/*
	 * (non-Javadoc)
	 * @see IShip#getOrientation()
	 */
	public int getOrientation() {
		return ori;
	}

	/*
	 * (non-Javadoc)
	 * @see IShip#getX()
	 */
	public int getX() {
		return x;
	}

	/*
	 * (non-Javadoc)
	 * @see IShip#getY()
	 */
	public int getY() {
		return y;
	}

	/*
	 * (non-Javadoc)
	 * @see IShip#isSunk()
	 */
	public boolean isSunk() {
		return destroy;
	}

	/*
	 * (non-Javadoc)
	 * @see IShip#setOrientation(int)
	 */
	public void setOrientation(int ori) {
		this.ori = ori;
	}

	/*
	 * (non-Javadoc)
	 * @see IShip#setX(int)
	 */
	public void setX(int x) {
		this.x = x;
	}

	/*
	 * (non-Javadoc)
	 * @see IShip#setY(int)
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/*
	 * (non-Javadoc)
	 * @see IShip#destroyXY(int, int)
	 */
	public boolean destroyXY(int x, int y) {
		//if the orientation of ships is horizontal
		if(this.ori == IShip.HORIZONTAL) {
			//if the attack is not in the range of coordinates of ships 
			//or this space has already been destroyed, then return false
			if(y != this.y || x < this.x || x > this.x + space - 1
			|| status[x - this.x] == BODY_STATUS_DESTORY) {
				return false;
			} else {
				status[x - this.x] = BODY_STATUS_DESTORY;  //set the destroyable space 
				checkShipStatus();       //check whether the ship is sunk or not
				return true;         //the attack succeeded
			}
		} else if(this.ori == IShip.VERTICAL) {   //if the orientation of ships is vertical
			//if the attack is not in the range of coordinates of ships 
			//or this space has already been destroyed, then return false
			if(x != this.x || y < this.y || y > this.y + space - 1
			|| status[y - this.y] == BODY_STATUS_DESTORY) {
				return false;
			} else {
				status[y - this.y] = BODY_STATUS_DESTORY;   //set the destroyable space 
				checkShipStatus();     //check whether the ship is sunk or not
				return true;     //the attack succeeded
			}
		}
		
		return false;
		
	}
	
	/**
	 * check whether the ship is sunk or not
	 */
	private void checkShipStatus() {
		for(int i = 0; i < status.length; i++) {
			if(status[i] == BODY_STATUS_HEALTH) break;
			//if every space of the ship is destroyed, the ship is sunk
			if(i == status.length - 1) destroy = true;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see IShip#getBodyStatus()
	 */
	public int[] getBodyStatus() {
		return status;
	}
}
