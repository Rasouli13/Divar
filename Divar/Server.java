

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            System.out.println("Divar is ready...");
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
    private String username,password,email,fullName = "none",phoneNumber="none";
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private Server server;
    clientManager(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }
    @Override
    public void run() {
        boolean exit = false;
        while (!exit) {
            sendMessage("1.Sign Up\n2.Sign In\n\nChoose a number, or type exit:");
            String menu  = getMessage();
            switch (menu) {
                case "1": {
                    //Username
                    sendMessage("Enter a Username, or type cancel:");
                    String username = getMessage();
                    username = new setUsername().setUsername(this,username);
                    if (username.equals("cancel")) {
                        break;
                    }
                    this.username = username;

                    //Password
                    sendMessage("Enter a Password, or type cancel:");
                    String password = getMessage();
                    password = new setPassword().setPassword(this,password);
                    if (password.equals("cancel")) {
                        break;
                    }

                    this.password = password;

                    //Email
                    sendMessage("Enter a Email, or type cancel:");
                    String email = getMessage();
                    if (email.equals("cancel")) {
                        break;
                    }
                    email = new setEmail().setEmail(this,email);
                    this.email = email;

                    new signUp().addUser(this,this.username, this.password, this.email);
                    sendMessage("Your account created successfully:)");
                    break;
                }
                case "2": {
                    sendMessage("Enter your username, or type cancel:");
                    String username = getMessage();
                    if(username.equals("cancel")){
                        break;
                    }
                    sendMessage("Enter your password, or type cancel:");
                    String password = getMessage();
                    if(password.equals("cancel")){
                        break;
                    }

                    boolean successSignIn = false;
                    successSignIn = new signIn().checkUser(this, username, password);

                    if (successSignIn) {
                        sendMessage("Your are Signed in as:"+username+" :)");
                        this.username = username;
                        this.password = password;
                        initialSettings(username);
                        boolean mainMenu = false;

                        while (!mainMenu){
                            sendMessage("\n1.Profile" +
                                    "\n2.Add an advertisement" +
                                    "\n3.Edit your Advertisement" +
                                    "\n4.Advertisements page" +
                                    "\n5.Sign Out...\n" +
                                    "\nChoose a number:");
                            String signedIn = getMessage();
                            switch (signedIn){
                                case "1":
                                    new Profile().showProfile(this);
                                    break;
                                case "2":
                                    new Advertisement_registry().addAdvertise(this);
                                    break;
                                case "3":
                                    new EditAdvertise(this);
                                    break;
                                case "4":
                                    try {
                                        new AdvertisementsPages(this);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "5":
                                    new signOut().reset(this);
                                    mainMenu = true;
                                    break;
                                default:
                                    sendMessage("Enter a valid number!");
                                    break;
                            }
                        }
                    }
                    break;
                }
                case "exit":{
                    exit =true;
                    break;}
                default:
                    sendMessage("\n!Enter a valid value!");
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
            else sendMessage("else");
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
                Matcher matcher = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)$").matcher(email);
                if (matcher.matches()) {
                    return email;
                } else throw new InvalidEmailAddress();
            }catch (InvalidEmailAddress e){
                client.sendMessage(e.toString());
                client.sendMessage("Enter a valid email, or type cancel:");
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
                    throw new InvalidPassword("Dose not include binary or enough 'a'!");
                if (hasOrdered(password))
                    throw new InvalidPassword("Do not use ordered numbers!");
                int key = new Random().nextInt(100)+1;
                return encrypt(password,key);
            }catch (InvalidPassword e){
                client.sendMessage(e.toString());
                client.sendMessage("Enter valid password, or type cancel:");
                password = client.getMessage();
            }catch (Exception e){
                client.sendMessage(e.toString());
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
                client.sendMessage("Enter a valid username, or type cancel:");
                username =  client.getMessage();
            }catch (IOException e){
                success=true;
                client.sendMessage("There is no User!");
            }
        }while (!success);
        return "cancel";
    }
}
class AdvertisementImageException extends Exception {
    AdvertisementImageException(){super("invalid image Address");}
}
class AdvertisementNotFound extends Exception{
    AdvertisementNotFound(String message){
        super(message);
    }
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
class Advertisement_registry {
    private String imageAddress;
    private String adName;
    private String description;
    private double price;
    private String phoneNumber;
    private String adAddress;
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
            File[] users = new File("C:/Divar/users").listFiles();
            for (File user : Objects.requireNonNull(users)) {
                if (user.getName().equals(client.getUsername())) {
                    File profile = new File("C:/Divar/users/" + client.getUsername() + "/profile.txt");
                    String[] username = new Scanner(profile).nextLine().split(":");
                    if (username[1].equals(client.getUsername())) {
                        File userAdsFolder = new File("C:/Divar/users/" + client.getUsername() + "/ads");
                        userAdsFolder.mkdir();
                        File userAd = new File("C:/Divar/users/" + client.getUsername() + "/ads/" + fileName + ".txt");
                        userAd.createNewFile();

                        FileWriter fw = new FileWriter(userAd);
                        fw.write("Name:" + this.adName +
                                "\nPrice:" + this.price +
                                "\nImage address:" + this.imageAddress +
                                "\nAdvertisement address:" + this.adAddress +
                                "\nPhone number:" + this.phoneNumber +
                                "\nDescription:" + this.description +
                                "\nLastUpgrade:" + this.LastUpgrade);
                        fw.flush();
                    }
                }
            }
        }catch (Exception e){
            client.sendMessage(e.toString());
        }
    }

    public void setAdAddress(clientManager client) {
        boolean success = false;
        while (!success) {
            try {
                client.sendMessage("Enter Address (like-> " + "Tehran: Shahriari Square,..." + "), or type cancel:");
                String address = client.getMessage();
                if (address.equals("cancel")) {
                    success = true;
                    break;
                }
                Matcher matcher = Pattern.compile("^([a-zA-Z]+: .+)").matcher(address);
                if (!matcher.matches())
                    throw new InvalidAddress();
                String toUpper = String.valueOf(Character.toUpperCase(address.charAt(0)));
                this.adAddress = toUpper.concat(address.substring(1));
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
        while(true){
            try {
                client.sendMessage("Enter image address, or type cancel:");
                String command = client.getMessage();
                command=command.replace("\\","/");
                if (command.equals("cancel"))
                    return;
                File imageAddress=new File(command);
                if(imageAddress.exists()){
                    this.imageAddress = command;
                    return;
                }
                throw new AdvertisementImageException();
            }catch (AdvertisementImageException e){
                client.sendMessage("!There is no Image, Enter a valid address!");
            }
        }

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
    List<File> allAds;
    List<File> rangeSort;
    List<File> results = new ArrayList<File>();
    AdvertisementsPages(clientManager client) throws FileNotFoundException {
        readAllAds();
        boolean success=false;
        while (!success){
            client.sendMessage("1.Sort"+
                    "\n2.Search"+
                    "\n3.Exit"+
                    "\n\nchoose number:");
            switch (client.getMessage()){
                case "1":
                    SortInAds(client);
                    break;
                case "2":
                    SearchInAds(client);
                    break;
                case "3":
                    success=true;
                    break;
                default:
                    client.sendMessage("enter valid number:");
                    break;
            }
        }
    }

    public void SortInAds(clientManager client) throws FileNotFoundException {
        boolean success = false;
        while (!success) {
            client.sendMessage("Sort By:" +
                    "\n1.Ascending price" +
                    "\n2.Descending price" +
                    "\n3.price range" +
                    "\n4.LastUpgrade" +
                    "\n5.Exit" +
                    "\n\nChoose valid number:");
            switch (client.getMessage()) {
                case "1":
                    SortAscendingPrice(client);
                    showSearchResults(client, results);
                    break;
                case "2":
                    SortPriceDescending(client);
                    showSearchResults(client,results);
                    break;
                case "3":
                    SortPriceRange(client);
                    showSearchResults(client, results);
                    break;
                case "4":
                    SortLastUpgrade(client);
                    showSearchResults(client,results);
                    break;
                case "5":
                    success = true;
                    break;
                default:
                    client.sendMessage("Enter valid number:");
            }
        }
    }

    public void readAllAds(){
        allAds = new ArrayList<>();
        File[] users = new File("C:/Divar/users").listFiles();
        for (File user : Objects.requireNonNull(users)) {
            File[] userAds = new File(user.getAbsolutePath() + "/ads").listFiles();
            allAds.addAll(Arrays.asList(Objects.requireNonNull(userAds)));
        }
    }
    public void SortAscendingPrice(clientManager client) {
        results = new ArrayList<>();
        results.addAll(allAds);
        try {
            for (int i = 0; i < results.size(); i++) {
                for (int j = 0; j < results.size() - i - 1; j++) {
                    if (Files.readAllLines(Paths.get(results.get(j).getPath())).get(1).substring(6).compareTo(Files.readAllLines(Paths.get(results.get(j+1).getPath())).get(1).substring(6)) >= 1) {
                        File temp = results.get(j);
                        results.set(j,results.get(j+1));
                        results.set(j+1,temp);
                    }
                }
            }
        }catch (IOException e){
            client.sendMessage(e.toString());
        }
    }
    public void SortPriceDescending(clientManager client) {
        results = new ArrayList<>();
        results.addAll(allAds);
        try {
            for (int i = 0; i < results.size(); i++) {
                for (int j = 0; j < results.size() - i - 1; j++) {
                    if (Files.readAllLines(Paths.get(results.get(j).getPath())).get(1).substring(6).compareTo(Files.readAllLines(Paths.get(results.get(j + 1).getPath())).get(1).substring(6)) < 1) {
                        File temp = results.get(j);
                        results.set(j , results.get(j+1));
                        results.set(j + 1, temp);
                    }
                }
            }
        }catch (IOException e){
            client.sendMessage(e.toString());
        }
    }
    public void SortPriceRange(clientManager client){
        results = new ArrayList<>();
        try {
            client.sendMessage("enter minimum number for price");
            Double startRange = Double.parseDouble(client.getMessage());
            client.sendMessage("enter maximum number for price ");
            Double endRange = Double.parseDouble(client.getMessage());
            for (int i = 0; i < allAds.size(); i++) {
                Double price = Double.parseDouble(Files.readAllLines(Paths.get(allAds.get(i).getPath())).get(1).substring(6));
                if (price >= startRange && price <= endRange)
                    results.add(allAds.get(i));
                client.sendMessage("-------------------------------------------------------");
            }
        }catch (IOException e){
            client.sendMessage(e.toString());
        }
    }
    public void SortLastUpgrade(clientManager client) {
        results = new ArrayList<>();
         try {
             results.addAll(allAds);
             for (int i = 0; i < results.size(); i++) {
                 for (int j = 0; j < results.size() - i - 1; j++) {
                     if (Files.readAllLines(Paths.get(results.get(j).getPath())).get(6).substring(12).compareTo(Files.readAllLines(Paths.get(results.get(j + 1).getPath())).get(6).substring(12)) < 1) {
                         File temp = results.get(j);
                         results.set(j, results.get(j+1));
                         results.set(j + 1, temp);
                     }
                 }
             }
         }catch (IOException e ){
             client.sendMessage(e.toString());
     }

    }
    public void SearchInAds(clientManager client) throws FileNotFoundException {
        boolean success=false;
        while (!success) {
            client.sendMessage("Search by:" +
                    "\n\t1.All City" +
                    "\n\t2.Specific city" +
                    "\n\t3.Name" +
                    "\n\t4.Exit" +
                    "\n\nChoose right number:");
            switch (client.getMessage()) {
                case "1":
                    SearchAllCities(client);
                    showSearchResults(client, results);
                    break;
                case "2":
                    SearchBySpecificCity(client);
                    showSearchResults(client, results);
                    break;
                case "3":
                    SearchByName(client);
                    showSearchResults(client, results);
                    break;
                case "4":
                    success = true;
                    break;
                default:
                    client.sendMessage("press valid number");
            }
        }
    }
    public void AddFavorite(clientManager client){
        boolean success=false;
        while(!success){
            try{
                if(results.size() > 0){
                    client.sendMessage("Enter a advertisement to add Favorites:");
                    String name = client.getMessage();
                    File favoriteDirectory=new File("C:/Divar/users/"+client.getUsername()+"/favorite");
                    favoriteDirectory.mkdir();
                    name = name.concat(".txt");
                    for (File ad : results) {
                        if (name.equals(ad.getName())) {
                            File favoriteAd = new File(favoriteDirectory.getPath() + "/" + ad.getName());
                            FileWriter fw = new FileWriter(favoriteAd);
                            Scanner scanner = new Scanner(ad);
                            while (scanner.hasNextLine()) {
                                fw.write(scanner.nextLine() + "\n");
                                fw.flush();
                            }
                            client.sendMessage("Added to favorite :)");
                        }
                    }
                    break;
                }
                else
                    throw new FileNotFoundException();
            }catch (FileNotFoundException e){
                client.sendMessage("file not found");
            }catch (IOException e){
                client.sendMessage(e.toString());
            }
        }
    }
    public void SearchByName(clientManager client) {
        results = new ArrayList<File>();
        boolean success = false;
        while (!success) {
            try{
                client.sendMessage("Enter your advertisement name for search, or type cancel:");
                    String name = client.getMessage();
                    if (name.equals("cancel"))
                        break;
                        name = name.concat(".txt");
                    for (File ad : allAds) {
                        if(ad.getName().equals(name)){
                            results.add(new File(ad.getPath()));
                            client.sendMessage(String.valueOf(results.size()));
                            success=true;
                            break;
                        }
                    }
                    if(!success)
                        throw new FileNotFoundException();
            }catch (FileNotFoundException e){
                client.sendMessage("There is no such Advertisement");
            }
        }
    }
    public void SearchBySpecificCity(clientManager client){
        results = new ArrayList<File>();
        boolean success=false;
        String city="this city";
        while (!success) {
            try {
                client.sendMessage("Enter city name, or type cancel:");
                city = client.getMessage();
                if (city.equals("cancel"))
                    break;
                city = Character.toUpperCase(city.charAt(0)) + city.substring(1);
                client.sendMessage("Enter advertisement name, or type cancel:");
                String name = client.getMessage();
                if (name.equals("cancel"))
                    break;
                name = name.concat(".txt");
                for (File file : allAds) {
                    String adCity = Files.readAllLines(Path.of(file.getPath())).get(3).substring(22);
                    adCity = adCity.substring(0,adCity.indexOf(':'));
                    if (file.getName().equals(name) && adCity.equals(city)) {
                        results.add(file);
                    }
                }
                if (results.size() == 0)
                    throw new FileNotFoundException();
                client.sendMessage("-------------------------------------------------------");
                break;
            } catch (FileNotFoundException e) {
                client.sendMessage("There is no such Advertisement in"+city);
            }catch (IOException e){
                client.sendMessage(e.toString());
            }
        }
    }

    public void SearchAllCities(clientManager client){
        results = new ArrayList<File>();
        boolean success=false;
        while (!success) {
            try {
                client.sendMessage("Enter advertisement name, or type cancel:");
                String name = client.getMessage();
                if (name.equals("cancel"))
                    break;
                name = name.concat(".txt");
                for (File ad : allAds) {
                    if (ad.getName().equals(name))
                        results.add(ad);
                }
                if (results.size() == 0)
                    throw new FileNotFoundException();
                client.sendMessage("-------------------------------------------------------");
                break;
            } catch (FileNotFoundException e) {
                client.sendMessage("There is no such Advertisement in cities!");
            }
        }
    }


    public void RemoveFavorite(clientManager client) {
        boolean success = false;
        while (!success) {
            try {
                client.sendMessage("enter advertisement name:");
                String name = client.getMessage();
                subjectFile = new File("C:/Divar/users/" + client.getUsername() + "/favorite/" + name + ".txt");
                if (subjectFile.exists()) {
                    subjectFile.delete();
                    success = true;
                }
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                client.sendMessage("File not Found");
            }
        }
    }

    public void showSearchResults(clientManager client,List<File> results) throws FileNotFoundException {
            for (File file : results) {
                if (file.isFile()) {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        client.sendMessage(scanner.nextLine());
                    }
                    client.sendMessage("-------------------------------------------------------");
                }
            }
            boolean success=false;
            while (!success){
                client.sendMessage("1.Add favorite\n2.Exit\n\nChoose a number:");
                String favorite = client.getMessage();
                switch (favorite){
                    case "1":
                        AddFavorite(client);
                        break;
                    case "2":
                        success = true;
                        break;
                    default:
                        client.sendMessage("Enter a valid number!");
                        break;
                }
            }

    }
}
class EditAdvertise {
    File adInUser;
    String fileName;
    EditAdvertise(clientManager client) {
        EditAds(client);
    }

    public void EditAds(clientManager client) {
        client.sendMessage("Enter your advertise name for edit");
        String adName = client.getMessage();
        fileName = adName.replaceAll("[\\/\\\\:?\"<>|*]", "");
        adInUser = new File("C:/Divar/users/"+client.getUsername()+"/ads/"+ fileName + ".txt");
        boolean success = false;
        while (!success) {
            try {
                System.out.println(adInUser.createNewFile());
                if (adInUser.exists()) {
                    client.sendMessage("1.Name" +
                            "\n2.Price" +
                            "\n3.Image address" +
                            "\n4.Advertisement address" +
                            "\n5.Phone number" +
                            "\n6.Description" +
                            "\n7.Cancel editing" +
                            "\n\nChoose number:");
                    switch (client.getMessage()) {
                        case "1":
                            EditName(client);
                            renameFile(client);
                            EditUpgrade(client);
                            break;
                        case "2":
                            EditPrice(client);
                            EditUpgrade(client);
                            break;
                        case "3":
                            EditImageAddress(client);
                            EditUpgrade(client);
                            break;
                        case "4":
                            EditAdsAddress(client);
                            EditUpgrade(client);
                            break;
                        case "5":
                            EditPhoneNumber(client);
                            EditUpgrade(client);
                            break;
                        case "6":
                            EditDescription(client);
                            EditUpgrade(client);
                            break;
                        case "7":
                            success = true;
                            break;
                        default:
                            client.sendMessage("Enter a valid number");
                            break;

                    }
                }
                else throw new AdvertisementNotFound("You do not have such Advertisement!");
            }catch (AdvertisementNotFound e){
                client.sendMessage(e.toString());
                break;
            }catch (IOException e){
                System.out.println(e);
            }
        }
    }

    public void EditName(clientManager client) {
        try {
            client.sendMessage("Enter your new advertisement name, or type cancel:");
            String newAdsName = client.getMessage();
            if (newAdsName.equals("cancel"))
                return;
            String oldAdsName = Files.readAllLines(Path.of(adInUser.getPath())).get(0).substring(5);
            editUserAd(adInUser.getPath(), "Name:", oldAdsName, newAdsName);
            this.fileName = newAdsName;
        }catch (IOException e){
            client.sendMessage(e.toString());
        }
    }
    public void EditPrice(clientManager client) {
        try {
            client.sendMessage("Enter your new Ads price, or type cancel:");
            String newAdsPrice = client.getMessage();
            if (newAdsPrice.equals("cancel"))
                return;
            System.out.println(adInUser.getPath());
            String oldAdsPrice = Files.readAllLines(Paths.get(adInUser.getPath())).get(1).substring(6);
            editUserAd(adInUser.getPath(), "Price:", oldAdsPrice, newAdsPrice);
        }catch (IOException e){
            client.sendMessage(e.toString());
        }
    }
    public void EditImageAddress(clientManager client){
        try {
            client.sendMessage("Enter your new image address, or type cancel:");
            String newImageAddress = client.getMessage();
            if (newImageAddress.equals("cancel"))
                return;
            String oldImageAddress = Files.readAllLines(Paths.get(adInUser.getPath())).get(2).substring(14);
            editUserAd(adInUser.getPath(), "Image address:", oldImageAddress, newImageAddress);
        }catch (IOException e){
            client.sendMessage(e.toString());
        }
    }
    public void EditAdsAddress(clientManager client){
        try {
            client.sendMessage("Enter your new advertisement address, or type cancel:");
            String newAdsAddress = client.getMessage();
            if (newAdsAddress.equals("cancel"))
                return;
            String oldAdsAddress = Files.readAllLines(Paths.get(adInUser.getPath())).get(3).substring(22);
            editUserAd(adInUser.getPath(), "Advertisement address:", oldAdsAddress, newAdsAddress);
        }catch (IOException e){
            client.sendMessage(e.toString());
        }
    }
    public void EditPhoneNumber(clientManager client){
        try {
            client.sendMessage("Enter your new phone number, or type cancel:");
            String newPhoneNumber = client.getMessage();
            if (newPhoneNumber.equals("cancel"))
                return;
            String oldPhoneNumber = Files.readAllLines(Paths.get(adInUser.getPath())).get(4).substring(13);
            editUserAd(adInUser.getPath(), "Phone number:", oldPhoneNumber, newPhoneNumber);
        }catch (IOException e){
            client.sendMessage(e.toString());
        }
    }
    public void EditDescription(clientManager client) {
        try{
            client.sendMessage("Enter your new Description in single line, or type cancel:");
            String newDescription = client.getMessage();
            if (newDescription.equals("cancel"))
                return;
            String oldDescription = Files.readAllLines(Paths.get(adInUser.getPath())).get(5).substring(12);
            editUserAd(adInUser.getPath(), "Description:", oldDescription, newDescription);
        }catch (IOException e){
            client.sendMessage(e.toString());
        }
    }

    public void EditUpgrade(clientManager client) {
        String lastUpgrade = LocalDateTime.now().toString();
        try {
            client.sendMessage("Advertise Upgraded at" + lastUpgrade);
            String oldTime = Files.readAllLines(Paths.get(adInUser.getPath())).get(6).substring(12);
            System.out.println(adInUser.getPath());
            editUserAd(adInUser.getPath(), "LastUpgrade:", oldTime, lastUpgrade);
        }catch (IOException e){
            client.sendMessage(e.toString());
        }
    }
    private void editUserAd(String baseAddress,String key,String old, String New) {
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(baseAddress), StandardCharsets.UTF_8));
            for (int i = 0; i < fileContent.size(); i++) {
                if (fileContent.get(i).equals(key + old)) {
                    fileContent.set(i, (key + New));
                    break;
                }
            }
            Files.write(Path.of(baseAddress), fileContent, StandardCharsets.UTF_8);
            if(key.equals("Name:")) {
                File file = new File(baseAddress);
                File rename = new File(file.getParent() + "/" + New + ".txt");
                System.out.println(file.renameTo(rename));
            }
        }catch (IOException e){
            System.out.println(e);
        }
    }
    void renameFile(clientManager client){
        adInUser = new File("C:/Divar/users/"+client.getUsername()+"/ads/"+ fileName + ".txt");
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
                            client.sendMessage("\n");
                            client.sendMessage(scanner.nextLine());//username
                            String[] password = scanner.nextLine().split(":");
                            String[] passKey = password[1].split("-");
                            client.sendMessage(password[0]+":"+signIn.decrypt(passKey[0], Integer.parseInt(passKey[1])));
                            while (scanner.hasNextLine()) {
                                client.sendMessage(scanner.nextLine());
                            }
                            client.sendMessage("\n1.Edit\n2.Exit\n\nChoose number:");
                            String edit = client.getMessage();
                            switch (edit){
                                case "1":
                                    editProfile(file.getPath() + "/profile.txt", client);
                                    break;
                                case "2":
                                    return;
                                default:
                                    client.sendMessage("!Enter a valid number!");
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

        boolean success = false;
        while (!success) {
            client.sendMessage("Edit:\n" +
                    "\t1.Password\n" +
                    "\t2.Email\n" +
                    "\t3.Fullname\n" +
                    "\t4.Phone number\n" +
                    "\t5.Exit\n" +
                    "\nChoose number:");
            String edit = client.getMessage();
            switch (edit) {
                case "1":
                    client.sendMessage("Enter new password, or type cancel:");
                    String newPassword = new setPassword().setPassword(client,client.getMessage());
                    if(newPassword.equals("cancel")){
                        success=true;
                        break;
                    }
                    editUserFile(baseAddress,"Password:",client.getPassword(),newPassword);
                    client.setPassword(newPassword);
                    client.sendMessage("Password Successfully changed!");
                    break;

                case "2":
                    client.sendMessage("Enter new email, or type cancel:");
                    String newEmail = new setEmail().setEmail(client, client.getMessage());
                    if(newEmail.equals("cancel")){
                        break;
                    }
                    editUserFile(baseAddress,"Email:",client.getEmail(),newEmail);
                    client.setEmail(newEmail);
                    client.sendMessage("Email Successfully changed!");
                    break;

                case "3":
                    client.sendMessage("Enter new fullname, or type cancel:");
                    String newFullName = client.getMessage();
                    if(newFullName.equals("cancel")){
                        break;
                    }
                    editUserFile(baseAddress,"Full name:",client.getFullName(),newFullName);
                    client.setFullName(newFullName);
                    client.sendMessage("Fullname Successfully changed!");
                    break;

                case "4":
                    client.sendMessage("Enter new phone number, or type cancel:");
                    String newPhoneNumber = new SetPhoneNumber().setPhoneNumber(client, client.getMessage());
                    if(newPhoneNumber.equals("cancel")){
                        break;
                    }
                    editUserFile(baseAddress,"Phone number:",client.getPhoneNumber(),newPhoneNumber);
                    client.setPhoneNumber(newPhoneNumber);
                    client.sendMessage("Phone number Successfully changed!");
                    break;
                case "5":
                    success = true;
                    break;
                default:
                    client.sendMessage("!Enter a valid number!");
                    break;
            }
        }
    }

    private void editUserFile(String baseAddress,String key,String old, String New) throws IOException {
        if (key.equals("Password:")) {
            old = Files.readAllLines(Paths.get(baseAddress)).get(1).substring(9);
        }
        List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(baseAddress), StandardCharsets.UTF_8));
        for (int i = 0; i < fileContent.size(); i++) {
            if (fileContent.get(i).equals(key+old)) {
                fileContent.set(i,(key+New));
                break;
            }
        }
        Files.write(Paths.get(baseAddress), fileContent, StandardCharsets.UTF_8);
    }
}
class signIn{
    public boolean checkUser(clientManager client,String username,String password) {
        do {
            try {
                File usersFolder = new File("C:/Divar/users");
                if(usersFolder.exists()) {
                    File[] users = usersFolder.listFiles();
                    for (File file : Objects.requireNonNull(users)) {
                        if (file.getName().equals(username)) {
                            File profile = new File("C:/Divar/users/"+username+"/profile.txt");
                            Scanner scanner = new Scanner(profile);
                            String[] user = scanner.nextLine().split(":");
                            String[] pass = scanner.nextLine().split(":");
                            if (user[1].equals(username)) {
                                String[] passKey = pass[1].split("-");
                                if ((decrypt(passKey[0], Integer.parseInt(passKey[1]))).equals(password)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                else {
                    throw new IOException();
                }
                throw new UsernameNotFound();
            }catch (UsernameNotFound e){
                client.sendMessage(e.toString());
                client.sendMessage("Enter a valid username, or type cancel:");
                username = client.getMessage();
                if (username.equals("cancel"))
                    return false;
                client.sendMessage("Enter a valid password, or type cancel:");
                password = client.getMessage();
                if(password.equals("cancel"))
                    return false;
            } catch (IOException e){
                client.sendMessage("There is no User");
                return false;
            }
        }while (true);
    }

    public static String decrypt(String password, int key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < password.length(); i++)
            sb.append(shiftBackward(password.charAt(i), key));
        return sb.toString();
    }

    private static char shiftBackward(char c, int key) {
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
            new File("C:/Divar").mkdir();
            new File("C:/Divar/users").mkdir();
            new File("C:/Divar/users/" + username).mkdir();
            File file = new File("C:/Divar/users/" + username + "/profile.txt");
            file.createNewFile();
            new File("C:/Divar/users/" + username +"/ads").mkdir();
            FileWriter fw = new FileWriter(file);
            fw.write("Username:" + username +
                    "\nPassword:" + password +
                    "\nEmail:" + email +
                    "\nFull name:none" +
                    "\nPhone number:none");
            fw.flush();
            fw.close();
        }catch (Exception e){
            client.sendMessage(e.toString());
        }
    }
}


