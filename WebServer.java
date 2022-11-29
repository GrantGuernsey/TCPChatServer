import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import java.net.*;
import java.util.*;


public final class WebServer {
	// Keeps track of the message indexes which are the ID used to retreive the messages
	public int currentMessageIndex = 0;

	// Creating the client handler that will be used to keep track of all of the client information
	public static ClientHandler handler = new ClientHandler();

	public static void main(String argv[]) throws Exception {
		// Creating a server socket on the port 6789, can be any number that is not already being used 
		handler.createList();
		int port = 6789;
		ServerSocket serverSocket = new ServerSocket(port);

		// Infinite loop
		while (true) {
			
			// Waiting for the server to accept a socket
			Socket connection = serverSocket.accept();

			// Once the server accepts the connection, it creates a chat request with the connected socket
			chatRequest request = new chatRequest(connection);


			// Spin off thread to the request, which will run the run in chatRequest
			Thread thread = new Thread(request);

			// Start the thread.
			thread.start();
		}
		

		
	}

}

final class chatRequest implements Runnable {
	// End line character
	final static String CRLF = "\r\n";

	// Socket that is being passed in by the server
	Socket socket;

	// Output stream : maybe change this to be in the client handler under  the user?

	// Construction
	public chatRequest(Socket socket) {
		// The socket that is being passed in is being set to the chatRequest socket
		this.socket = socket;

	}

	// Override the run method, this is where the thread enters
	@Override
	public void run() {
		try {
			// Have the runnable process the message data
			processMessage();
		} catch (Exception e) {
			System.out.println(e);
		}
	}


	public void processMessage() throws Exception {
		// Get a reference to the socket's input and output streams.
		InputStream is = new DataInputStream(this.socket.getInputStream());
		// Set up input stream filters.
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		// Get username
		String userName = br.readLine();

		// Print that the current user has joined the server
		System.out.println(userName + " has joined the server!");

		// Current Users
		String currentUsers = WebServer.handler.displayUsers();

		// Calls to the handler in the web server and adds the new user based on the connection and name pulled in
		ClientHandler.User thisUser = WebServer.handler.addUser(userName, this.socket);

		// Creates the last two messages, AKA the join message
		String joinMessage = WebServer.handler.displayLastTwoMessage();

		// Writes the join message to the current user
		WebServer.handler.writeToUser(thisUser, "Current users:\n" + currentUsers);
		WebServer.handler.writeToUser(thisUser, "Messages in server:\n" +joinMessage);

		
		// Infinite processing loop for getting the commands from the user
		while (true) {
			// This reads the entire user message that was sent to the server
			String information = br.readLine();
			if(information == null){
				is.close();
				br.close();
				WebServer.handler.exit(thisUser);
				return;
			}
			// Splits the command name from the rest of the information
			String[] splitCommand = information.split(" ", 2);
			
			if (splitCommand[0].equalsIgnoreCase("post")) {
				//posts a message in the global group chat
				String[] subMess = splitCommand[1].split(" ", 2);

				Date date = Calendar.getInstance().getTime();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
				String strDate = dateFormat.format(date);
				System.out.println(strDate);
				WebServer.handler.addMessage(userName, strDate, subMess[0], subMess[1]);
					
			} else if (splitCommand[0].equalsIgnoreCase("users")) {
				// Print all users currently connected
				String users = WebServer.handler.displayUsers();
				System.out.println(users + ": are the users");
				WebServer.handler.writeToUser(thisUser, users);

			} else if (splitCommand[0].equalsIgnoreCase("request")) {
				// request followed by a message ID will retreive the message
				System.out.println("Request is made for ID" + splitCommand[1]);
				String message = WebServer.handler.retrieve(Integer.parseInt(splitCommand[1]));
				WebServer.handler.writeToUser(thisUser, message);

			} else if (splitCommand[0].equalsIgnoreCase("exit")) {
				// Closes all streams and sockets, but does not leave groups
				is.close();
				br.close();
				WebServer.handler.exit(thisUser);
			} else if (splitCommand[0].equalsIgnoreCase("groups")) {
				// displays list of all active groups
				String groups = "1\n2\n3\n4\n5\n";
				WebServer.handler.writeToUser(thisUser, groups);

			} else if (splitCommand[0].equalsIgnoreCase("groupJoin")) {
				// joins a group given the group number
				String users = WebServer.handler.displayUsers(Integer.parseInt(splitCommand[1]));

				WebServer.handler.addUser(Integer.parseInt(splitCommand[1]), userName, this.socket);
				String lastTwo = WebServer.handler.displayLastTwoMessage(Integer.parseInt(splitCommand[1]));
				
				WebServer.handler.writeToUser(thisUser, "Users in server:\n" + users);
				WebServer.handler.writeToUser(thisUser, "Messages in server:\n" + lastTwo);

			} else if (splitCommand[0].equalsIgnoreCase("groupUsers")) {
				//displays all users in a group to the active user
				String groupUsers = WebServer.handler.displayUsers(Integer.parseInt(splitCommand[1]));
				WebServer.handler.writeToUser(thisUser, groupUsers);

			} else if (splitCommand[0].equalsIgnoreCase("groupLeave")) {
				//leaves the specified group
				WebServer.handler.leaveGroup(Integer.parseInt(splitCommand[1]), thisUser);

			} else if (splitCommand[0].equalsIgnoreCase("groupPost")) {
				// adds a message to a group
				String[] subMess = splitCommand[1].split(" ", 3);
				Date date = Calendar.getInstance().getTime();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
				String strDate = dateFormat.format(date);
				System.out.println(strDate);
				WebServer.handler.addMessage(Integer.parseInt(subMess[0]), userName, strDate, subMess[1], subMess[2]);

			} else if (splitCommand[0].equalsIgnoreCase("groupRequest")) {
				// displays the contents of a message with a given message id
				String[] subMess = splitCommand[1].split(" ", 2);
				String message = WebServer.handler.retrieve(Integer.parseInt(subMess[0]), Integer.parseInt(subMess[1]));
				WebServer.handler.writeToUser(thisUser, message);

			} else {
				// handles if user gives a command that does not exist
				WebServer.handler.writeToUser(thisUser, "This is not a command!");
			}
		}
	}
}

