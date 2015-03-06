package digiTakeNumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/*
 * This class sets up a server and allows multiple
 * clients to connect to it. Also responds accordingly
 * to any client input
 */

public class InstructorServer {
	
	//arbitrary port number
	public static final int PORT_NUM = 8000;
	
	private List<String> clients;
		
	private List<String> requests;
	
	private InstructorUI instUI;
	
	private ServerSocket sSocket;
	
	private LabState labState;
	
	private boolean labReady;
	
	private List<String> topThree;
	
	/*
	 * Instantiate instance variables
	 */
	public InstructorServer() {
		clients = new ArrayList<String>();
		requests = new ArrayList<String>();
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

	//render the seating chart to a string so that it can be 
	//sent to a client
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
	 * Creates a listener thread that constantly listens 
	 * for new clients to connect. Creating a listener thread
	 * prevents the program from halting while it waits for a connection
	 */
	public void listen() throws IOException {
	
		new ListenerThread().start();
	
	}
	
	//close the server socket, ending all connections
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
		//instUI.addMessage(client + " sat at " + row + " " + col);
	}
	
	public void removeClient(String client, int row, int col) {
		clients.remove(client);
		System.out.println(client + " cleared location from " + row + " " + col);
		//instUI.addMessage(client + " cleared location from " + row + " " + col);
	}
	
	
	/*
	 * add a request to the server's list of 
	 * requests
	 */
	public void addRequest(String req) {
		requests.add(req);
		System.out.println(requests);
	}
	
	/*
	 * remove request from server's list of requests
	 */
	public void removeRequest(String req) {
		requests.remove(req);
		System.out.println(requests);
	}
	
	public List<String> getClients() {
		return clients;
	}
	
	public List<String> getTopThree() {
		List<String> topThree = new ArrayList<String>();
		for (int i = 0; i < requests.size(); i++) {
			topThree.add(requests.get(i));
		}
		return topThree;
	}
	
	public void printTopThree() {
		for (int i = 0; i < topThree.size(); i++) {
			System.out.println("Request " + i + ": " + topThree.get(i));
		}
	}
	
	
	/* 
	 * creates a new server socket and fires off a thread
	 * every time a client connects. Client threads allow
	 * for multiple clients to connect to the server
	 */
	private class ListenerThread extends Thread {
		public ListenerThread() { };
		public void run() {
			try {
				sSocket = new ServerSocket(PORT_NUM);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			while(true) {
				try {
					//every time a new client connects, create a new
					//thread and run it
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
		private int rowLoc;
		private int colLoc;
		
		public ClientThread(Socket socket) {
			this.socket = socket;
		}
		
		public void run() {
			try {
				System.out.println("New client");
				//set up input and output streams between client and server
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
				String seats = seatsToString();
				
				
				//send client seating dimensions and wait
				//to receive seating locations
				output.println("SEATDIM:" + rows + "#" + 
								cols + "#" + aisle + "#" + seats);
				
				while(true){
					//once client takes a seat, update the lab state
					//and add the client
					boolean firstLoop = true;
					boolean secondLoop = false;
					while (firstLoop) {
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
							firstLoop = false;
							secondLoop = true;
						}
					}
					
					
					//wait for client requests, add message
					//depending on the request
					
					while (secondLoop) {
						cIn = input.readLine();
						if (cIn == null) return;
						
						if (cIn.startsWith("HELP")) {
							addRequest(clientName + "#" + rowLoc + "#" + colLoc);
							topThree = getTopThree();
							//instUI.addMessage(clientName + " needs help");
							System.out.println(clientName + " needs help");
							instUI.updateSeats();
							//printTopThree();
						}
						
						if (cIn.startsWith("CHECKPOINT")) {
							addRequest(clientName + "#" + rowLoc + "#" + colLoc);
							//instUI.addMessage(clientName + " finished a checkpoint");
							System.out.println(clientName + " finished a checkpoint");
							topThree = getTopThree();
							instUI.updateSeats();
							//printTopThree();
						}
						
						if (cIn.startsWith("CLEARLOC")) {
							//instUI.addMessage(clientName + " wants to clear location");
							System.out.println(clientName + " wants to clear location");
							labState.leaveSeat(rowLoc, colLoc);
							removeClient(clientName, rowLoc, colLoc);
							secondLoop = false;
							firstLoop = true;
						}
						
						if (cIn.startsWith("CANCEL")) {
							//instUI.addMessage(clientName + " cancelled request");
							System.out.println(clientName + " cancelled request");
							removeRequest(clientName + "#" + rowLoc + "#" + colLoc);
							topThree = getTopThree();
							instUI.updateSeats();
							//printTopThree();
						}
						
						
					}	
				}
			}
			//handle when a client disconnects
			catch (IOException e) {
				System.out.println(e);
				removeRequest(clientName + "#" + rowLoc + "#" + colLoc);
				removeClient(clientName, rowLoc, colLoc);
				labState.leaveSeat(rowLoc, colLoc);
				instUI.updateSeats();
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
