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
	
	private BufferedReader input;
	private PrintWriter output;
	private JFrame pFrame;
	
	public Participant() {
		
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
		addRequestButton();
		Socket socket = new Socket(serverAddress, 8000);
		input = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
		output.println(name);
	}
	
	public void addRequestButton() {
		JPanel panel = new JPanel();
		JButton request = new JButton("Send Request");
		
		request.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.println("REQUEST");
			}
		});
		
		panel.add(request);
		pFrame.add(panel);
		pFrame.revalidate();
		pFrame.repaint();
	}

}
