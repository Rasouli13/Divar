package Project;
import Project.sets.*;
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
        String username,password,email,fullName,phoneNumber;
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        System.out.print("Edit:\n" +
                "\t1.Username\n" +
                "\t2.Password\n" +
                "\t3.Email\n" +
                "\t4.Fullname\n" +
                "\t5.Phone number\n" +
                "\nChoose number:");
        boolean success = false;
        while (!success) {
            String edit = new Scanner(System.in).nextLine();
            switch (edit) {
                case "1":
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    System.out.print("Enter a username, for Exit press Enter:");
                    username = new Scanner(System.in).nextLine();
                    if(username.equals("")){
                        success=true;
                        break;
                    }
                    this.username = setUsername.setUsername(username);
                    success = true;
                    break;

                case "2":
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    System.out.print("Enter a password, for Exit press Enter:");
                    password = new Scanner(System.in).nextLine();
                    if(password.equals("")){
                        success=true;
                        break;
                    }
                    this.password = setPassword.setPassword(password);
                    success = true;
                    break;

                case "3":
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    System.out.print("Enter an email, for Exit press Enter:");
                    email = new Scanner(System.in).nextLine();
                    if(email.equals("")){
                        success=true;
                        break;
                    }
                    this.email = setEmail.setEmail(email);
                    success = true;
                    break;

                case "4":
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    System.out.print("Enter fullname, for Exit press Enter:");
                    fullName = new Scanner(System.in).nextLine();
                    if(fullName.equals("")){
                        success=true;
                        break;
                    }
                    this.fullName = fullName;
                    success = true;
                    break;

                case "5":
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    System.out.print("Enter phone number, for Exit press Enter:");
                    phoneNumber = new Scanner(System.in).nextLine();
                    if(phoneNumber.equals("")){
                        success=true;
                        break;
                    }
                    this.phoneNumber = SetPhoneNumber.setPhoneNumber(phoneNumber);
                    success = true;
                    break;

                default:
                    System.out.print("Enter a valid number:");
                    break;
            }
        }

    }
}
