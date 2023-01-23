package Project;

import Project.sets.*;
import java.io.*;
import java.util.Scanner;

public class creatAccount{
    private String username, password, email;
    public creatAccount() throws IOException, InterruptedException {
        addUser();
    }


    public void addUser() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a Username: ");
        String username = scanner.nextLine();
        this.username= setUsername.setUsername(username);

        System.out.print("Enter a Password:");
        String password = scanner.nextLine();
        this.password = setPassword.setPassword(password);

        System.out.print("Enter a Email:");
        String email = scanner.nextLine();
        this.email = setEmail.setEmail(email);



        String fileName = this.username.replaceAll("[\\/\\\\:?\"<>|*]","");
        File usersFolder = new File("/users");
        usersFolder.mkdir();
        File file = new File("/users/"+fileName+".txt");
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        fw.write("Username:"+this.username +
                "\nPassword:" + this.password +
                "\nEmail:" + this.email +
                "\nFullname:" +
                "\nPhone number:");
        fw.flush();
        fw.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        creatAccount ca = new creatAccount();
    }
}
