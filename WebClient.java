
import java.io.*;
import java.net.*;


public class WebClient {
	final static String CRLF = "\r\n";
	private Socket controlSocket = null;
	private BufferedReader controlReader = null;
	private DataOutputStream controlWriter = null;

	// Set up input stream, what will be used to receive

	// set up output stream which will send in data
	// What is used to actually read
	public static void main(String args[]) {
		WebClient client = new WebClient();
		client.connect();
	}

	public void connect() {
		try {
			controlSocket = new Socket("localhost", 6789);

			// get references to the socket input and output streams
			InputStream is = new DataInputStream(controlSocket.getInputStream());
			controlWriter = new DataOutputStream(controlSocket.getOutputStream());

			controlReader = new BufferedReader(new InputStreamReader(is));
			clientProcess();
		} catch (UnknownHostException ex) {
			System.out.println("UnknownHostException: " + ex);
		} catch (IOException ex) {
			System.out.println("IOException: " + ex);
		}
	}

	public void clientProcess() {
		try {

			// Connect to the command line read so users can input stuff in the command line
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			// Asks for a user name and retreives it
			System.out.println("Enter User Name: ");
			String userName = reader.readLine();

			// Send the username to the server
			controlWriter.writeBytes(userName + CRLF);

			// Creates and spins off thread to read everything from the server
			reader read = new reader(controlSocket);
			Thread thread = new Thread(read);
			thread.start();

			// Prints out the commands that can be used by the user
			help();
			while (true) {
				// This is the user command that is being put in
				String choice = reader.readLine();

				if (choice.equalsIgnoreCase("help")) {
					help();
					// send to help function
				} else if (choice.equalsIgnoreCase("join")) {
					// connect back to server
					connect();
				} else if (choice.equalsIgnoreCase("exit")) {
					// Send exit command to server and delete it from everything that its in there

					controlWriter.writeBytes(choice + CRLF);

					controlReader.close();
					controlWriter.close();
					controlSocket.close();
					// close all streams and connection
				} else {
					// If it is any other than help, join, or exit, send to the server to deal with
					controlWriter.writeBytes(choice + CRLF);
				}
			}

		} catch (UnknownHostException ex) {
			System.out.println("UnknownHostException: " + ex);
		} catch (IOException ex) {
			System.out.println("IOException: " + ex);
		}

	}

	// This class will constantly check up on the TCP connection to retrieve any
	// message that is sent from the server

	public static class reader extends Thread {
		// this will poll and read from the input stream as it is coming in
		Socket socket;

		public reader(Socket socket) {
			this.socket = socket;
		}

		// This is what the runs

		public void run() {
			// create the input stream from the socket and the buffer reader to read from
			// the socket
			try {
				InputStream is = new DataInputStream(socket.getInputStream());
				BufferedReader controlReader = new BufferedReader(new InputStreamReader(is));
				while (true) {
					// Wait for a message to be send by the user
					String newMsg = controlReader.readLine();
					// Print out the message to the terminal
					System.out.println(newMsg);
				}

			} catch (UnknownHostException ex) {
				System.out.println("UnknownHostException: " + ex);
			} catch (IOException ex) {
				System.out.println("IOException: " + ex);
			}
		}
	}
	// function to show user available commands
	public void help() {
		System.out.println(
				"\n\nFormat for commands: To see this again use help\njoin\npost StringSubject StringMessage\nusers\nrequest IntIDnum\nexit");
		System.out.println(
				"groups\ngroupJoin groupNumber\ngroupUsers\ngroupLeave groupNumber\ngroupPost groupID StringSubject StringMessage\ngroupRequest groupNumber ID\n\n");
	}
}

/*
 * 
 * FORMAT FOR COMMANDS
 * 
 * join
 * post StringSubject StringMessage
 * users
 * request IntIDnum
 * exit
 * 
 * groups
 * groupJoin groupNumbers
 * groupUsers
 * groupLeave groupNumber
 * groupPost groupID StringSubject StringMessage
 * 
 * help: print this
 * 
 */