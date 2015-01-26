package digiTakeNumber;

import java.awt.Graphics;
import java.awt.Label;
import java.util.HashSet;

import javax.swing.*;

/**
 * This class contains all graphic components for each
 * Server UI screen. 
 * @author Team Digital Take-A-Number
 * @version 11/16/2014
 */
public class InstructorUI extends JPanel {
	
	private JFrame mainFrame;
	private JPanel mainPanel;
	private InstructorServer server;
	
	/**
	 * Constructor for ServerUI
	 */
	public InstructorUI(JFrame mFrame){
		mainFrame = mFrame;
	}
	
	/**
	 * Main drawing method for the entire program
	 * @param g graphics component
	 */
	public void paint(Graphics g){
		return;
	}
	
	/**
	 * Draws the welcome screen of the program.
	 * @param g graphics component
	 */
	private void drawWelcomeScreen(Graphics g){

	}
	
	/**
	 * Draws the screen to enter the dimensions of the
	 * lab room.
	 * @param g graphics component
	 */
	private void drawInputLabInfoScreen(Graphics g){
		return;
	}

	/**
	 * Draws the screen that will be seen by the instructor
	 * @param g graphics component
	 */
	private void drawInstructorLabDisplay(Graphics g){
		return;
	}

	/**
	 * Draws the finish lab screen for the instructor
	 * @param g graphics component
	 */
	private void drawInstructorFinishLabScreen(Graphics g){
		return;
	}

	/**
	 * Draws the lab overview screen once a lab has been
	 * finished
	 * @param g graphics component
	 */
	private void drawLabOverviewScreen(Graphics g){
		return;
	}
	
	public void drawClientList() {
		JLabel cLabel = new JLabel("Clients");
		mainPanel.add(cLabel);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	//clear contents of the main frame
	public void refreshFrame() {
		mainFrame.getContentPane().removeAll();
		mainFrame.revalidate();
		mainFrame.repaint();
	}
	
	public void popUp(String message) {
		JOptionPane.showMessageDialog(mainFrame, message);
	}
	
	public void updateClientList(HashSet<String> clients) {
		
	}
	
}