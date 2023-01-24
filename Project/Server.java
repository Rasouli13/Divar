package Project;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Server {
    public static void main(String args[]) {
        Server server = new Server(443);
    }
    private List<clientAccepter> clients = new ArrayList<>();
    private ServerSocket server = null;

    public Server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");
            while (true) {
                clientAccepter cl =  new clientAccepter(server.accept(),this);
                clients.add(cl);
                cl.start();
            }
        } catch(IOException e) {
            System.out.println(e);
        }
    }
    public void sendMessage(String message,int Senderid){
        for (clientAccepter clientAccepter : clients) {
            if (clientAccepter.getClientId()!=Senderid) {
                clientAccepter.sendMessage(Senderid+" said: "+message);
            }
        }
    }
    public void deleteClient(clientAccepter client){
        clients.remove(client);
    }
}

public class clientAccepter extends Thread{
    private static int lastId= 0;
    private int id;
    private Socket socket = null;
    private DataInputStream in	 = null;
    private static DataOutputStream out = null;
    private Server server;
    clientAccepter(Socket socket,Server server){
        this.socket = socket;
        id = lastId++;
        this.server = server;
        try {
            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        boolean Cancel = false;
        while (!Cancel) {
            sendMessage("1.Creat Account\n2.Sign In\n\nChoose a number:");
            String menu  = getMessage();
            switch (menu){
                case "1":
                    this.sendMessage("press Enter to cancel..." +
                            "\n\tEnter a Username:");
                    String username = getMessage();
                    if (username.equals("")) {
                        Cancel =true;
                        break;
                    }
                    sendMessage("\npress Enter to cancel..." +
                            "\n\tEnter a Password:");
                    String password = getMessage();
                    if (password.equals("")) {
                        Cancel =true;
                        break;
                    }
                    sendMessage("press Enter to cancel..." +
                            "\n\tEnter a Email:");
                    String email = getMessage();
                    if (email.equals("")) {
                        Cancel =true;
                        break;
                    }
                    creatAccount ca = new creatAccount(username,password,email);
                case "2":
                    signIn si = new signIn();
            }
            try {
                menu = in.readUTF();
                System.out.println("Client "+id+" said: ");
                System.out.println(menu);
                server.sendMessage(menu, id);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        server.deleteClient(this);
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String message){
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage(){
        try {
            return in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return " ";
    }
    public int getClientId(){
        return id;
    }

}