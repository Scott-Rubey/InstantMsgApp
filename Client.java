import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    protected static Socket clientSocket = null;
    protected static Scanner scanner = new Scanner(System.in);
    protected static String username = null;
    protected static DataInputStream inputStream = null;
    protected static DataOutputStream outputStream = null;

    public static void main(String[] args) {
        try {
            System.out.print("Please enter your username: ");
            username = scanner.next();
            System.out.print("\n");

            //connect to server
            boolean connected = connect("127.0.0.1", 6789);
            if(connected) {
                System.out.println("Connected to server");

                // obtaining input and out streams
                inputStream = new DataInputStream(clientSocket.getInputStream());
                outputStream = new DataOutputStream(clientSocket.getOutputStream());

                boolean disc = false;  //user does not presently wish to disconnect
                int choice = Menu.topMenu();

                //while user wishes to stay connected
                while (!disc) {
                    String command = null;

                    //if user opts to list existing rooms
                    if (1 == choice) {
                        command = username + " LIST";
                    }
                    //if user opts to join an existing room
                    else if (2 == choice) {
                        System.out.println("Please enter the name of the room you would like to join: ");
                        String roomName = scanner.next();

                        command = username + " JOIN " + roomName;
                    }
                    //if user opts to create a new room
                    else if (3 == choice) {
                        System.out.print("Please enter the name of the room you would like to create (no spaces allowed): ");
                        String newRoomName = scanner.next();

                        command = username + " CTRM " + newRoomName;
                    }
                    //if user opts to list all users in a chat room
                    else if (4 == choice) {
                        System.out.print("Please enter the name of the room whose members you would like to list: ");
                        String roomName = scanner.next();

                        command = username + " LSMB " + roomName;
                    }
                    //if user opts to post a msgText to a chat room
                    else if (5 == choice) {
                        System.out.print("Please enter the name of the room you would like to post to: ");
                        String roomName = scanner.next();
                        scanner.nextLine();

                        System.out.print("Please enter your message: ");
                        String message = scanner.nextLine();

                        command = username + " POST " + roomName + " " + message;
                    }
                    //if user opts to retrieve all messages posted to a chat room
                    else if (6 == choice) {
                        System.out.print("Please enter the name of the room whose messages you would like shown: ");
                        String roomName = scanner.next();

                        command = username + " RETV " + roomName;
                    }
                    //if user opts to leave a chat room
                    else if (7 == choice) {
                        System.out.print("Please enter the name of the room you would like to leave: ");
                        String roomName = scanner.next();

                        command = username + " LEAV " + roomName;
                    }
                    //if user opts to disconnect from server
                    else if (8 == choice) {
                        command = username + " DSCT";
                        disc = true;
                    }

                    //send command to server
                    outputStream.writeUTF(command);

                    //if the user hasn't disconnected, read the return message
                    if (!disc) {
                        String retMsg = inputStream.readUTF();
                        System.out.println(msgHandler(retMsg));

                        choice = Menu.topMenu();
                    }
                }
            }

            //disconnect from server
            boolean disconnected = disconnect();
            if(disconnected)
                System.out.println("Disconnected from server\n");
            else
                System.out.println("Error disconnecting from server");
        }catch(Exception e){
            //print user friendly message and close all open resources
            System.out.println("Server has crashed. Please try again later.");
            disconnect();
        }
    }

    //connect to the server
    protected static boolean connect(String address, int port) {
        boolean success = false;

        try {
            clientSocket = new Socket(address, port);
            success = true;
        } catch (IOException i) {
            System.out.println("Error connecting to server");
        }

        return success;
    }

    //disconnect from the server
    protected static boolean disconnect(){
        boolean success = false;

        try {
            clientSocket.close();
            inputStream.close();
            outputStream.close();
            scanner.close();
            success = true;
        }
        catch(IOException i) {
            System.out.println(i);
        }

        return success;
    }

    //handle messages returned from server
    protected static String msgHandler(String retMsg){
        String toPrint;

        //return user-friendly msgText derived from server response.
        //if server returns info other than OK or ERR msgs, just return that info (i.e. lists, chat messages, etc)
        switch(retMsg){
            case "OK_CTRM":
                toPrint = "\nRoom Created\n";
                break;
            case "OK_JOIN":
                toPrint = "\nRoom joined\n";
                break;
            case "OK_LEAV":
                toPrint = "\nRoom left\n";
                break;
            case "OK_POST":
                toPrint = "\nMessage posted\n";
                break;
            case "ERR_ALREADYJOINED":
                toPrint = "\nYou are already a member of that room.\n";
                break;
            case "ERR_DUPLICATEROOM":
                toPrint = "\nA room by that name already exists.  Please choose a different name.\n";
                break;
            case "ERR_NOROOMS":
                toPrint = "\nThere are currently no rooms available to list\n";
                break;
            case "ERR_NONEXISTENTROOM":
                toPrint = "\nRoom does not exist\n";
                break;
            case "ERR_NOTINROOM":
                toPrint = "\nYou are not currently in that room\n";
                break;
            case "ERR_ROOMEMPTY":
                toPrint = "\nRoom is empty\n";
                break;
            case "ERR_ILLEGALCOMMAND":
                toPrint = "\nClient error: illegal command.  Please contact the developer.\n";
                break;
            case "ERR_NOMESSAGES":
                toPrint = "\nNo messages have been posted to this room\n";
                break;
            default:
                toPrint = "\n" + retMsg;
        }

        return toPrint;
    }
}