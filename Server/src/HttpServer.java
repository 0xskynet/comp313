import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JOptionPane;

public class HttpServer {
   NewJFrame gui;
   ChatFrame gui2;
   BufferedReader in = null;
   BufferedWriter out = null;
   Socket clientSocket = null;
   int port = server.httpPort;
   String opbuffer = "";
   DataInputStream jarFile;
   File j=new File("HelloWorldApplet.jar");
   
    HttpServer (NewJFrame gui,ChatFrame gui2) throws IOException {
    this.gui = gui;
    this.gui2 = gui2;
   
    ServerSocket serverSocket = new ServerSocket(port);
    System.out.println("Listening the server at the port : " + port);

    while (true) {
            try  {
                clientSocket = serverSocket.accept();
                //sout
                        
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                String s,fileName="";
                while ((s = in.readLine()) != null || !(s.equals(""))) {
                StringTokenizer tokenizer = new StringTokenizer(s," ");
                if(!tokenizer.hasMoreTokens()){ break;}
                        if(tokenizer.nextToken().equals("GET")){
                            fileName = tokenizer.nextToken();
                            if(fileName.equals("/")){
                                fileName = "index.html";
                            }else{
                                fileName = fileName.substring(1);
                            }
                        }
                    if (s.isEmpty()) {
                        break;
                    }
                }
                if("index.html".equals(fileName)) {
                    mainpage();
                }
                else
                {
                    getdata(URLDecoder.decode(fileName, "UTF-8"));
                }

                System.out.println( "Request : "+ fileName);
                System.out.println("Connection closed");
            }
            catch(Exception e ) {
                try { out.close();  }
                catch(Exception ex ) { }
                try { in.close(); } catch (Exception ex2 ) {   }
            }
            finally {
                try { out.close();  }
                catch(Exception e ) { }
                try { in.close(); } catch (Exception e ) {   }
            }
    }
}
   private void send (String s) throws IOException {
         out.write(s);
         out.flush();
    }

private static String readFile(InputStream in) throws IOException  {
    try {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    StringBuilder out = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
        out.append(line + "\n");
    }
    return out.toString();
    }
    catch(Exception e ) {
        return "Error in reading file ";
    }
}
private void mainpage() throws FileNotFoundException, IOException {
    String flle = "";
       flle = readFile(this.getClass().getClass().getResourceAsStream("/index.htm"));
       sendHttpHeaders(true);
       out.write(flle);
       out.flush();
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
            if(first.equals("FTP")) {
                serveRequest(ftp.handler(parts));  
            }
            if(first.equals("TSK")) {
                serveRequest(tsk.handler(parts));  
            }
            if(first.equals("ALN")) {
                if(parts[1].equals("CMD")) {
                    excmd2(parts[2],true);
                }
            }
            if(first.equals("POL")) {
                pollMsg();
            }
            if(first.equals("MSG")) {
               msg.handler(parts);
            }
    }
private void pollMsg() throws IOException {
    if(!"".equals(gui2.pendingMsg)) {
        serveRequest("MSG|CHT|"+ gui2.pendingMsg);
        gui2.pendingMsg = "";
    }
    if(!"".equals(opbuffer)){
        out.write("ALN|CMD|"+ opbuffer);
        out.flush();
        opbuffer = "";
    }
}
private void sendHttpHeaders(boolean http) throws IOException{
    out.write("HTTP/1.0 200 OK\r\n");
    out.write("Server: Apache/0.8.4\r\n");
    out.write("Cache-Control: no-cache\r\n");
    if(http ){
        out.write("Content-Type: text/html\r\n");
    }
    else
    {
        out.write("Content-Type: text/plain\r\n");
    }
    out.write("\r\n");
}
private void serveRequest(String s) throws IOException {
    sendHttpHeaders(false);
    out.write(s);
    out.flush();
}

    private void excmd (final String s, final boolean wait) throws IOException {
        sendHttpHeaders(false);
        Process p; String send = "", line,ret="";
        p = Runtime.getRuntime().exec(s);
        try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            while ((line = input.readLine()) != null) {
            ret += line + "|";
            }
            input.close();
        }
        out.write( "ALN|CMD|"+ ret);
    }
    
    private void excmd2 (final String s, final boolean wait) throws IOException {
        new Thread() {
            @Override
	    public void run() {
                Process p; String send = "", line,ret="";
                        try {
                            p = Runtime.getRuntime().exec(s);
                            try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                                while ((line = input.readLine()) != null) {
                                opbuffer += line + "|";
                                }
                                input.close();
                            }
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
	    }
	}.start();
    }

}