/**
 * This class is used to execute and handle with the commands
 * @author Bowen Yan
 * @version May, 2008
 */

import java.util.Map;

public class Command {
	/**
	 * execute commands
	 * and invoke corresponding controllers to modify the models
	 * @param id
	 * @param msg
	 * @param controllers
	 */
	public void execute(String id, String msg, Map<Class<?>, Object> controllers) {
		if(id.equals("END")) {
			Object controller = controllers.get(ConsoleController.class);
			if(controller != null) {
				ConsoleController cController = (ConsoleController)controller;
				cController.receiveMsg("Server finish setup, End...");
				cController.serverEnd();
			}
		} else if(id.equals("FIRSTGO")) {
			if("ME".equals(msg.trim())) {
				Object controller = controllers.get(ConsoleController.class);
				if(controller != null) {
					ConsoleController cController = (ConsoleController)controller;
					cController.receiveMsg("Your are the first player, you can fire opponent first, now set your ship first and click \"READY\"...");
					cController.setMyTurn(true);
				}
			} else if("YOU".equals(msg.trim())) {
				Object controller = controllers.get(ConsoleController.class);
				if(controller != null) {
					ConsoleController cController = (ConsoleController)controller;
					cController.receiveMsg("Your are the second player, opponent can fire you first now set your ship first and click \"READY\"...");
					cController.setMyTurn(false);
				}
			}
		} else if(id.equals("HIT")) {
			Object controller = controllers.get(FireOpponentController.class);
			if(controller != null) {
				FireOpponentController foController = (FireOpponentController)controller;
				foController.hit();
			}
			controller = controllers.get(ConsoleController.class);
			if(controller != null) {
				ConsoleController cController = (ConsoleController)controller;
				cController.receiveMsg("Your fire hit a " + msg + "...");
			}
		} else if(id.equals("ILOSE")) {
			Object controller = controllers.get(FireMeController.class);
			if(controller != null) {
				FireMeController feController = (FireMeController)controller;
				feController.win();
			}
			
			controller = controllers.get(ConsoleController.class);
			if(controller != null) {
				ConsoleController cController = (ConsoleController)controller;
				cController.receiveMsg("Congratulation, you perish all the opponent ships...");
			}
		} else if(id.equals("MISS")) {
			Object controller = controllers.get(FireOpponentController.class);
			if(controller != null) {
				FireOpponentController foController = (FireOpponentController)controller;
				foController.miss();
			}
			controller = controllers.get(ConsoleController.class);
			if(controller != null) {
				ConsoleController cController = (ConsoleController)controller;
				cController.receiveMsg("Your fire miss...");
			}
		} else if(id.equals("READY")) {
			Object controller = controllers.get(ConsoleController.class);
			if(controller != null) {
				ConsoleController cController = (ConsoleController)controller;
				cController.opponentReady();
				cController.receiveMsg("Opponent is on ready...");
			}
		} else if(id.equals("SHIPFIRE")) {
			Object controller = controllers.get(FireMeController.class);
			String[] pos = msg.trim().split(" ");
			if(controller != null) {
				FireMeController fmController = (FireMeController)controller;
				int x = Integer.valueOf(pos[0]);
				int y = Integer.valueOf(pos[1]);
				fmController.shipFire(x, y);
			}
			controller = controllers.get(ConsoleController.class);
			if(controller != null) {
				ConsoleController cController = (ConsoleController)controller;
				cController.receiveMsg("Opponent use ship fire at your coordinate (" + pos[0] + "," + pos[1] + ")...");
				cController.setMyTurn(true);
			}
		} else if(id.equals("START")) {
			Object controller = controllers.get(ConsoleController.class);
			if(controller != null) {
				ConsoleController cController = (ConsoleController)controller;
				cController.finishConnect();
				cController.receiveMsg("Your computer has connected the server, wait for the support...");
			}
		} else if(id.equals("SUBFIRE")) {
			Object controller = controllers.get(FireMeController.class);
			String pos = msg.trim();
			if(controller != null) {
				FireMeController fmController = (FireMeController)controller;
				int y = Integer.valueOf(pos);
				fmController.subFire(y);
			}
			controller = controllers.get(ConsoleController.class);
			if(controller != null) {
				ConsoleController cController = (ConsoleController)controller;
				cController.receiveMsg("Opponent use sub fire at your coordinate (" + pos + ")...");
				cController.setMyTurn(true);
			}
		} else if(id.equals("SUNK")) {
			Object controller = controllers.get(FireOpponentController.class);
			if(controller != null) {
				FireOpponentController foController = (FireOpponentController)controller;
				foController.sunk(msg.trim());
			}
			controller = controllers.get(ConsoleController.class);
			if(controller != null) {
				ConsoleController cController = (ConsoleController)controller;
				cController.receiveMsg("Your fire sunk a " + msg.trim() + "...");
			}
		} else if(id.equals("SUPPORTS")) {
			Object controller = controllers.get(ConsoleController.class);
			if(controller != null) {
				ConsoleController cController = (ConsoleController)controller;
				String[] msgs = msg.split("#");
				String last = "			SUPPORTS\n";
				for(int i = 0; i < msgs.length; i++) {
					last += msgs[i] + "\n";
				}
				last += "------------------------------------------------------------------";
				cController.receiveMsg(last);
			}
		} else if(id.equals("TALK")) {
			Object controller = controllers.get(ConsoleController.class);
			if(controller != null) {
				ConsoleController cController = (ConsoleController)controller;
				cController.receiveMsg("Opponent say:" + msg);
			}
		} else if(id.equals("TURNTOFIRE")) {
			Object controller = controllers.get(ConsoleController.class);
			if(controller != null) {
				ConsoleController cController = (ConsoleController)controller;
				cController.setMyTurn(Boolean.valueOf(msg.trim()));
			}
		}
	}
	
	/**
	 * obtain the description of commands
	 * send normative commands to server
	 * @param id
	 * @param args
	 * @return
	 */
	public String synthesis(String id, Object ... args) {
		if(id.equals("END")) {
			return "END\n";
		} else if(id.equals("FIRSTGO")) {
			return "FIRSTGO " + String.valueOf(args[0]) + "\n";
		} else if(id.equals("HIT")) {
			return "HIT " + (String)args[0] + "\n";
		} else if(id.equals("ILOSE")) {
			return "ILOSE\n";
		} else if(id.equals("MISS")) {
			return "MISS\n";
		} else if(id.equals("YES")) {
			return "YES\n";
		}  else if(id.equals("READY")) {
			return "READY\n";
		} else if(id.equals("SHIPFIRE")) {
			int x = (Integer)args[0];
			int y = (Integer)args[1];
			return "SHIPFIRE " + x + " " + y + "\n";
		} else if(id.equals("START")) {
			return "START\n";
		} else if(id.equals("SUBFIRE")) {
			return "SUBFIRE " + args[0] + "\n";
		} else if(id.equals("SUNK")) {
			return "SUNK " + (String)args[0] + "\n";
		} else if(id.equals("SUPPORTS")) {
			return "SUPPORTS Your computer has connected the server, please wait for the start.#" +
					"The host has already started the game. START.#" +
					"This game can support:#" +
					"1.This game would send a string describing each extension accepted by the client.#" +
					"2.The player can place images of ships on the grid horizontally or vertically by using \"DOWN\" or \"RIGHT\" key.#" +
					"3.Threaded interaction is completed. Players can chat with each other any time.#" +
					"4.This game supports a GUI, which is written by myself.#" +
					"5.This game supports sound.#" +
					"The host has finished. END.#" +
					"Please enter yes to place your ships\n";
		} else if(id.equals("TALK")) {
			if(args.length == 0 || !(args[0] instanceof String)) {
				return "TALK \n";
			} else {
				return "TALK " + (String)args[0] + "\n";
			}
		} else if(id.equals("TURNTOFIRE")) {
			return "TURNTOFIRE " + ((Boolean)args[0] ? "true" : "false") + "\n";
		} else {
			return null;
		}
	}
}
