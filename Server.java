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
//                DataInputStream in = null;
                ObjectInputStream inputStream = null;
                ObjectOutputStream out = null;

                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("Connected to client");

                String code = "";
                Object obj;

                while(!code.equals("DSCT")) {
                    try {
                        /*in = new DataInputStream(
                                new BufferedInputStream(connectionSocket.getInputStream()));  */

                        out = new ObjectOutputStream(connectionSocket.getOutputStream());
                        inputStream = new ObjectInputStream(connectionSocket.getInputStream());

                        obj = inputStream.readObject();
                        String objStr = obj.toString();
                        Command command = parseCommand(objStr);
                        code = command.getCode();

                        //TODO: compare user to list, add if not already in the DB

                        //only perform commands if not "disconnect" code
                        if(!code.equals("DSCT")){
                            String retMsg = exec(command);
                            out.writeObject(retMsg);
                        }
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
//                in.close();
            } catch (IOException i) {
                //System.out.println(i);
            }
        }
    }

    protected Command parseCommand(String objStr){
        //split message from client into individual words
        //first two are prefix and code, then rejoin the rest to create message
        String[] split = objStr.split(" ");
        String prefix = split[0];
        String code = split[1];
        String message = "";

        //only add message if it exists
        if(split.length > 2) {
            for (int i = 2; i < split.length; ++i)
                message += split[i];
        }

        Command command = new Command(prefix, code, message);

        return command;
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
        }

        return retMsg;
    }

    protected String createRoom(Command command){
        String name = command.getMessage();
        String message = "";
        Room room = new Room(name);

        //TODO: search rooms to make sure this isn't a duplicate; rtn false and notify client if duplicate

        rooms.addRoom(room);

        message = "Room created";

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
}