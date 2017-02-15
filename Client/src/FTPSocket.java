
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class FTPSocket {
   PrintWriter writer;
    ServerSocket server;
    Socket client;
    BufferedReader reader;
    BufferedReader in = null;
    Socket serv;
    FTPSocket () throws IOException, Exception  {
        init();
    }

    public void init() throws IOException, Exception {
        server = new ServerSocket(8888);
        while(true) {
        try {
                System.out.println("LIstening the connection");
                
                serv = server.accept();
                listen();
                writer = new PrintWriter(serv.getOutputStream(), true);
                writer.flush();
                System.out.println("In the code ");
        }
        catch (IOException e) {
            System.out.println("System is closed");
            serv.close();
        }
        }
    }
    public void listen() throws Exception {
        in = new BufferedReader(new InputStreamReader(serv.getInputStream()));
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
                            getdata(response);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(FTPSocket.class.getName()).log(Level.SEVERE, null, ex);
                    try {
                        throw new IOException();
                    } catch (IOException ex1) {
                        Logger.getLogger(FTPSocket.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
	    }
	}.start();
    }

    public void getdata(String s) throws IOException {
    System.out.println("Recieved : " + s);
        String first = null;
        String parts[] = s.split("\\|");
        try {
            first = parts[0];
        } catch (Exception e) {
        }
    if(first.equals("FTP")) {
        handler(parts);
    }
    }
    
    private void handler(String[] s1 ) throws IOException {
        switch(s1[1]) {
        
        }
    }

    public void send(String s) throws IOException {
            writer.println(s);
            writer.flush();
            
    }
    
    }
