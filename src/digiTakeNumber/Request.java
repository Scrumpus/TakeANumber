package digiTakeNumber;

/**
 * This class contains information about the client that
 * submitted a request. This class will be passed between
 * clients and servers.
 * @author Scott Schwalbe
 * @version 11/16/2014
 */
public class Request {
	
	private int cpNum;
	private String clientName;
	private int row;
	private int col;
	public enum ReqType {CHECKPOINT,QUESTION,CLEAR_REQUEST,CLEAR_LOCATION,SET_LOCATION};
	private ReqType type;
	
	/**
	 * Constructor for the Request class.
	 * @param cpNum the current checkpoint number of the Client.
	 * @param type the type of request to be sent.
	 * @param clientName the name of the Client sending the request.
	 * @param clientRow the row of the seating position of the Client.
	 * @param clientCol the column of the seating position of the Client.
	 */
	public Request(String clientName){
		this.clientName = clientName;
	}
	
	public String toString(){
		return "";
	}
	
	/**
	 * Return client's name of request
	 * @return client's name
	 */
	public String getName(){
		return "";
	}
	
	/**
	 * Return row of client's seat
	 * @return client's row position
	 */
	public int getRow(){
		return 0;
	}
	
	/**
	 * Return column of client's seat
	 * @return client's column position
	 */
	public int getCol(){
		return 0;
	}
	
	/**
	 * Return type of request
	 * @return type of request
	 */
	public ReqType getReqType(){
		return null;
		
	}

}
