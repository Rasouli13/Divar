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
        boolean exit = false;
        while (!exit) {
            sendMessage("1.Creat Account\n2.Sign In\n3.Exit\n\nChoose a number:");
            String menu  = getMessage();
            switch (menu) {
                case "1": {
                    //Username
                    sendMessage("Enter a Username, or type cancel:");
                    String username = getMessage();
                    username = setUsername.setUsername(username);
                    if (username.equals("cancel")) {
                        break;
                    }
                    this.username = username;

                    //Password
                    sendMessage("Enter a Password, or type cancel:");
                    String password = getMessage();
                    password = setPassword.setPassword(password);
                    if (password.equals("cancel")) {
                        break;
                    }
                    this.password = password;

                    //Email
                    sendMessage("Enter a Email, or type cancel:");
                    String email = getMessage();
                    email = setPassword.setPassword(email);
                    if (email.equals("cancel")) {
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
                        break;
                    }
                    sendMessage("Enter your password, for Cancel press Enter:");
                    String password = getMessage();
                    if(password.equals("cancel")){
                        break;
                    }
                    boolean success = new signIn().checkUser(username,password);
                    if (success) {
                        this.username = username;
                        this.password = password;
                        initialSettings(username);
                        boolean mainMenu = false;
                        while (!mainMenu){
                            sendMessage("\n1.Profile" +
                                    "\n2.Add an advertisement" +
                                    "\n3.Advertisements page" +
                                    "\n4.Sign Out...\n" +
                                    "\nChoose a number:");
                            String signedIn = getMessage();
                            switch (signedIn){
                                case "1":
                                case "2":
                                case "3":
                                case "4":
                                    mainMenu = true;
                                    break;
                            }
                        }
                    }
                    break;
                }
                case "3":
                    sendMessage("See You SOON!!!");
                    exit = true;
                    break;
                default:
                    sendMessage("\n!Enter a valid number!\n");
                    break;
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
