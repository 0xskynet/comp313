import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

class TestC {
    static PrintWriter writer;
    ServerSocket server;
    Socket client;
    BufferedReader in = null;
    Socket serv;
    NewJFrame gui;
    ChatFrame gui2;
    TestC (NewJFrame gui, ChatFrame gui2) throws IOException, Exception  {
        this.gui = gui;
        this.gui2 = gui2;
        init();
    }

    private void init() throws IOException, Exception {
        server = new ServerSocket(7888); // 7888
        while(true) {
        try {
                System.out.println("LIstening the connection");
                serv = server.accept();
                listen();
                writer = new PrintWriter(serv.getOutputStream(), true);
                writer.flush();
                send("ACK|CON1|z");
        }
        catch (IOException e) {
            System.out.println("System is closed");
            serv.close();
        }
        }
    }
    
    private void listen() throws Exception {
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
                        try {
                            getdata(response);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(TestC.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(TestC.class.getName()).log(Level.SEVERE, null, ex);
                    try {
                        throw new IOException();
                    } catch (IOException ex1) {
                        Logger.getLogger(TestC.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
	    }
	}.start();
    }

    private void getdata(String s) throws IOException, InterruptedException {
        System.out.println("REcieved : " + s);
            FTP ftp = new FTP();
            TSK tsk = new TSK();
            MSG msg = new MSG(gui,gui2);
            String first = "";
            String parts[] = s.split("\\|");
            try {
                first = parts[0];
            } catch (Exception e) {
            }
            if(first.equals("GEN")) { // General 
                switch(parts[1]) {
                    case "ATH" : {
                        if(gui.generatedPin.equals(parts[2]) && !"".equals(parts[2])) {
                            // pin correct
                            send("GEN|ATH|ACC");
                        } else {
                            // pin incorrect
                            send("GEN|ATH|REJ");
                        }
                    } break;
                }
            }
            if(first.equals("FTP")) {
                send(ftp.handler(parts));  
            }
            if(first.equals("TSK")) {
                send(tsk.handler(parts));  
            }
            if(first.equals("ALN")) {
                if(parts[1].equals("CMD")) {
                    excmd(parts[2],true);
                }
            }
            if(first.equals("MSG")) {
                msg.handler(parts);
            }
    }

    public static void send(String s) {
        try{
        System.out.println("Sent : " + s);
            writer.println(s);
            writer.flush();
        }
        catch(Exception e) {
        }
    }
        private void excmd (final String s, final boolean wait) {
            String send = "";
                    new Thread() {
                        public void run() {
                            String line = "", ret = "";
                            try {
                                    Process p = Runtime.getRuntime().exec(s);
                                    if(wait)
                                    {
                                        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                                        while ((line = input.readLine()) != null) {
                                            //System.out.println(line);
                                            //ret += line + "|";
                                            send("ALN|CMD|" + line);
                                        }
                                        send("ALN|CMD|" + "\n\n");
                                        int exitVal = p.waitFor();
                                        input.close();
                                    }
                            }
                            catch (Exception e ) {
                                    System.out.println(e);
                                   send("ALN|CMD|Invalid Command");
                            }
                                //if(ret == null || ("".equals(ret.trim())) ) ret = " ";
                                //ret  = "TSK|CMD|" + ret + "|";
                        }
                    }.start();
        }
    
    }
