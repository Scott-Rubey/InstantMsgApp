import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    protected static Socket clientSocket = null;
    protected static DataInputStream input = null;
    protected static ObjectOutputStream out = null;
    protected static Scanner in = new Scanner(System.in);
    protected static String username = null;

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

        while (4 != choice){
            String command = null;

            //if user opts to list existing rooms
            if (1 == choice){
                command = username + " LIST";
            }
            //if user opts to join an existing room
            else if (2 == choice){
                System.out.print("You chose 2");
            }
            //if user opts to create a new room
            else if (3 == choice) {
                System.out.print("Please enter the name of the room you would like to create: ");
                String newRoomName = in.next();
                System.out.print("\n");

                command = username + " CTRM " + newRoomName;
            }

            boolean success = client.sendCommand(command);

            choice = Menu.topMenu();
        }

        //disconnect from server
        boolean disconnected = client.disconnect();
        if(disconnected)
            System.out.println("Disconnected from server");
        else
            System.out.println("Error disconnecting from server");
    }

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

    protected boolean sendCommand(String command){
        boolean success = false;

        try {
            //send command to server
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(command);
            success = true;
        }catch(IOException io){
            System.out.print("Server communcation error");
        }

        //TODO: return boolean from server
        return success;
    }

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