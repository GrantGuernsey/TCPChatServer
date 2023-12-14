# Web Server and Client Application
##Overview
This documentation provides a guide on running the web server and client application developed by Grant Guernsey and Preston Savey. The application enables communication between clients through various commands executed in the client-side terminal.

## Steps to Run the Code
File Setup:
Ensure all three files (ClientHandler.java, WebClient.java, WebServer.java) are in the same directory.

Run the Web Server:
Execute the WebServer.java file. This initializes the server and prepares it for incoming client connections.

Launch WebClient Instances:
Open and launch as many WebClient.java files as needed. Each client represents a user in the system.

Command Execution:
The implemented commands are printed with the required parameters. The details of each command are explained below.

## Implemented Commands
1. join:

Usage: Can be run after the client has exited the server.
Description: Rejoins the server after a client has exited.

2. post:

Usage: post <message>
Description: Posts a message to the global chat (group 1).

3. users:

Usage: No parameters needed.
Description: Prints all users connected to the server.

4. request:

Usage: request <message>
Description: Requests a message from the global server.

5. exit:

Usage: No parameters needed.
Description: Exits the user, closing all sockets and streams.

6. groups:

Usage: No parameters needed.
Description: Prints the available groups.

7. groupJoin:

Usage: groupJoin <groupID>
Description: Joins a specific group.

8. groupUsers:

Usage: groupUsers <groupID>
Description: Shows the users for a specific group.

9. groupLeave:

Usage: groupLeave <groupID>
Description: Leaves a specific group.

10. groupPost:

Usage: groupPost <groupID> <message>
Description: Posts a message to a certain group.

1.. groupRequest:

Usage: groupRequest <groupID> <message>
Description: Retrieves a message from a certain group.
## Additional Information
All commands and functions are commented and explained in the Java source code for detailed understanding.
Feel free to explore and interact with the server and clients using the provided commands. For any further assistance, refer to the comments in the source code or contact the developers.
