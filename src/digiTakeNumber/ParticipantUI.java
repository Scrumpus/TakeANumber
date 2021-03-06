package digiTakeNumber;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;


/**
 * A class to do all the drawing for a Client object
 * @author Team Digital Take-A-Number 
 * @version 04/02/2015
 */
public class ParticipantUI {
	
	private RequestMenu reqMenu;
	private SeatMenu seatMenu;
	private Participant client;
	private JFrame currFrame;
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
	
	public JFrame getFrame() {
		return currFrame;
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
	
	public void takeSeat(int row, int col) {
		seatMenu.disableSeat(row, col);
	}
	public void leaveSeat(int row, int col) {
		seatMenu.enableSeat(row, col);
	}
	
	public void showPosition(int pos) {
		reqMenu.showPosition(pos);
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
		private JLabel reqConfirm;
		private JLabel showPos;
		
		public RequestMenu() {
			reqFrame = new JFrame("Request Frame");
			reqFrame.setSize(400,200);
			reqFrame.setVisible(false);
			reqFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			buttonPanel = new JPanel();
			helpRequest = new JButton("Request Help");
			cpRequest = new JButton("Checkpoint Completed");
			clearLoc = new JButton("Clear location");
			cancelRequest = new JButton("Clear Request");
			reqConfirm = new JLabel("Your request has been submitted.");
			showPos = new JLabel();
			cancelRequest.setVisible(false);
			reqConfirm.setVisible(false);
			showPos.setVisible(false);
			
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
				}
			});
			
			cpRequest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!client.isPending()) {
						client.sendServer("CHECKPOINT");
						client.togglePending();
						hideRequestButtons();
					}
				}
			});
			
			clearLoc.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					client.sendServer("CLEARLOC");
					client.setRow(-1);
					client.setCol(-1);
					showSeatMenu();
					reqFrame.setVisible(false);
				}
			});
			
			cancelRequest.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (client.isPending()) {
						client.sendServer("CANCEL:" + client.getPos());
						client.togglePending();
						showRequestButtons();
					}
				}
			});
			
			//add buttons to client's frame
			buttonPanel.add(helpRequest);
			buttonPanel.add(cpRequest);
			buttonPanel.add(clearLoc);
			buttonPanel.add(reqConfirm);
			buttonPanel.add(showPos);
			buttonPanel.add(cancelRequest);
			
			
			reqFrame.add(buttonPanel);
			reqFrame.revalidate();
			reqFrame.repaint();
			
			currFrame = reqFrame;
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
			clearLoc.setVisible(false);
			cancelRequest.setVisible(true);
			reqConfirm.setVisible(true);
			showPos.setVisible(true);
		}
		public void showRequestButtons() {
			helpRequest.setVisible(true);
			cpRequest.setVisible(true);
			clearLoc.setVisible(true);
			cancelRequest.setVisible(false);
			reqConfirm.setVisible(false);
			showPos.setVisible(false);
		}
		public void remove() {
			reqFrame.dispose();
		}
		public void showPosition(int pos) {
			showPos.setText("Your position in line is " + pos);
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
		private GridLayout seatSpacing = new GridLayout(1,2);
		private List<ArrayList<JButton>> seats = new ArrayList<ArrayList<JButton>>();
		
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
				seats.add(new ArrayList<JButton>());
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
			currFrame = seatMenuFrame;
		}
		
		//handles which seat got selected by the client
		public ActionListener seatSelect(final int row, final int col) {
			return new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					client.setRow(row);
					client.setCol(col);
					client.sendServer("TAKESEAT:" + row + "#" + col);
					hideSeatMenu();
					showReq();
				}
			};
		}
		
		public void disableSeat(int row, int col) {
			seats.get(row).get(col).setEnabled(false);
		}
		
		public void enableSeat(int row, int col) {
			seats.get(row).get(col).setEnabled(true);
		}
		
		public void hide() {
			seatMenuFrame.setVisible(false);
		}
		public void show() {
			seatMenuFrame.setVisible(true);
		}
	}
}
