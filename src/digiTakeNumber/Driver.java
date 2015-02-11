package digiTakeNumber;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


/**
 * The main opening class of the program.
 * creates the welcome screen and creates a participant
 * or instructor
 * 
 * @author Team Digital Take-A-Number 
 * @version 11/16/2014
 *
 */
public class Driver {

	private InstructorServer instructor;
	private Participant participant;
	
	private JFrame welcomeFrame;
	private JPanel welcomePanel;
	private JButton createLabButton;
	private JButton joinLabButton;
	private JLabel waiting;
	
	
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
	}
	
	/*
	 * Draw the welcome screen when the program is executed
	 */
	public void drawWelcomeScreen() {
		welcomeFrame = new JFrame("Welcome");
		welcomeFrame.setSize(400,200);
		welcomeFrame.setVisible(true);
		welcomeFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		welcomePanel = new JPanel();
		joinLabButton = new JButton("Join Lab");
		createLabButton = new JButton("Create Lab");
		waiting = new JLabel("Trying to connect to lab...");
		waiting.setVisible(false);
		welcomePanel.add(joinLabButton);
		welcomePanel.add(createLabButton);
		welcomePanel.add(waiting);
		welcomeFrame.add(welcomePanel);
		
		/*
		 * When lab is joined, create new participant
		 * and attempt to connect to server
		 */
		joinLabButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					participant = new Participant();
					//createLabButton.setEnabled(false);
					//joinLabButton.setEnabled(false);;
					//waiting.setVisible(true);
					participant.connectToServer();
					
				} catch (IOException ioe) {
					ioe.printStackTrace();
					participant.removeUI();
					//waiting.setVisible(false);
					//createLabButton.setEnabled(true);
					//joinLabButton.setEnabled(true);
					return;
				}
				welcomeFrame.dispose();
				participant.setUI();
			}
		}
		);
		
		/*
		 * When create lab is pressed, attempt to create new
		 * InstructorServer and listen for client connections
		 */
		createLabButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					instructor = new InstructorServer();
					instructor.listen();
					
				} catch (IOException e1) {
					e1.printStackTrace();
					return;
				}
				welcomeFrame.dispose();
				instructor.setUI();		
			}
		}
		);
		welcomeFrame.revalidate();
		welcomeFrame.repaint();
	}

}
