package Project;
import Project.Exceptions.AdvertisementNameNotFoundException;

import java.io.*;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

public class  AdvertisementsPages {
    File[] Advertises;
    private String SearchAdName;
    File AdvertisementDirectory;
    AdvertisementsPages() throws FileNotFoundException {
        AdvertisementDirectory=new File("Project/Advertisements");
        Advertises=AdvertisementDirectory.listFiles();
        setSearchAdName();
    }
   public void setSearchAdName() throws FileNotFoundException {
       boolean success = false;
       try {
           while (!success) {
               System.out.println("Enter The name of Ad for search, for Cancel press Enter:");
               SearchAdName = (new Scanner(System.in).nextLine());
               if (SearchAdName.equals(""))
                   break;

               for (File advertise : Advertises) {
                   Scanner scanner = new Scanner(advertise);
                   String[] split = scanner.nextLine().split(": ");
                   if (split[1].equals(SearchAdName)) {
                       success = true;
                       break;
                   }
               }
               if (!success)
                   throw new AdvertisementNameNotFoundException();
           }
       } catch (AdvertisementNameNotFoundException e) {
           System.out.println(e);
       }
   }
}
