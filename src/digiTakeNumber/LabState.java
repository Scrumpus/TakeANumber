package digiTakeNumber;

import java.util.PriorityQueue;
import java.util.Vector;

/**
 * This class keeps track of the queue of requests
 * and the location of each requests in the seating chart
 * @author Scott Schwalbe
 * @version 11/16/2014
 */

public class LabState {
	
	private String[][] seatingChart;
	private Vector<Request> requestQueue;
	private int aisleLocation;
	
	/**
	 * Default constructor for LabState.
	 * Used for testing.
	 */
	public LabState(){
		
	}
	
	/**
	 * Constructor for LabState
	 * @param rows
	 * @param cols
	 * @param aisleLoc
	 */
	public LabState(int rows, int cols, int aisleLoc){
		
	}
	
	/**
	 * Returns the seating chart
	 * @return 2d String array seating chart
	 */
	public String[][] getSeatingChart(){
		return seatingChart;
	}
	
	/**
	 * returns the top three requests in the queue
	 * @return first three requests in requests queue
	 */
	public String[] getTopThree(){
		return seatingChart[1];
	}
	
	/**
	 * Add a request to the end of the priority queue
	 * @param req Request to be added
	 * @exception ClassCastException if the specified element cannot be compared with elements currently 
	 * in this priority queue according to the priority queue's ordering
	 * @exception NullPointerException If the specified element is null
	 */
	public void addRequest(Request req)
	{
		return;
	}
	
	/**
	 * Remove a request from the queue
	 * @param req Request to be removed
	 * @exception NullPointerException If the specified element is null
	 */
	public void removeRequest(Request req)
	{
		return;
	}

}
