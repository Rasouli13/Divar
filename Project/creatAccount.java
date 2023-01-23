package Project;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Project.Exceptions.*;

public class creatAccount{
    private String username, password, email;
    public creatAccount() throws IOException, InterruptedException {
        addUser();
    }


    public void addUser() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter a Username: ");
        String username = scanner.nextLine();
        setUsername(username);

        System.out.print("\nEnter a Passwrd:");
        String password = scanner.nextLine();
        setPassword(password);

        System.out.print("\nEnter a Email:");
        String email = scanner.nextLine();
        setEmail(email);

        File file = new File("users.txt");

        if(!file.exists()){
            file.createNewFile();
            FileWriter fw = new FileWriter(file,true);
            fw.write(this.username + ":" + this.password + ":" + this.email);
            fw.flush();
            fw.close();
        }
        else {
            FileWriter fw = new FileWriter(file,true);
            fw.write("\n" + this.username + ":" + this.password + ":" + this.email);
            fw.flush();
            fw.close();
        }
    }

    private void setUsername(String username) throws IOException, InterruptedException {
        boolean success = false;
        do {
            try {
                Matcher matcher = Pattern.compile("^\\w+$").matcher(username);
                if (!matcher.matches())
                    throw new InvalidUsername("Invalid username!");
                File file = new File("users.txt");
                if (file.exists()) {
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNextLine()) {
                        String[] userPass = scanner.nextLine().split(":");
                        if (userPass[0].equals(username))
                            throw new InvalidUsername("This username already taken!");
                    }
                }
                this.username = username;
                success =true;
            }catch (InvalidUsername e){
                System.out.println(e);
                System.out.println("\npress any key to continue...");
                new Scanner(System.in).nextLine();
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.print("Enter a valid username:");
                username =  new Scanner(System.in).nextLine();
            }catch (IOException e){
                System.out.println("There is no User!");
            }catch (Exception e){
                System.out.println(e);
            }
        }while (!success);
    }

    private void setEmail(String email) throws IOException, InterruptedException {
        boolean success = false;
        do {
            try {
                Matcher matcher = Pattern.compile("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(email);
                if (matcher.matches()) {
                    this.email = email;
                    success = true;
                } else throw new InvalidEmailAddress();
            }catch (InvalidEmailAddress e){
                System.out.println(e);
                System.out.println("\npress any key to continue...");
                new Scanner(System.in).nextLine();
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.print("Enter a valid email:");
                email = new Scanner(System.in).nextLine();
            }catch (Exception e){
                System.out.println(e);
                System.out.println("\npress any key to continue...");
                new Scanner(System.in).nextLine();
            }
        }while (!success);
    }

    private void setPassword(String password) throws IOException, InterruptedException {
        boolean success = false;
        do {
            try {
                if (!checkSpelling(password))
                    throw new InvalidPassword("Password must include 8 lowercase characters!");
                if (binaryNotFound(password) && aIsNotEnough(password))
                    throw new InvalidPassword("Dose not include binary or enough 'a'!'");
                if (hasOrdered(password))
                    throw new InvalidPassword("Do not use ordered numbers!");
                int key = new Random().nextInt(100)+1;
                this.password = encrypt(password,key);
                success = true;
            }catch (InvalidPassword e){
                System.out.println(e);
                System.out.println("\npress any key to continue...");
                new Scanner(System.in).nextLine();
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.print("Enter valid password:");
                password = new Scanner(System.in).nextLine();
            }catch (Exception e){
                System.out.println(e);
            }
        }while (!success);
    }



    //setPassword Methods---------------------------------------------------------------------------------------------------------------------

    private boolean checkSpelling(String password){
        Matcher matcher = Pattern.compile("(?=[a-z0-9@#$%^&+!?=_]+$)^(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+!?=_])(?=.{8,}).*$").matcher(password);
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

    private String encrypt(String password, int key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < password.length(); i++)
            sb.append(shiftForward(password.charAt(i), key));
        sb.append("-").append(key);
        return sb.toString();
    }

    private char shiftForward(char c, int key) {
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