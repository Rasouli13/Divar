package Project;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Project.Exceptions.*;

public class creatAccount{

    public void setUsername(String username) throws IOException, InvalidUsername {
        Matcher matcher = Pattern.compile("^\\w+$").matcher(username);
        if(!matcher.matches())
            throw new InvalidUsername("Invalid username!");
        File file = new File("users.txt");
        FileWriter fw = new FileWriter(file,true);
        if(!file.exists()){
            file.createNewFile();
            fw.write(username+ ":");
            fw.flush();
        }
        else {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String[] userPass = scanner.nextLine().split(":");
                if(userPass[0].equals(username))
                    throw new InvalidUsername("This username already taken!");
            }
            fw.write(username+ ":");
            fw.flush();
        }
    }


    public void setPassword(String password) throws InvalidPassword, IOException {

        if (!checkSpelling(password))
            throw new InvalidPassword();
        if(binaryNotFound(password) && aIsNotEnough(password))
            throw new InvalidPassword();
        if( hasConsecutive(password))
            throw new InvalidPassword();


    }
    public boolean checkSpelling(String password){
        Matcher matcher = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[a-z]).{8,}$").matcher(password);
        return matcher.matches();
    }
    public boolean aIsNotEnough(String password){
        Matcher matcher1 = Pattern.compile("([a])").matcher(password);
        int aCounter=0;
        while (matcher1.find())
            aCounter++;
        return aCounter < 2;
    }

    public boolean isConsecutive(String str) {
        int start;
        for (int i = 0; i < str.length() / 2; i++) {
            String new_str = str.substring(0, i + 1);
            int num = Integer.parseInt(new_str);
            start = num;
            while (new_str.length() < str.length()) {
                num++;
                new_str = new_str.concat(String.valueOf(num));
            }
            if (new_str.equals(str))
                return true;
        }
        return false;
    }

    public boolean hasConsecutive(String password){
        String[] numbers = password.replaceAll("\\D"," ").split(" ");
        for (String num: numbers)
            if(!num.equals(""))
                if(isConsecutive(num))
                    return true;
        return false;
    }

    public boolean binaryNotFound(String password){
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

    public boolean containBinary(String s) {
        int n = Integer.parseInt(s);
        return (n & n-1)==0;
    }
    public static void main(String[] args) throws InvalidUsername, IOException, InvalidPassword {

    }
}