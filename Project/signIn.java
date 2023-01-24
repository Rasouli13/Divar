package Project;

import Project.Exceptions.*;

import java.io.*;
import java.util.*;

public class signIn extends User {
    public signIn() throws IOException, InterruptedException {
        checkUser();
    }

    protected void checkUser() throws IOException, InterruptedException {
        boolean seccess = false;
        boolean notFound = true;
        do {
            try {
                System.out.print("Enter your username, for Cancel press Enter:");
                String username = new Scanner(System.in).nextLine();
                System.out.print("Enter your password, for Cancel press Enter:");
                String password = new Scanner(System.in).nextLine();
                String fileName = username.replaceAll("[\\/\\\\:?\"<>|*]","");
                File usersFolder = new File("Project/users");
                if(usersFolder.exists()) {
                    File[] users = usersFolder.listFiles();
                    for (File file : Objects.requireNonNull(users)) {
                        if (file.getName().equals(fileName)) {
                            File profile = new File("Project/users/"+fileName+"/profile.txt");
                            Scanner scanner = new Scanner(profile);
                            while (scanner.hasNextLine()) {
                                String[] user = scanner.nextLine().split(":");
                                String[] pass = scanner.nextLine().split(":");
                                if (user[1].equals(username)) {
                                    String[] passKey = pass[1].split("-");
                                    if ((decrypt(passKey[0], Integer.parseInt(passKey[1]))).equals(password)) {
                                        seccess = true;
                                        notFound = false;
                                        this.username = username;
                                        this.password = password;
                                        System.out.println("Your are Signed in!");
                                        System.out.println("\npress Enter to continue...");
                                        new Scanner(System.in).nextLine();
                                    }
                                }
                            }
                        }
                    }
                }
                if (notFound)
                    throw new UsernameNotFound();
            }catch (UsernameNotFound e){
                System.out.println(e);
                System.out.println("\npress any key to continue...");
                new Scanner(System.in).nextLine();
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (Exception e){
                System.out.println(e);
            }
        }while (!seccess);
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
