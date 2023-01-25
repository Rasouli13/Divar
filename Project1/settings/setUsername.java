package Project.settings;
import Project.Exceptions.InvalidUsername;
import Project.clientManager;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class setUsername {
    public static String setUsername(String username){
        boolean success = false;
        do {
            try {
                if(username.equals("cancel"))
                    break;
                Matcher matcher = Pattern.compile("^\\w+$").matcher(username);
                if (!matcher.matches())
                    throw new InvalidUsername("Invalid username!");
                File usersFolder = new File("Project/users");
                if(usersFolder.exists()) {
                    File[] users = usersFolder.listFiles();
                    for (File file : Objects.requireNonNull(users)) {
                        if (file.getName().equals(username)) {
                            File profile = new File("Project/users/"+username+"/profile.txt");
                            Scanner scanner = new Scanner(profile);
                            String[] user = scanner.nextLine().split(":");
                            if (user[1].equals(username))
                                throw new InvalidUsername("This username already taken!");
                        }
                    }
                }
                return username;
            }catch (InvalidUsername e){
                clientManager.sendMessage(e.toString());
                clientManager.sendMessage("\nEnter a valid username, or type cancel:");
                username =  clientManager.getMessage();
            }catch (IOException e){
                clientManager.sendMessage("There is no User!");
            }
        }while (!success);
        return "cancel";
    }
}
