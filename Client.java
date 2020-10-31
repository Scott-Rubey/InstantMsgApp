import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    protected static Socket clientSocket = null;
    protected static ObjectOutputStream out = null;
    protected static Scanner in = new Scanner(System.in);
    protected static String username = null;
    protected static ObjectInputStream inputStream = null;

    public static void main(String argv[]){
        Client client = new Client();

        System.out.print("Please enter your username: ");
        username = in.next();
        System.out.print("\n");

        //connect to server
        boolean connected = client.connect("127.0.0.1", 6789);
        if(connected)
            System.out.println("Connected to server");
        else
            System.out.println("Error connecting to server");

        int choice = Menu.topMenu();

        while (8 != choice){
            String command = null;

            //if user opts to list existing rooms
            if (1 == choice){
                command = username + " LIST";
            }
            //if user opts to join an existing room
            else if (2 == choice){
                System.out.println("Please enter the name of the room you would like to join: ");
                String roomName = in.next();

                command = username + " JOIN " + roomName;
            }
            //if user opts to create a new room
            else if (3 == choice) {
                System.out.print("Please enter the name of the room you would like to create: ");
                String newRoomName = in.next();
                System.out.print("\n");

                command = username + " CTRM " + newRoomName;
            }
            //if user opts to list all users in a chat room
            else if (4 == choice){
                System.out.print("LSRM not yet implemented\n");
            }
            //if user opts to post a message to a chat room
            else if (5 == choice){
                System.out.print("POST not yet implemented\n");
            }
            //if user opts to retrieve all messages posted to a chat room
            else if (6 == choice){
                System.out.print("RETV not yet implemented\n");
            }
            //if user opts to leave a chat room
            else if (7 == choice){
                System.out.print("LEAV not yet implemented\n");
            }

            //send command to the server, capture the server's return message
            String retMsg = client.sendCommand(command);

            //send return message to handler, print result
            System.out.print(msgHandler(retMsg));

            //return to menu
            choice = Menu.topMenu();
        }

        //disconnect from server
        boolean disconnected = client.disconnect();
        if(disconnected)
            System.out.println("Disconnected from server");
        else
            System.out.println("Error disconnecting from server");
    }

    //connect to the server
    protected boolean connect(String address, int port) {
        boolean success = false;

        try {
            clientSocket = new Socket(address, port);
            success = true;
        } catch (IOException i) {
            System.out.println(i);
        }

        return success;
    }

    //send command to the server in the form of a string object
    protected String sendCommand(String command){
        String message = "";

        try {
            //send command to server
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(command);

            //read message returned from server
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            Object obj = inputStream.readObject();
            message = obj.toString();
        }catch(IOException io){
            message = "Server communcation error";
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return message;
    }

    //handle messages returned from server
    protected static String msgHandler(String retMsg){
        String toPrint;

        //return user-friendly message derived from server response.
        //if server returns info other than OK or ERR msgs, just return that info (i.e. lists, chat messages, etc)
        switch(retMsg){
            case "OK_CTRM":
                toPrint = "Room Created\n";
                break;
            case "OK_JOIN":
                toPrint = "Room joined\n";
                break;
            case "ERR_DUPLICATEROOM":
                toPrint = "A room by that name already exists.  Please choose a different name.\n";
                break;
            case "ERR_NOROOMS":
                toPrint = "There are currently no rooms available to list\n";
                break;
            case "ERR_NONEXISTENTROOM":
                toPrint = "Room does not exist\n";
                break;
            default:
                toPrint = retMsg;
        }

        return toPrint;
    }

    //disconnect from the server
    protected boolean disconnect(){
        boolean success = false;

        try {
            //input.close();
            //out.close();
            clientSocket.close();
            success = true;
        }
        catch(IOException i) {
            System.out.println(i);
        }

        return success;
    }
}
