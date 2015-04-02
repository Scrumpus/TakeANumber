package digiTakeNumber;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.*;

/**
 * This class contains all graphic components for each
 * Server UI screen. 
 * @author Team Digital Take-A-Number
 * @version 04/02/2014
 */
public class InstructorUI {
	
	private JFrame iFrame = new JFrame("Instructor Frame");
	private JTextArea messages;
	private SeatingLayout seatingLayout;
	private InstructorServer server;
	
	/**
	 * Constructor for ServerUI. Creates a seating layout and 
	 * client message area
	 */
	public InstructorUI(InstructorServer server){
		this.server = server;
		iFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//addCloseButton();
		setSeatingLayout();
		//addMessageArea();
		//iFrame.pack();
		iFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		iFrame.setVisible(true);
	}
	
	//initializes the seating layout
	public void setSeatingLayout() {
		seatingLayout = new SeatingLayout();
		seatingLayout.init();
		seatingLayout.setDefault();
	}
	
	//initializes the message area
	public void addMessageArea() {
		messages = new JTextArea(8, 40);
		messages.setEditable(false);;
		iFrame.getContentPane().add(new JScrollPane(messages), "Center");
	}
	
	//add message to client messages
	public void addMessage(String message) {
		messages.append(message + "\n");
	}
	
	//update the seating display
	public void updateSeats() {
		seatingLayout.update();
	}
	
	//helper class that keeps track of the seating layout
	//as the lab progresses
	private class SeatingLayout {
		//default dimensions
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
		
		//left side and right side of the lab will be put on a 
		//Gridlayout to show the divisor
		private GridLayout seatSpacing = new GridLayout(1,2);
		private GridLayout leftSpacing;
		private GridLayout rightSpacing;
		
		private List<ArrayList<JButton>> seats = new ArrayList<ArrayList<JButton>>();
		private JComboBox rowNum;
		private JComboBox colNum;
		private JComboBox divisor;
		private JButton apply = new JButton("Apply Dimensions");
		private JButton create = new JButton("Create lab");
		private JButton endLab = new JButton("End lab");
		private final String[] rowOpts = {"1","2","3","4","5","6","7","8",
										  "9","10"};
		private final String[] colOpts = {"1","2","3","4","5","6","7","8",
										  "9","10"};
		private final String[] divOpts = {"1","2","3","4","5","6","7","8",
				  						  "9","10"};;
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
			
			//handle Apply Dimensions button click
			apply.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					leftSide.removeAll();
					rightSide.removeAll();
					seatsPanel.removeAll();
					
					numRows = Integer.parseInt((String)rowNum.getSelectedItem());
					numCols = Integer.parseInt((String)colNum.getSelectedItem());
					divLoc = Integer.parseInt((String)divisor.getSelectedItem());
					
					if (divLoc > numCols) { divLoc = numCols; }
					leftSpacing = new GridLayout(numRows, divLoc);
					rightSpacing = new GridLayout(numRows, numCols - divLoc);
					leftSpacing.setHgap(10);
					leftSpacing.setVgap(10);
					rightSpacing.setHgap(10);
					rightSpacing.setVgap(10);
					leftSide.setLayout(leftSpacing);
					rightSide.setLayout(rightSpacing);
					
					seats.clear();

					for (int i = 0; i < numRows; i++) {
						seats.add(new ArrayList<JButton>());
						for (int j = 0; j < divLoc; j++) {
							seats.get(i).add(new JButton(""));
							seats.get(i).get(j).setFont(new Font("Arial", Font.BOLD, 20));
							leftSide.add(seats.get(i).get(j));
						}
						for (int k = divLoc; k < numCols; k++) {
							seats.get(i).add(new JButton(""));
							seats.get(i).get(k).setFont(new Font("Arial", Font.BOLD, 20));
							rightSide.add(seats.get(i).get(k));
						}
					}
							
					seatsPanel.add(leftSide);
					seatsPanel.add(rightSide);
					seatSpacing.setHgap(30);
					seatSpacing.layoutContainer(seatsPanel);
					refreshFrame();
				}
			});
			
			//handle Create Lab button click
			create.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int confirm = JOptionPane.showConfirmDialog(iFrame, "Is this correct?");
					if (confirm == JOptionPane.YES_OPTION) {
						server.setLabState(new LabState(numRows, numCols, divLoc));
						options.removeAll();
						options.setLayout(new GridLayout(5,5));
						options.add(endLab);
						refreshFrame();
						
						// start listening for clients
						try {
							server.listen();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			
			//handle End Lab button click
			endLab.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int confirm = JOptionPane.showConfirmDialog(iFrame,
									"Are you sure you want to end lab?");
					if (confirm == JOptionPane.YES_OPTION) {
						System.exit(0);
					}				
				}
			});
			
			iFrame.add(seatsPanel, BorderLayout.CENTER);
			iFrame.add(options, BorderLayout.SOUTH);
			refreshFrame();
		}
		
		//set the default dimensions of the lab before any customizations
		public void setDefault() {
			leftSide.removeAll();
			rightSide.removeAll();
			seatsPanel.removeAll();
			seats.clear();
			
			numCols = DEFAULT_COLS;
			numRows = DEFAULT_ROWS;
			divLoc = DIVISOR;
			leftSpacing = new GridLayout(numRows, divLoc);
			rightSpacing = new GridLayout(numRows, numCols - divLoc);
			leftSpacing.setHgap(10);
			leftSpacing.setVgap(10);
			rightSpacing.setHgap(10);
			rightSpacing.setVgap(10);
			leftSide.setLayout(leftSpacing);
			rightSide.setLayout(rightSpacing);
			
			for (int i = 0; i < numRows; i++) {
				seats.add(new ArrayList<JButton>());
				for (int j = 0; j < divLoc; j++) {
					seats.get(i).add(new JButton(""));
					seats.get(i).get(j).setFont(new Font("Arial", Font.BOLD, 20));
					leftSide.add(seats.get(i).get(j));
				}
				for (int k = divLoc; k < numCols; k++) {
					seats.get(i).add(new JButton(""));
					seats.get(i).get(k).setFont(new Font("Arial", Font.BOLD, 20));
					rightSide.add(seats.get(i).get(k));
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
		
		//update the top three requests display
		public void update() {
			resetButtonText();
			List<String> topThree = server.getTopThree();
			String[] temp;
			int currPos = 0;
			int currRow, currCol;
			String name;
			for (int i = 0; i < topThree.size(); i++) {
				temp = topThree.get(i).split("#");
				name = temp[0];
				currRow = Integer.parseInt(temp[1]);
				currCol = Integer.parseInt(temp[2]);
				currPos = i+1;
				seats.get(currRow).get(currCol).setText("" + currPos + ") " + name);
			}
		}
		
		public void resetButtonText() {
			for (int i = 0; i < seats.size(); i++) {
				for (int j = 0; j < seats.get(i).size(); j++) {
					seats.get(i).get(j).setText("");
				}
			}
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