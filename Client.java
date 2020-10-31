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

        while (4 != choice){
            String command = null;

            //if user opts to list existing rooms
            if (1 == choice){
                command = username + " LIST";
            }
            //if user opts to join an existing room
            else if (2 == choice){
                System.out.println("Please enter the number of the room you would like to join: ");
                int roomNum = in.nextInt();
                in.nextLine();

                command = username + " JOIN " + roomNum;
            }
            //if user opts to create a new room
            else if (3 == choice) {
                System.out.print("Please enter the name of the room you would like to create: ");
                String newRoomName = in.next();
                System.out.print("\n");

                command = username + " CTRM " + newRoomName;
            }

            //send command to the server
            String retMsg = client.sendCommand(command);

            //print the returned message from the server
            System.out.print(retMsg);

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

//TODO: return message handler: switch stmt containing server responses that returns client output