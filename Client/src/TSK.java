
import java.util.Arrays;
import javax.swing.SwingUtilities;


public class TSK {
    NewJFrame gui;
    TSK (NewJFrame gui) {
        this.gui = gui;
    }
        public String handler(String[] s) {
        switch(s[1]) {
            case "LST" :
            {
                addToProc(Arrays.copyOfRange(s, 2, s.length));
            } break;
            case "CMD" :
            {
                String text = "";
                for(int i=2;i<s.length ;i++) {
                    text += s[i] ;
                }
                if(text != null) {
                    cmdoutput(text);                    
                }
            }
            case "INF" :
            {
                for(int i=0;i<s.length;i++){
                    gui.txtsysteminfo.append(s[i] + "\n" );    
                }
                
            } break;
        }
        return null;
    }
        
        public void addToProc(final String s[]) {
        SwingUtilities.invokeLater(new Runnable(){@Override
            public void run(){
        gui.list2.clear();
        for(int i=0;i<s.length;i++) {
            gui.list2.addElement(s[i]); }
        }});
        }
        
        public void cmdoutput(String s) {
            gui.cmdoutput(s);
            System.out.println("\n");
        }
}
