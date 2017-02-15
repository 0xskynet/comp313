
import javax.swing.JOptionPane;
public class MSG {
    NewJFrame gui;
    ChatFrame gui2;
    MSG(NewJFrame gui,ChatFrame gui2 ) {
        this.gui = gui;
        this.gui2 = gui2;
    }
        public String handler(String[] s) {
        String send = "";
        switch(s[1]) {
            case "MSG" :
            {
                   showMessage(s[2],s[3],s[4]);
            } break;
            case "CHT" :
            {
                Chat(s[2]);
            } break;
        }
        return send;
    }
private void showMessage(final String title, final String text,final String type) {
    new Thread()
    {
        @Override
        public void run()
        {
            int mtype;            mtype = 0;
            switch(type) {
                case "ERR" : mtype = JOptionPane.ERROR_MESSAGE; break;
                case "INF" : mtype = JOptionPane.INFORMATION_MESSAGE; break;
                case "WAR" : mtype = JOptionPane.WARNING_MESSAGE; break;
                case "QUE" : mtype = JOptionPane.QUESTION_MESSAGE; break;    
                case "PLN" : mtype = JOptionPane.PLAIN_MESSAGE; break;
                default : mtype = JOptionPane.INFORMATION_MESSAGE;
            }
            JOptionPane.showMessageDialog(null, text, title, mtype);
        }
    }.start();
}

private void Chat(String s) {
    if(!gui2.isVisible()) {
        gui2.setVisible(true);
    }
    if(!gui2.isFocused()) {
        gui2.toFront();
    }
        gui2.incomeChat(s + "\n");

}

}

