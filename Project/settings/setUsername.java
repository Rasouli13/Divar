package Project.settings;
import Project.Exceptions.InvalidUsername;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class setUsername {
    public static String setUsername(String username) throws IOException, InterruptedException {
        boolean success = false;
        do {
            try {
                if(username.equals(""))
                    break;
                Matcher matcher = Pattern.compile("^\\w+$").matcher(username);
                if (!matcher.matches())
                    throw new InvalidUsername("Invalid username!");
                String fileName = username.replaceAll("[\\/\\\\:?\"<>|*]","");
                File usersFolder = new File("Project/users");
                if(usersFolder.exists()) {
                    File[] users = usersFolder.listFiles();
                    for (File file : Objects.requireNonNull(users)) {
                        if (file.getName().equals(fileName)) {
                            File profile = new File("Project/users/"+fileName+"/profile.txt");
                            Scanner scanner = new Scanner(file);
                            String[] user = scanner.nextLine().split(":");
                            if (user[1].equals(username))
                                throw new InvalidUsername("This username already taken!");
                        }
                    }
                }
                return username;
            }catch (InvalidUsername e){
                System.out.println(e);
                System.out.println("\npress Enter to continue...");
                new Scanner(System.in).nextLine();
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.print("Enter a valid username:");
                username =  new Scanner(System.in).nextLine();
            }catch (IOException e){
                System.out.println("There is no User!");
            };
        }while (!success);
        return username;
    }
}
