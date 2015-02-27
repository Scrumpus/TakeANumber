package digiTakeNumber;

/**
 * This class keeps track of the queue of requests
 * and the location of each requests in the seating chart
 * @author Scott Schwalbe
 * @version 11/16/2014
 */

public class LabState {
	
	private int[][] seatingChart;
	private int aisleLocation;
	private int numRows;
	private int numCols;
	
	private static final int EMPTY = 0;
	private static final int TAKEN = 9;
	
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
		seatingChart = new int[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				seatingChart[i][j] = EMPTY;
			}
		}
		numRows = rows;
		numCols = cols;
		aisleLocation = aisleLoc;
	}
	
	public int getRows() { return numRows; }
	public int getCols() { return numCols; }
	public int getAisle() { return aisleLocation; }
	
	public boolean seatOccupied(int row, int col) {
		return seatingChart[row][col] == TAKEN;
	}
	public void takeSeat(int row, int col) {
		seatingChart[row][col] = TAKEN;
	}
	public void leaveSeat(int row, int col) {
		seatingChart[row][col] = EMPTY;
	}
	
	/**
	 * Returns the seating chart
	 * @return 2d String array seating chart
	 */
	public int[][] getSeatingChart(){
		return seatingChart;
	}
	
	public void setSeatingChart(int[][] seats) {
		seatingChart = seats;
	}
	
	/**
	 * returns the top three requests in the queue
	 * @return first three requests in requests queue
	 */
	public int[] getTopThree(){
		return seatingChart[1];
	}
}
