package classes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientListener extends Thread {
private BufferedReader in = null;

   public ClientListener (Socket s) throws IOException{
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    }

   @Override
    public void run(){
        String msg=null,response=null;
        while(true) {
        try {
            response = in.readLine();
            if (response == null) 
                break;
        } catch (IOException ex) {
        }
        datarecieved(response);
    }
    }
    
    public void datarecieved (String s ) {  }
}