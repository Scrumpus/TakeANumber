package digiTakeNumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
	
	private JFrame iFrame;
	private InstructorUI instUI;
	
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
	public void setFrame() {
		iFrame = new JFrame("Server Frame");
		iFrame.setSize(400,200);
		iFrame.setVisible(true);
		JOptionPane.showMessageDialog(iFrame, "new frame");
	}
	
	public JFrame getFrame() {
		return iFrame;
	}
		
	/*
	 * Continuously listen for new clients to connect.
	 * Every time a client connects to the server, create
	 * a new thread for the connected socket
	 */
	public void listen() throws IOException {
		ServerSocket serverSocket = new ServerSocket(PORT_NUM);
		//new sUIThread(this).start();
		try {
			//make a new thread for every client that connects
			while (true) {
				System.out.println("Waiting for client");
				new ClientThread(serverSocket.accept(), this).start();
			}
		}
		finally {
			serverSocket.close();
		}
	}
	
	/*
	 * Add client to server's list of clients
	 */
	public void addClient(String client) {
		clients.add(client);
		System.out.println(client + " connected");
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
		
		public ClientThread(Socket socket, InstructorServer server) {
			this.socket = socket;
			this.server = server;
		}
		
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
						System.out.println(clientName + " needs help");
					}
					
					if (cIn.startsWith("CHECKPOINT")) {
						server.addRequest(clientName);
						System.out.println(clientName + " finished a checkpoint");
					}
					
					if (cIn.startsWith("CLEARLOC")) {
						System.out.println(clientName + " wants to clear location");
					}
					
					if (cIn.startsWith("CANCEL")) {
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
	
	// May need another thread to update UI?
	private class sUIThread extends Thread {
		private InstructorServer server;
		private JFrame frame;
		
		public sUIThread(InstructorServer server) {
			this.server = server;
			frame = server.getFrame();
		}
		
		public void run() {
			JLabel label = new JLabel("Clients");
			JPanel panel = new JPanel();
			panel.add(label);
			frame.add(panel);
			frame.revalidate();
			frame.repaint();
		}
	}
	
}
