/**
 * The whole view for players, which contains battlefields, chat text area and buttons
 * It implements ConsoleActionListener interface 
 * @author Bowen Yan
 * @version May, 2008
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class ConsoleView implements ConsoleActionListener {
	private JFrame frame;
	private JTextField talkTF;
	private JTextArea msgTA;
	private JButton readyBtn;
	private JButton yesBtn;
	private BuildBattleFieldView bbfView;
	private BattleFieldView bfView;
	private ConsoleController cController;
	private Console console;
	
	public ConsoleView(BuildBattleFieldView bbfView, BattleFieldView bfView, ConsoleController cController, Console console) {
		this.frame = new JFrame();
		frame.setTitle("BattleShip V1.0");
		this.bbfView = bbfView;
		this.bfView = bfView;
		this.cController = cController;
		this.console = console;
		/*register view as a listener on console model*/
		this.console.addListener(this);
		initialize();
	}
	
	private void initialize() {		
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setBounds(100, 100, 613, 586);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*initialize a panel*/
		final JPanel tipsPanel = new JPanel();
		tipsPanel.setBackground(new Color(135, 206, 235));
		tipsPanel.setLayout(null);
		tipsPanel.setPreferredSize(new Dimension(613, 30));
		frame.getContentPane().add(tipsPanel, BorderLayout.NORTH);

		/*draw note labels*/
		//my battlefield
		final JLabel hitLab = new JLabel();
		hitLab.setForeground(Color.ORANGE);
		hitLab.setFont(new Font("", Font.BOLD, 16));
		hitLab.setBounds(40, 5, 54, 18);
		hitLab.setText("HIT");
		tipsPanel.add(hitLab);

		final JLabel sunkLab = new JLabel();
		sunkLab.setFont(new Font("", Font.BOLD, 16));
		sunkLab.setForeground(Color.RED);
		sunkLab.setText("SUNK");
		sunkLab.setBounds(100, 5, 54, 18);
		tipsPanel.add(sunkLab);

		final JLabel missLab = new JLabel();
		missLab.setFont(new Font("", Font.BOLD, 16));
		missLab.setForeground(Color.WHITE);
		missLab.setText("MISS");
		missLab.setBounds(185, 5, 54, 18);
		tipsPanel.add(missLab);

		//opponent battlefield
		final JLabel oppHitLab = new JLabel();
		oppHitLab.setForeground(Color.ORANGE);
		oppHitLab.setFont(new Font("", Font.BOLD, 16));
		oppHitLab.setText("HIT");
		oppHitLab.setBounds(420, 5, 54, 18);
		tipsPanel.add(oppHitLab);

		final JLabel oppMissLab = new JLabel();
		oppMissLab.setFont(new Font("", Font.BOLD, 16));
		oppMissLab.setForeground(Color.WHITE);
		oppMissLab.setText("MISS");
		oppMissLab.setBounds(485, 5, 54, 18);
		tipsPanel.add(oppMissLab);
		
		JPanel consolePanel = new JPanel();
		consolePanel.setLayout(null);
		consolePanel.setPreferredSize(new Dimension(610, 230));
		frame.getContentPane().add(consolePanel, BorderLayout.SOUTH);
		

		/*add scroll bar*/
		final JScrollPane msgTASP = new JScrollPane();
		msgTASP.setBounds(12, 30, 585, 117);
		consolePanel.add(msgTASP);
		
		/*draw message view*/
		msgTA = new JTextArea();
		msgTA.setEditable(false);
		msgTA.setFocusable(false);
		msgTA.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		msgTASP.setViewportView(msgTA);

		/*draw talk view*/
		talkTF = new JTextField();
		talkTF.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
		talkTF.setBounds(53, 155, 544, 25);
		talkTF.setEnabled(false);
		talkTF.setFocusable(false);
		//listen to a keyborad event
		//use Enter to send messages
		talkTF.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) { //listen to the keypress event
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					//console controller modify the console
					cController.talk(talkTF.getText().trim());
					//clear the text area
					talkTF.setText("");
				}
			}
			//need not listen to these events
			public void keyReleased(KeyEvent e) {
			}
			public void keyTyped(KeyEvent e) {
			}
		});
		consolePanel.add(talkTF);

		/*draw buttons*/
		readyBtn = new JButton();
		readyBtn.setBounds(90, 186, 74, 30);
		readyBtn.setText("Ready");
		readyBtn.setEnabled(false);
		readyBtn.setFocusable(false);
		//listen to an event
		readyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//the player is ready
				cController.iamReady();
			}
		});
		consolePanel.add(readyBtn);

		yesBtn = new JButton();
		yesBtn.setBounds(10, 186, 74, 30);
		yesBtn.setText("Yes");
		yesBtn.setEnabled(false);
		yesBtn.setFocusable(false);
		//listen to an event
		yesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//press the yes buttol
				cController.yes();
			}
		});
		consolePanel.add(yesBtn);

		/*draw a talk label*/
		final JLabel talkLab = new JLabel();
		talkLab.setFont(new Font("", Font.BOLD, 14));
		talkLab.setText("Talk");
		talkLab.setBounds(15, 155, 32, 22);
		consolePanel.add(talkLab);

		/*draw a message label*/
		final JLabel msgLab = new JLabel();
		msgLab.setFont(new Font("", Font.BOLD, 14));
		msgLab.setText("Message");
		msgLab.setBounds(15, 10, 66, 18);
		consolePanel.add(msgLab);
	}
	
	/**
	 * close frame
	 */
	public void closeFrame() {
		frame.dispose();
	}
	
	/**
	 * open frame
	 */
	public void openFrame() {
		frame.setVisible(true);
	}
	
	/**
	 * display the initial view 
	 */
	public void changeToBuildView() {
		frame.getContentPane().remove(bfView);
		frame.getContentPane().add(bbfView, BorderLayout.CENTER);
		frame.validate();
	}
	
	/**
	 * display the battlefield view
	 */
	public void changeToFireView() {
		frame.getContentPane().remove(bbfView);
		frame.getContentPane().add(bfView, BorderLayout.CENTER);
		frame.validate();
	}
	
	/**
	 * handle with the scroll of message
	 * @param msg
	 */
	private void appendMsg(String msg) {
		msgTA.append(msg + "\n");
		int end = msgTA.getText().length() - 1;
		msgTA.select(end, end);
	}
	
	/**
	 * WindowCloseHander listens the event of closing windows
	 * @param listener
	 */
	public void addCloseListener(WindowListener listener) {
		this.frame.addWindowListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see ConsoleActionListener#baseActionPerformed(java.lang.String, java.lang.Object)
	 */
	public void baseActionPerformed(String event, Object arg) {
		if(Console.FINISH_CONNECT.equals(event)) {
			yesBtn.setEnabled(true);
		} else if(Console.YES.equals(event)) {
			yesBtn.setEnabled(false);
		} else if(Console.SERVER_END.equals(event)) {
			readyBtn.setEnabled(true);
			bbfView.setEnable(true);//allow user to set the ships position
		} else if(Console.OPPONENT_READY.equals(event)) {
			if(console.isIamReady()) {
				readyInit();
			}
		} else if(Console.I_AM_READY.equals(event)) {
			readyBtn.setEnabled(false);
			if(console.isOpponentReady()) {
				readyInit();
			}
		} else if(Console.RECEIVE_MESSAGE.equals(event)) {
			appendMsg(console.getMessage());
		} else if(Console.TURN_TO_FIRE.equals(event)) {
				bfView.setEnable((Boolean)arg);
		}
	}
	
	/**
	 * Initialize the view for battle
	 */
	private void readyInit() {
		changeToFireView();
		talkTF.setEnabled(true);
		talkTF.setFocusable(true);
		if(console.isMyTurn()) appendMsg("It is your turn to fire now...");
		else appendMsg("Waiting opponent fire...");
	}
}
