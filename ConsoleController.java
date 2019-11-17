/**
 * The class is a controller,
 * which is used to modify console model 
 * @author Bowen Yan
 * @version May, 2008
 *
 */
public class ConsoleController {
	private Console console;
	
	/**
	 * Constructor for objects of class ConsoleController
	 * @param console
	 */
	public ConsoleController(Console console) {
		this.console = console;
	}
	
	/**
	 * set the status of connection
	 */
	public void finishConnect() {
		console.setFinishConnect(true);
	}
	
	/**
	 * set the status of the server assignment
	 */
	public void serverEnd() {
		console.setServerEnd(true);
	}
	
	/**
	 * set the status of fire
	 * @param turn
	 */
	public void setMyTurn(boolean turn) {
		console.setIsMyTurn(turn);
	}
	
	public void no() {
		console.setNo(true);
	}

	/**
	 * set the status of IamReady
	 */
	public void iamReady() {
		console.setIamReady(true);
	}
	
	/**
	 * set the status of OpponentReady
	 */
	public void opponentReady() {
		console.setOpponentReady(true);
	}

	/**
	 * set reveived messages
	 * @param msg
	 */
	public void receiveMsg(String msg) {
		console.setMessage(msg);
	}

	/**
	 * set my talk messages
	 * @param talk
	 */
	public void talk(String talk) {
		console.setTalk(talk);
	}

	/**
	 * set the status of yes
	 */
	public void yes() {
		console.setYes(true);
	}

}
