package Project.sets;
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
                File usersFolder = new File("/users");
                if(usersFolder.exists()) {
                    File[] users = usersFolder.listFiles();
                    for (File file : Objects.requireNonNull(users)) {
                        if (file.exists()) {
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
        return "";
    }
}
