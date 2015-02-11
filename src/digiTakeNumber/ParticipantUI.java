package digiTakeNumber;

import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


/**
 * A class to do all the drawing for a Client object
 * @author Team Digital Take-A-Number 
 *
 */
public class ParticipantUI {
	
	private RequestMenu reqMenu;
	private JFrame locFrame;
	private Participant client;
	
	
	/**
	 * Constructor for the ClientUI class
	 */
	public ParticipantUI(Participant client){
		this.client = client;
		reqMenu = new RequestMenu();
	}
	
	public void removeReq() {
		reqMenu.remove();
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
			 * request button listeners. Send a string to the client
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
	
	
	private class SeatMenu {
		public SeatMenu() {
			
		}
	}
	
	/**
	 * Called to paint the whole window appropriately
	 * @param g The graphics object to do the drawing with
	 */
	public void paint(Graphics g){
		
	}
	

	/**
	 * Called to paint the Select Seat Location Screen
	 * @param g The graphics object to do the drawing with
	 */
	private void drawSelectSeatLocScreen(Graphics g){
		
	}

	/**
	 * Called to paint the Input Name Screen
	 * @param g The graphics object to do the drawing with
	 */
	private void drawInputNameScreen(Graphics g){
		
	}

	/**
	 * Called to paint the Select Request Screen
	 * @param g The graphics object to do the drawing with
	 */
	public void drawSelectRequestScreen(){
		
	}
	
	/**
	 * Called to paint the Request Received Screen
	 * @param g The graphics object to do the drawing with
	 */
	private void drawRequestReceivedScreen(Graphics g){
		
	}
}
