import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    Socket socket;
    DataInputStream inStream;
    DataOutputStream outStream;

    protected ClientHandler(Socket socket, DataInputStream in, DataOutputStream out){
        this.socket = socket;
        this.inStream = in;
        this.outStream = out;
    }

    public void run(){
        boolean dsct = false;  //represents client's wish to disconnect
        boolean crashed = false;
        String message;
        String code = "";


        while(!dsct && !crashed){
            try {
                message = inStream.readUTF();
                Command command = parseCommand(message);
                code = command.getCode();

                //if command code is "DSCT", close the socket
                if (code.equals("DSCT")){
                    rmvUsrAllRooms(command.getUser());
                    System.out.println("\nClosing connection " + this.socket);
                    this.socket.close();
                    dsct = true;
                }
                //otherwise, perform command
                else{
                    String retMsg = exec(command);
                    outStream.writeUTF(retMsg);
                }

            } catch (IOException i) {
                crashed = true;
            }
        }

        //print readable error if client unexpectedly disconnects
        if(crashed)
            System.out.println("Client " + this.socket + " disconnected\n");

        //close the input and output streams
        try {
            inStream.close();
            outStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    protected Command parseCommand(String msg){
        //split msgText from client into individual words
        //the first two are username and command code
        //the third, if present, is roomname
        //then rejoin anything remaining to create msgText from user

        String[] split = msg.split(" ");
        String name = split[0];
        String code = split[1];
        String roomName = "";
        String message = "";

        //if third arg present, it's the roomName
        if(split.length > 2)
            roomName = split[2];

        //anything else in the array is the msgText from the user
        if(split.length > 3) {
            for (int i = 3; i < split.length; ++i)
                message += split[i] + " ";
        }

        //create User to associate with new Command
        //if user already present, associate with that user.  If not, create new one.
        User user = null;
        boolean found = false;
        UserList users = Server.users;
        for(User u : users.getUsers()){
            if(!found && name.equals(u.getName())) {
                user = u;
                found = true;
            }
        }
        if(!found) {
            user = new User(name);
            Server.users.addUser(user);
        }

        //create new command object
        Command command = new Command(user, code, roomName, message);

        return command;
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
            /* list members of a chat room */
            case "LSMB":
                retMsg = listMembers(command);
                break;
            /* post msgText to chat room */
            case "POST":
                retMsg = postToRoom(command);
                break;
            /* retrieve all messages from a chat room */
            case "RETV":
                retMsg = retrieveMessages(command);
                break;
            default:
                retMsg = "ERR_ILLEGALCOMMAND";
        }

        return retMsg;
    }

    //create a chatroom
    protected String createRoom(Command command){
        RoomList rooms = Server.rooms;
        String roomName = command.getRoom();
        String retMsg = "";
        boolean found = false;

        //search to see if roomname already exists
        for(int i = 0; i < rooms.getRooms().size(); ++i){
            if(roomName.equals(rooms.getRooms().get(i).getName()))
                found = true;
        }

        //if roomname already exists, return error
        if(found)
            retMsg = "ERR_DUPLICATEROOM";
            //otherwise, create room, add to rooms list, and return OK
        else {
            Room room = new Room(roomName);
            rooms.addRoom(room);
            retMsg = "OK_CTRM";
        }

        return retMsg;
    }

    //list all chat rooms
    protected String listRooms(){
        RoomList rooms = Server.rooms;
        String retMsg = "";

        //return msgText reflects either the list of available rooms, or that there are none to list
        if(rooms.getRooms().size() > 0) {
            for (int i = 0; i < rooms.getRooms().size(); ++i)
                retMsg += i+1 + ". " + rooms.getRooms().get(i).getName() + "\n";
        }
        else {
            retMsg = "ERR_NOROOMS";
        }

        return retMsg;
    }

    //join a chat room
    protected String joinRoom(Command command){
        RoomList rooms = Server.rooms;
        String retMsg = "";
        User user = command.getUser();
        String roomName = command.getRoom();

        //verify room is on list; add user to room if found
        Room room = rooms.findRoom(roomName);

        //if room exists
        if(room != null){
            //...and user is not in the room, add user to room
            if(!room.getUsers().contains(user)) {
                room.addUser(user);
                retMsg = "OK_JOIN";
            }
            //...if user is already in the room, send message stating so
            else
                retMsg = "ERR_ALREADYJOINED";
        }
        //if room does not exist
        else
            retMsg = "ERR_NONEXISTENTROOM";

        return retMsg;
    }

    //list all members in a chat room
    protected String listMembers(Command command){
        String retMsg = "";
        User user = command.getUser();
        String roomName = command.getRoom();

        Room room = Server.rooms.findRoom(roomName);

        //if room exists but is empty, return error
        if(room != null && room.getUsers().size() == 0)
            retMsg = "ERR_ROOMEMPTY";

            //if room exists and has members, create a list
        else if(room != null && room.getUsers().size() > 0){
            for(int i = 0; i < room.getUsers().size(); ++i)
                retMsg += room.users.get(i).name + "\n";
        }

        //if room exists but is empty, return error
        else
            retMsg = "ERR_NONEXISTENTROOM";

        return retMsg;
    }

    //leave a chat room
    protected String leaveRoom(Command command){
        RoomList rooms = Server.rooms;
        String retMsg = "";
        User user = command.getUser();
        String roomName = command.getRoom();

        //if room exists, remove user from userlist
        Room room = rooms.findRoom(roomName);
        if(room != null){
            boolean userInRoom = room.findUser(user);

            //if user is found in room, remove them and return OK
            if(userInRoom){
                room.removeUser(user);
                retMsg = "OK_LEAV";
            }
            //if room exists but user is not on room's userlist, return err
            else
                retMsg = "ERR_NOTINROOM";
        }
        //if room does not exist, return error
        else
            retMsg = "ERR_NONEXISTENTROOM";

        return retMsg;
    }

    //post msgText to chat room
    protected String postToRoom(Command command) {
        RoomList rooms = Server.rooms;
        String retMsg = "";
        User user = command.getUser();
        String roomName = command.getRoom();

        //find room, if it exists
        Room room = rooms.findRoom(roomName);

        //if room exists...
        if(room != null){
            boolean userInRoom = room.findUser(user);

            //...and user is a member of the room, add msgText to room, return OK
            if(userInRoom){
                Message message = new Message(command.getUser().getName(), command.getMessage());
                room.addMessage(message);
                retMsg = "OK_POST";
            }
            //if user is not in room, do not create msgText object, return error
            else
                retMsg = "ERR_NOTINROOM";
        }
        //if room does not exist, return error
        else
            retMsg = "ERR_NONEXISTENTROOM";

        return retMsg;
    }

    //retrieve all messages from a chat room
    protected String retrieveMessages(Command command){
        RoomList rooms = Server.rooms;
        String retMsg = "";
        User user = command.getUser();
        String roomName = command.getRoom();

        //find room, if it exists
        Room room = rooms.findRoom(roomName);

        //if room exists...
        if(room != null) {
            boolean userInRoom = room.findUser(user);

            //...and user is a member of the room but there are no messages, return error
            if (userInRoom && room.getMessages().size() == 0)
                retMsg = "ERR_NOMESSAGES";

                //...if user is a member of the room and there are messages, return them
            else if(userInRoom && room.getMessages().size() > 0){
                ArrayList<Message> messages = room.getMessages();

                //heading
                retMsg += "\nMessages posted to " + roomName + ":\n\n";
                for(Message msg:messages)
                    retMsg += msg.toString() + "\n";
            }
            //if user is not in room, return error
            else
                retMsg = "ERR_NOTINROOM";
        }
        //if room does not exist, return error
        else
            retMsg = "ERR_NONEXISTENTROOM";

        return retMsg;
    }

    protected void rmvUsrAllRooms(User user){
        RoomList rooms = Server.rooms;

        //search all rooms for user.  If user in room, remove.
        for(int i = 0; i < rooms.getRooms().size(); ++i){
            if(rooms.getRooms().get(i).getUsers().contains(user)){
                rooms.getRooms().get(i).removeUser(user);
            }
        }
    }
}