import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    Socket clientSocket = null;
    DataInputStream input = null;
    ObjectOutputStream out = null;

    public static void main(String argv[]){
        Client client = new Client();

        //connect to server
        boolean connected = client.connect("127.0.0.1", 6789);
        if(connected)
            System.out.println("Connected to server");
        else
            System.out.println("Error connecting to server");

        int choice = Menu.topMenu();

        while (4 != choice){
            if (1 == choice){
                System.out.print("You chose 1");
            }
            else if (2 == choice){
                System.out.print("You chose 2");
            }
            else if (3 == choice) {
                System.out.print("Your chose 3");
            }

            choice = Menu.topMenu();
        }

        //disconnect from server
        boolean disconnected = client.disconnect();
        if(disconnected)
            System.out.println("Disconnected from server");
        else
            System.out.println("Error disconnecting from server");
    }

    public boolean connect(String address, int port) {
        boolean success = false;

        try {
            clientSocket = new Socket(address, port);
            success = true;
        } catch (IOException i) {
            System.out.println(i);
        }

        return success;
    }

    public boolean disconnect(){
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

    protected void communicate(){
        // takes input from terminal
        input = new DataInputStream(System.in);

        try {
            // sends output to the socket
            out = new ObjectOutputStream(clientSocket.getOutputStream());
        }catch(IOException io){
            System.out.print(io);
        }

        // string to read message from input
        String line = "";

        // keep reading until "Over" is input
        while (!line.equals("Over")) {
            try {
                line = input.readLine();
                CRIMP crimp = new CRIMP();
                String message = crimp.getPrefix() + " " + crimp.getCommand();
                out.writeObject(message);
                out.writeUTF(line);
            }
            catch(IOException i) {
                System.out.println(i);
            }
        }
    }
}