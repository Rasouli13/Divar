package Project;

import Project.Exceptions.*;
import Project.settings.*;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Advertisement_regist extends User {
    private String imageAddress;
    private String adName;
    private String description;
    private double price;
    private String phoneNumber;
    private String adAddress;
    Advertisement_regist() throws IOException, InterruptedException {
         addAdvertise();
    }
    public void addAdvertise() throws IOException, InterruptedException {
        setAdName();
        setPrice();
        setImageAddress();
        setDescription();
        setAdAddress();
        setPhoneNumber();
        String fileName = this.adName.replaceAll("[\\/\\\\:?\"<>|*]","");
        File adsFolder = new File("/Advertisements");
        adsFolder.mkdir();
        File file = new File("Project/Advertisements/"+fileName+".txt");
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        fw.write("Name:"+this.adName+
                    "\nPrice"+this.price+
                    "\nImage address:"+this.imageAddress+
                    "\nAdvertisement address:"+this.adAddress+
                    "\nPhone number:"+this.phoneNumber+
                    "\nDescription:" +this.description);
        fw.flush();
        //adding AD to user folder
        String userFileName = this.username.replaceAll("[\\/\\\\:?\"<>|*]","");;
        File[] users = new File("Project/users").listFiles();
        for (File user : Objects.requireNonNull(users)){
            if(user.getName().equals(userFileName)){
                File profile = new File("Project/users/"+userFileName+"/profile.txt");
                String[] username = new Scanner(profile).nextLine().split(":");
                if(username[1].equals(this.username)){
                    File ad = new File("Project/users/"+fileName+"/Ads.txt");
                    ad.createNewFile();
                    fw = new FileWriter(ad,true);
                    fw.write("Name:"+this.adName+
                            "\nPrice"+this.price+
                            "\nImage address:"+this.imageAddress+
                            "\nAdvertisement address:"+this.adAddress+
                            "\nPhone number:"+this.phoneNumber+
                            "\nDescription:" +this.description+
                            "\n\n");
                    fw.flush();
                }
            }
        }
        fw.close();
    }

    public void setAdAddress() {
        boolean success = false;
        while (!success) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.println("Enter Address(like-> " + "Tehran: Shahriari Square,..." + "), for Cancel press Enter:");
                String address = new Scanner(System.in).nextLine();
                if (address.equals("")){
                    success = true;
                    break;
                }
                Matcher matcher = Pattern.compile("^([a-zA-Z]+: .+)").matcher(address);
                if(!matcher.matches())
                    throw new InvalidAddress();
                this.adAddress = address;
                success = true;
            }catch (InvalidAddress | IOException | InterruptedException e){
                System.out.println(e);
            }
        }
    }

    public void setAdName() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        System.out.print("Enter advertisement name, for Cancel press Enter...:");
        String command=(new Scanner(System.in).nextLine());
        if(command.equals(""))
            return;
        this.adName = (new Scanner(System.in).nextLine());
    }

    public void setDescription() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        System.out.print("Enter description, for Cancel press Enter:");
        String command=(new Scanner(System.in).nextLine());
        if(command.equals(""))
            return;
        this.description = (new Scanner(System.in).nextLine());
    }

    public void setPrice() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        System.out.println("Enter price, for Cancel press Enter:");
        String command=(new Scanner(System.in).nextLine());
        if(command.equals(""))
            return;
        this.price =(new Scanner(System.in).nextDouble());
    }

    public void setImageAddress() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        System.out.println("Enter image address, for Cancel press Enter:");
        String command=(new Scanner(System.in).nextLine());
        if(command.equals(""))
            return;
        this.imageAddress =(new Scanner(System.in).nextLine());
    }
    public void setPhoneNumber() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        System.out.println("Enter phone number, for Cancel press Enter:");
        String command=(new Scanner(System.in).nextLine());
        if(command.equals(""))
            return;
        this.phoneNumber = SetPhoneNumber.setPhoneNumber(command);
    }
}
