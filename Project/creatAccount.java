package Project;

import Project.settings.*;
import java.io.*;
import java.util.Scanner;

public class creatAccount{
    private String username, password, email;
    public creatAccount() throws IOException, InterruptedException {
        addUser();
    }


    public void addUser() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("press Enter to cancel..." +
                "\n\tEnter a Username:");
        String username = scanner.nextLine();
        if (username.equals(""))
            return;
        this.username= setUsername.setUsername(username);

        System.out.print("press Enter to cancel..." +
                "\n\tEnter a Password:");
        String password = scanner.nextLine();
        if(password.equals(""))
            return;
        this.password = setPassword.setPassword(password);

        System.out.print("press Enter to cancel..." +
                        "\n\tEnter a Email:");
        String email = scanner.nextLine();
        if(email.equals(""))
            return;
        this.email = setEmail.setEmail(email);



        String fileName = this.username.replaceAll("[\\/\\\\:?\"<>|*]","");
        File usersFolder = new File("Project/users/"+fileName);
        usersFolder.mkdir();
        File file = new File("Project/users/"+fileName+"/profile.txt");
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        fw.write("Username:" + this.username +
                "\nPassword:" + this.password +
                "\nEmail:" + this.email +
                "\nFull name:" +
                "\nPhone number:");
        fw.flush();
        fw.close();
        System.out.println("Congratulations!" +
                                "\n\tYour account created successfully!" +
                            "\n\npress Enter to continue in main menu...");
        new Scanner(System.in).nextLine();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        creatAccount ca = new creatAccount();
    }
}
