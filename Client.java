import java.net.*;
import java.io.*;

public class Client {
    Socket clientSocket = null;
    DataInputStream input = null;
    ObjectOutputStream out = null;

    public static void main(String argv[]){
        Client client = new Client();

        //connect to server
        client.connect("127.0.0.1", 6789);

        int choice = Menu.topMenu();
    }

    public void connect(String address, int port) {
        try {
            clientSocket = new Socket(address, port);
            System.out.println("Connected to server");
        } catch (IOException i) {
            System.out.println(i);
        }
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
}