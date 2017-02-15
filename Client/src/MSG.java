public class MSG {
    NewJFrame gui;
    MSG (NewJFrame gui) {
        this.gui = gui;
    }
    public String handler(String[] s) {
        switch(s[1]) {
            case "MSG" :
            {
                gui.incomeChat(s[2] + "\n");
            }
             break;
            case "CHT" :
            {
                
            } break;
        }
              return null;
        }
      
    }
