/**
 * Listen the event of closing windows
 * @author Bowen Yan
 * @version May, 2008
 */

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class WindowCloseHandler extends WindowAdapter {
	private Server server;
	private Client client;	

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent e) {
		try {		
			if(client != null) client.close();
			if(server != null) server.close();
		} catch(Exception ex){
			Util.log("Shutdown the client error...");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
	 */
    public void windowClosed(WindowEvent e) {
		System.exit(0);
	}
	
    /**
     * add a server
     * @param server
     */
	public void addServer(Server server) {
		this.server = server;
	}
	
	/**
	 * add a client
	 * @param client
	 */
	public void addClient(Client client) {
		this.client = client;
	}

}
