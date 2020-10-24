import java.net.*;
import java.io.*;

public class Server {
    public void run(int port){
        ServerSocket welcomeSocket;

        System.out.println("Server process initiated");
        System.out.println("Listening on port " + port);

        //start server, run persistently
        while(true) {
            try {
                welcomeSocket = new ServerSocket(port);

                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("Connected to client");

                // takes input from the client connectionSocket
                DataInputStream in = new DataInputStream(
                        new BufferedInputStream(connectionSocket.getInputStream()));

                ObjectInputStream test = new ObjectInputStream(connectionSocket.getInputStream());

                String line = "";
                String testStr = "";
                Object obj;

                // reads message from client until "Over" is sent
                while (!line.equals("Over")) {
                    try {
                        obj = test.readObject();
                        testStr = obj.toString();
                        System.out.println(testStr);
                        line = in.readUTF();
                        System.out.println(line);

                    } catch (IOException i) {
                        System.out.println(i);
                    } catch (ClassNotFoundException c){
                        System.out.println(c);
                    }
                }
                System.out.println("Closing connection");

                // close connection
                connectionSocket.close();
                in.close();
            } catch (IOException i) {
                System.out.println(i);
            }
        }
    }

    public static void main(String args[]) {
        Server server = new Server();
        server.run(6789);
    }
}