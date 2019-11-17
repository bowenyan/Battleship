/**
 * This class is a signaler
 * Transmits the message between two players
 * It implements ConsoleActionListener, BattleGridListener and OpponentBattleGridListener interfaces
 * Listens to the changes of models
 * @author Bowen Yan
 * @version May, 2008
 *
 */



public class ClientSignaler implements ConsoleActionListener, BattleGridListener, OpponentBattleGridListener {
	private Client client;
	private Parser parser;
	
	public ClientSignaler(Client client, Parser phraser) {
		this.client = client;
		this.parser = phraser;
	}

	/*
	 * (non-Javadoc)
	 * @see BattleGridListener#battleGridUpdate(java.lang.String, java.lang.Object)
	 */
	public void battleGridUpdate(String event, Object arg) {
		//listen what is the result of opponent fire me,
		//parser the message
		//and then send the result to server.
		String msg = null;
		if(BattleGrid.FIRE_HIT.equals(event)) {
			msg = parser.synthesis("HIT", (String)arg);
		} else if(BattleGrid.FIRE_MISS.equals(event)) {
			msg = parser.synthesis("MISS");
		} else if(BattleGrid.SHIP_SUNK.equals(event)) {
			msg = parser.synthesis("SUNK", (String)arg);
		} else if(BattleGrid.ILOSE.equals(event)) {
			msg = parser.synthesis("ILOSE");
		} else {
			return;
		}
		client.sendMessage(msg);  //send messages
		parser.execute(parser.synthesis("TURNTOFIRE", true));
	}

	/*
	 * (non-Javadoc)
	 * @see OpponentBattleGridListener#opponentBattleGridUpdate(java.lang.String, java.lang.Object)
	 */
	public void opponentBattleGridUpdate(String event, Object arg) {
		//listen what method of my fire to opponent
		//parser the message
		//and send it to server
		String msg = null;
		if(OpponentBattleGrid.SHIP_FIRE.equals(event)) {
			int[] pos = (int[]) arg;
			msg = parser.synthesis("SHIPFIRE", pos[0], pos[1]);
		} else if(OpponentBattleGrid.SUB_FIRE.equals(event)) {
			msg = parser.synthesis("SUBFIRE", (Integer)arg);
		} else {
			return;
		}
		client.sendMessage(msg);  //send messages
		parser.execute(parser.synthesis("TURNTOFIRE", false));
	}

	/*
	 * (non-Javadoc)
	 * @see ConsoleActionListener#baseActionPerformed(java.lang.String, java.lang.Object)
	 */
	public void baseActionPerformed(String event, Object arg) {
		//listen the changes of console
		String msg = null;
		if(Console.I_AM_READY.equals(event)) {
			msg = parser.synthesis("READY");
		} else if(Console.YES.equals(event)) {
			msg = parser.synthesis("YES");
		} else if(Console.NO.equals(event)) {
			msg = parser.synthesis("NO");
		} else if(Console.TALK.equals(event)) {
			msg = parser.synthesis("TALK", arg);
		} else {
			return;
		}
		client.sendMessage(msg);  //send messages
	}

}
