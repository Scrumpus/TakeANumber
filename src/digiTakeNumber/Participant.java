package digiTakeNumber;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;


/*
 * This class connects a client to a server
 * and gives options to send requests to the 
 * server 
 */

public class Participant {
	private String name;
	private String serverIP;
	private int seatLoc;
	private boolean pendingReq;
	
	private BufferedReader input;
	private PrintWriter output;
	private ParticipantUI pUI;
	
	public Participant() {
		pendingReq = false;
		setServerIP();
		setName();
	}
	
	public boolean isPending() { return pendingReq; };
	public void togglePending() { pendingReq = !pendingReq; }
	
	/*
	 * Create a new frame for the participant
	 */
	public void setUI() {
		pUI = new ParticipantUI(this);
	}
	public void removeUI() {
		pUI.removeReq();
	}
	
	/*
	 * Send a message to the server
	 */
	public void sendServer(String msg) {
		output.println(msg);
	}
	
	/*
	 * Prompt participant for server address
	 */
	public void setServerIP() {
		serverIP =  JOptionPane.showInputDialog(
				"Enter Instructor's IP Address");
	}
	
	
	/*
	 * Prompt participant for name
	 */
	public void setName() {
		name = JOptionPane.showInputDialog(
				"Enter Your Name");
	}
	
	
	
	/*
	 * Set up socket to connect to the server.
	 * Set up input and output streams between
	 * participant and server.
	 */
	public void connectToServer() throws IOException {
		
		//get server address and participant name
		
		Socket socket = new Socket(serverIP, InstructorServer.PORT_NUM);
		input = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
		
		//send participant's name to the server
		output.println(name);
		
		// TODO eventually, server will be sending data
		// back to the clients. Need to handle server output
		// here 
	}
	
	

}
