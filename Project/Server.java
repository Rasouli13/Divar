package Project;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Server {
    public static void main(String args[]) {
        Server server = new Server(5000);
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

class clientManager extends Thread{
    private String username,password,email,fullName = "",phoneNumber="";
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private Server server;
    clientManager(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        System.out.println("created");

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
                    if (username.equals("cancel")) {
                        break;
                    }
                    username = new setUsername().setUsername(this,username);
                    this.username = username;

                    //Password
                    sendMessage("Enter a Password, or type cancel:");
                    String password = getMessage();
                    if (password.equals("cancel")) {
                        break;
                    }
                    password = new setPassword().setPassword(this,password);
                    this.password = password;

                    //Email
                    sendMessage("Enter a Email, or type cancel:");
                    String email = getMessage();
                    if (email.equals("cancel")) {
                        break;
                    }
                    email = new setEmail().setEmail(this,email);
                    this.email = email;

                    new signUp().addUser(this,username, password, email);
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
                    boolean success = new signIn().checkUser(this);
                    if (success) {
                        this.username = username;
                        this.password = password;
                        initialSettings(username);
                        boolean mainMenu = false;
                        while (!mainMenu){
                            sendMessage("\n1.Project.Profile" +
                                    "\n2.Add an advertisement" +
                                    "\n3.Advertisements page" +
                                    "\n4.Sign Out...\n" +
                                    "\nChoose a number:");
                            String signedIn = getMessage();
                            switch (signedIn){
                                case "1":
                                    new Profile().showProfile(this);
                                    break;
                                case "2":
                                    new Advertisement_regist().addAdvertise(this);
                                case "3":
                                    //new Project.AdvertisementsPages(this);
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

    public void sendMessage(String message){
        try {
            out.writeUTF(message);
            out.flush();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public String getMessage(){
        try {
            return in.readUTF();
        } catch (IOException e) {
            System.out.println(e);
        }
        return "1";
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
            File usersFolder = new File("C:/Divar/users");
            if (usersFolder.exists()) {
                File[] users = usersFolder.listFiles();
                for (File file : Objects.requireNonNull(users)) {
                    if (file.getName().equals(username)) {
                        File profile = new File("C:/Divar/users/" + username + "/profile.txt");
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
        }catch (IOException e){sendMessage(e.toString());}
    }


}



class setEmail {
    public String setEmail(clientManager client, String email){
        boolean success = false;
        do {
            try {
                if(email.equals("cancel"))
                    break;
                Matcher matcher = Pattern.compile("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(email);
                if (matcher.matches()) {
                    return email;
                } else throw new InvalidEmailAddress();
            }catch (InvalidEmailAddress e){
                client.sendMessage(e.toString());
                client.sendMessage("\nEnter a valid email, or type cancel:");
                email = client.getMessage();
            }catch (Exception e) {
                client.sendMessage(e.toString());
            }
        }while (!success);
        return "cancel";
    }
}
class setPassword {
    public String setPassword(clientManager client, String password){
        boolean success = false;
        do {
            try {
                if(password.equals("cancel"))
                    break;
                if (!checkSpelling(password))
                    throw new InvalidPassword("Password must include 8 lowercase characters, numbers, \n\tor can include these: @#$%^&+!?=_");
                if (binaryNotFound(password) && aIsNotEnough(password))
                    throw new InvalidPassword("Dose not include binary or enough 'a'!'");
                if (hasOrdered(password))
                    throw new InvalidPassword("Do not use ordered numbers!");
                int key = new Random().nextInt(100)+1;
                return encrypt(password,key);
            }catch (InvalidPassword e){
                client.sendMessage(e.toString());
                client.sendMessage("\nEnter valid password, or type cancel:");
                password = client.getMessage();
            }catch (Exception e){
                System.out.println(e);
            }
        }while (!success);
        return "cancel";
    }



    //Project.setPassword Methods---------------------------------------------------------------------------------------------------------------------

    private static boolean checkSpelling(String password){
        Matcher matcher = Pattern.compile("(?=[a-z0-9@#$%^&+!?=_\\.]+$)^(?=.*[a-z])(?=.*[0-9])(?=.{8,}).*$").matcher(password);
        return matcher.matches();
    }
    private static boolean aIsNotEnough(String password){
        Matcher matcher1 = Pattern.compile("([a])").matcher(password);
        int aCounter=0;
        while (matcher1.find())
            aCounter++;
        return aCounter < 2;
    }

    private static boolean isOrder(String str) {
        for (int i = 0; i < str.length() / 2; i++) {
            String new_str = str.substring(0, i + 1);
            int num = Integer.parseInt(new_str);
            while (new_str.length() < str.length()) {
                num++;
                new_str = new_str.concat(String.valueOf(num));
            }
            if (new_str.equals(str))
                return true;
        }
        return false;
    }

    private static boolean hasOrdered(String password){
        String[] numbers = password.replaceAll("\\D"," ").split(" ");
        for (String num: numbers)
            if(!num.equals(""))
                if(isOrder(num))
                    return true;
        return false;
    }

    private static boolean binaryNotFound(String password){
        String[] numbers = password.replaceAll("\\D"," ").split(" ");
        for (String num: numbers)
            if(!num.equals("")){
                String[] n = num.split("");
                for (int i=0; i < n.length; i++)
                    for (int j = i; j < n.length;j++) {
                        String n1 = "";
                        n1 = n[j].concat(n1);
                        if(containBinary(n1))
                            return false;//BinaryFound
                    }
            }
        return true;
    }

    private static boolean containBinary(String s) {
        int n = Integer.parseInt(s);
        return (n & n-1)==0;
    }

    private static String encrypt(String password, int key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < password.length(); i++)
            sb.append(shiftForward(password.charAt(i), key));
        sb.append("-").append(key);
        return sb.toString();
    }

    private static char shiftForward(char c, int key) {
        key = key%26;
        if(Character.isLowerCase(c)) {
            if (((char) (c + key) <= 'z'))
                c = (char) (c + key);
            else
                c = (char) (c + key - 26);
        }
        if(Character.isUpperCase(c)){
            if (((char) (c + key) <= 'Z'))
                c = (char) (c + key);
            else
                c = (char) (c + key - 26);
        }

        return c;
    }



    //End Project.setPassword Methods------------------------------------------------------------------------------------------------------------------------

}

class SetPhoneNumber {
    public String setPhoneNumber(clientManager client, String phoneNumber){
        while(true){
            try {
                if(phoneNumber.equals("cancel"))
                    break;
                Matcher matcher= Pattern.compile("[+98]\\d{12}").matcher(phoneNumber);
                if(!matcher.matches())
                    throw new invalidPhoneNumberException();
                return phoneNumber;
            }catch (invalidPhoneNumberException e){
                client.sendMessage(e.toString());
                client.sendMessage("\nEnter phone number in format +989123545678, or type cancel");
                phoneNumber= client.getMessage();
            }
        }
        return "cancel";
    }
}

class setUsername {
    public String setUsername(clientManager client, String username){
        boolean success = false;
        do {
            try {
                if(username.equals("cancel"))
                    break;
                Matcher matcher = Pattern.compile("^\\w+$").matcher(username);
                if (!matcher.matches())
                    throw new InvalidUsername("Invalid username!");
                File usersFolder = new File("C:/Divar/users");
                if(usersFolder.exists()) {
                    File[] users = usersFolder.listFiles();
                    for (File file : Objects.requireNonNull(users)) {
                        if (file.getName().equals(username)) {
                            File profile = new File("C:/Divar/users/"+username+"/profile.txt");
                            Scanner scanner = new Scanner(profile);
                            String[] user = scanner.nextLine().split(":");
                            if (user[1].equals(username))
                                throw new InvalidUsername("This username already taken!");
                        }
                    }
                }
                return username;
            }catch (InvalidUsername e){
                client.sendMessage(e.toString());
                client.sendMessage("\nEnter a valid username, or type cancel:");
                username =  client.getMessage();
            }catch (IOException e){
                client.sendMessage("There is no User!");
            }
        }while (!success);
        return "cancel";
    }
}
class AdvertisementImageException extends Exception {
    AdvertisementImageException(){super("invalid image Address");}
}
class InvalidAddress extends Exception{
    public InvalidAddress(){
        super("Address must be like-> "+'"'+"Tehran: Shahriari Square,..."+'"');
    }
}
class InvalidEmailAddress extends Exception{
    public InvalidEmailAddress(){
        super("Enter a valid Email: example.2@example.com");
    }
}
class InvalidPassword extends Exception{
    public InvalidPassword(String message){
        super(message);
    }
}
class invalidPhoneNumberException extends Exception {
    public invalidPhoneNumberException(){super("Invalid format for phoneNumber");}
}
class InvalidUsername extends Exception{
    public InvalidUsername(String message){
        super(message);
    }
}
class UsernameNotFound extends Exception{
    public UsernameNotFound(){
        super("There is no such user!");
    }
}
class Advertisement_regist{
    private String imageAddress;
    private String adName;
    private String description;
    private double price;
    private String phoneNumber;
    private String adAddress;
    private String city;
    private LocalDateTime LastUpgrade;
    public void addAdvertise(clientManager client) {
        try {
            setAdName(client);
            setPrice(client);
            setImageAddress(client);
            setDescription(client);
            setAdAddress(client);
            setPhoneNumber(client);
            setLastUpgrade(client);
            String fileName = this.adName.replaceAll("[\\/\\\\:?\"<>|*]", "");
            File adsFolder = new File("C:/Divar/Advertisements");
            adsFolder.mkdir();
            File file = new File("C:/Divar/Advertisements/" + fileName + ".txt");
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            fw.write("Name:" + this.adName +
                    "\nPrice:" + this.price +
                    "\nImage address:" + this.imageAddress +
                    "\nAdvertisement address:" + this.adAddress +
                    "\nPhone number:" + this.phoneNumber +
                    "\nDescription:" + this.description +
                    "\nLastUpgrade:" + this.LastUpgrade);
            fw.flush();

            File cities = new File("C:/Divar/Advertisements/cities");
            cities.mkdir();
            File city = new File("C:/Divar/Advertisements/cities" + this.city);
            city.mkdir();
            file = new File("C:/Divar/Advertisements/cities" + this.city + "/" + fileName + ".txt");
            file.createNewFile();
            fw = new FileWriter(file);
            fw.write("Name:" + this.adName +
                    "\nPrice:" + this.price +
                    "\nImage address:" + this.imageAddress +
                    "\nAdvertisement address:" + this.adAddress +
                    "\nPhone number:" + this.phoneNumber +
                    "\nDescription:" + this.description +
                    "\nLastUpgrade:" + this.LastUpgrade);
            fw.flush();

            //adding AD to user folder
            File[] users = new File("C:/Divar/users").listFiles();
            for (File user : Objects.requireNonNull(users)) {
                if (user.getName().equals(client.getUsername())) {
                    File profile = new File("C:/Divar/users/" + client.getUsername() + "/profile.txt");
                    String[] username = new Scanner(profile).nextLine().split(":");
                    if (username[1].equals(client.getUsername())) {
                        File userAdsFolder = new File("C:/Divar/users/" + client.getUsername() + "/ads/");
                        userAdsFolder.mkdir();
                        File userAd = new File("C:/Divar/users/" + client.getUsername() + "/ads/" + fileName + ".txt");
                        userAd.createNewFile();
                        fw = new FileWriter(userAd, true);
                        fw.write("Name:" + this.adName +
                                "\nPrice" + this.price +
                                "\nImage address:" + this.imageAddress +
                                "\nAdvertisement address:" + this.adAddress +
                                "\nPhone number:" + this.phoneNumber +
                                "\nDescription:" + this.description +
                                "\nLastUpgrade:" + this.LastUpgrade);
                        fw.flush();
                    }
                }
            }
            fw.close();
        }catch (Exception e){
            client.sendMessage(e.toString());
        }
    }

    public void setAdAddress(clientManager client) {
        boolean success = false;
        while (!success) {
            try {
                client.sendMessage("Enter Address (like-> " + "Tehran: Shahriari Square,..." + "), or type cancel:");
                String address = new Scanner(System.in).nextLine();
                if (address.equals("cancel")) {
                    success = true;
                    break;
                }
                Matcher matcher = Pattern.compile("^([a-zA-Z]+: .+)").matcher(address);
                if (!matcher.matches())
                    throw new InvalidAddress();


                this.city = address.substring(0,address.indexOf(':'));
                this.adAddress = address;
                success = true;
            } catch (InvalidAddress e) {
                client.sendMessage(e.toString());
            }
        }
    }

    public void setAdName(clientManager client) {
        client.sendMessage("Enter advertisement name, or type cancel:");
        String command =client.getMessage();
        if (command.equals("cancel"))
            return;
        this.adName = command;
    }

    public void setDescription(clientManager client){
        client.sendMessage("Enter description, or type cancel:");
        String command = client.getMessage();
        if (command.equals("cancel"))
            return;
        this.description = command;
    }

    public void setPrice(clientManager client){
        client.sendMessage("Enter price, or type cancel:");
        String command = client.getMessage();
        if (command.equals("cencel"))
            return;
        this.price = Double.parseDouble(command);
    }

    public void setImageAddress(clientManager client){
        client.sendMessage("Enter image address, or type cancel:");
        String command = client.getMessage();
        if (command.equals("cancel"))
            return;
        this.imageAddress = command;
    }

    public void setPhoneNumber(clientManager client){
        client.sendMessage("Enter phone number, or type cancel:");
        String command = client.getMessage();
        if (command.equals("cancel"))
            return;
        this.phoneNumber =new SetPhoneNumber().setPhoneNumber(client,command);
    }

    public void setLastUpgrade(clientManager client) {
        LastUpgrade = LocalDateTime.now();
        client.sendMessage("Ad with name:" + this.adName + "Upgrade at" + LastUpgrade);
    }
}

class  AdvertisementsPages {

    File subjectFile;
    public void SearchInAds(clientManager client){
        while (true){
            try {
                client.sendMessage("enter your advertisement name for search:");
                String name=client.getMessage();
                subjectFile=new File("C:/Divar/Advertisements/"+name+".txt");
                if(!subjectFile.exists())
                    throw new FileNotFoundException();
            }catch (FileNotFoundException e){
                client.sendMessage("enter correct name for File:");
            }
        }
    }
}

class Profile {
    public void showProfile(clientManager client) {
        try {
            File usersFolder = new File("C:/Divar/users");
            String username = client.getUsername();
            if (usersFolder.exists()) {
                File[] users = usersFolder.listFiles();
                for (File file : Objects.requireNonNull(users)) {
                    if (file.getName().equals(username)) {
                        File profile = new File("C:/Divar/users/" + username + "/profile.txt");
                        Scanner scanner = new Scanner(profile);
                        String[] user = scanner.nextLine().split(":");
                        if (user[1].equals(username)) {
                            scanner = new Scanner(profile);
                            while (scanner.hasNextLine()) {
                                while (scanner.hasNextLine()) {
                                    client.sendMessage(scanner.nextLine());
                                }
                            }
                            while (true) {
                                client.sendMessage("\n\n1.Edit\n2.Exit\n\nChoose number:");
                                String edit = client.getMessage();
                                if (edit.equals("1")) {
                                    editProfile(file.getPath() + "/profile.txt", client);
                                } else if (edit.equals("2")) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e ){
            client.sendMessage(e.toString());
        }
    }

    private void editProfile(String baseAddress, clientManager client) throws IOException {
        client.sendMessage("Edit:\n" +
                "\t1.Username\n" +
                "\t2.Password\n" +
                "\t3.Email\n" +
                "\t4.Fullname\n" +
                "\t5.Phone number\n" +
                "\nChoose number:");
        boolean success = false;
        while (!success) {
            String edit = client.getMessage();
            switch (edit) {
                case "1":
                    client.sendMessage("Enter a username, or type cancel:");
                    String newUsername = new setUsername().setUsername(client,client.getMessage());
                    if(newUsername.equals("cancel")){
                        success=true;
                        break;
                    }
                    editUserFile(baseAddress,"Username:",client.getUsername(),newUsername);
                    client.setUsername(newUsername);
                    success = true;
                    break;

                case "2":
                    client.sendMessage("Enter a password, or type cancel:");
                    String newPassword = new setPassword().setPassword(client,client.getMessage());
                    if(newPassword.equals("cancel")){
                        success=true;
                        break;
                    }
                    editUserFile(baseAddress,"Password:",client.getPassword(),newPassword);
                    client.setPassword(newPassword);
                    success = true;
                    break;

                case "3":
                    client.sendMessage("Enter an email, or type cancel:");
                    String newEmail = new setEmail().setEmail(client, client.getMessage());
                    if(newEmail.equals("cancel")){
                        success=true;
                        break;
                    }
                    editUserFile(baseAddress,"Email:",client.getEmail(),newEmail);
                    success = true;
                    break;

                case "4":
                    client.sendMessage("Enter fullname, or type cancel:");
                    String newFullName = client.getMessage();
                    if(newFullName.equals("cancel")){
                        success=true;
                        break;
                    }
                    editUserFile(baseAddress,"Full name",client.getFullName(),newFullName);
                    client.setFullName(newFullName);
                    success = true;
                    break;

                case "5":
                    client.sendMessage("Enter phone number, or type cancel:");
                    String newPhoneNumber = new SetPhoneNumber().setPhoneNumber(client, client.getMessage());
                    if(newPhoneNumber.equals("cancel")){
                        success=true;
                        break;
                    }
                    editUserFile(baseAddress,"Phone number:",client.getPhoneNumber(),newPhoneNumber);
                    client.setPhoneNumber(newPhoneNumber);
                    success = true;
                    break;

                default:
                    client.sendMessage("Enter a valid number:");
                    break;
            }
        }
    }

    private void editUserFile(String baseAddress,String key,String old, String New) throws IOException {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(Path.of(baseAddress), StandardCharsets.UTF_8));
        for (int i = 0; i < fileContent.size(); i++) {
            if (fileContent.get(i).equals(key+old)) {
                fileContent.set(i,(key+New));
                break;
            }
        }
        Files.write(Path.of(baseAddress), fileContent, StandardCharsets.UTF_8);
    }
}
class signIn{
    public boolean checkUser(clientManager client) {
        do {
            try {
                File usersFolder = new File("C:/Divar/users");
                if(usersFolder.exists()) {
                    File[] users = usersFolder.listFiles();
                    for (File file : Objects.requireNonNull(users)) {
                        if (file.getName().equals(client.getUsername())) {
                            File profile = new File("C:/Divar/users/"+client.getUsername()+"/profile.txt");
                            Scanner scanner = new Scanner(profile);
                            String[] user = scanner.nextLine().split(":");
                            String[] pass = scanner.nextLine().split(":");
                            if (user[1].equals(client.getUsername())) {
                                String[] passKey = pass[1].split("-");
                                if ((decrypt(passKey[0], Integer.parseInt(passKey[1]))).equals(client.getPassword())) {
                                    client.sendMessage("Your are Signed in!\n");
                                    return true;
                                }
                            }
                        }
                    }
                }
                throw new UsernameNotFound();
            }catch (UsernameNotFound e){
                client.sendMessage(e.toString());
                client.sendMessage("\nEnter a valid client.getUsername(), or type cancel:");
                String username = client.getMessage();
                if (username.equals("cancel"))
                    return false;
                client.sendMessage("\nEnter a valid client.getUsername(), or type cancel:");
                String password = client.getMessage();
                if(password.equals("cancel"))
                    return false;
            } catch (Exception e){
                client.sendMessage(e.toString());
            }
        }while (true);
    }

    private String decrypt(String password, int key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < password.length(); i++)
            sb.append(shiftBackward(password.charAt(i), key));
        return sb.toString();
    }

    private char shiftBackward(char c, int key) {
        key = key%26;
        if(Character.isLowerCase(c)) {
            if (((char) (c - key) >= 'a'))
                c = (char) (c - key);
            else
                c = (char) (c - key + 26);
        }
        if(Character.isUpperCase(c)) {
            if (((char) (c - key) >= 'A'))
                c = (char) (c - key);
            else
                c = (char) (c - key + 26);
        }
        return c;
    }
}
class signOut {
    signOut(clientManager client){
        reset(client);
    }
    void reset(clientManager client){
        client.setUsername("");
        client.setPassword("");
        client.setEmail("");
        client.setFullName("");
        client.setPhoneNumber("");
    }
}
class signUp {
    public void addUser(clientManager client, String username, String password, String email){
        try {
            File usersDir = new File("C:/Divar/users");
            usersDir.mkdir();
            File usersFolder = new File("C:/Divar/users/" + username);
            usersFolder.mkdir();
            File file = new File("C:/Divar/users/" + username + "/profile.txt");
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            fw.write("Username:" + username +
                    "\nPassword:" + password +
                    "\nEmail:" + email +
                    "\nFull name:" +
                    "\nPhone number:");
            fw.flush();
            fw.close();
        }catch (Exception e){
            client.sendMessage(e.toString());
        }
    }
}



