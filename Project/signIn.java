package Project;

import Project.Exceptions.*;

import java.io.*;
import java.util.Scanner;

public class signIn {
    public signIn() throws IOException, InterruptedException {
        checkUser();
    }

    private void checkUser() throws IOException, InterruptedException {
        boolean seccess = false;
        boolean notFound = true;
        do {
            try {
                System.out.print("Enter your username:");
                String username = new Scanner(System.in).nextLine();
                System.out.print("Enter your password:");
                String password = new Scanner(System.in).nextLine();
                File file = new File("users.txt");
                if (file.exists()) {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()){
                        String[] userPass = scanner.nextLine().split(":");
                        if (userPass[0].equals(username)){
                            String[] keyPass = userPass[1].split("-");
                            String pass = decrypt(keyPass[0], Integer.parseInt(keyPass[1]));
                            if (pass.equals(password)){
                                seccess =true;
                                notFound = false;
                            }
                        }
                    }
                    if (notFound)
                        throw new UsernameNotFound();
                }
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
