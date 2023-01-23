package Project;
import Project.settings.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Profile extends User {
    public void showProfile() throws Exception {
        File usersFolder = new File("Project/users");
        if(usersFolder.exists()) {
            File[] users = usersFolder.listFiles();
            for (File file : Objects.requireNonNull(users)) {
                if (file.exists()) {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        String[] user = scanner.nextLine().split(":");
                        if (user[1].equals(this.username)) {
                            scanner = new Scanner(file);
                            while (scanner.hasNextLine()){
                                System.out.println(scanner.nextLine());
                            }
                            while (true) {
                                System.out.print("\n\n1.Edit\n2.Exit\n\nChoose number:");
                                String edit = new Scanner(System.in).nextLine();
                                if (edit.equals("1")){
                                    editProfile(file.getPath());
                                }
                                else if (edit.equals("2")){
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void editProfile(String baseAddress) throws IOException, InterruptedException {
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
                    editUserFile(baseAddress,"Username:",this.username,username);
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
                    editUserFile(baseAddress,"Password:",this.password,setPassword.setPassword(password));
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
                    editUserFile(baseAddress,"Email:",this.email,setEmail.setEmail(email));
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
                    editUserFile(baseAddress,"Full name",this.fullName,fullName);
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
                    editUserFile(baseAddress,"Phone number:",this.phoneNumber,SetPhoneNumber.setPhoneNumber(phoneNumber));
                    this.phoneNumber = SetPhoneNumber.setPhoneNumber(phoneNumber);
                    success = true;
                    break;

                default:
                    System.out.print("Enter a valid number:");
                    break;
            }
        }
    }

    private void editUserFile(String baseAddress,String key,String old, String New) throws IOException {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(Path.of(baseAddress), StandardCharsets.UTF_8));
        for (int i = 0; i < fileContent.size(); i++) {
            System.out.println(fileContent.get(i));
            if (fileContent.get(i).equals(key+old)) {
                fileContent.set(i,(key+New));
                break;
            }
        }
        Files.write(Path.of(baseAddress), fileContent, StandardCharsets.UTF_8);
    }
}
