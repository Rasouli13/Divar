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


    AdvertisementsPages(clientManager client) throws IOException {
        SearchInAds(client);

    }
    public void SearchInAds(clientManager client) throws IOException {
                boolean success=false;
                while (!success){
                    client.sendMessage("Search by:"+
                            "\n1.All City"+
                            "\n2.name"+
                            "\n3.add favorite"+
                            "\n4.Specific city"+
                            "\n5.Cancel"+
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
                            File favoriteDirectory=new File("Project/users/"+client.getUsername()+"/favorite");
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
                        System.out.println(e);
                    }
                }
            }
            public void SearchByName(clientManager client) throws IOException {
                boolean success = false;
                try{
                    while (!success) {
                        client.sendMessage("enter your advertisement name for search:");
                        String name = client.getMessage();
                        subjectFile = new File("Project/Advertisements");
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
                    System.out.println(e);
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
                        searchResult = new File("Project/Advertisements/cities/"+city+"/"+ name + ".txt");
                        if(!searchResult.exists())
                            throw new FileNotFoundException();
                        else
                            success=true;
                    }

                }catch (FileNotFoundException e){
                    System.out.println(e);
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
                        File[]AllCities = new File("Project/Advertisements/cities").listFiles();
                        for (File allCity : AllCities) {
                            File[] citiesDirectory = allCity.listFiles();
                            for (File file : citiesDirectory) {
                                searchResult = new File("Project/Advertisements/cities/" + allCity + "/" + file + ".txt");
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
                    System.out.println(e);
                }
            }
}
