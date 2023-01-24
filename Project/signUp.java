package Project;

import java.io.*;

public class signUp {
    public signUp(String username, String password, String email){
        addUser(username, password, email);
    }


    public void addUser(String username, String password, String email){
        try {
            File usersFolder = new File("Project/users/" + username);
            usersFolder.mkdir();
            File file = new File("Project/users/" + username + "/profile.txt");
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            fw.write("Username:" + username +
                    "\nPassword:" + password +
                    "\nEmail:" + email +
                    "\nFull name:" +
                    "\nPhone number:");
            fw.flush();
            fw.close();
        }catch (Exception e){
            clientManager.sendMessage(e.toString());
        }
    }
}
