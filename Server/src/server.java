import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class server {
    static PrintWriter writer;
    ServerSocket server;
    Socket client;
    BufferedReader reader;
    NewJFrame gui;
    ChatFrame gui2;
    static Transfer transfer;
    public final static int httpPort	= 7889; //  7889
    public final static int mainPort2	= 7888; //  7888
    public final static int transferPort	= 7887; //  7887
    public void go() throws IOException, Exception {
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui = new NewJFrame();
                gui.setVisible(true);
            }
        });
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui2 = new ChatFrame();
                //gui2.setVisible(true);
                gui2.setResizable(false);
            }
        });

        Thread.sleep(2000);
       setUpNetworking();
    }

        public void setUpNetworking() throws IOException, Exception {
            gui.changeWindowTitle("asdf");
	new Thread() {
            @Override
	    public void run() {
                try { 
                    TestC t1 = new TestC(gui,gui2);
                } catch (IOException ex) {
                    Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
                }
	    }
	}.start();

        new Thread() {
            @Override
	    public void run() {
                    HttpServer serv;
                        try {
                            serv = new HttpServer(gui,gui2);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
	    }
	}.start();


        
       transfer = new Transfer();
 


    }
    public static void send(String s) throws IOException {
        System.out.println("Sent : " + s);
        try {
            writer.println(s);
            writer.flush();
        }catch (Exception ex) {
        }     
    }
}