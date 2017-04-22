import java.io.*;
import java.net.*;
import javax.swing.SwingUtilities;

public class ClientA {
BufferedReader reader;
static PrintWriter writer;
ServerSocket server;
Socket client;
static NewJFrame gui;
static Transfer transfer;
public static final String serverIp = "127.0.0.1"; //192.168.137.131
public static int httpPort	= 7889; //  7889    // 8889
public static int mainPort	= 7888; //  7888    // 8888
public static int transferPort	= 7887; //  7887    // 8887

    public void go() throws InterruptedException, UnknownHostException, IOException, Exception
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());       
                            //info.getClassName()
                            //javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
                            //javax.swing.UIManager.getSystemLookAndFeelClassName()
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
                gui  = new NewJFrame();
                gui.setVisible(true);
                gui.setResizable(false);
            }
        });
        Thread.sleep(2000);
        //setUpNetworking(serverIp);
    }

    public void setUpNetworking(String ip) throws UnknownHostException, IOException, Exception {
        MainSocket MainSocket1 = new MainSocket(ip,gui);
        transfer = new Transfer(ip);
    }

}
