package digiTakeNumber;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Participant {
	private String name;
	private int seatLoc;
	private boolean pendingReq;
	
	private BufferedReader input;
	private PrintWriter output;
	private JFrame pFrame;
	
	public Participant() {
		pendingReq = false;
	}
	
	public void setFrame() {
		pFrame = new JFrame("Client Frame");
		pFrame.setSize(400,200);
		pFrame.setVisible(true);
		pFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	private String getServerIP() {
		return JOptionPane.showInputDialog(
				"Enter Instructor's IP Address");
	}
	
	private void setName() {
		name = JOptionPane.showInputDialog(
				"Enter Your Name");
	}
	
	public void connectToServer() throws IOException {
		String serverAddress = getServerIP();
		setName();
		addRequestButtons();
		Socket socket = new Socket(serverAddress, InstructorServer.PORT_NUM);
		input = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
		output.println(name);
	}
	
	public void addRequestButtons() {
		JPanel panel = new JPanel();
		JButton helpRequest = new JButton("Request Help");
		JButton cpRequest = new JButton("Checkpoint Completed");
		JButton clearLoc = new JButton("Clear location");
		JButton cancelRequest = new JButton("Clear Request");
		
		helpRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!pendingReq) {
					output.println("HELP");
					pendingReq = true;
				}
				else {
					System.out.println("Request already pending");
				}
			}
		});
		
		cpRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!pendingReq) {
					output.println("CHECKPOINT");
					pendingReq = true;
				}
				else {
					System.out.println("Request already pending");
				}
			}
		});
		
		clearLoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.println("CLEARLOC");
			}
		});
		
		cancelRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pendingReq) {
					output.println("CANCEL");
					pendingReq = false;
				}
			}
		});
		
		
		panel.add(helpRequest);
		panel.add(cpRequest);
		panel.add(clearLoc);
		panel.add(cancelRequest);
		
		pFrame.add(panel);
		pFrame.revalidate();
		pFrame.repaint();
	}

}
