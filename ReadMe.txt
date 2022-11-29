Grant Guernsey
Preston Savey

Steps to run the code:
1. Have all three files in the same directory: ClientHandler.Java WebClient.java WebServer.java
2. Run the WebServer.java file
3. Open and launch as many WebClient.java files as wanted
4. The commands that are implemented are printed with what to pass into them

All of the commands and the functions are commented and explained in the java source code, but for ease of reading, commands are explained below

The commands are each explained below:
join: Can be ran after the client has exited the server
post: Post a message to the global chat (group 1)
users: Prints all of the users connected to the server
request: how a message is requested from the global server
exit: the user is exited and all of the sockets and streams are closed
groups: prints the groups
groupJoin: join a group 
groupUsers: shows the users for a specific group
groupLeave: leaves a group
groupPost: posts to a certain group
groupRequest: retrieves a message from a certain group

Major Issues
Threading: We had never worked with threading in java so figuring out how to create a globally used client handler was difficult
Client: We had difficulty implementing the client because we weren't spinning off another thread to read the messages and instead was getting it in sequence with the message sending
Task 2: Creating the structure to hold all of the messages was confusing. Initially we were going to use a hash map but instead we went with an array list of array lists, this allows us to easily add more groups if we wanted and send as many messages as we wanted
Working with java: Neither of us had developed in java to this extent since high school so getting used to how java worked was a learning curve. A lot of google searches were used to figure out syntactical issues
Testing: We had a major issue with testing our code because of the manner we chose to write it. We implemented the client handler before the web server or the client which meant that we had to fully implement the client and server before we could test any of the code
