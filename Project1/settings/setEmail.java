package Project.settings;

import Project.Exceptions.InvalidEmailAddress;
import Project.clientManager;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class setEmail {
    public static
    String setEmail(String email){
        boolean success = false;
        do {
            try {
                if(email.equals("cancel"))
                    break;
                Matcher matcher = Pattern.compile("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(email);
                if (matcher.matches()) {
                    return email;
                } else throw new InvalidEmailAddress();
            }catch (InvalidEmailAddress e){
                clientManager.sendMessage(e.toString());
                clientManager.sendMessage("\nEnter a valid email, or type cancel:");
                email = clientManager.getMessage();
            }catch (Exception e) {
                clientManager.sendMessage(e.toString());
            }
        }while (!success);
        return "/CANCEL/";
    }
}
