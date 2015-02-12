package digiTakeNumber;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.*;

/**
 * This class contains all graphic components for each
 * Server UI screen. 
 * @author Team Digital Take-A-Number
 * @version 11/16/2014
 */
public class InstructorUI {
	
	private JFrame iFrame = new JFrame("Instructor Frame");
	private JTextArea messages;
	private SeatingLayout seatingLayout;
	private InstructorServer server;
	
	/**
	 * Constructor for ServerUI
	 */
	public InstructorUI(InstructorServer server){
		this.server = server;
		iFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//addCloseButton();
		setSeatingLayout();
		addMessageArea();
		iFrame.pack();
		iFrame.setVisible(true);
	}
	
	public void setSeatingLayout() {
		seatingLayout = new SeatingLayout();
		seatingLayout.init();
		seatingLayout.setDefault();
	}
	public void addMessageArea() {
		messages = new JTextArea(8, 40);
		messages.setEditable(false);;
		iFrame.getContentPane().add(new JScrollPane(messages), "Center");
	}
	
	public void addCloseButton() {
		JPanel panel = new JPanel();
		JButton closeButton = new JButton("Close Lab");
		
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Clicked");
				//server.stopListening();				
			}
		}
		);
		
		panel.add(closeButton);
		iFrame.add(panel);
		iFrame.revalidate();
		iFrame.repaint();
		
	}
	
	public void addMessage(String message) {
		messages.append(message + "\n");
	}
	
	private class SeatingLayout {
		private static final int DEFAULT_ROWS = 5;
		private static final int DEFAULT_COLS = 7;
		private static final int DIVISOR = 4;
		private int numRows;
		private int numCols;
		private int divLoc;
		private JPanel leftSide = new JPanel();
		private JPanel rightSide = new JPanel();
		private JPanel options = new JPanel();
		private JPanel seatsPanel = new JPanel();
		private GridLayout seatSpacing = new GridLayout(1,2);
		private Vector<Vector<JButton>> seats = new Vector<Vector<JButton>>();
		private JComboBox rowNum;
		private JComboBox colNum;
		private JComboBox divisor;
		private JButton apply = new JButton("Apply Dimensions");
		private JButton create = new JButton("Create lab");
		private JButton endLab = new JButton("End lab");
		private final String[] rowOpts = {"1","2","3","4","5","6","7","8",
										  "9","10","11","12","13","14","15"};
		private final String[] colOpts = {"1","2","3","4","5","6","7","8",
										  "9","10","11","12","13","14","15"};
		private final String[] divOpts = {"1","2","3","4","5","6","7","8",
				  						  "9","10","11","12","13","14","15"};;
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public SeatingLayout() {	
			
		}
		
		public void init() {
			//this panel contains the left and right side of the room
			seatsPanel.setLayout(seatSpacing);
			
			//this panel contains the options of the room dimensions
			options.setLayout(new GridLayout(2,4));
			options.add(new JLabel("Number of columns"));
			options.add(new JLabel("Number of rows"));
			options.add(new JLabel("Column before divisor"));
			options.add(apply);
			rowNum = new JComboBox(rowOpts);
			colNum = new JComboBox(colOpts);
			divisor = new JComboBox(divOpts);
			options.add(colNum);
			options.add(rowNum);
			options.add(divisor);
			options.add(create);
			
			apply.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					leftSide.removeAll();
					rightSide.removeAll();
					seatsPanel.removeAll();
					
					numRows = Integer.parseInt((String)rowNum.getSelectedItem());
					numCols = Integer.parseInt((String)colNum.getSelectedItem());
					divLoc = Integer.parseInt((String)divisor.getSelectedItem());
					
					if (divLoc > numCols) { divLoc = numCols; }
					leftSide.setLayout(new GridLayout(numRows, divLoc));
					rightSide.setLayout(new GridLayout(numRows, numCols - divLoc));
					
					seats.removeAllElements();

					for (int i = 0; i < numRows; i++) {
						for (int j = 0; j < divLoc; j++) {
							leftSide.add(new JButton("Seat"));
						}
						for (int k = 0; k < numCols - divLoc; k++) {
							rightSide.add(new JButton("Seat"));
						}
					}
							
					seatsPanel.add(leftSide);
					seatsPanel.add(rightSide);
					seatSpacing.setHgap(30);
					seatSpacing.layoutContainer(seatsPanel);
					refreshFrame();
				}
			});
			
			
			create.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int confirm = JOptionPane.showConfirmDialog(iFrame, " is this correct?");
					if (confirm == JOptionPane.YES_OPTION) {
						server.setLabState(new LabState(numRows, numCols, divLoc));
						options.removeAll();
						options.add(endLab);
						refreshFrame();
						System.out.println("Lab created");
						
						// start listening for clients
						try {
							server.listen();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			
			iFrame.setLayout(new GridLayout(3,1));
			iFrame.add(seatsPanel);
			iFrame.add(options, BorderLayout.SOUTH);
			refreshFrame();
		}
		
		public void setDefault() {
			leftSide.removeAll();
			rightSide.removeAll();
			seatsPanel.removeAll();
			seats.removeAllElements();
			
			numCols = DEFAULT_COLS;
			numRows = DEFAULT_ROWS;
			divLoc = DIVISOR;
			leftSide.setLayout(new GridLayout(numRows, divLoc));
			rightSide.setLayout(new GridLayout(numRows, numCols - divLoc));
			
			for (int i = 0; i < numRows; i++) {
				for (int j = 0; j < divLoc; j++) {
					leftSide.add(new JButton("Seat"));
				}
				for (int k = 0; k < numCols - divLoc; k++) {
					rightSide.add(new JButton("Seat"));
				}
			}
					
			seatsPanel.add(leftSide);
			seatsPanel.add(rightSide);
			seatSpacing.setHgap(30);
			seatSpacing.layoutContainer(seatsPanel);
			
			rowNum.setSelectedItem("" + DEFAULT_ROWS);
			colNum.setSelectedItem("" + DEFAULT_COLS);
			divisor.setSelectedItem("" + DIVISOR);
			
			refreshFrame();
		}
		
		public void disableSeat(int row, int col) {
			
		}
		
		public void openSeat(int row, int col) {
			
		}
	}
		
	
	public void drawClientList() {
		
	}
	
	//clear contents of the main frame
	public void refreshFrame() {
		iFrame.revalidate();
		iFrame.repaint();
	}
	
	public void popUp(String message) {
		JOptionPane.showMessageDialog(iFrame, message);
	}
	
	public void updateClientList(HashSet<String> clients) {
		
	}
	
}