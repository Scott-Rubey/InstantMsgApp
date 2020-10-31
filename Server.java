import java.net.*;
import java.io.*;

public class Server {
    RoomList rooms;
    UserList users;

    protected Server(){
        rooms = new RoomList();
        users = new UserList();
    }

    public static void main(String args[]) {
        Server server = new Server();
        server.run(6789);
    }

    public void run(int port){
        ServerSocket welcomeSocket;
        RoomList rooms = new RoomList();
        UserList users = new UserList();

        System.out.println("Server process initiated");
        System.out.println("Listening on port " + port);

        //start server, run persistently
        while(true) {
            try {
                welcomeSocket = new ServerSocket(port);
                ObjectInputStream inputStream = null;
                ObjectOutputStream out = null;

                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("Connected to client");

                String code = "";
                Object obj;

                while(!code.equals("DSCT")) {
                    try {
                        out = new ObjectOutputStream(connectionSocket.getOutputStream());
                        inputStream = new ObjectInputStream(connectionSocket.getInputStream());

                        obj = inputStream.readObject();
                        String objStr = obj.toString();
                        Command command = parseCommand(objStr);
                        code = command.getCode();

                        //only perform commands if not "disconnect" code
                        if(!code.equals("DSCT")){
                            String retMsg = exec(command);
                            out.writeObject(retMsg);
                        }

                        //TODO: deal with socket exception messages
                    } catch (IOException i) {
                        System.out.println(i);
                    } catch (ClassNotFoundException c) {
                        System.out.println(c);
                    }
                }

                System.out.println("Closing connection");

                // close connection
                connectionSocket.close();
                inputStream.close();
            } catch (IOException i) {
                //System.out.println(i);
            }
        }
    }

    protected Command parseCommand(String objStr){
        //split message from client into individual words
        //the first two are username and command code
        //the third, if present, is roomname
        //then rejoin anything remaining to create message from user

        String[] split = objStr.split(" ");
        String name = split[0];
        String code = split[1];
        String roomName = "";
        String message = "";

        //if third arg present, it's the roomName
        if(split.length >2)
           roomName = split[2];

        //anything else in the array is the message from the user
        if(split.length > 3) {
            for (int i = 3; i < split.length; ++i)
                message += split[i];
        }

        User user = addUser(name);
        Command command = new Command(user, code, roomName, message);

        return command;
    }

    protected User findUser(String name){
        User user = null;
        boolean found = false;

        //find out if user currently resides in the database
        for(int i = 0; i < users.getUsers().size(); ++i){
            if(name.equals(users.getUsers().get(i).getName())) {
                user = users.getUsers().get(i);
                found = true;
            }
        }

        //if not, add user
        if(!found)
            user = addUser(name);

        return user;
    }

    protected User addUser(String name){
        User user = new User(name);
        users.addUser(user);

        return user;
    }

    protected String exec(Command command) {
        String code = command.getCode();
        String retMsg = "";

        switch(code){
            /*create room*/
            case "CTRM":
                retMsg = createRoom(command);
                break;
            /* list rooms */
            case "LIST":
                retMsg = listRooms();
                break;
            /* join room */
            case "JOIN":
                retMsg = joinRoom(command);
                break;
            /* leave room */
            case "LEAV":
                retMsg = leaveRoom(command);
                break;
        }

        return retMsg;
    }

    //create a chatroom
    protected String createRoom(Command command){
        String roomName = command.getRoom();
        String message = "";
        boolean found = false;

        //search to see if roomname already exists
        for(int i = 0; i < rooms.getRooms().size(); ++i){
            if(roomName.equals(rooms.getRooms().get(i).getName()))
                found = true;
        }

        //if roomname already exists, return error
        if(found)
            message = "ERR_DUPLICATEROOM";
        //otherwise, create room, add to rooms list, and return OK
        else {
            Room room = new Room(roomName);
            rooms.addRoom(room);
            message = "OK_CTRM";
        }

        return message;
    }

    //list all chat rooms
    protected String listRooms(){
        String message = "";

        //return message reflects either the list of available rooms, or that there are none to list
        if(rooms.getRooms().size() > 0) {
            for (int i = 0; i < rooms.getRooms().size(); ++i)
                message += i+1 + ". " + rooms.getRooms().get(i).getName() + "\n";
        }
        else {
            message = "ERR_NOROOMS";
        }

        return message;
    }

    //join a chat room
    protected String joinRoom(Command command){
        String message = "";
        User user = command.getUser();
        String roomName = command.getRoom();

        //verify room is on list; add user to room if found
        //Room room = findRoom(roomName);
        Room room = rooms.findRoom(roomName);
        if(room != null){
            room.addUser(user);
            message = "OK_JOIN";
        }
        else
            message = "ERR_NONEXISTENTROOM";

        return message;
    }

    //leave a chat room
    protected String leaveRoom(Command command){
        String message = "";
        User user = command.getUser();
        String roomName = command.getRoom();

        //if room exists, remove user from userlist
        Room room = rooms.findRoom(roomName);
        if(room != null){
            boolean userInRoom = room.findUser(user);

            //if user is found in room, remove them and return OK
            if(userInRoom){
                room.removeUser(user);
                message = "OK_LEAV";
            }
            //if room exists but user is not on room's userlist, return err
            else
                message = "ERR_NOTINROOM";
        }
        //if room does not exist, return error
        else
            message = "ERR_NONEXISTENTROOM";

        return message;
    }

    //list users in a particular room
    protected String listUsers(Command command){
        String message = "";
        //code got a little funky -- here's a start

/*        for(int j = 0; j < room.getUsers().size(); ++j){
            System.out.print(room.users.get(j).name);
        }          */

        return message;
    }
}

//TODO: when server gets a request to post a message to a room, it responds by sending all messages to every client on the userlist
//And remember, the server needs to know which room it is posting a message to.  Do I need another field in the string for this?
