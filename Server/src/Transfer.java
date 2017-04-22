import java.io.*;
import java.net.*;

public class Transfer {
    InputStream in;
    OutputStream out;
    Socket socket;
    boolean isTransferring = false ;
    Transfer() throws IOException{
        initNetwork();
    }
    public  void sendFile(String sfileName) throws IOException{
        try{
            System.out.println("String generating of file : " + sfileName);
            in = new FileInputStream(sfileName);
            out = socket.getOutputStream();
            transferData(in,out);
            System.out.println("Transfer completed");
        }
        finally {
            in.close();
            in = null;
            System.gc();
            // socket.shutdownOutput();
            //socket.close();
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
    
    public void initNetwork() throws IOException {
        new Thread() {
            @Override
	    public void run() {
                try {
                    ServerSocket ss = new ServerSocket(server.transferPort);
                    socket = ss.accept();
                    System.out.println("Transfer accepted");
                } catch (Exception ex) {
                    System.out.println(ex);
                }
	    }
	}.start();

    }
    
   private void transferData(InputStream in, OutputStream out) throws IOException  {
        isTransferring = true;
        System.out.println("File Transfer Started");
        byte[] buf = new byte[8192];
        int len = 0;
        while(in.available()==0);
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        out.flush();
        System.out.println("File Transfer Completed !!!!!!!!!");
        isTransferring = false;
        }
}
