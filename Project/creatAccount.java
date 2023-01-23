package Project;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Project.Exceptions.*;

public class creatAccount{
    private String username, password, email;
    public creatAccount(String username,String password,String email) throws InvalidUsername, IOException, InvalidPassword, InvalidEmailAddress {
        addUser(username,password,email);
    }


    private void addUser(String username, String password, String email) throws IOException, InvalidUsername, InvalidPassword, InvalidEmailAddress {
        setUsername(username);
        setPassword(password);
        setEmail(email);
        File file = new File("users.txt");
        FileWriter fw = new FileWriter(file,true);
        if(!file.exists()){
            file.createNewFile();
            fw.write(this.username + ":" + this.password + ":" + this.email);
            fw.flush();
        }
        else {
            fw.write("\n" + this.username + ":" + this.password + ":" + this.email);
            fw.flush();
        }
    }

    private void setUsername(String username) throws IOException, InvalidUsername {
        Matcher matcher = Pattern.compile("^\\w+$").matcher(username);
        if(!matcher.matches())
            throw new InvalidUsername("Invalid username!");
        File file = new File("users.txt");
        if(file.exists()) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String[] userPass = scanner.nextLine().split(":");
                if (userPass[0].equals(username))
                    throw new InvalidUsername("This username already taken!");
            }
        }
        this.username = username;
    }

    private void setEmail(String email) throws InvalidEmailAddress {
        Matcher matcher = Pattern.compile("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(email);
        if(matcher.matches())
            this.email = email;
        else throw new InvalidEmailAddress();
    }

    private void setPassword(String password) throws InvalidPassword{
        if (!checkSpelling(password))
            throw new InvalidPassword("Password must include 8 lowercase characters!");
        if(binaryNotFound(password) && aIsNotEnough(password))
            throw new InvalidPassword("Dose not include binary or enough 'a'!'");
        if( hasOrdered(password))
            throw new InvalidPassword("Do not use ordered numbers!");

        this.password = password;
    }



    //setPassword Methods---------------------------------------------------------------------------------------------------------------------

    private boolean checkSpelling(String password){
        Matcher matcher = Pattern.compile("(?=[a-z0-9@#$%^&+!=]+$)^(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+!=])(?=.{8,}).*$").matcher(password);
        return matcher.matches();
    }
    private boolean aIsNotEnough(String password){
        Matcher matcher1 = Pattern.compile("([a])").matcher(password);
        int aCounter=0;
        while (matcher1.find())
            aCounter++;
        return aCounter < 2;
    }

    private boolean isOrder(String str) {
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

    private boolean hasOrdered(String password){
        String[] numbers = password.replaceAll("\\D"," ").split(" ");
        for (String num: numbers)
            if(!num.equals(""))
                if(isOrder(num))
                    return true;
        return false;
    }

    private boolean binaryNotFound(String password){
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

    private boolean containBinary(String s) {
        int n = Integer.parseInt(s);
        return (n & n-1)==0;
    }

    //End setPassword Methods------------------------------------------------------------------------------------------------------------------------
}