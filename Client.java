import java.net.*;
import java.io.*;

public class Client {
    public void run(String address, int port) {
        Socket clientSocket = null;
        DataInputStream input = null;
        ObjectOutputStream out = null;

        //connect to server
        try {
            clientSocket = new Socket(address, port);
            System.out.println("Connected to server");

            // takes input from terminal
            input = new DataInputStream(System.in);

            // sends output to the socket
            out = new ObjectOutputStream(clientSocket.getOutputStream());
        }
        catch(IOException i) {
            System.out.println(i);
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

        // close the connection
        try {
            input.close();
            out.close();
            clientSocket.close();
        }
        catch(IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String argv[]){
        Client client = new Client();
        client.run("127.0.0.1", 6789);
    }
}