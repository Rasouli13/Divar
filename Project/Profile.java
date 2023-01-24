package Project;
import Project.settings.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Profile {
    public void showProfile(clientManager client) throws Exception {
        File usersFolder = new File("Project/users");
        String username = client.getUsername();
        if(usersFolder.exists()) {
            File[] users = usersFolder.listFiles();
            for (File file : Objects.requireNonNull(users)) {
                if (file.getName().equals(username)) {
                    File profile = new File("Project/users/"+username+"/profile.txt");
                    Scanner scanner = new Scanner(profile);
                    String[] user = scanner.nextLine().split(":");
                    if(user[1].equals(username)) {
                        scanner = new Scanner(profile);
                        while (scanner.hasNextLine()) {
                            while (scanner.hasNextLine()) {
                                clientManager.sendMessage(scanner.nextLine());
                            }
                        }
                        while (true) {
                            clientManager.sendMessage("\n\n1.Edit\n2.Exit\n\nChoose number:");
                            String edit = clientManager.getMessage();
                            if (edit.equals("1")){
                                editProfile(file.getPath()+"/profile.txt",client);
                            }
                            else if (edit.equals("2")){
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private void editProfile(String baseAddress, clientManager client) throws IOException {
        clientManager.sendMessage("Edit:\n" +
                "\t1.Username\n" +
                "\t2.Password\n" +
                "\t3.Email\n" +
                "\t4.Fullname\n" +
                "\t5.Phone number\n" +
                "\nChoose number:");
        boolean success = false;
        while (!success) {
            String edit = clientManager.getMessage();
            switch (edit) {
                case "1":
                    clientManager.sendMessage("Enter a username, or type cancel:");
                    String newUsername = setUsername.setUsername(clientManager.getMessage());
                    if(newUsername.equals("cancel")){
                        success=true;
                        break;
                    }
                    editUserFile(baseAddress,"Username:",client.getUsername(),newUsername);
                    client.setUsername(newUsername);
                    success = true;
                    break;

                case "2":
                    clientManager.sendMessage("Enter a password, or type cancel:");
                    String newPassword = setPassword.setPassword(clientManager.getMessage());
                    if(newPassword.equals("cancel")){
                        success=true;
                        break;
                    }
                    editUserFile(baseAddress,"Password:",client.getPassword(),newPassword);
                    client.setPassword(newPassword);
                    success = true;
                    break;

                case "3":
                    clientManager.sendMessage("Enter an email, or type cancel:");
                    String newEmail = setEmail.setEmail(clientManager.getMessage());
                    if(newEmail.equals("cancel")){
                        success=true;
                        break;
                    }
                    editUserFile(baseAddress,"Email:",client.getEmail(),newEmail);
                    success = true;
                    break;

                case "4":
                    System.out.print("Enter fullname, or type cancel:");
                    String newFullName = clientManager.getMessage();
                    if(newFullName.equals("cancel")){
                        success=true;
                        break;
                    }
                    editUserFile(baseAddress,"Full name",client.getFullName(),newFullName);
                    client.setFullName(newFullName);
                    success = true;
                    break;

                case "5":
                    clientManager.sendMessage("Enter phone number, or type cancel:");
                    String newPhoneNumber = SetPhoneNumber.setPhoneNumber(clientManager.getMessage());
                    if(newPhoneNumber.equals("cancel")){
                        success=true;
                        break;
                    }
                    editUserFile(baseAddress,"Phone number:",client.getPhoneNumber(),newPhoneNumber);
                    client.setPhoneNumber(newPhoneNumber);
                    success = true;
                    break;

                default:
                    clientManager.sendMessage("Enter a valid number:");
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
