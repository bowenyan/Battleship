/**
 * Ship images factory
 * @author Bowen Yan
 * @version May, 2008
 */

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ShipImageFactory {
	private static final ShipImageFactory factory = new ShipImageFactory();
	private Map<String, Image> hImgs = new HashMap<String, Image>();
	private Map<String, Image> vImgs = new HashMap<String, Image>();
    //other classes cannot instant class ShipImageFactory
	private ShipImageFactory() {
		//Use the getResource method to get the absolute URL which stores the images
		Class<?> resource = ShipImageFactory.class;
		try {
			hImgs.put("BATTLESHIP", ImageIO.read(resource.getResource("images/h_Battleship.gif")));
			hImgs.put("DESTROYERS", ImageIO.read(resource.getResource("images/h_Destroyers.gif")));
			hImgs.put("SUBMARINES", ImageIO.read(resource.getResource("images/h_Submarines.gif")));
			
			vImgs.put("BATTLESHIP", ImageIO.read(resource.getResource("images/v_Battleship.gif")));
			vImgs.put("DESTROYERS", ImageIO.read(resource.getResource("images/v_Destroyers.gif")));
			vImgs.put("SUBMARINES", ImageIO.read(resource.getResource("images/v_Submarines.gif")));
		} catch(IOException e) {
			Util.log(e, "read images fail...");
		}
	}

	/**
	 * return an instance
	 * @return
	 */
	public static ShipImageFactory getInstance() {
		return factory;
	}

	/**
	 * get corresponding orientation image
	 * @param ship
	 * @return
	 */
	public Image getShipImage(IShip ship) {
		String type = ship.getType();
		int ori = ship.getOrientation();
		if(IShip.HORIZONTAL == ori) {
			return hImgs.get(type);
		} else if(IShip.VERTICAL == ori) {
			return vImgs.get(type);
		}
		return null;
	}
}
