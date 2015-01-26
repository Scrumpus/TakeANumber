package digiTakeNumber;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


/**
 * The main opening class of the program.
 * This classes sole intention is to instantiate the client/server
 * @author Team Digital Take-A-Number 
 * @version 11/16/2014
 *
 */
public class Driver {

	private InstructorServer instructor;
	private Participant participant;
	
	private JFrame mainFrame;
	private JPanel welcomePanel;
	private JButton createLabButton;
	private JButton joinLabButton;
	
	
	public Driver() {
		
	}
	
	/**
	 * Starting method of the whole program. Gets init to start doing its job
	 * @param args 
	 */
	public static void main(String[] args) {
		Driver driver = new Driver();
		driver.init();
		driver.drawWelcomeScreen();
		
	}
	
	/**
	 * Initializes the program, creating a Client object and a Server object.
	 * Uses one of these to draw the initial Welcome Screen, putting the user in a position to choose. 
	 * "Create Lab" or "Join Lab"
	 */
	private void init(){
		instructor = null;
		participant = null;
		mainFrame = new JFrame("Welcome");
		mainFrame.setSize(400,200);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		return;
	}
	
	public void drawWelcomeScreen() {
		welcomePanel = new JPanel();
		joinLabButton = new JButton("Join Lab");
		createLabButton = new JButton("Create Lab");
		welcomePanel.add(joinLabButton);
		welcomePanel.add(createLabButton);
		mainFrame.add(welcomePanel);
		
		joinLabButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				participant = new Participant();
				participant.setFrame();
				mainFrame.dispose();
				try {
					participant.connectToServer();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				
			}
		}
		);
		
		createLabButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instructor = new InstructorServer();
				mainFrame.dispose();
				instructor.setFrame();
				try {
					instructor.listen();
				} catch (IOException e1) {
					e1.printStackTrace();
				}				
			}
		}
		);
		
	}

}
