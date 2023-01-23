package Project;

import Project.Exceptions.invalidPhoneNumberException;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetPhoneNumber {
    public static String setPhoneNumber() {
        while(true){
            try {
                System.out.println("Enter phoneNumber in format +989123545678, for Exit press enter");
                String phone=(new Scanner(System.in).nextLine());
                Matcher matcher= Pattern.compile("[+98]\\d{12}").matcher(phone);
                if(phone.equals("Exit"))
                    break;
                if(!matcher.matches())
                    throw new invalidPhoneNumberException();
                return phone;
            }catch (invalidPhoneNumberException e){
                System.out.println(e);
            }
        }
        return "";
    }
}
