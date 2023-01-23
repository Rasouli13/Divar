package Project.settings;

import Project.Exceptions.InvalidEmailAddress;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class setEmail {
    public static
    String setEmail(String email) throws IOException, InterruptedException {
        boolean success = false;
        do {
            try {
                if(email.equals(""))
                    break;
                Matcher matcher = Pattern.compile("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(email);
                if (matcher.matches()) {
                    return email;
                } else throw new InvalidEmailAddress();
            }catch (InvalidEmailAddress e){
                System.out.println(e);
                System.out.println("\npress Enter to continue...");
                new Scanner(System.in).nextLine();
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.print("Enter a valid email:");
                email = new Scanner(System.in).nextLine();
            }catch (Exception e){
                System.out.println(e);
                System.out.println("\npress Enter to continue...");
                new Scanner(System.in).nextLine();
            }
        }while (!success);
        return email;
    }
}
