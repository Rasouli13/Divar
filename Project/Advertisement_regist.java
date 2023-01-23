package Project;

import Project.Exceptions.*;

import javax.jws.Oneway;
import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Advertisement_regist {
    private String imageAddress;
    private String adName;
    private String description;
    private double price;
    private String phone;
    private String adAddress;
    Advertisement_regist() throws IOException {
         addAdvertise();
    }
    public void addAdvertise() throws IOException {
        setAdName();
        setPrice();
        setImageAddress();
        setDescription();
        setAdAddress();
        this.phone=SetPhoneNumber.setPhoneNumber();
        File adsFolder = new File("/Advertisements");
        adsFolder.mkdir();

        String fileName = this.adName.replaceAll("[\\/\\\\:?\"<>|*]","");
        File file = new File("/Advertisements/"+fileName+".txt");
        file.createNewFile();

        FileWriter fw = new FileWriter(file);
        fw.write("Name:"+this.adName+
                    "\nPrice"+this.price+
                    "");
    }
//    public boolean checkName(String nameAdvertise) {
//        Scanner scanner;
//        try {
//             scanner=new Scanner(Advertisements);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        while (scanner.hasNextLine()){
//            if(scanner.nextLine().contains(nameAdvertise))
//                return true;
//        }
//        return false;
//    }
    public void setAdAddress() {
        boolean success = false;
        while (!success) {
            try {
                System.out.println("Enter Address(like-> " + "Tehran: Shahriari Square,..." + "):");
                String address = new Scanner(System.in).nextLine();
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
        System.out.print("press Enter to exit else Enter advertisement name:");
        String command=(new Scanner(System.in).nextLine());
        if(command.equals(""))
            return;
        this.adName = (new Scanner(System.in).nextLine());
    }

    public void setDescription() {
        System.out.print("press Enter to exit else Enter description:");
        String command=(new Scanner(System.in).nextLine());
        if(command.equals(""))
            return;
        this.description = (new Scanner(System.in).nextLine());
    }

    public void setPrice() {
        System.out.println("press Enter to exit else Enter Price");
        String command=(new Scanner(System.in).nextLine());
        if(command.equals(""))
            return;
        this.price =(new Scanner(System.in).nextDouble());
    }

    public void setImageAddress() {
        System.out.println("press Enter to exit else Enter imageAddress");
        String command=(new Scanner(System.in).nextLine());
        if(command.equals(""))
            return;
        this.imageAddress =(new Scanner(System.in).nextLine());
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
