package Project.settings;

import Project.Exceptions.InvalidPassword;
import Project.clientManager;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class setPassword {
    static public String setPassword(String password){
        boolean success = false;
        do {
            try {
                if(password.equals("cancel"))
                    break;
                if (!checkSpelling(password))
                    throw new InvalidPassword("Password must include 8 lowercase characters, numbers, and at least one of thsese: @#$%^&+!?=_");
                if (binaryNotFound(password) && aIsNotEnough(password))
                    throw new InvalidPassword("Dose not include binary or enough 'a'!'");
                if (hasOrdered(password))
                    throw new InvalidPassword("Do not use ordered numbers!");
                int key = new Random().nextInt(100)+1;
                 return encrypt(password,key);
            }catch (InvalidPassword e){
                clientManager.sendMessage(e.toString());
                clientManager.sendMessage("\nEnter valid password, or type cancel:");
                password = clientManager.getMessage();
            }catch (Exception e){
                System.out.println(e);
            }
        }while (!success);
        return "/CANCEL/";
    }



    //setPassword Methods---------------------------------------------------------------------------------------------------------------------

    private static boolean checkSpelling(String password){
        Matcher matcher = Pattern.compile("(?=[a-z0-9@#$%^&+!?=_]+$)^(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+!?=_])(?=.{8,}).*$").matcher(password);
        return matcher.matches();
    }
    private static boolean aIsNotEnough(String password){
        Matcher matcher1 = Pattern.compile("([a])").matcher(password);
        int aCounter=0;
        while (matcher1.find())
            aCounter++;
        return aCounter < 2;
    }

    private static boolean isOrder(String str) {
        for (int i = 0; i < str.length() / 2; i++) {
            String new_str = str.substring(0, i + 1);
            int num = Integer.parseInt(new_str);
            while (new_str.length() < str.length()) {
                num++;
                new_str = new_str.concat(String.valueOf(num));
            }
            if (new_str.equals(str))
                return true;
        }
        return false;
    }

    private static boolean hasOrdered(String password){
        String[] numbers = password.replaceAll("\\D"," ").split(" ");
        for (String num: numbers)
            if(!num.equals(""))
                if(isOrder(num))
                    return true;
        return false;
    }

    private static boolean binaryNotFound(String password){
        String[] numbers = password.replaceAll("\\D"," ").split(" ");
        for (String num: numbers)
            if(!num.equals("")){
                String[] n = num.split("");
                for (int i=0; i < n.length; i++)
                    for (int j = i; j < n.length;j++) {
                        String n1 = "";
                        n1 = n[j].concat(n1);
                        if(containBinary(n1))
                            return false;//BinaryFound
                    }
            }
        return true;
    }

    private static boolean containBinary(String s) {
        int n = Integer.parseInt(s);
        return (n & n-1)==0;
    }

    private static String encrypt(String password, int key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < password.length(); i++)
            sb.append(shiftForward(password.charAt(i), key));
        sb.append("-").append(key);
        return sb.toString();
    }

    private static char shiftForward(char c, int key) {
        key = key%26;
        if(Character.isLowerCase(c)) {
            if (((char) (c + key) <= 'z'))
                c = (char) (c + key);
            else
                c = (char) (c + key - 26);
        }
        if(Character.isUpperCase(c)){
            if (((char) (c + key) <= 'Z'))
                c = (char) (c + key);
            else
                c = (char) (c + key - 26);
        }

        return c;
    }



    //End setPassword Methods------------------------------------------------------------------------------------------------------------------------

}
