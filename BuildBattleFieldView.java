/**
 * This class builds the initial view of battlefield
 * It inherits JPanel and implement BattleGridListener interface
 * Listens to the change of model (BattleGrid)
 * @author Bowen Yan
 * @version May, 2008
 * 
 */
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

import javax.swing.JPanel;

public class BuildBattleFieldView extends JPanel implements BattleGridListener {
	private static final long serialVersionUID = -886162511417682976L;
	private static final int GRID_BOX_WIDTH = 30;   //the size of each grid
	private static final int SEPARATOR_WIDTH = 5;   //the size of gap between two players' battlefield
	/*the usual sign of mouse*/
	private static final Cursor CURSOR_CUSTOMER = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	/*the sign of mouse represents that the ship cannot be placed*/
	private static final Cursor CURSOR_CANNOT = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
	/*the sign of dragging mouse*/
	private static final Cursor CURSOR_DRAG = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	/*isDragShip means whether the ship is moving or not*/
	private boolean isDragShip, enable;
	private IShip dragShip;  //store the previous information of the ship, which has been dragged
	private IShip freebackShip;  //the dynamic effect of ships
	private BattleGrid battleGrid;
	private BuildBattleFieldController controller;
	private ShipImageFactory imgFactory;
	private ShipFactory shipFactory;
	
	/**
	 * Constructor for objects of class BuildBattleFieldView
	 * @param buildBattlefieldController
	 * @param battleGrid
	 */
	public BuildBattleFieldView(BuildBattleFieldController buildBattlefieldController, BattleGrid battleGrid) {
		this.battleGrid = battleGrid;
		this.controller = buildBattlefieldController;
		this.imgFactory = ShipImageFactory.getInstance();  //get shipimagefactory instance
		this.shipFactory = ShipFactory.getInstance();     //get shipfactory instance
		//create an object for mouse event on this view
		/*register the object as a listener*/
		this.addMouseListener(new BuildMouseListener(this)); //click event
		this.addMouseMotionListener(new BuildMouseMotionListener(this));  //drag event
		//create an object for keyboard event on this view
		/*register the object as a listener*/
		this.addKeyListener(new BuildKeyAdapter(this));
		battleGrid.addListener(this);    //add BuildBattleFiedleView into listener list
		initialize();   //initialize the position of all the ships
	}
	
	public void initialize() {	
		this.setPreferredSize(new Dimension(613, 311));
		/*initialize 6 ships*/
		controller.addShip("BATTLESHIP", 0, 0, IShip.HORIZONTAL);
		controller.addShip("DESTROYERS", 0, 1, IShip.HORIZONTAL);
		controller.addShip("DESTROYERS", 0, 2, IShip.HORIZONTAL);
		controller.addShip("DESTROYERS", 0, 3, IShip.HORIZONTAL);
		controller.addShip("SUBMARINES", 0, 4, IShip.HORIZONTAL);
		controller.addShip("SUBMARINES", 0, 5, IShip.HORIZONTAL);
	}

	/**
	 * Repaint the view of battlefield.
	 * Invoke the abstract method in interface BattleGridListener.
	 */
	public void battleGridUpdate(String event, Object arg) {
		//invoke the static string from BattleGrid class
		//estimate whether add a new ship or not
		//repaint the GUI
		if(BattleGrid.ADD_SHIP.equals(event)) {
			repaint();  
		}
	}

	/**
	 * How to repaint the initial view of battlefield.
	 */
	protected void paintComponent(Graphics g) {
		paintGrid(g);
		paintShip(g);
		if(isDragShip) {
			paintFreeback(g);
		}
	}
	
	/**
	 * draw both battlefields
	 * @param g
	 */
	private void paintGrid(Graphics g) {
		int row = battleGrid.getRow();
		int column = battleGrid.getColumn();
		int width = GRID_BOX_WIDTH * column;
		int height = GRID_BOX_WIDTH * row;
		/*draw my battlefield*/
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, width, height);  //draw a rectangle from (0,0) coordinate
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width, height);  //draw a frame
		//draw horizontal lines
		for(int i = 0; i < height; i += GRID_BOX_WIDTH) {
			g.drawLine(0, i, width, i);
		}
		//draw vertical lines
		for(int j = 0; j < width; j += GRID_BOX_WIDTH) {
			g.drawLine(j, 0, j, height);
		}
		
		/*draw seperator*/
		g.setColor(Color.GRAY);
		g.fillRect(width + 1, 0, SEPARATOR_WIDTH, height);
		
		/*draw opponent battlefield*/
		int x = width + SEPARATOR_WIDTH;
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x, 0, width, height);  //draw a rectangle from (x,0) coordinate
		g.setColor(Color.BLACK);
		g.drawRect(x, 0, width, height);  //draw a frame
		//draw horizontal lines
		for(int i = 0; i < height; i += GRID_BOX_WIDTH) {
			g.drawLine(x, i, width + x, i);
		}
		//draw vertical lines
		for(int j = 0; j < width; j += GRID_BOX_WIDTH) {
			g.drawLine(j + x, 0, j + x, height);
		}
	}
	
	/**
	 * draw my ships
	 * @param g
	 */
	private void paintShip(Graphics g) {
		List<IShip> ships = battleGrid.getShips();  //get the ships list
		for(IShip ship : ships) {
			// get ships' images from ShipImageFactory
			Image img = imgFactory.getShipImage(ship);
			if(img != null) {
				//draw ships on the corresponding coordinate
				g.drawImage(img, ship.getX() * GRID_BOX_WIDTH, ship.getY()* GRID_BOX_WIDTH, this);
			}
		}
	}
	
	/**
	 * draw the image of dragging a ship
	 * @param g
	 */
	private void paintFreeback(Graphics g) {
		g.drawImage(imgFactory.getShipImage(freebackShip), 
					freebackShip.getX() * GRID_BOX_WIDTH, 
					freebackShip.getY() * GRID_BOX_WIDTH, this);
	}
	
	/**
	 * estimate whether the current position can place a ship or not
	 * @return
	 */
	private boolean canDrop() {
		return controller.canSet(  //invoke BuildBattleFieldController method
		freebackShip.getType(), freebackShip.getX(), 
		freebackShip.getY(), freebackShip.getOrientation());
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.Component#isFocusTraversable()
	 */
	public boolean isFocusTraversable() {
		return true;
	}
	
	/**
	 * set whether this view can be operated or not
	 * @param enable
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	/**
	 * listen to the mouse event (click)
	 * @author Bowen Yan
	 *
	 */
	private class BuildMouseListener extends MouseAdapter {
		private BuildBattleFieldView view;
		BuildMouseListener(BuildBattleFieldView view) {
			this.view = view;
		}
		public void mouseClicked(MouseEvent e) {
			if(view.enable) {  //if the view is enable to operate
				if(!isDragShip) {  //if click a ship before dragging a ship
					int mx = e.getX();
					int my = e.getY();
					int x = mx / GRID_BOX_WIDTH;
					int y = my / GRID_BOX_WIDTH;
					IShip ship = controller.getShip(x, y);
					
					if(ship != null) {
						controller.removeShip(x, y); //remove the ship from model
						/*create a view of feedback for the ship*/
						freebackShip = shipFactory.getShip(ship.getType());
						/*set the information of the ship*/
						freebackShip.setX(x);
						freebackShip.setY(y);
						isDragShip = true;  //could drag the ship
						//store the information of removed ship
						//if meet irregular operation, the ship will be back to the previous postion
						dragShip = ship;
						view.setCursor(CURSOR_DRAG); //set the sign of dragging mouse
					}
				} else {  //the ship has already been dragged, which needs to be placed
					if(canDrop()) {  //if can drop
						controller.addShip(  //add the ship with current information into model again
						freebackShip.getType(), freebackShip.getX(), 
						freebackShip.getY(), freebackShip.getOrientation());
					} else {  //if cannot drop, add the ship with previoud information into model again
						controller.addShip( 
						dragShip.getType(), dragShip.getX(), 
						dragShip.getY(), dragShip.getOrientation());
					}
					isDragShip = false;
					dragShip = null;
					freebackShip = null;
					view.setCursor(CURSOR_CUSTOMER);  //set the usual sign of mouse
				}
			}
		}			
	}
	
	/**
	 * listen to the motion event of mouse (drag)
	 * @author Bowen Yan
	 *
	 */
	private class BuildMouseMotionListener extends MouseMotionAdapter {
		private BuildBattleFieldView view;
		BuildMouseMotionListener(BuildBattleFieldView view) {
			this.view = view;
		}
		public void mouseMoved(MouseEvent e) {
			//if the ship has already been clicked
			//record the position of dragging the ship
			if(isDragShip) {
				int mx = e.getX();
				int my = e.getY();
				int x = mx / GRID_BOX_WIDTH;
				int y = my / GRID_BOX_WIDTH;
				freebackShip.setX(x);
				freebackShip.setY(y);
				//if meet the irregular operation, set the sign of mouse represents that the ship cannot be placed
				if(!canDrop()) view.setCursor(CURSOR_CANNOT); 
				else view.setCursor(CURSOR_DRAG);  //set the sign of dragging mouse
				repaint(); //repaint the view immediately in order to display the view of feedback
			}
		}
	}
	
	/**
	 * listen to the keyboard event
	 * use Right and Down keys to assign the orientation of ships
	 * @author Bowen Yan
	 *
	 */
	private class BuildKeyAdapter implements KeyListener {
		private BuildBattleFieldView view;
		BuildKeyAdapter(BuildBattleFieldView view) {
			this.view = view;
		}
		public void keyPressed(KeyEvent e) {
			if(isDragShip) {  //if drag a ship
				if(e.getKeyCode() == KeyEvent.VK_DOWN) { //if click a Down key
					//if the orientation of the ship is vertical
					if(freebackShip.getOrientation() == IShip.VERTICAL) //reduce repaint
						return;
					//set the orientation of the ship
					freebackShip.setOrientation(IShip.VERTICAL);
					refreshGrid();  //refresh the view
					
				} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) { //if click a Right key
					//if the orientation of the ship is horizontal
					if(freebackShip.getOrientation() == IShip.HORIZONTAL) //reduce repaint
						return;
					//set the orientation of the ship
					freebackShip.setOrientation(IShip.HORIZONTAL);
					//refresh the view
					refreshGrid();
				}
			}
		}
		
		/**
		 * check and repaint the view
		 */
		private void refreshGrid() {
			//when change the orientation of the ship, check the operation is irregular or not
			if(!canDrop()) view.setCursor(CURSOR_CANNOT);
			else view.setCursor(CURSOR_DRAG);
			repaint();  //repaint the view
		}
		
		//need not listen to these events
		public void keyReleased(KeyEvent e)	{ /* do nothing*/ }
		public void keyTyped(KeyEvent e)	{ /* do nothing*/ }		
	}

}
