package digiTakeNumber;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Vector;


/*
 * This class sets up a server and allows multiple
 * clients to connect to it. Also responds accordingly
 * to any client input
 */

public class InstructorServer {
	
	//arbitrary port number
	public static final int PORT_NUM = 8000;
	
	private Vector<String> clients;
	
	//list of client printwriters so the server can send 
	//updates to all clients
	private Vector<PrintWriter> clientWriters;
	
	private Vector<String> requests;
	
	private InstructorUI instUI;
	
	private ServerSocket sSocket;
	
	private LabState labState;
	
	private boolean labReady;
	
	/*
	 * Instantiate instance variables
	 */
	public InstructorServer() {
		clients = new Vector<String>();
		clientWriters = new Vector<PrintWriter>();
		requests = new Vector<String>();
		labReady = false;
	}
	
	public void setReady() {
		labReady = true;
	}
	public boolean isReady() {
		return labReady;
	}
	/*
	 * Create a new frame for the server
	 */
	public void setUI() {
		instUI = new InstructorUI(this);
	}
	
	public void setLabState(LabState labState) {
		this.labState = labState;
	}
	
	public LabState getLabState() { return labState; }

	public String seatsToString() {
		String temp = "";
		int[][] seats = labState.getSeatingChart();
		int rows = labState.getRows();
		int cols = labState.getCols();
		//System.out.println(rows);
		//System.out.println(cols);
		for (int i = 0; i < rows; i++) {
			for (int j =0; j < cols; j ++) {
				temp = temp + seats[i][j];
			}
		}
		return temp;
	}
		
	/*
	 * Continuously listen for new clients to connect.
	 * Every time a client connects to the server, create
	 * a new threat for the connected socket
	 */
	public void listen() throws IOException {
		
		//make a thread that constantly listens for new clients to connect
		new ListenerThread().start();
	
	}
	
	public void stopListening() {
		try {
			sSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Add client to server's list of clients
	 */
	public void addClient(String client, int row, int col) {
		clients.add(client);
		System.out.println(client + " sat at " + row + " " + col);
		instUI.addMessage(client + " sat at " + row + " " + col);
	}
	
	
	/*
	 * add a request to the server's list of 
	 * requests
	 */
	public void addRequest(String req) {
		requests.addElement(req);
		System.out.println(requests);
	}
	
	/*
	 * remove request from server's list of requests
	 */
	public void removeRequest(String req) {
		requests.remove(req);
		System.out.println(requests);
	}
	
	public Vector<String> getClients() {
		return clients;
	}
	
	private class ListenerThread extends Thread {
		public ListenerThread() { };
		public void run() {
			//while (!labReady) { }
			try {
				sSocket = new ServerSocket(PORT_NUM);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			while(true) {
				try {
					new ClientThread(sSocket.accept()).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * A thread that is created for each client.
	 * Handles all messages sent by the clients
	 * to the server
	 */
	private class ClientThread extends Thread {
		private String clientName;
		private Socket socket;
		private BufferedReader input;
		private PrintWriter output;
		private InstructorServer server;
		private int rowLoc;
		private int colLoc;
		private boolean connected;
		
		public ClientThread(Socket socket) {
			this.socket = socket;
		}
		
		//set up input streams and output streams for client
		public void run() {
			try {
				System.out.println("New client");
				input = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				output = new PrintWriter(socket.getOutputStream(), true);
				
				String cIn;
				
				//wait to get client's name
				while (true) {
					cIn = input.readLine();
					if (cIn == null) return;
					if (cIn.startsWith("NAME:")) {
						clientName = cIn.substring(5);
						break;
					}
				}
				
				
				int rows = labState.getRows();
				int cols = labState.getCols();
				int aisle = labState.getAisle();
				int[][] sChart = labState.getSeatingChart();
				String seats = seatsToString();
				
				
				//send client seating dimensions and wait
				//to receive seating locations
				output.println("SEATDIM:" + rows + "#" + 
								cols + "#" + aisle + "#" + seats);
				while (true) {
					cIn = input.readLine();
					if (cIn == null) return;
					if (cIn.startsWith("TAKESEAT:")) {
						String[] takenSeat = cIn.substring(9).split("#");
						int takeRow = Integer.parseInt(takenSeat[0]);
						int takeCol = Integer.parseInt(takenSeat[1]);
						rowLoc = takeRow;
						colLoc = takeCol;
						labState.takeSeat(takeRow, takeCol);
						addClient(clientName, rowLoc, colLoc);
						break;
					}
				}
				
				
				//wait for client requests, print to the console
				//depending on the request
				
				while (true) {
					cIn = input.readLine();
					if (cIn == null) return;
					
					if (cIn.startsWith("HELP")) {
						//server.addRequest(clientName);
						addRequest(clientName);
						instUI.addMessage(clientName + " needs help");
						System.out.println(clientName + " needs help");
					}
					
					if (cIn.startsWith("CHECKPOINT")) {
						//server.addRequest(clientName);
						addRequest(clientName);
						instUI.addMessage(clientName + " finished a checkpoint");
						System.out.println(clientName + " finished a checkpoint");
					}
					
					if (cIn.startsWith("CLEARLOC")) {
						instUI.addMessage(clientName + " wants to clear location");
						System.out.println(clientName + " wants to clear location");
					}
					
					if (cIn.startsWith("CANCEL")) {
						instUI.addMessage(clientName + " cancelled request");
						System.out.println(clientName + " cancelled request");
						removeRequest(clientName);
					}
					
					
				}	
			}
			
			catch (IOException e) {
				System.out.println(e);
			}
			
			finally {
				try {
					socket.close();
				}
				catch (IOException e) {}
			}
		}
	}

	
}
