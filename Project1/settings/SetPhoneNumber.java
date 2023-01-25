package Project.settings;

import Project.Exceptions.*;
import Project.clientManager;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetPhoneNumber {
    public static String setPhoneNumber(String phoneNumber){
        while(true){
            try {
                if(phoneNumber.equals("cancel"))
                    break;
                Matcher matcher= Pattern.compile("[+98]\\d{12}").matcher(phoneNumber);
                if(!matcher.matches())
                    throw new invalidPhoneNumberException();
                return phoneNumber;
            }catch (invalidPhoneNumberException e){
                clientManager.sendMessage(e.toString());
                clientManager.sendMessage("\nEnter phone number in format +989123545678, or type cancel");
                phoneNumber= clientManager.getMessage();
            }
        }
        return "/CANCEL/";
    }
}
