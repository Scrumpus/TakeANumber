package digiTakeNumber;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;

/**
 * A class that contains all the functionality to allow Server and Client (subclasses) to 
 * communicate with one another
 * @author Team Digital Take-A-Number 
 * @version 11/16/2014
 *
 */
public abstract class Network {
	protected BufferedReader input;
	protected BufferedWriter output;
	protected Boolean isComplete;
	
	/**
	 * Constructor for the Network class
	 */
	public Network(){
	}
	
	/**
	 * Returns a line of input from the client/server.
	 * @return the info read in from the BufferReader object
	 */	
	protected String readInputLine(){
		return "";
	}
	
	/**
	 * Writes a line of output to send to the client/server.
	 * @param outputString The string to be written to the PrintWriter object.
	 */
	protected void writeOutputLine(String outputString){
		return;
	}
	
	/**
	 * Returns the port number on which the server listens for client connections.
	 * @return the port number of the port this server is operating through
	 */
	public int getServerPort(){
		return 0;
	}
	
	/**
	 * Encrypt a given String to be sent to the client/server using DES.
	 * @param info the String to be encrypted
	 * @return the encrypted version of info
	 * @exception NoSuchAlgorithmException If DES is specified incorrectly somehow
	 * @exception NoSuchPaddingException If padding method not available
	 * @exception InvalidKeyException If encryption/decryption key is invalid
	 * @exception IllegalBlockSizeException If the length of the data does not match the block size of the cipher.
	 * @exception BadPaddingException If the data is not padded properly
	 */
	protected String encrypt(String info){
		return "";
	}
	
	/**
	 * Decrypt a given string received from the client/server.
	 * @param encryptedInfo the encrypted String to decrypt
	 * @return the decrypted String
	 * @exception NoSuchAlgorithmException If DES is specified incorrectly somehow
	 * @exception NoSuchPaddingException If padding method not available
	 * @exception InvalidKeyException If encryption/decryption key is invalid
	 * @exception IllegalBlockSizeException If the length of the data does not match the block size of the cipher.
	 * @exception BadPaddingException If the data is not padded properly
	 */
	protected String decrypt(String encryptedInfo){
		return "";
	}
}
