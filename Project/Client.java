package Project;

import java.net.*;
import java.io.*;
public class Client{
    public static void main(String args[])
    {
        Client client = new Client("127.0.0.1", 5000);
    }
    private Socket socket		 = null;
    BufferedReader br= null;
    private DataOutputStream out	 = null;
    private DataInputStream in	 = null;

    public Client(String address, int port)
    {
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");
            br = new BufferedReader(new InputStreamReader(System.in));
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            System.out.println("newed");
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
        recieve r = new recieve(in);
        r.start();
        String line = "";
        String respond = "";
        while (!respond.equals("See You SOON!!!"))
        {
            try
            {
                line = br.readLine();
                out.writeUTF(line);
                out.flush();
            }
            catch(IOException i)
            {
                System.out.println(i);
            }
        }
        r.turnOff();

        try
        {
            br.close();
            out.close();
            socket.close();
            in.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
}

class recieve extends Thread{
    DataInputStream in;
    Boolean on = true;
    recieve(DataInputStream in){
        this.in = in;
    }
    @Override
    public void run() {
        String line = "";
        while(on){
            try {
                line = in.readUTF();
            } catch (IOException e) {
                if (on) {
                    e.printStackTrace();
                }
            }
            System.out.println(line);
        }
    }
    public void turnOff(){
        on = false;
    }

}
