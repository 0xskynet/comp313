
import java.io.*;
import java.net.*;

public class Transfer {
    InputStream in;
    OutputStream out;
    Socket socket;
    Transfer(String ip) throws UnknownHostException, IOException{
        initNetwork(ip);
    }

    public  void sendFile(String sfileName) throws IOException{
        try{
            System.out.println("File output Stream of : " + sfileName);
            in = new FileInputStream(sfileName);
            out = socket.getOutputStream();
            transferData(in,out);
        }
        finally {
            in.close();
            in = null;
            System.gc();
            socket.shutdownOutput();
            socket.close();
        }
    }
    
    public void recieveFile(String rfileName) throws IOException{
        try{
            in = socket.getInputStream();
            System.out.println("Reciever file : " + rfileName);
            out = new FileOutputStream(rfileName);
            transferData(in,out);
        }
        finally{
            out.flush();
            out.close();
            out = null;
            System.gc();
        }
    }

    public void initNetwork(String ip) throws UnknownHostException, IOException{
            try {
                socket = new Socket(ip, ClientA.transferPort);             
            }
            catch(Exception e){
            }
    }

    private void transferData(InputStream in, OutputStream out) throws IOException {
        System.out.println("File Transfer Started at : ");
        byte[] buf = new byte[8192];
        int len = 0;
        while(in.available()==0);
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        out.flush();
        System.out.println("File Transfer Completed !!!!!!!!! ");
    }
}