/**
 * The view for real-time battlefield
 * This class inherits JPanel
 * It implements BattleGridListener and OpponentBattleGridListener interfaces 
 * It can reprint view immediately according to listening the change of models
 * Models are BattleGrid and OpponentBattleGrid
 * @author Bowen Yan
 * @version May, 2008
 */
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JPanel;

public class BattleFieldView extends JPanel implements BattleGridListener, OpponentBattleGridListener {
	private static final long serialVersionUID = -7655845152450294264L;
	private static final int GRID_BOX_WIDTH = 30;  //the size of each grid
	private static final int SEPARATOR_WIDTH = 5;  //the size of gap between two players' battlefield
	/**
	 * Use the getResource method to get the absolute URL which stores the WAV 
	 */
	private static final AudioClip shipfire = Applet.newAudioClip(BattleFieldView.class.getResource("sounds/shipfire.wav"));
	private static final AudioClip subfire = Applet.newAudioClip(BattleFieldView.class.getResource("sounds/subfire.wav"));
	private static final AudioClip sunk = Applet.newAudioClip(BattleFieldView.class.getResource("sounds/sunk.wav"));
	private static final AudioClip hit = Applet.newAudioClip(BattleFieldView.class.getResource("sounds/hit.wav"));
	private static final AudioClip miss = Applet.newAudioClip(BattleFieldView.class.getResource("sounds/miss.wav"));
	private static final AudioClip winner = Applet.newAudioClip(BattleFieldView.class.getResource("sounds/winner.wav"));
	private static final AudioClip loser = Applet.newAudioClip(BattleFieldView.class.getResource("sounds/loser.wav"));
	private boolean enable, gameover;
	private BattleGrid battleGrid;
	private OpponentBattleGrid opponentBattleGrid;
	private FireOpponentController fireOpponentController;
	private ShipImageFactory imgFactory;
	
	/**
	 * Constructor for objects of class BattleFieldView
	 * @param fireOpponentController
	 * @param battleGrid
	 * @param opponentBattleGrid
	 */
	public BattleFieldView(FireOpponentController fireOpponentController, 
						   BattleGrid battleGrid, 
						   OpponentBattleGrid opponentBattleGrid) {
		this.battleGrid = battleGrid;
		this.opponentBattleGrid = opponentBattleGrid;
		this.fireOpponentController = fireOpponentController;
		this.imgFactory = ShipImageFactory.getInstance();  //get shipimagefactory instance
		//create an object for mouse event on this view
		FireMouseListener fml = new FireMouseListener();
		/*register the object as a listener*/
		this.addMouseListener(fml);
		this.addMouseMotionListener(fml);
		/*register view as a listener on BattleGrid and OpponentBattleGrid models*/
		battleGrid.addListener(this);
		opponentBattleGrid.addListener(this);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		paintGrid(g);
		paintShip(g);
		paintStatus(g);
		paintOpponent(g);
		if(gameover) paintWinnerAndLoser(g);
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
		g.drawRect(x, 0, width, height);   //draw a frame
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
				g.drawImage(img, ship.getX() * GRID_BOX_WIDTH, ship.getY() * GRID_BOX_WIDTH, this);
			}
		}
	}
	
	/**
	 * draw the status of my battlefield
	 * hit: orange, sunk: red, miss: white
	 * @param g
	 */
	private void paintStatus(Graphics g) {
		/*get the status of my battlefield*/
		int[][] status = battleGrid.getStatus();
		/*get transparent colour*/
		Color red = Util.getTransparentColor(Color.RED, 0.6f);
		Color orange = Util.getTransparentColor(Color.ORANGE, 0.6f);
		Color white = Util.getTransparentColor(Color.WHITE, 0.6f);
		/*draw every grid with corresponding colours*/
		for(int x = 0; x < status.length; x++) {
			for(int y = 0; y < status[x].length; y++) {
				if(status[x][y] == BattleGrid.COORDINATE_HIT) g.setColor(orange);
				else if(status[x][y] == BattleGrid.COORDINATE_SUNK) g.setColor(red);
				else if(status[x][y] == BattleGrid.COORDINATE_MISS) g.setColor(white);
				else continue;
				//draw every grid
				g.fillRect(x * GRID_BOX_WIDTH, y * GRID_BOX_WIDTH,  
						   GRID_BOX_WIDTH, GRID_BOX_WIDTH);
			}
		}
	}
	/**
	 * draw opponent battlefield
	 * @param g
	 */
	private void paintOpponent(Graphics g) {
		//get the offset from my battlefield and separator
		int offset = GRID_BOX_WIDTH * battleGrid.getColumn() + SEPARATOR_WIDTH;
		//get the status of opponent battlefield
		int[][] oppGrid = opponentBattleGrid.getGridState();
		Color color = null;
		/*draw every grid with corresponding colours*/
		for(int i = 0; i < oppGrid.length; i++) {
			for(int j = 0; j < oppGrid[i].length; j++) {
				if(oppGrid[i][j] == OpponentBattleGrid.COORDINATE_MISS) {
					color = Util.getTransparentColor(Color.WHITE, 0.6f);
				} else if(oppGrid[i][j] == OpponentBattleGrid.COORDINATE_HIT) {
					color = Util.getTransparentColor(Color.ORANGE, 0.6f);
				} else if(oppGrid[i][j] == OpponentBattleGrid.COORDINATE_SUNK) {
					color = Util.getTransparentColor(Color.RED, 0.6f);
				} else {
					continue;
				}
				g.setColor(color);
				//draw every grid
				g.fillRect(i * GRID_BOX_WIDTH + offset, 
						   j * GRID_BOX_WIDTH, 
						   GRID_BOX_WIDTH, GRID_BOX_WIDTH);
			}
		}
	}
	
	/**
	 * draw a sign for winner and loser 
	 * @param g
	 */
	private void paintWinnerAndLoser(Graphics g) {
		int x = (GRID_BOX_WIDTH * battleGrid.getColumn() / 3);
		int y = (GRID_BOX_WIDTH * battleGrid.getColumn() / 3);
		int offset = GRID_BOX_WIDTH * battleGrid.getColumn() + SEPARATOR_WIDTH;
		g.setFont(new Font("", Font.BOLD, 25));
		if(battleGrid.isWinner()) {
			g.setColor(Color.RED);
			g.drawString("Winner", x, y);
			g.setColor(Color.GREEN);
			g.drawString("Loser", x + offset, y);
		} else {
			g.setColor(Color.GREEN);
			g.drawString("Loser", x, y);
			g.setColor(Color.RED);
			g.drawString("Winner", x + offset, y);
		}
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

	/*
	 * (non-Javadoc)
	 * @see BattleGridListener#battleGridUpdate(java.lang.String, java.lang.Object)
	 */
	public void battleGridUpdate(String event, Object arg) {
		/*inform audio*/
		if(BattleGrid.SHIP_SUNK.equals(event)) {
			sunk.play();
		} else if(BattleGrid.FIRE_HIT.equals(event)) { 
			hit.play();
		} else if(BattleGrid.FIRE_MISS.equals(event)) {
			miss.play();
		} else if(BattleGrid.ILOSE.equals(event)) {
			loser.play();
			gameover = true;
		} else if(BattleGrid.IWIN.equals(event)) {
			winner.play();
			gameover = true;
		} else {
			return;
		}
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see OpponentBattleGridListener#opponentBattleGridUpdate(java.lang.String, java.lang.Object)
	 */
	public void opponentBattleGridUpdate(String event, Object arg) {
		/*inform audio*/
		if(OpponentBattleGrid.SHIP_FIRE_HIT.equals(event)) {
			shipfire.play();
		} else if(OpponentBattleGrid.SUB_FIRE_HIT.equals(event)) {
			subfire.play();
		} else if(OpponentBattleGrid.SHIP_FIRE_MISS.equals(event)) {
			miss.play();
		} else if(OpponentBattleGrid.SHIP_FIRE_SUNK.equals(event)) {
			sunk.play();
		} else {
			return;
		}
		repaint();
	}
	
	/**
	 * listen to the mouse event
	 * click: shipfire, double click: subfire
	 * @author Bowen Yan
	 *
	 */
	private class FireMouseListener implements MouseListener, MouseMotionListener {
		private int width = GRID_BOX_WIDTH * battleGrid.getColumn();
		
		public void mouseClicked(MouseEvent e) {
			if(enable && !gameover) {
				int mx = e.getX();
				int my = e.getY();
				int clickCount = e.getClickCount();
				//if click a grid and can fire the position
				if(clickCount == 1 && fireOpponentController.canShipFire()) {
					shipFire(mx, my);
				} else if(clickCount == 2 && fireOpponentController.canSubFire()) {  //if double click and can fire the position
					subFire(mx, my);
				}
			}
		}
		
		/**
		 * shipfire the appointed position (grid)
		 * @param mx
		 * @param my
		 */
		private void shipFire(int mx, int my) {
			//estimate the position where the mouse clicked
			if(mx < width + SEPARATOR_WIDTH) return;
			//convert coordinate
			int x = (mx - width - SEPARATOR_WIDTH) / GRID_BOX_WIDTH;
			int y = my / GRID_BOX_WIDTH;
			fireOpponentController.shipFire(x, y);
		}

		/**
		 * subfire the appointed position (row)
		 * @param mx
		 * @param my
		 */
		private void subFire(int mx, int my) {
			//estimate the coordinate where the mouse clicked
			//click the submarines on my battlefield to fire a torpedo
			if(mx > width) return;
			int x = mx / GRID_BOX_WIDTH;
			int y = my / GRID_BOX_WIDTH;
			//get the coordinate of submarine
			//inform the coordinate of row
			IShip ship = fireOpponentController.getMyShip(x, y);
			if(ship != null && !ship.isSunk()
			&& "SUBMARINES".equals(ship.getType()) ) {
				fireOpponentController.subFire(y);
			}
		}
		
		//need not listen to these events
		public void mouseMoved(MouseEvent e)	{ /* do nothing */ }
		public void mouseEntered(MouseEvent e)	{ /* do nothing */ }
		public void mouseExited(MouseEvent e)	{ /* do nothing */ }
		public void mousePressed(MouseEvent e)	{ /* do nothing */ }
		public void mouseReleased(MouseEvent e)	{ /* do nothing */ }
		public void mouseDragged(MouseEvent e)	{ /* do nothing */ }
	}

}
