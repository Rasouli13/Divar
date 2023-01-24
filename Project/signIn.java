package Project;

import Project.Exceptions.*;

import java.io.*;
import java.util.*;

public class signIn{
    protected boolean checkUser(clientManager client) {
        do {
            try {
                File usersFolder = new File("Project/users");
                if(usersFolder.exists()) {
                    File[] users = usersFolder.listFiles();
                    for (File file : Objects.requireNonNull(users)) {
                        if (file.getName().equals(client.getUsername())) {
                            File profile = new File("Project/users/"+client.getUsername()+"/profile.txt");
                            Scanner scanner = new Scanner(profile);
                            String[] user = scanner.nextLine().split(":");
                            String[] pass = scanner.nextLine().split(":");
                            if (user[1].equals(client.getUsername())) {
                                String[] passKey = pass[1].split("-");
                                if ((decrypt(passKey[0], Integer.parseInt(passKey[1]))).equals(client.getPassword())) {
                                    clientManager.sendMessage("Your are Signed in!\n");
                                    return true;
                                }
                            }
                        }
                    }
                }
                throw new UsernameNotFound();
            }catch (UsernameNotFound e){
                clientManager.sendMessage(e.toString());
                clientManager.sendMessage("\nEnter a valid client.getUsername(), or type cancel:");
                String username = clientManager.getMessage();
                if (username.equals("cancel"))
                    return false;
                clientManager.sendMessage("\nEnter a valid client.getUsername(), or type cancel:");
                String password = clientManager.getMessage();
                if(password.equals("cancel"))
                    return false;
            } catch (Exception e){
                clientManager.sendMessage(e.toString());
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
