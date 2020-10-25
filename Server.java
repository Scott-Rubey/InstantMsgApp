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
        //first two are prefix and code, then rejoin the rest to create message
        String[] split = objStr.split(" ");
        String name = split[0];
        String code = split[1];
        String message = "";

        //only add message if it exists
        if(split.length > 2) {
            for (int i = 2; i < split.length; ++i)
                message += split[i];
        }

        User user = addUser(name);
        Command command = new Command(user, code, message);

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
            case "CTRM":
                retMsg = createRoom(command);
                break;
            case "LIST":
                retMsg = listRooms();
                break;
            case "JOIN":
                retMsg = joinRoom(command);
                break;
        }

        return retMsg;
    }

    protected String createRoom(Command command){
        String name = command.getMessage();
        String message = "";
        Room room = new Room(name);
        boolean found = false;

        for(int i = 0; i < rooms.getRooms().size(); ++i){
            if(name.equals(rooms.getRooms().get(i).getName()))
                found = true;
        }

        if(found)
            message = "A room by that name already exists.  Please choose a different name.\n";
        else {
            rooms.addRoom(room);
            message = "Room created.\n";
        }

        return message;
    }

    protected String listRooms(){
        String message = "";

        //return message reflects either the list of available rooms, or that there are none to list
        if(rooms.getRooms().size() > 0) {
            for (int i = 0; i < rooms.getRooms().size(); ++i)
                message += i+1 + ". " + rooms.getRooms().get(i).getName() + "\n";
        }
        else
            message = "There are currently no rooms available to list\n";

        return message;
    }

    protected String joinRoom(Command command){
        String message = "";
        User user = command.getUser();

        //parse the room index from the command
        int i = Integer.parseInt(command.getMessage());
        int roomNbr = i-1;

        //find the room
        if(i < 1 || i > rooms.getRooms().size())
            message = "Room index out of range.\n";
        else {
            Room room = rooms.getRooms().get(roomNbr);
            room.addUser(user);
            message = "You have been added to " + room.name + "\n";
        }

        return message;
    }

    protected String listUsers(Command command){
        String message = "";
        //code got a little funky -- here's a start

/*        for(int j = 0; j < room.getUsers().size(); ++j){
            System.out.print(room.users.get(j).name);
        }          */

        return message;
    }
}