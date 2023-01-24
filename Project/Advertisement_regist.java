package Project;

import Project.Exceptions.*;
import Project.settings.*;
import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Advertisement_regist {
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
        File adsFolder = new File("Project/Advertisements");
        adsFolder.mkdir();

        String fileName = this.adName.replaceAll("[\\/\\\\:?\"<>|*]","");
        File file = new File("Project/Advertisements/"+fileName+".txt");
        file.createNewFile();

        FileWriter fw = new FileWriter(file);
        fw.write("Name: "+this.adName+
                    "\nPrice: "+this.price+
                    "\nImage address: "+this.imageAddress+
                "\nAd address: "+this.imageAddress+
                "\nPhone number: "+this.phoneNumber+
                "\nDescription: "+this.description);
    }

    public void setAdAddress() {
        boolean success = false;
        while (!success) {
            try {
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
            }catch (InvalidAddress e){
                System.out.println(e);
            }
        }
    }

    public void setAdName() {
        System.out.print("Enter advertisement name, for Cancel press Enter...:");
        String command=(new Scanner(System.in).nextLine());
        if(command.equals(""))
            return;
        this.adName = (new Scanner(System.in).nextLine());
    }

    public void setDescription() {
        System.out.print("Enter description, for Cancel press Enter:");
        String command=(new Scanner(System.in).nextLine());
        if(command.equals(""))
            return;
        this.description = (new Scanner(System.in).nextLine());
    }

    public void setPrice() {
        System.out.println("Enter price, for Cancel press Enter:");
        String command=(new Scanner(System.in).nextLine());
        if(command.equals(""))
            return;
        this.price =(new Scanner(System.in).nextDouble());
    }

    public void setImageAddress() {
        System.out.println("Enter image address, for Cancel press Enter:");
        String command=(new Scanner(System.in).nextLine());
        if(command.equals(""))
            return;
        this.imageAddress =(new Scanner(System.in).nextLine());
    }
    public void setPhoneNumber() throws IOException, InterruptedException {
        System.out.println("Enter phone number, for Cancel press Enter:");
        String command=(new Scanner(System.in).nextLine());
        if(command.equals(""))
            return;
        this.imageAddress =(new Scanner(System.in).nextLine());
        this.phoneNumber=SetPhoneNumber.setPhoneNumber(command);
    }


    public String getAdAddress() {
        return adAddress;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImageAddress() {
        return imageAddress;
    }


    public String getAdName() {
        return adName;
    }
}
