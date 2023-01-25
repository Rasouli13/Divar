package Project;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;

public class Server {
    public static void main(String args[]) {
        Server server = new Server(443);
    }
    private static List<clientManager> clients = new ArrayList<clientManager>();
    private ServerSocket server = null;

    public Server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started...");
            while (true) {
                clientManager client =  new clientManager(server.accept(),this);
                clients.add(client);
                client.start();
            }
        } catch(IOException e) {
            System.out.println(e);
        }
    }
    public static boolean containUser(String username, String password){
        for (clientManager client : clients) {
            if (client.getUsername().equals(username)) {
                if (client.getPassword().equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }
    public void deleteClient(clientManager client){
        clients.remove(clients.remove(client));
    }
}

