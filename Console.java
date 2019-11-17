/**
 * This class represents console model
 * It Stores information of console
 * @author Bowen Yan
 * @version May,2008
 */
public class Console {
	/*initialize constant*/
	public static final String FINISH_CONNECT = "finishconnect";
	public static final String YES = "yes";
	public static final String NO = "no";
	public static final String SERVER_END = "serverend";
	public static final String IS_MY_TURN = "ismyturn";
	public static final String I_AM_READY = "iamready";
	public static final String OPPONENT_READY = "opponentready";
	public static final String RECEIVE_MESSAGE = "receviemessage";
	public static final String CLEAR_MESSAGE = "clearmessage";
	public static final String TALK = "talk";
	public static final String CLEAR_TALK = "cleartalk";
	public static final String TURN_TO_FIRE = "turntofire";
	
	private String msg = "";  //receive messages
	private String talk = "";  //send my talk messages
	private boolean isFinishConnect;
	private boolean isOpponentReady;
	private boolean isServerEnd;
	private boolean isMyTurn;
	private boolean isYes;
	private boolean isNo;
	private boolean isReady;
	private ConsoleActionSupport listeners = new ConsoleActionSupport();
	
	/**
	 * set reveived messages
	 * @param msg
	 */
	public void setMessage(String msg) {
		this.msg = msg;
		fireListener(RECEIVE_MESSAGE);
	}

	/**
	 * set my talk messages
	 * @param talk
	 */
	public void setTalk(String talk) {
		this.talk = talk;
		fireListener(TALK, this.talk);
	}

	/**
	 * clear received messages
	 */
	public void clearMessage() {
		msg = "";
		fireListener(CLEAR_MESSAGE);
	}

	/**
	 * clear my talk messages
	 */
	public void clearTalk() {
		talk = "";
		fireListener(CLEAR_TALK);
	}
	
	/**
	 * get received messages
	 * @return
	 */
	public String getMessage() {
		return msg;
	}
	
	/**
	 * get my talk messages
	 * @return
	 */
	public String getTalk() {
		return talk;
	}
	
	/**
	 * estimate whether the server assignment is finished
	 * @return
	 */
	public boolean isServerEnd() {
		return isServerEnd;
	}

	/**
	 * set the status of the server assignment
	 * @param end
	 */
	public void setServerEnd(boolean end) {
		this.isServerEnd = end;
		if(end) fireListener(SERVER_END);//if true, the server assignment has finished
	}

	/**
	 * estimate whether the player is ready or not
	 * @return
	 */
	public boolean isIamReady() {
		return isReady;
	}

	/**
	 * set the status of IamReady
	 * @param ready
	 */
	public void setIamReady(boolean ready) {
		this.isReady = ready;
		if(ready) fireListener(I_AM_READY);
	}

	public boolean isNo() {
		return isNo;
	}

	/**
	 * estimate whether the player accept all of extra features or not
	 * @return
	 */
	public boolean isYes() {
		return isYes;
	}

	public void setNo(boolean no) {
		this.isNo = no;
		if(no) fireListener(NO);
	}

	/**
	 * set the status of yes
	 * @param yes
	 */
	public void setYes(boolean yes) {
		this.isYes = yes;
		if(yes) fireListener(YES);
	}

	/**
	 * estimate whether the connection between client and server has finished or not
	 * @return
	 */
	public boolean isFinishConnect() {
		return isFinishConnect;
	}

	/**
	 * set the status of connection
	 * @param finish
	 */
	public void setFinishConnect(boolean finish) {
		this.isFinishConnect = finish;
		if(finish) fireListener(FINISH_CONNECT);
	}
	
	/**
	 * estimate whether the opponent is ready or not
	 * @return
	 */
	public boolean isOpponentReady() {
		return isOpponentReady;
	}

	/**
	 * set the status of OpponentReady
	 * @param ready
	 */
	public void setOpponentReady(boolean ready) {
		this.isOpponentReady = ready;
		if(ready) fireListener(OPPONENT_READY);
	}
	
	/**
	 * estimate whether it is my turn to fire or not
	 * @return
	 */
	public boolean isMyTurn() {
		return isMyTurn;
	}

	/**
	 * set the status of fire
	 * @param isMyTurn
	 */
	public void setIsMyTurn(boolean isMyTurn) {
		this.isMyTurn = isMyTurn;
		fireListener(TURN_TO_FIRE, isMyTurn);
	}

	/**
	 * add the listener
	 * @param l
	 */
	public void addListener(ConsoleActionListener l) {
		listeners.addListener(l);
	}

	/**
	 * remove the listener
	 * @param l
	 */
	public void removeListener(ConsoleActionListener l) {
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
