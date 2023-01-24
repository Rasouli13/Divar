package Project;

import Project.Exceptions.*;
import Project.settings.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Advertisement_regist{
    private String imageAddress;
    private String adName;
    private String description;
    private double price;
    private String phoneNumber;
    private String adAddress;

    private LocalDateTime LastUpgrade;

    Advertisement_regist(clientManager client) throws IOException {
        addAdvertise(client);
    }

    public void addAdvertise(clientManager client) throws IOException {
        setAdName();
        setPrice();
        setImageAddress();
        setDescription();
        setAdAddress();
        setPhoneNumber();
        String fileName = this.adName.replaceAll("[\\/\\\\:?\"<>|*]", "");
        File adsFolder = new File("Project/Advertisements");
        adsFolder.mkdir();
        File file = new File("Project/Advertisements/" + fileName + ".txt");
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        fw.write("Name:" + this.adName +
                "\nPrice:" + this.price +
                "\nImage address:" + this.imageAddress +
                "\nAdvertisement address:" + this.adAddress +
                "\nPhone number:" + this.phoneNumber +
                "\nDescription:" + this.description+
                "\nLastUpgrade:" + this.LastUpgrade);
        fw.flush();
        //adding AD to user folder
        File[] users = new File("Project/users").listFiles();
        for (File user : Objects.requireNonNull(users)) {
            if (user.getName().equals(client.getUsername())) {
                File profile = new File("Project/users/" + client.getUsername() + "/profile.txt");
                String[] username = new Scanner(profile).nextLine().split(":");
                if (username[1].equals(client.getUsername())) {
                    File userAdsFolder = new File("Project/users/"+client.getUsername()+"/ads/");
                    userAdsFolder.mkdir();
                    File userAd = new File("Project/users/"+client.getUsername()+"/ads/"+fileName+".txt");
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
    }

    public void setAdAddress() {
        boolean success = false;
        while (!success) {
            try {
                clientManager.sendMessage("Enter Address(like-> " + "Tehran: Shahriari Square,..." + "), for Cancel press Enter:");
                String address = new Scanner(System.in).nextLine();
                if (address.equals("")) {
                    success = true;
                    break;
                }
                Matcher matcher = Pattern.compile("^([a-zA-Z]+: .+)").matcher(address);
                if (!matcher.matches())
                    throw new InvalidAddress();
                this.adAddress = address;
                success = true;
            } catch (InvalidAddress e) {
                clientManager.sendMessage(e.toString());
            }
        }
    }

    public void setAdName() {
        System.out.print("Enter advertisement name, for Cancel press Enter...:");
        String command = (new Scanner(System.in).nextLine());
        if (command.equals(""))
            return;
        this.adName = (new Scanner(System.in).nextLine());
    }

    public void setDescription(){
        System.out.print("Enter description, for Cancel press Enter:");
        String command = (new Scanner(System.in).nextLine());
        if (command.equals(""))
            return;
        this.description = (new Scanner(System.in).nextLine());
    }

    public void setPrice(){
        clientManager.sendMessage("Enter price, for Cancel press Enter:");
        String command = (new Scanner(System.in).nextLine());
        if (command.equals(""))
            return;
        this.price = (new Scanner(System.in).nextDouble());
    }

    public void setImageAddress(){
        clientManager.sendMessage("Enter image address, for Cancel press Enter:");
        String command = (new Scanner(System.in).nextLine());
        if (command.equals(""))
            return;
        this.imageAddress = (new Scanner(System.in).nextLine());
    }

    public void setPhoneNumber(){
        clientManager.sendMessage("Enter phone number, for Cancel press Enter:");
        String command = (new Scanner(System.in).nextLine());
        if (command.equals(""))
            return;
        this.phoneNumber = SetPhoneNumber.setPhoneNumber(command);
    }

    public void setLastUpgrade() {
        LastUpgrade = LocalDateTime.now();
        clientManager.sendMessage("Ad with name:" + this.adName + "Upgrade at" + LastUpgrade);
    }
}