package Project.sets;

import Project.Exceptions.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetPhoneNumber {
    public static String setPhoneNumber(String phoneNumber) throws IOException, InterruptedException {
        while(true){
            try {
                Matcher matcher= Pattern.compile("[+98]\\d{12}").matcher(phoneNumber);
                if(phoneNumber.equals(""))
                    break;
                if(!matcher.matches())
                    throw new invalidPhoneNumberException();
                return phoneNumber;
            }catch (invalidPhoneNumberException e){
                System.out.println(e);
                System.out.println("\npress Enter to continue...");
                new Scanner(System.in).nextLine();
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.println("Enter phone number in format +989123545678, for Exit press enter");
                phoneNumber=(new Scanner(System.in).nextLine());
            }
        }
        return "";
    }
}
