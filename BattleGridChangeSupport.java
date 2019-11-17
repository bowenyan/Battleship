/**
 *Manage listeners
 *Broadcast events to listeners
 *@author Bowen Yan
 *@version May, 2008 
 */
import java.util.List;
import java.util.Vector;

public class BattleGridChangeSupport {
	/*the list of listeners*/
	private List<BattleGridListener> listeners = new Vector<BattleGridListener>();
	
	/**
	 * add a listener
	 * @param listener
	 */
	public void addListener(BattleGridListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * remove a listener
	 * @param listener
	 */
	public void removeListener(BattleGridListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * remove all the listeners
	 */
	public void removeAllListeners() {
		listeners.clear();
	}
	
	/**
	 * broadcast all of events to listeners
	 * @param event
	 * @param arg
	 */
	public void fireUpdate(String event, Object arg) {
		for(int i = 0; i < listeners.size(); i++) {
			listeners.get(i).battleGridUpdate(event, arg);
		}
	}

}
