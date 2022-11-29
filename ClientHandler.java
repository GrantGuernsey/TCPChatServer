import java.util.*;
import java.io.*;
import java.net.*;

public class ClientHandler {
	final static String CRLF = "\r\n";
	ArrayList<ArrayList<UserMessage>> groupChat = new ArrayList<>(5);
	ArrayList<ArrayList<User>> userList = new ArrayList<>(5);
	ArrayList<User> users = new ArrayList<User>();
	ArrayList<UserMessage> globalChat = new ArrayList<UserMessage>();
	int currentId = 0;

	public void createList() {
		//instantiates a blank 2d array list for the user list and for each group chats messages
		for (int i = 0; i < 5; i++) {
			userList.add(new ArrayList<User>());
			groupChat.add(new ArrayList<UserMessage>());
		}
	}

	public User addUser(String name, Socket s) {
		// adds a user to the global user list
		User tempUser = new User(s, name);
		users.add(tempUser);
		writeToUsers(users, name + " has joined the global server!", name);
		return tempUser;
	}

	public User addUser(int group, String name, Socket s) {
		// adds a user to a specified group
		User tempUser = new User(s, name);
		userList.get(group-1).add(tempUser);
		writeToUsers(userList.get(group-1), name + " has joined group " +group +"!", name);
		return tempUser;
	}

	public void addMessage(String uName, String date, String sub, String mess) {
		// adds a message to the global chat
		UserMessage tempMessage = new UserMessage(currentId, uName, date, sub, mess);
		currentId = currentId + 1;
		globalChat.add(tempMessage);
		writeToUsers(users, tempMessage.sendMessage(), uName);
	}
	
	public void addMessage(int group, String uName, String date, String sub, String mess) {
		// adds a message to a specified group chat
		UserMessage tempMessage = new UserMessage(currentId, uName, date, sub, mess);
		System.out.println(tempMessage.sendMessage());
		currentId = currentId + 1;
		groupChat.get(group-1).add(tempMessage);
		writeToUsers(userList.get(group-1), tempMessage.sendMessage(), uName);
		// Display it to all members of the group  
	}
	public void leaveGroup(int group, User u){
		// removes user from specified group user list
		for(int lcv = 0; lcv < userList.get(group-1).size(); lcv++){
			if(userList.get(group-1).get(lcv).username == u.username){
				userList.get(group-1).remove(lcv);
			}
		}
	}
	public void exit(User u){
		//closes socket and stream for user, but does not leave the groups the user is in
		try{
			u.os.close();
			u.connection.close();
		}
		catch(Exception e){

		}
		
	}
	public String displayUsers() {
		// displays global user list
		String userString = "";
		for(int x = 0; x < users.size(); x++)
		{
			userString = userString + users.get(x).username + "\n";
		}
		return userString;
	}

	public String displayUsers(int group) {
		// displays user list of specified group
		String userString = "";
		for(int lcv = 0; lcv < userList.get(group-1).size();lcv++){
			userString += userList.get(group-1).get(lcv).username + "\n";
		}
		return userString;
	}

	public String displayLastTwoMessage(){       
		// displays the 2 most recent messages in global chat     
		String retString = "";
		if(globalChat.size() == 0){ 
			return "";
		}
		if(globalChat.size()==1){
			return globalChat.get(0).sendMessage();
		}
		for (int i = globalChat.size() - 2; i < globalChat.size(); i++) {
			retString = retString + globalChat.get(i).sendMessage() + "\n";
		}
		return retString;
	}

	public String displayLastTwoMessage(int group) {
		// displays last two messages of a specified group chat
		String retString = "";
		// returns null if no messages have been sent
		if(groupChat.get(group-1).size() == 0){
			return "";
		}
		// returns one message if only one message has been sent
		if(groupChat.get(group-1).size()==1){
			return groupChat.get(group-1).get(0).sendMessage();
		}
		// returns the two most recent messages
		for (int i = groupChat.get(group-1).size() - 2; i < groupChat.get(group-1).size(); i++) {
			retString = retString + groupChat.get(group-1).get(i).sendMessage() + "\n";
		}
		return retString;
	}

	public String retrieve(int id){
		// retrieves a message given a message id in global chat
		for(int i = 0; i < globalChat.size(); i++)
		{
			if(globalChat.get(i).messageID == id){
				return globalChat.get(i).message;
			}
		}
		return "Message not found";
	}
  
	public String retrieve(int group, int id){
		// retrieves a specified group's message given a message id and group number
	  for(int i = 0; i < groupChat.get(group-1).size();i++){
			if(groupChat.get(group-1).get(i).messageID == id){
				return groupChat.get(group-1).get(i).message;
			}
		}
		return "Message not found";
  }
	
	public void writeToUsers(ArrayList<User> userList, String s, String uName){
		// writes string s to all users passed in through userList
		for(int lcv = 0; lcv < userList.size(); lcv++){
			if(userList.get(lcv).username != uName){  	
				try{
					writeToUser(userList.get(lcv), s);
				}
				catch(Exception e){}
			}
		}
		
  }

    public void writeToUser(User currUser, String msg){
		// writes msg to one specific user
		try{
			currUser.os.writeBytes(msg + CRLF);
		}
		catch(Exception e){}

	}

	public class User{
		//class to contain user information
		public Socket connection;
		public String username;
		public DataOutputStream os; 
		//constructor
		public User(Socket s, String user) {
			try{
				this.connection = s;
				this.username = user;
				os = new DataOutputStream(this.connection.getOutputStream());
			}
			catch(Exception e){}
		}
		//blank constructor
		public User() {
			this.username = "";
		}
	}

	public class UserMessage {
		//contains all fields related to a user message
		int messageID;
		String sender;
		String postDate;
		String subject;
		String message;
		//blank constructor
		public UserMessage() {

		}
		//constructor
		public UserMessage(int id, String send, String date, String sub, String mess) {
			messageID = id;
			sender = send;
			postDate = date;
			subject = sub;
			message = mess;
		}
		//returns string containing message contents separated by "::"
		public String sendMessage() {
			String retString = messageID + "::" + sender + "::" + postDate + "::" + subject;
			return retString;
		}

	}

}