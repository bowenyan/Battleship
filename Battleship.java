/**
 * This is the main entrance to the game Battleships
 * Instantiate models, controllers, views, server, client, parser and communication objects
 * @author Bowen Yan
 * @version May, 2008
 *
 */
public class Battleship {
	public static void main(String[] args) {

		/*create models*/
		BattleGrid battleGrid = new BattleGrid(10, 10);
		OpponentBattleGrid opponentBattleGrid = new OpponentBattleGrid(10, 10);
		Console console = new Console();
		
		/*create controllers*/
		/*pass corresponding models as parameters to controllers*/
		BuildBattleFieldController bbfController = new BuildBattleFieldController(battleGrid);
		FireMeController fmController = new FireMeController(battleGrid);
		FireOpponentController foController = new FireOpponentController(opponentBattleGrid, battleGrid);
		ConsoleController cController = new ConsoleController(console);
		
		/*create views*/
		/*pass corresponding controllers as parameters to views*/
		//build initial view
		BuildBattleFieldView bbfView = new BuildBattleFieldView(bbfController, battleGrid);
		//build battle view
		BattleFieldView bfView = new BattleFieldView(foController, battleGrid, opponentBattleGrid);		
		
		/*create console view and close listener*/
		WindowCloseHandler wcHandler = new WindowCloseHandler();  //close windows listener
		/*pass corresponding parameters to console view*/
		ConsoleView vm = new ConsoleView(bbfView, bfView, cController, console);
		vm.addCloseListener(wcHandler);    //WindowCloseHander listens the event of closing windows
		vm.openFrame();   //open windows
		vm.changeToBuildView();  //switch to BuildBattleFieldView
		
		/*create and initialize parser*/
		Parser parser = new Parser();
		/*add controllers*/
		parser.addControllers(cController);
		parser.addControllers(bbfController);
		parser.addControllers(fmController);
		parser.addControllers(foController);
		
		ClientSignaler signaler = null;
		
		if(args.length == 2) {
			if("-host".equals(args[0]) && Util.isInteger(args[1])) {
				int port = Integer.valueOf(args[1]);
				/*create server*/
				Server server = new Server(port, parser);
				/*create default client for server*/
				Client client = new Client("127.0.0.1", port, parser);
				signaler = new ClientSignaler(client, parser);
				wcHandler.addServer(server);  //monitor a new client
				wcHandler.addClient(client);  //monitor a new client
			} else {
				Util.log("startup params error...");
				System.exit(0);
			}
		} else if(args.length == 3) {
			if("-join".equals(args[0]) && Util.isIP(args[1]) && Util.isInteger(args[2])) {
				/*create client*/
				Client client = new Client(args[1], Integer.valueOf(args[2]), parser);
				signaler = new ClientSignaler(client, parser);
				wcHandler.addClient(client);  //monitor a new client 
			} else {
				Util.log("startup params error...");
				System.exit(0);
			}
		} else {
			Util.log("startup params error...");
			System.exit(0);
		}
		
		/*add client signaler to all of models*/
		console.addListener(signaler);
		battleGrid.addListener(signaler);
		opponentBattleGrid.addListener(signaler);
	}

}
