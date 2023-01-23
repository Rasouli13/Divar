package Project;

import java.io.*;
import java.util.*;

public class Profile extends User {
    public void showProfile() throws Exception {
        File usersFolder = new File("/users");
        if(usersFolder.exists()) {
            File[] users = usersFolder.listFiles();
            for (File file : Objects.requireNonNull(users)) {
                if (file.exists()) {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        String[] userPassEmail = scanner.nextLine().split(":");
                        if (userPassEmail[0].equals(this.username)) {
                            scanner = new Scanner(file);
                            while (scanner.hasNextLine()){
                                System.out.println(scanner.nextLine());
                            }
                        }
                    }
                }
            }
        }
        while (true) {
            System.out.print("1.Edit\n2.Exit\n\nChoose number:");
            String edit = new Scanner(System.in).nextLine();
            if (edit.equals("1")){
                editProfile();
            }
            else if (edit.equals("2")){
                break;
            }
        }
    }

    public void editProfile() throws IOException, InterruptedException {
        String username,password,email,fullName,location,phoneNumber;
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        System.out.print("Edit:\n" +
                "\t1.Username\n" +
                "\t2.Password\n" +
                "\t3.Email\n" +
                "\t4.Fullname\n" +
                "\t5.Location\n" +
                "\t6.Phone number\n" +
                "\nChoose number:");
        String edit = new Scanner(System.in).nextLine();
        boolean success = false;
        while (!success)
        switch (edit){
            case "1":
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.print("Enter a username");
                username = new Scanner(System.in).nextLine();
                success = true;
                break;
            case "2":
                success = true;
                break;
            case "3":
                success = true;
                break;
            case "4":
                success = true;
                break;
            case "5":
                success = true;
                break;
            case "6":
                success = true;
                break;
            default:
                System.out.print("Enter a valid number:");
                edit = new Scanner(System.in).nextLine();
                break;
        }
    }


}
