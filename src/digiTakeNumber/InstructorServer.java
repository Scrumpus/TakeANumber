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
	public static final int PORT_NUM = 9000;
	
	private HashSet<String> clients;
	
	//list of client printwriters so the server can send 
	//updates to all clients
	private HashSet<PrintWriter> clientWriters;
	
	private Vector<String> requests;
	
	private InstructorUI instUI;
	
	private ServerSocket sSocket;
	
	/*
	 * Instantiate instance variables
	 */
	public InstructorServer() {
		clients = new HashSet<String>();
		clientWriters = new HashSet<PrintWriter>();
		requests = new Vector<String>();
	}
	
	/*
	 * Create a new frame for the server
	 */
	public void setUI() {
		instUI = new InstructorUI(this);
	}
	

		
	/*
	 * Continuously listen for new clients to connect.
	 * Every time a client connects to the server, create
	 * a new threat for the connected socket
	 */
	public void listen() throws IOException {
		
		sSocket = new ServerSocket(PORT_NUM);
		//make a new thread for every client that connects
		new ListenerThread(sSocket, this).start();
	
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
	public void addClient(String client) {
		clients.add(client);
		instUI.addMessage(client + " connected");
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
	
	public HashSet<String> getClients() {
		return clients;
	}
	
	private class ListenerThread extends Thread {
		private ServerSocket sSocket;
		private InstructorServer server;
		public ListenerThread(ServerSocket sSocket, InstructorServer server) {
			this.sSocket = sSocket;
			this.server = server;
		}
		public void run() {
			while(true) {
				try {
					new ClientThread(sSocket.accept(), server).start();
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
		private boolean connected;
		
		public ClientThread(Socket socket, InstructorServer server) {
			this.socket = socket;
			this.server = server;
		}
		
		//set up input streams and output streams for client
		public void run() {
			try {
				System.out.println("New client");
				input = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				output = new PrintWriter(socket.getOutputStream(), true);
				//wait to get client's name
				while (true) {
					String cIn = input.readLine();
					if (cIn == null) return;
					clientName = cIn;
					server.addClient(clientName);
					break;
				}
				
				//wait for client requests, print to the console
				//depending on the request
				while (true) {
					String cIn = input.readLine();
					if (cIn == null) return;
					
					if (cIn.startsWith("HELP")) {
						server.addRequest(clientName);
						instUI.addMessage(clientName + " needs help");
						System.out.println(clientName + " needs help");
					}
					
					if (cIn.startsWith("CHECKPOINT")) {
						server.addRequest(clientName);
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
						server.removeRequest(clientName);
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
