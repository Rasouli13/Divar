package Project;

import Project.Exceptions.*;
import Project.settings.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Advertisement_regist{
    private String imageAddress;
    private String adName;
    private String description;
    private double price;
    private String phoneNumber;
    private String adAddress;
    private String city;
    private LocalDateTime LastUpgrade;
    public void addAdvertise(clientManager client) {
        try {
            setAdName(client);
            setPrice(client);
           // setImageAddress(client);
            setDescription(client);
            setAdAddress(client);
            setPhoneNumber(client);
            setLastUpgrade(client);
            String fileName = this.adName.replaceAll("[\\/\\\\:?\"<>|*]", "");
            File adsFolder = new File("C:/Divar/Advertisements");
            adsFolder.mkdir();
            File file = new File("C:/Divar/Advertisements/" + fileName + ".txt");
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            fw.write("Name:" + this.adName +
                    "\nPrice:" + this.price +
                    "\nImage address:" + this.imageAddress +
                    "\nAdvertisement address:" + this.adAddress +
                    "\nPhone number:" + this.phoneNumber +
                    "\nDescription:" + this.description +
                    "\nLastUpgrade:" + this.LastUpgrade);
            fw.flush();

            File cities = new File("C:/Divar/Advertisements/cities");
            cities.mkdir();
            File city = new File("C:/Divar/Advertisements/cities" + this.city);
            city.mkdir();
            file = new File("C:/Divar/Advertisements/cities" + this.city + "/" + fileName + ".txt");
            file.createNewFile();
            fw = new FileWriter(file);
            fw.write("Name:" + this.adName +
                    "\nPrice:" + this.price +
                    "\nImage address:" + this.imageAddress +
                    "\nAdvertisement address:" + this.adAddress +
                    "\nPhone number:" + this.phoneNumber +
                    "\nDescription:" + this.description +
                    "\nLastUpgrade:" + this.LastUpgrade);
            fw.flush();

            //adding AD to user folder
            File[] users = new File("C:/Divar/users").listFiles();
            for (File user : Objects.requireNonNull(users)) {
                if (user.getName().equals(client.getUsername())) {
                    File profile = new File("C:/Divar/users/" + client.getUsername() + "/profile.txt");
                    String[] username = new Scanner(profile).nextLine().split(":");
                    if (username[1].equals(client.getUsername())) {
                        File userAdsFolder = new File("C:/Divar/users/" + client.getUsername() + "/ads/");
                        userAdsFolder.mkdir();
                        File userAd = new File("C:/Divar/users/" + client.getUsername() + "/ads/" + fileName + ".txt");
                        userAd.createNewFile();
                        fw = new FileWriter(userAd, true);
                        fw.write("Name:" + this.adName +
                                "\nPrice" + this.price +
                                "\nImage address:" + this.imageAddress +
                                "\nAdvertisement address:" + this.adAddress +
                                "\nPhone number:" + this.phoneNumber +
                                "\nDescription:" + this.description +
                                "\nLastUpgrade:" + this.LastUpgrade);
                        fw.flush();
                    }
                }
            }
            fw.close();
        }catch (Exception e){
            client.sendMessage(e.toString());
        }
    }

    public void setAdAddress(clientManager client) {
        boolean success = false;
        while (!success) {
            try {
                client.sendMessage("Enter Address (like-> " + "Tehran: Shahriari Square,..." + "), or type cancel:");
                String address = new Scanner(System.in).nextLine();
                if (address.equals("cancel")) {
                    success = true;
                    break;
                }
                Matcher matcher = Pattern.compile("^([a-zA-Z]+: .+)").matcher(address);
                if (!matcher.matches())
                    throw new InvalidAddress();


                this.city = address.substring(0,address.indexOf(':'));
                this.adAddress = address;
                success = true;
            } catch (InvalidAddress e) {
                client.sendMessage(e.toString());
            }
        }
    }

    public void setAdName(clientManager client) {
        client.sendMessage("Enter advertisement name, or type cancel:");
        String command =client.getMessage();
        if (command.equals("cancel"))
            return;
        this.adName = command;
    }

    public void setDescription(clientManager client){
        client.sendMessage("Enter description, or type cancel:");
        String command = client.getMessage();
        if (command.equals("cancel"))
            return;
        this.description = command;
    }

    public void setPrice(clientManager client){
        client.sendMessage("Enter price, or type cancel:");
        String command = client.getMessage();
        if (command.equals("cencel"))
            return;
        this.price = Double.parseDouble(command);
    }

    public void setImageAddress(clientManager client){
        while(true){
            try {
                client.sendMessage("Enter image address, or type cancel:");
                String command = client.getMessage();
                if (command.equals("cancel"))
                    return;
                File imageAddress=new File(command);
                if(imageAddress.exists()){
                    this.imageAddress = command;
                    return;
                }
                 throw new AdvertisementImageException();
            }catch (AdvertisementImageException e){
                System.out.println(e);
            }
        }

    }

    public void setPhoneNumber(clientManager client){
        client.sendMessage("Enter phone number, or type cancel:");
        String command = client.getMessage();
        if (command.equals("cancel"))
            return;
        //this.phoneNumber =new SetPhoneNumber().setPhoneNumber(client,command);
    }

    public void setLastUpgrade(clientManager client) {
        LastUpgrade = LocalDateTime.now();
        client.sendMessage("Ad with name:" + this.adName + "Upgrade at" + LastUpgrade);
    }
}