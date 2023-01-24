package Project;
import java.io.*;

import java.util.ArrayList;

public class  AdvertisementsPages {

    File subjectFile;

    AdvertisementsPages(clientManager client){
        SearchInAds(client);

    }
    public void SearchInAds(clientManager client){

            while (true){
                try {
                    clientManager.sendMessage("enter your advertisement name for search:");
                    String name=clientManager.getMessage();
                    subjectFile=new File("Project/Advertisements/"+name+".txt");
                    if(!subjectFile.exists())
                        throw new FileNotFoundException();
                }catch (FileNotFoundException e){
                    clientManager.sendMessage("enter correct name for File:");
                }

            }



    }

}
