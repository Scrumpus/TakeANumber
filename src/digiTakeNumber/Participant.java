package digiTakeNumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;


/**
 * This class connects a client to a server
 * and gives options to send requests to the 
 * server 
 * @author Team Digital Take-A-Number
 * @version 04/02/2015
 */

public class Participant {
	private String name;
	private String serverIP;
	private int seatRow;
	private int seatCol;
	private int currPos;
	private boolean pendingReq;
	private LabState labState;
	
	private BufferedReader input;
	private PrintWriter output;
	private ParticipantUI pUI;
	
	//prompt the participant for the server IP address and name
	public Participant() {
		pendingReq = false;
		currPos = 0;
		setServerIP();
		setName();
	}
	
	public int getRow() { return seatRow; }
	public int getCol() { return seatCol; }
	public int getPos() { return currPos; }
	public void setRow(int row) { seatRow = row; }
	public void setCol(int col) { seatCol = col; }
	
	
	public boolean isPending() { return pendingReq; };
	public void togglePending() { pendingReq = !pendingReq; }
	
	public LabState getLabState() { return labState; }
	
	
	/*
	 * Create a new frame for the participant
	 */
	public void setUI() {
		pUI = new ParticipantUI(this);
	}
	public void removeUI() {
		pUI.removeReq();
	}
	public ParticipantUI getUI() {
		return pUI;
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
	 * Prompt participant for name. Make sure name is not empty
	 * and does not contain '#'
	 */
	public void setName() {
		boolean validName = false;
		do {
		name = JOptionPane.showInputDialog(
				"Enter Your Name. Your name cannot contain '#'");
		if (name == null) {
			name = JOptionPane.showInputDialog("Did not enter your name. "
					+ "Please enter your name. Cannot contain '#'");
		}
		if (name.equals("")) {
			name = JOptionPane.showInputDialog("Did not enter your name. "
					+ "Please enter your name. Cannot contain '#'");
		}
		else if (name.contains("#")) {
			name = JOptionPane.showInputDialog("Entered name contains '#'."
					+ "Please enter your name. Cannot contain '#'");
		}
		else validName = true;
		}
		while(!validName);
	}
	
	//parse a string of seating states sent by the server into a 
	//2d array of integers
	public int[][] parseSeatingStates(String sStates) {
		int rows = labState.getRows();
		int cols = labState.getCols();
		int[][] seatStates = new int[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				seatStates[i][j] = Integer.parseInt(sStates.charAt(i*cols + j) + "");
			}
		}
		return seatStates;
	}
	
	/*
	 * Set up socket to connect to the server.
	 * Set up input and output streams between
	 * participant and server.
	 */
	public void connectToServer() throws IOException {
		Socket socket = new Socket(serverIP, InstructorServer.PORT_NUM);
		input = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
		
		//create a thread that constantly listens for server messages
		new ServerThread(this, socket).start();
		
	}
	
	// thread that camps on the server port
	// and reponds to server messages
	private class ServerThread extends Thread {
		private Participant client;
		private Socket socket;
		public ServerThread(Participant client, Socket socket) {
			this.client = client;
			this.socket = socket;
		}
		public void run() {
			int row, col, aisle;
			String sIn;
			//get server address and participant name
			try {
				//send participant's name to the server
				output.println("NAME:" + name);
			
				//wait to get seating dimensions from the server,
				//then create a seat selection menu with those dimensions
				while (true) {
					sIn = input.readLine();
					if (sIn == null) return;
					
					if (sIn.startsWith("SEATDIM:")) {
						String[] dims = sIn.substring(8).split("#");
						row = Integer.parseInt(dims[0]);
						col = Integer.parseInt(dims[1]);
						aisle = Integer.parseInt(dims[2]);
						labState = new LabState(row, col, aisle);
						int[][] seatStates = parseSeatingStates(dims[3]);
						pUI.setSeatMenu(row, col, aisle, seatStates);

					}
					
					//update layout if a seat is taken
					else if(sIn.startsWith("TAKESEAT:")) {
						String[] dims = sIn.substring(9).split("#");
						int takeRow = Integer.parseInt(dims[0]);
						int takeCol = Integer.parseInt(dims[1]);
						pUI.takeSeat(takeRow, takeCol);
					}
					else if (sIn.startsWith("CLEARSEAT:")) {
						String[] dims = sIn.substring(10).split("#");
						int takeRow = Integer.parseInt(dims[0]);
						int takeCol = Integer.parseInt(dims[1]);
						pUI.leaveSeat(takeRow, takeCol);
					}
					
					//update position display if a request is removed
					else if (sIn.startsWith("CLEARREQ:")) {
						int pos = Integer.parseInt(sIn.substring(9));
						System.out.println("Clear " + pos);
						if (pos < currPos) {
							currPos -= 1;
							pUI.showPosition(currPos);
						}
					}
					
					//get current position in queue
					else if (sIn.startsWith("POSITION:")) {
						currPos = Integer.parseInt(sIn.substring(9));
						pUI.showPosition(currPos);
					}
					
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(pUI.getFrame(), "The lab has disconnected");
				System.exit(0);
			}	
		}
	}
}
