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
import javax.swing.WindowConstants;

public class InstructorServer {
	
	private static final int PORT_NUM = 8000;
	
	private HashSet<String> clients;
	
	private HashSet<PrintWriter> clientWriters;
	
	private Vector<String> requests;
	
	private JFrame iFrame;
	private InstructorUI instUI;
	
	public InstructorServer() {
		clients = new HashSet<String>();
		clientWriters = new HashSet<PrintWriter>();
		requests = new Vector<String>();
	}
	
	public void setFrame() {
		iFrame = new JFrame("Server Frame");
		iFrame.setSize(400,200);
		iFrame.setVisible(true);
		JOptionPane.showMessageDialog(iFrame, "new frame");
	}
	
	public JFrame getFrame() {
		return iFrame;
	}
		
	public void listen() throws IOException {
		ServerSocket serverSocket = new ServerSocket(PORT_NUM);
		//new sUIThread(this).start();
		try {
			//make a new thread for every client that connects
			while (true) {
				new ClientThread(serverSocket.accept(), this).start();
			}
		}
		finally {
			serverSocket.close();
		}
	}
	
	public void addClient(String client) {
		clients.add(client);

		System.out.println(client + " connected");
	}
	
	public void addRequest(String req) {
		requests.addElement(req);
		System.out.println(requests);
	}
	
	public void removeRequest(Request req) {
		requests.remove(req);
		System.out.println(requests);
	}
	
	public HashSet<String> getClients() {
		return clients;
	}
	
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
				
				//wait for client requests
				while (true) {
					String cIn = input.readLine();
					if (cIn == null) return;
					if (cIn.startsWith("REQUEST")) {
						server.addRequest(clientName);
						System.out.println("Request from " + clientName);
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
