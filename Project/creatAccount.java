package Project;

import Project.settings.*;
import java.io.*;
import java.util.Scanner;

public class creatAccount {
    private String username, password, email;

    public creatAccount(String username, String password, String email){
        addUser(username, password, email);
    }


    public void addUser(String username, String password, String email) throws IOException {
        this.username = setUsername.setUsername(username);
        this.password = setPassword.setPassword(password);
        this.email = setEmail.setEmail(email);
        String fileName = this.username.replaceAll("[\\/\\\\:?\"<>|*]", "");
        File usersFolder = new File("Project/users/" + fileName);
        usersFolder.mkdir();
        File file = new File("Project/users/" + fileName + "/profile.txt");
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        fw.write("Username:" + this.username +
                "\nPassword:" + this.password +
                "\nEmail:" + this.email +
                "\nFull name:" +
                "\nPhone number:");
        fw.flush();
        fw.close();
    }
}
