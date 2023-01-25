package Project;
import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class  AdvertisementsPages {

    File subjectFile;

    File searchResult;

    ArrayList<String> Sort;

    AdvertisementsPages(clientManager client) throws IOException {
        boolean success=false;
        while (!success){
            client.sendMessage("1.Sort"+
                    "\n2.Search"+
                    "\n3.Cancel"+
                    "\n\nchoose number:");
            switch (client.getMessage()){
                case "1":
                    SortInAds(client);
                case "2":
                    SearchInAds(client);
                case "3":
                    success=true;break;
                default:
                    client.sendMessage("enter valid number:");break;
            }
        }
    }

    public void SortInAds(clientManager client) throws IOException {
        boolean success=false;
        while (!success){
            client.sendMessage("Sort By:"+
                    "\n1.price increase"+
                    "\n2.price decrease"+
                    "\n3.price range"+
                    "\n4.LastUpgrade"+
                    "\n5.Cancel"+
                    "\n\nChoose valid number:");
            switch (client.getMessage()){
                case "1":
                    SortPriceIncrease(client);break;
                case "2":
                    SortPriceDecrease(client);break;
                case "3":
                    SortPriceRange(client);break;
                case "4":
                    SortLastUpgrade(client);break;
                case "5":
                    success=true;break;
                default:
                    client.sendMessage("Enter valid number:");
            }
        }
    }

    public void SortPriceIncrease(clientManager client) throws IOException {
        Sort=new ArrayList<>();
        File[] Advertises=new File("C:/Divar/Advertisements").listFiles();
        for (int i=0;i< Advertises.length;i++){
            for (int j=0;j< Advertises.length-i-1;j++){
                if(Files.readAllLines(Paths.get(Advertises[j].getPath())).get(1).substring(14).compareTo(Files.readAllLines(Paths.get(Advertises[j+1].getPath())).get(1).substring(14))>=1){
                    File temp=Advertises[j];
                    Advertises[j+1]=Advertises[j];
                    Advertises[j+1]=temp;
                }
            }
        }
        for (int i=0;i<Advertises.length;i++){
            Sort.add(Files.readAllLines(Paths.get(Advertises[i].getPath())).get(0).substring(5));
        }
    }
    public void SortPriceDecrease(clientManager client) throws IOException {
        Sort=new ArrayList<>();
        File[] Advertises=new File("C:/Divar/Advertisements").listFiles();
        for (int i=0;i< Advertises.length;i++){
            for (int j=0;j< Advertises.length-i-1;j++){
                if(Files.readAllLines(Paths.get(Advertises[j].getPath())).get(1).substring(14).compareTo(Files.readAllLines(Paths.get(Advertises[j+1].getPath())).get(1).substring(14))<1){
                    File temp=Advertises[j];
                    Advertises[j+1]=Advertises[j];
                    Advertises[j+1]=temp;
                }
            }
        }
        for (int i=0;i<Advertises.length;i++){
            Sort.add(Files.readAllLines(Paths.get(Advertises[i].getPath())).get(0).substring(5));
        }
    }
    public void SortPriceRange(clientManager client) throws IOException {
        Sort=new ArrayList<>();
        File[] Advertises=new File("C:/Divar/Advertisements").listFiles();
        client.sendMessage("enter minimum number for price");
        Double startRange=Double.parseDouble(client.getMessage());
        client.sendMessage("enter maximum number for price ");
                Double endRange=Double.parseDouble(client.getMessage());
        for (int i=0;i< Advertises.length;i++){
            Double price=Double.parseDouble(Files.readAllLines(Paths.get(Advertises[i].getPath())).get(1).substring(6));
            if(price>=startRange&&price<=startRange)
                Sort.add(Files.readAllLines(Paths.get(Advertises[i].getPath())).get(0).substring(5));
        }
    }
    public void SortLastUpgrade(clientManager client) throws IOException {
        Sort=new ArrayList<>();
        File[] Advertises=new File("C:/Divar/Advertisements").listFiles();
        for (int i=0;i< Advertises.length;i++){
            for (int j=0;j< Advertises.length-i-1;j++){
                if(Files.readAllLines(Paths.get(Advertises[j].getPath())).get(6).substring(12).compareTo(Files.readAllLines(Paths.get(Advertises[j+1].getPath())).get(6).substring(12))>=1){
                    File temp=Advertises[j];
                    Advertises[j+1]=Advertises[j];
                    Advertises[j+1]=temp;
                }
            }
        }
        for (int i=0;i<Advertises.length;i++){
            Sort.add(Files.readAllLines(Paths.get(Advertises[i].getPath())).get(0).substring(5));
        }
    }
    public void SearchInAds(clientManager client) throws IOException {
                boolean success=false;
                while (!success){
                    client.sendMessage("Search by:"+
                            "\n1.All City"+
                            "\n2.name"+
                            "\n3.add favorite"+
                            "\n4.Specific city"+
                            "\n5.remove favorite"+
                            "\n6.Cancel"+
                            "\n\nChoose right number:");
                    switch (client.getMessage()){
                        case "1":
                            SearchByCity(client);
                        case "2":
                            SearchByName(client);
                        case "3":
                            AddFavorite(client);
                        case "4":
                            SearchBySpecificCity(client);
                        case "5":
                           RemoveFavorite(client);
                        case "6":
                            success=true;break;
                        default:
                            client.sendMessage("press valid number");
                    }
                }
            }
            public void AddFavorite(clientManager client) throws IOException {
                boolean success=false;
                while(!success){
                    try{
                        if(searchResult.exists()){
                            File favoriteDirectory=new File("C:/Divar/users/"+client.getUsername()+"/favorite");
                            favoriteDirectory.mkdir();
                            File favoriteAd=new File(favoriteDirectory.getPath()+"/"+searchResult.getName());
                            favoriteAd.createNewFile();
                            FileWriter fw=new FileWriter(favoriteAd,true);
                            Scanner scanner=new Scanner(searchResult);
                            while (scanner.hasNextLine()){
                                fw.write(scanner.nextLine()+"\n");
                                fw.flush();
                            }
                            success=true;
                            fw.close();
                        }
                        else
                            throw new FileNotFoundException();
                    }catch (FileNotFoundException e){
                        client.sendMessage("file not found");
                    }
                }
            }
            public void SearchByName(clientManager client) throws IOException {
                boolean success = false;
                try{
                    while (!success) {
                        client.sendMessage("enter your advertisement name for search:");
                        String name = client.getMessage();
                        subjectFile = new File("C:/Divar/Advertisements");
                        File[] AdsDirectory = subjectFile.listFiles();
                        for (int i = 0; i < Objects.requireNonNull(AdsDirectory).length; i++) {
                                if(Files.readAllLines(Paths.get(AdsDirectory[i].getPath())).get(0).equals(name)){
                                    searchResult=new File(AdsDirectory[i].getPath());
                                    success=true;break;
                                }
                        }
                        if(!success)
                            throw new FileNotFoundException();
                    }
                }catch (FileNotFoundException e){
                    client.sendMessage("file not found");
                }
            }
            public void SearchBySpecificCity(clientManager client){
                boolean success=false;
                try{
                    while (!success) {
                        client.sendMessage("enter advertisement name:");
                        String name = client.getMessage();
                        client.sendMessage("enter city:");
                        String city=client.getMessage();
                        searchResult = new File("C:/Divar/Advertisements/cities/"+city+"/"+ name + ".txt");
                        if(!searchResult.exists())
                            throw new FileNotFoundException();
                        else
                            success=true;
                    }

                }catch (FileNotFoundException e){
                    client.sendMessage("file not found");
                }
            }

            public void SearchByCity(clientManager client){
                boolean success=false;
                try{
                    while (!success) {
                        client.sendMessage("enter advertisement name:");
                        String name = client.getMessage();
                        client.sendMessage("enter city:");
                        String city=client.getMessage();
                        File[]AllCities = new File("C:/Divar/Advertisements/cities").listFiles();
                        for (File allCity : AllCities) {
                            File[] citiesDirectory = allCity.listFiles();
                            for (File file : citiesDirectory) {
                                searchResult = new File("C:/Divar/Advertisements/cities/" + allCity + "/" + file + ".txt");
                                if (searchResult.exists()) {
                                    success = true;
                                    break;
                                }
                            }
                            if (success)
                                break;
                        }
                        if(!success)
                            throw new FileNotFoundException();
                    }

                }catch (FileNotFoundException e){
                   client.sendMessage("file not found");
                }
            }


            public void RemoveFavorite(clientManager client) {
                boolean success = false;
                while (!success) {
                    try {
                        client.sendMessage("enter advertisement name:");
                        String name = client.getMessage();
                        subjectFile = new File("C:/Divar/users/" + client.getUsername() + "/favorite/" + name + ".txt");
                        if (subjectFile.exists()) {
                            subjectFile.delete();
                            success = true;
                        }
                        throw new FileNotFoundException();
                    } catch (FileNotFoundException e) {
                        client.sendMessage("File not Found");
                    }
                }
            }
}
