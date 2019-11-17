/**
 * An interface for ships
 * @author Bowen Yan
 * @version May,2008
 */
public interface IShip {
	/*initialize constant variables*/
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	public static final int BODY_STATUS_HEALTH = 0;
	public static final int BODY_STATUS_DESTORY = 1;
	
	/*declare methods*/
	/**
	 * get the type of ships
	 * @return the type of ships
	 */
	public String getType();
	
	/**
	 * set coordinate of ships (x axis)
	 * @param x 
	 */
	public void setX(int x);
	
	/**
	 * get coordinate of ships (x axis)
	 * @return x axis coordinate
	 */
	public int getX();
	
	/**
	 * set coordinate of ships (y axis)
	 * @param y 
	 */
	public void setY(int y);
	
	/**
	 * get coordinate of ships (y axis)
	 * @return y axis coordinate
	 */
	public int getY();
	
	/**
	 * get the length of ships (spaces)
	 * @return spaces
	 */
	public int getSpace();
	
	/**
	 * set orientation of ships
	 * the orientation: horizontal and vertical
	 * use integer constant to represent orientation
	 * @param ori 
	 */
	public void setOrientation(int ori);
	
	/**
	 * get orientation of ships
	 * @return integer constant 0 or 1
	 */
	public int getOrientation();
	
	/**
	 * estimate whether the ship is sunk or not
	 * @return true or false
	 */
	public boolean isSunk();
	
	/**
	 * attack the ship with coordinate (x,y)
	 * @param x
	 * @param y
	 * @return true or false
	 */
	public boolean destroyXY(int x, int y);
	
	/**
	 * get the status of every space of ships
	 * the status: HEALTH or DESTORY
	 * @return the status 0 or 1
	 */
	public int[] getBodyStatus();
}
