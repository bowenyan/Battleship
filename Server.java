/**
 * This class represents a server communication object
 * It inherits thread, and assorts with connection and transmission data between two players
 * @author Bowen Yan
 * @version May, 2008
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class Server extends Thread {
	private int port;   //the port of server
	private boolean death;   //estimate the status of thread
	private ServerSocket server;
	private Parser phraser;  //parser the protocol
	private List<Service> services = new Vector<Service>(2);  //open two spaces
	private int yes;

	public Server(int port, Parser phraser) {
		this.port = port;
		this.phraser = phraser;
		if(!initialize()) {
			close();
			return;
		}
		this.start();   //start a thread
	}
	
	/**
	 * initialize operation (socket connection etc.)
	 * @return
	 */
	private boolean initialize() {
		try {
			server = new ServerSocket(port); //open a socket for server
		} catch (IOException e) {
			Util.log("Cannot create server...");
			return false;
		}
		Util.log("Server start finished...");
		Util.log("Waiting client...");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			while (!death) {  //if the thread exists, poll waiting data
				Socket client = server.accept(); //accept a socket from client
				if(services.size() == 2) {  //estimate whether the number of players are more than 2 or not
					Util.log("Cannot accept any more player, max num is 2...");
				} else {
					services.add(new Service(this, client));
					Util.log("There are " + services.size() + " players connect...");
					if(services.size() == 2) {
						/*broadcast*/
						broadcast(phraser.synthesis("START"));
						broadcast(phraser.synthesis("SUPPORTS"));
					}
				}
			}
		} catch (IOException e) {
			Util.log("Cannot provide service...");
		} finally {
			close();
		}
	}
	
	/**
	 * broadcast to every player
	 * @param msg
	 */
	private void broadcast(String msg) {
		for(int i = 0; i < services.size(); i++) {
			services.get(i).sendMessage(msg);
		}
	}
	
	/**
	 * handle with messages from players
	 * @param service
	 * @param msg
	 */
	void playerMsgProcess(Service service, String msg) {
		String tmp = msg.trim().toUpperCase();
		if("YES".equals(tmp)) {  //handle with Yes message
			processYES(service);
		} else {                 //handle with other messages
			processSimple(service, msg);
		}
	}
	
	/**
	 * handle with Yes message
	 * each player confirm yes
	 * and then send End message and Firstgo message to players
	 * @param service
	 */
	private void processYES(Service service) {
		yes++;
		if(yes == 2) {  //two players
			/*broadcast*/
			broadcast(phraser.synthesis("END"));
			/*random*/
			Random random = new Random();
			int firstgo = random.nextInt(2);
			//send messages to players
			services.get(firstgo).sendMessage(phraser.synthesis("FIRSTGO", "ME"));
			services.get(1 - firstgo).sendMessage(phraser.synthesis("FIRSTGO", "YOU"));
		}
	}
	
	/**
	 * handle with normal messages
	 * @param service
	 * @param msg
	 */
	private void processSimple(Service service, String msg) {
		Service target = null;
		for(int i = 0; i < services.size(); i++) {
			//send messages to the other one
			if(services.get(i) != service) {
				target = services.get(i);
				break;
			}
		}
		if(target != null) {
			//here have to add '\n' because when the server 
			//readline one client's message, it will delete 
			//the last '\n', we must make sure server trainsmit
			//message do not lost its last '\n'
			target.sendMessage(msg + "\n"); 
		} else {
			Util.log("cannot find the target player service...");
		}
	}
	
	/**
	 * close server
	 */
	public void close() {
		try {
			for(int i = 0; i < services.size(); i++) {
				if(services.get(i) != null) {
					//inform client to close
					services.get(i).sendMessage("CLOSE");
				}
			}
			//close server
			if(server != null) server.close();
			death = true;
		} catch (IOException e) {
			Util.log(e, "Closing server socket fail...");
		}
	}
	
	/**
	 * remove service
	 * @param service
	 */
	void clearService(Service service) {
		services.remove(service);
	}
	
	public static void main(String[] arg) {
		new Server(8888, new Parser());
	}
}

/**
 * service thread
 * @author Bowen Yan
 *
 */
class Service extends Thread {
	private boolean death;
	private BufferedReader in;
	private PrintWriter out;
	private Socket client;
	private Server server;

	public Service(Server server, Socket client) {
		this.server = server;
		this.client = client;
		if(!initialize()) {
			close();
			return;
		}
		this.start();   //start a thread
	}
	
	private boolean initialize() {
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
		} catch (IOException e) {
			Util.log(e, "initialize client service fail in server...");
			return false;
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {		
		try {
			String msg = null;
			while (!death) {    //if the thread exists, poll waiting data
				msg = in.readLine();  //read the line of data
				receiveMessage(msg);  //handle with the data
			}
		} catch (IOException e) {
			Util.log(e, "Read client data fail...");
		} finally {
			shutdown();
		}
	}
	
	/**
	 * sends the message to players
	 * @param msg
	 */
	void sendMessage(String msg) {
		if(msg == null) {
			Util.log("cannot send a message which is null");
			return;
		}
		out.print(msg);   //print the data
		out.flush();
	}
	
	/**
	 * handle with messages from players
	 * @param msg
	 */
	private void receiveMessage(String msg) {
		if(msg == null) return;
		//if receive a close command, close the service
		if("CLOSE".equals(msg)) close();
		//send messages
		else server.playerMsgProcess(this, msg);
	}
	
	/**
	 * close service thread
	 */
	private void shutdown() {
		try {
			if(out != null) out.close();
			if(in != null) in.close();
			if(client != null) client.close();
		} catch (IOException e) {
			Util.log(e, "Closing client socket fail in server...");
		}
	}
	
	/**
	 * inform server to remove the record of the service thread
	 */
	void close() {
		death = true;
		server.clearService(this);
	}
}
