/**
 * Parse commands
 * @author Bowen Yan
 * @version May, 2008
 */

import java.util.HashMap;
import java.util.Map;

public class Parser {
	private Map<Class<?>, Object> controllers = new HashMap<Class<?>, Object>();	
	private Command command = new Command();
	
	/**
	 * add controllers
	 * @param controller
	 */
	public void addControllers(Object controller) {
		if(controller == null) return;
		else this.controllers.put(controller.getClass(), controller);
	}
	
	/**
	 * parse commands
	 * @param msg
	 */
	public void execute(String msg) {
		if(msg == null) Util.log("Grammar error, parser is required...");
		msg = Util.trimLeft(msg);  //delete the hide chars in the left of string
		int pos = msg.indexOf(' ');  //find the space in string in order to divide commands
		if(pos == -1) {   //if not find a space, the string does not have parameters
			String id = msg;
			command.execute(id, null, controllers);  //execute the command and trigger corresponding controllers
		} else {
			String id = msg.substring(0, pos);  //if find a space, separate the command and parameters
			String params = (pos < msg.length() - 1) ? msg.substring(pos, msg.length()) : null;
			command.execute(id, params, controllers);  //execute the command and trigger corresponding controllers
		}
	}
	
	/**
	 * obtain the required form of commands
	 * @param id
	 * @param args
	 * @return
	 */
	public String synthesis(String id, Object ... args) {
		return command.synthesis(id, args);
	}

}
