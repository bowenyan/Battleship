/**
 * This class represents a client communication object
 * It inherits thread, and maintains the socket between client and server
 * @author Bowen Yan
 * @version May, 2008
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {
	private String ip;  // the IP address of server
	private int port;   //the port of server
	private boolean death;  //estimate the status of thread
	private Socket server;  
	private BufferedReader in;  //read buffered data
	private PrintWriter out;   //output data
	private Parser phraser;   //parser the protocol
	
	public Client(String ip, int port, Parser phraser) {
		this.ip = ip;
		this.port = port;
		this.phraser = phraser;
		if(!initialize()) {
			//close();
			shutdown();
			return;
		}
		this.start();  //start a thread
	}
	
	/**
	 * initialize operation (socket connection etc.)
	 * @return
	 */
	private boolean initialize() {
		try {
			server = new Socket(ip, port);			
			in = new BufferedReader(new InputStreamReader(server.getInputStream()));  
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(server.getOutputStream())), true);
		} catch (IOException e) {
			Util.log("Cannot connect with server " + ip + ":" + port);
			return false;
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		String msg = null;
		try {
			while (!death) {  //if the thread exists, poll waiting data
				msg = in.readLine();  //read a line of data
				receiveMessage(msg);  //handle with the data
			}
		} catch (IOException e) {
			Util.log(e, "Read server data fail...");
		} finally {
			shutdown();
		}
	}
	
	/**
	 * client sends the message to server
	 * @param msg
	 */
	public void sendMessage(String msg) {
		if(msg == null) {
			Util.log("Cannot send any message which is null...");
			return;
		}
		out.print(msg); //print the data
		out.flush();
	}
	
	/**
	 * handle with received message
	 * @param msg
	 */
	private void receiveMessage(String msg) {
		if(msg == null) return;
		//if receive a close command, close the client
		if("CLOSE".equals(msg)) shutdown();
		//else invoke parser to parse the message
		else phraser.execute(msg);
	}
	
	/**
	 * close the connection
	 */
	private void shutdown() {
		try {
			Util.log("Close the client socket connect...");
			death = true;
			if(out != null) out.close();
			if(in != null) in.close();
			if(server != null) server.close();
		} catch (IOException e) {
			Util.log("Cannot close socket...");
		}
	}
	
	/**
	 * the client send a close message to the server
	 */
	public void close() {
		sendMessage("CLOSE");
	}
	
	public static void main(String[] args) {
		Client c = new Client("137.222.231.119", 8888, new Parser());
		c.sendMessage("READY\n");
	}
}
