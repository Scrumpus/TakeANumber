package digiTakeNumber;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;


/**
 * A class to do all the drawing for a Client object
 * @author Team Digital Take-A-Number 
 *
 */
public class ParticipantUI {
	
	private RequestMenu reqMenu;
	private SeatMenu seatMenu;
	private JFrame locFrame;
	private Participant client;
	
	
	/**
	 * Constructor for the ClientUI class
	 */
	public ParticipantUI(Participant client){
		this.client = client;
	}
	
	public void refresh(JFrame frame) {
		frame.revalidate();
		frame.repaint();
	}
	
	public void hideReq() {
		reqMenu.hide();
	}
	public void showReq() {
		reqMenu.show();
	}
	public void removeReq() {
		reqMenu.remove();
	}
	
	
	public void setSeatMenu(int row, int col, int pos, int[][] seatStates) {
		//hideReq();
		seatMenu =  new SeatMenu(row, col, pos, seatStates);
		seatMenu.init();
	}
	public void setRequestMenu() {
		reqMenu = new RequestMenu();
	}
	public void hideSeatMenu() {
		seatMenu.hide();
	}
	public void showSeatMenu() {
		seatMenu.show();
	}
	
	/*
	 * Helper class to manage request menu
	 */
	private class RequestMenu {
		private JFrame reqFrame;
		private JPanel buttonPanel;
		private JButton helpRequest;
		private JButton cpRequest;
		private JButton clearLoc;
		private JButton cancelRequest;
		
		public RequestMenu() {
			reqFrame = new JFrame("Request Frame");
			reqFrame.setSize(400,200);
			reqFrame.setVisible(true);
			reqFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			buttonPanel = new JPanel();
			helpRequest = new JButton("Request Help");
			cpRequest = new JButton("Checkpoint Completed");
			clearLoc = new JButton("Clear location");
			cancelRequest = new JButton("Clear Request");
			
			/*
			 * request button listeners. Send a string to the server
			 * depending on which request button was pressed
			 */
			helpRequest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!client.isPending()) {
						client.sendServer("HELP");
						client.togglePending();
						hideRequestButtons();
					}
					else {
						System.out.println("Request already pending");
					}
				}
			});
			
			cpRequest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!client.isPending()) {
						client.sendServer("CHECKPOINT");
						client.togglePending();
						hideRequestButtons();
					}
					else {
						System.out.println("Request already pending");
					}
				}
			});
			
			clearLoc.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					client.sendServer("CLEARLOC");
				}
			});
			
			cancelRequest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (client.isPending()) {
						client.sendServer("CANCEL");
						client.togglePending();;
						showRequestButtons();
					}
				}
			});
			
			//add buttons to client's frame
			buttonPanel.add(helpRequest);
			buttonPanel.add(cpRequest);
			buttonPanel.add(clearLoc);
			buttonPanel.add(cancelRequest);
			
			reqFrame.add(buttonPanel);
			reqFrame.revalidate();
			reqFrame.repaint();
		}
		
		public void hide() {
			reqFrame.setVisible(false);
		}
		public void show() {
			reqFrame.setVisible(true);
		}
		public void hideRequestButtons() {
			helpRequest.setVisible(false);
			cpRequest.setVisible(false);
		}
		public void showRequestButtons() {
			helpRequest.setVisible(true);
			cpRequest.setVisible(true);
		}
		public void remove() {
			reqFrame.dispose();
		}
	}	
	
	/*
	 * helper class that keeps track of the seat selection menu
	 */
	private class SeatMenu {
		private int rows;
		private int cols;
		private int aisle;
		int[][] seatStates;
		private JFrame seatMenuFrame = new JFrame("Select a seat");
		private JPanel leftSide = new JPanel();
		private JPanel rightSide = new JPanel();
		private JPanel seatsPanel = new JPanel();
		private JButton select = new JButton("Join lab");
		private GridLayout seatSpacing = new GridLayout(1,2);
		private Vector<Vector<JButton>> seats = new Vector<Vector<JButton>>();
		
		public SeatMenu(int rows, int cols, int aisle, int[][] seatStates) {
			this.rows = rows;
			this.cols = cols;
			this.aisle = aisle;
			this.seatStates = seatStates;
		}
		
		public void init() {
			seatsPanel.setLayout(seatSpacing);
			leftSide.setLayout(new GridLayout(rows, aisle));
			rightSide.setLayout(new GridLayout(rows, cols - aisle));
			
			for (int i = 0; i < rows; i++) {
				seats.addElement(new Vector<JButton>());
				for (int j = 0; j < aisle; j++) {
					seats.get(i).add(new JButton("Seat"));
					if (seatStates[i][j] != 0) {
						seats.get(i).get(j).setEnabled(false);
					}
					seats.get(i).get(j).addActionListener(seatSelect(i,j));
					leftSide.add(seats.get(i).get(j));
				}
				for (int k = aisle; k < cols; k++) {
					seats.get(i).add(new JButton("Seat"));
					if (seatStates[i][k] != 0) {
						seats.get(i).get(k).setEnabled(false);
					}
					seats.get(i).get(k).addActionListener(seatSelect(i,k));
					rightSide.add(seats.get(i).get(k));
				}
			}
			seatsPanel.add(leftSide);
			seatsPanel.add(rightSide);
			seatSpacing.setHgap(30);
			seatSpacing.layoutContainer(seatsPanel);
			seatMenuFrame.add(seatsPanel);
			seatMenuFrame.pack();
			seatMenuFrame.setVisible(true);
			refresh(seatMenuFrame);
		}
		
		//handles which seat got selected by the client
		public ActionListener seatSelect(final int row, final int col) {
			return new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					client.setRow(row);
					client.setCol(col);
					System.out.println("client row: " + row);
					System.out.println("client col: " + col);
					client.sendServer("TAKESEAT:" + row + "#" + col);
					hideSeatMenu();
					//showReq();
				}
			};
		}
		
		public void hide() {
			seatMenuFrame.setVisible(false);
		}
		public void show() {
			seatMenuFrame.setVisible(true);
		}
		
		public void disableSeat(int row, int col) {
			seats.get(row).get(col).setEnabled(false);
		}
		
		public void enableSeat(int row, int col) {
			seats.get(row).get(col).setEnabled(true);
		}
	}

}
