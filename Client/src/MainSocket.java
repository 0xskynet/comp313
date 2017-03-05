import java.io.BufferedReader;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class MainSocket {
BufferedReader in;
static PrintWriter writer;
ServerSocket server;
Socket client;
NewJFrame gui;

    MainSocket(String ip, NewJFrame gui) throws UnknownHostException, IOException, Exception {
        client = new Socket(ip,ClientA.mainPort);
        writer = new PrintWriter(client.getOutputStream(), true);
        writer.flush();
        this.gui = gui;
        listen();
   }

    public static void senddata(String s) {
        System.out.println("Sent : " +s);
        writer.println(s);
        writer.flush();
    }

    public void getdata(String s) throws InterruptedException, UnknownHostException, IOException {
        System.out.println("Recieved : " +s);
        String first = "";
        String parts[] = s.split("\\|");
        FTP ftp = new FTP(gui);
        TSK tsk = new TSK(gui);
        MSG msg = new MSG(gui);
        try {
            first = parts[0];
        } catch (Exception e) {
        }
        
        if(first.equals("GEN")) { // General 
            switch(parts[1]) {
                case "ATH" : {
                    if("ACC".equals(parts[2]) && !"".equals(parts[2])) {
                        // pin correct
                        gui.enableControls();
                    } else {
                        // pin incorrect
                        gui.disableControls();
                        JOptionPane.showMessageDialog(null, "Incorrect Pin.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } break;
            }
        }
        
        if(first.equals("ACK")) {
            if("CON1".equals(parts[1])) {
                senddata("FTP|DRV|z");
                senddata("TSK|LST|z");
                senddata("TSK|INF|z");
            }
        }
        if(first.equals("FTP")) {
            ftp.handler(parts);  
        }
        if(first.equals("TSK")) {
            tsk.handler(parts);  
        }
        if(first.equals("ALN")) {
            tsk.handler(parts);  
        }
        if(first.equals("MSG")) {
            msg.handler(parts);  
        }
    }
    
    private void listen() throws Exception {
    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    new Thread() {
        @Override
        public void run() {
            String response="";
            try {
                while( (response = in.readLine()) != null ) {
                        if (response.length() == 0) continue; 
                        if (response == null) {
                            break;
                        }
                    try {
                        getdata(response);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainSocket.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(MainSocket.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    throw new IOException();
                } catch (IOException ex1) {
                    Logger.getLogger(MainSocket.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
    }.start();
}
        

}
