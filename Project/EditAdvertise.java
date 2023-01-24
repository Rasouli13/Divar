package Project;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EditAdvertise {
    File subjectFile;

    EditAdvertise(clientManager client) throws IOException {
       EditAds(client);
    }
public void EditAds(clientManager client) throws IOException {
    clientManager.sendMessage("Enter your advertise name for edit");
    String adName=clientManager.getMessage();
    subjectFile=new File("Project/users/"+client.getUsername()+"/ads/"+adName+".txt");
    boolean success=false;
    if(subjectFile.exists()){
        while (!success){
            clientManager.sendMessage("1.Name"+
                    "\n2.Price"+
                    "\n3.Image address"+
                    "\n4.Advertisement address"+
                    "\n5.Phone number"+
                    "\n6.Description"+
            "\n7.Cancel editing"+
                    "\n\nChoose number:");
            switch (clientManager.getMessage()){
                case "1":
                    EditName();EditUpgrade();break;
                case "2":
                    EditPrice();EditUpgrade();break;
                case "3":
                    EditImageAddress();EditUpgrade();break;
                case "4":
                    EditAdsAddress();EditUpgrade();break;
                case "5":
                    EditPhoneNumber();EditUpgrade();break;
                case "6":
                    EditDescription();EditUpgrade();break;
                case "7":
                    success=true;break;
            }
        }
    }
}

public void EditName() throws IOException {
    clientManager.sendMessage("Enter your new advertisement name, or type cancel:");
    String newAdsName=clientManager.getMessage();
    if(newAdsName.equals("cancel"))
        return;
    String oldAdsName=Files.readAllLines(Paths.get(subjectFile.getPath())).get(0).substring(5);
    editUserAd(subjectFile.getPath(),"Name:",oldAdsName,newAdsName);
}
    public void EditPrice() throws IOException {
        clientManager.sendMessage("Enter your new Ads price, or type cancel:");
        String newAdsPrice=clientManager.getMessage();
        if(newAdsPrice.equals("cancel"))
            return;
        String oldAdsPrice=Files.readAllLines(Paths.get(subjectFile.getPath())).get(1).substring(14);
        editUserAd(subjectFile.getPath(),"Price:",oldAdsPrice,newAdsPrice);
    }
    public void EditImageAddress() throws IOException {
        clientManager.sendMessage("Enter your new image address, or type cancel:");
        String newImageAddress=clientManager.getMessage();
        if(newImageAddress.equals("cancel"))
            return;
        String oldImageAddress=Files.readAllLines(Paths.get(subjectFile.getPath())).get(2).substring(14);
        editUserAd(subjectFile.getPath(),"Image address:",oldImageAddress,newImageAddress);
    }
    public void EditAdsAddress() throws IOException {
        clientManager.sendMessage("Enter your new advertisement address, or type cancel:");
        String newAdsAddress=clientManager.getMessage();
        if(newAdsAddress.equals("cancel"))
            return;
        String oldAdsAddress=Files.readAllLines(Paths.get(subjectFile.getPath())).get(3).substring(22);
        editUserAd(subjectFile.getPath(),"advertisement address:",oldAdsAddress,newAdsAddress);
    }
    public void EditPhoneNumber() throws IOException {
        clientManager.sendMessage("Enter your new phone number, or type cancel:");
        String newPhoneNumber=clientManager.getMessage();
        if(newPhoneNumber.equals("cancel"))
            return;
        String oldPhoneNumber=Files.readAllLines(Paths.get(subjectFile.getPath())).get(4).substring(13);
        editUserAd(subjectFile.getPath(),"Phone number:",oldPhoneNumber,newPhoneNumber);
    }
    public void EditDescription() throws IOException {
        clientManager.sendMessage("Enter your new Description in single line, or type cancel:");
        String newDescription=clientManager.getMessage();
        if(newDescription.equals("cancel"))
            return;
        String oldDescription=Files.readAllLines(Paths.get(subjectFile.getPath())).get(5).substring(12);
        editUserAd(subjectFile.getPath(),"Description:",oldDescription,newDescription);
    }

    public void EditUpgrade() throws IOException {
        String lastUpgrade=LocalDateTime.now().toString();
        clientManager.sendMessage("Advertise Upgraded at"+lastUpgrade);
        String oldTime=Files.readAllLines(Paths.get(subjectFile.getPath())).get(6).substring(12);
        editUserAd(subjectFile.getPath(),"LastUpgrade:",oldTime,lastUpgrade);
    }
    private void editUserAd(String baseAddress,String key,String old, String New) throws IOException {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(baseAddress), StandardCharsets.UTF_8));
        for (int i = 0; i < fileContent.size(); i++) {
            System.out.println(fileContent.get(i));
            if (fileContent.get(i).equals(key+old)) {
                fileContent.set(i,(key+New));
                break;
            }
        }
        Files.write(Paths.get(baseAddress), fileContent, StandardCharsets.UTF_8);
    }
}
