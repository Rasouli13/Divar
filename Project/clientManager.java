package Project;
import Project.settings.*;
import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class clientManager extends Thread{
    private String username,password,email,fullName = "",phoneNumber="";
    private Socket socket = null;
    private static DataInputStream in = null;
    private static DataOutputStream out = null;
    private Server server;
    clientManager(Socket socket, Server server){
        this.socket = socket;
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
            switch (menu) {
                case "1": {
                    //Username
                    sendMessage("Enter a Username, or type cancel:");
                    String username = getMessage();
                    username = setUsername.setUsername(username);
                    if (username.equals("cancel")) {
                        Cancel = true;
                        break;
                    }
                    this.username = username;

                    //Password
                    sendMessage("Enter a Password, or type cancel:");
                    String password = getMessage();
                    password = setPassword.setPassword(password);
                    if (password.equals("cancel")) {
                        Cancel = true;
                        break;
                    }
                    this.password = password;

                    //Email
                    sendMessage("Enter a Email, or type cancel:");
                    String email = getMessage();
                    email = setPassword.setPassword(email);
                    if (email.equals("cancel")) {
                        Cancel = true;
                        break;
                    }
                    this.email = email;

                    new signUp(username, password, email);
                    break;
                }
                case "2": {
                    sendMessage("Enter your username, or type Cancel:");
                    String username = getMessage();
                    if(username.equals("cancel")){
                        Cancel = true;
                        break;
                    }
                    sendMessage("Enter your password, for Cancel press Enter:");
                    String password = getMessage();
                    if(password.equals("cancel")){
                        Cancel = true;
                        break;
                    }
                    boolean success = new signIn().checkUser(username,password);
                    if (success) {
                        this.username = username;
                        this.password = password;
                        initialSettings(username);
                    }
                }
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
            System.out.println(e);
        }
    }

    public static String getMessage(){
        try {
            return in.readUTF();
        } catch (IOException e) {
            System.out.println(e);
        }
        return " ";
    }

    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public String getEmail(){
        return this.email;
    }
    public String getFullName(){
        return this.fullName;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setFullName(String fullName){
        this.fullName = fullName;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    private void initialSettings(String username) {
        try {
            File usersFolder = new File("Project/users");
            if (usersFolder.exists()) {
                File[] users = usersFolder.listFiles();
                for (File file : Objects.requireNonNull(users)) {
                    if (file.getName().equals(username)) {
                        File profile = new File("Project/users/" + username + "/profile.txt");
                        Scanner scanner = new Scanner(profile);
                        scanner.nextLine();scanner.nextLine();
                        String[] email = scanner.nextLine().split(":");
                        String[] fullName = scanner.nextLine().split(":");
                        String[] phoneNumber = scanner.nextLine().split(":");
                        this.email = email[1];
                        this.fullName = fullName[1];
                        this.phoneNumber = phoneNumber[1];
                    }
                }
            }
        }catch (IOException e){sendMessage(e.getMessage());}
    }


}
