import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class FTP {
    NewJFrame gui;
    FTP (NewJFrame gui) {
        this.gui = gui;
    }
    public String handler(String[] s) throws InterruptedException, UnknownHostException, IOException {
        switch(s[1]) {
            case "DRV" :
            {
                addTocombo(Arrays.copyOfRange(s, 2, s.length)); 
            } break;
            case "DIR" :
            {
                addToList(Arrays.copyOfRange(s, 2, s.length)); 
            } break;
            case "REF" :
            {
                MainSocket.senddata("FTP|DIR|" +gui.getcurdir() );
            } break;
            case "TSF" :
            {
                ClientA.transfer.initNetwork(ClientA.serverIp);
            } break;
            case "UPL" :
            {
                ClientA.transfer.sendFile(s[2]);
            } break;
            case "PRO" :
            {
                String text="",title;
                title = s[2];
                String creationTime = s[3];
                String lastAccessTime = s[4];
                String lastModifiedTime = s[5];

                 String isDirectory = s[6];
                String isOther = s[7];
                String isRegularFile = s[8];
                String isSymbolicLink = s[9];
                String size = s[10];
                
                text += "created Time : " + creationTime + "\n" +
                        "Last Accessed : " + lastAccessTime + "\n" +
                        "Last Modified : " + lastModifiedTime + "\n" +
                        "is Directory : " +isDirectory + "\n" +
                        "is Other File : " +isOther + "\n" +
                        "is Regular File : " +isRegularFile + "\n" +
                        "is Symbolic Link : " +isSymbolicLink + "\n" +
                        "File Size : " + size;
                        
                JOptionPane.showMessageDialog(null,text,title,JOptionPane.INFORMATION_MESSAGE);
            } break;
        }
        return null;
    }
    
    public void addTocombo(final String s[]) {
    SwingUtilities.invokeLater(new Runnable(){@Override
        public void run(){
    gui.combo1.removeAllElements();
    for(int i=0;i<s.length;i++) {
        gui.combo1.addElement(s[i]); }
    }});
    }


    public void addToList(final String s[]){
        System.out.println("List + " + s.length);
    SwingUtilities.invokeLater(new Runnable(){@Override
    public void run(){
    gui.list1.clear();
    for(int i=0;i<s.length;i++)  {
        gui.list1.addElement(s[i]); 
    }
    if(s.length == 0) {
        gui.list1.addElement("    "); 
    }
    }});
    }
}
