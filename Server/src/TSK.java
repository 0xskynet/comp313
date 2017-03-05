import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TSK {
        public String handler(String[] s ) {
            String send = "";
            switch(s[1]) {
            case "LST" :
            {
                 send =  "TSK|LST|" + listprocess() ;
            } break;
            case "KIL" :
            {
                killtask(s[2]);
            } break;
            case "SHT" :
            {
                shutdown(s[2],s[3],s[4],s[5]);
            } break;
            case "ABT" :
            {
                abortShutdown();
            } break;
            case "OPN" :
            {
                openfile(s[2]);
            } break;
            case "INF" :
            {
                send = "TSK|INF|" + info();
            } break;
        }
            return send;
    }
        private String info() {
            String toret = "";
            
                toret += ("Java Vendor : " + System.getProperty("java.vendor")) + "|" ;
                toret +=("Java Version : " + System.getProperty("java.version")) + "|" ;
                toret += "|";
                
                toret +=("OS Architecture  : " + System.getProperty("os.arch")) + "|" ;
                toret +=("OS Name  : " + System.getProperty("os.name")) + "|" ;
                toret +=("OS Version  : " + System.getProperty("os.version")) + "|" ;
                toret += "|";
                
                toret +=("User Directory  : " + System.getProperty("user.dir")) + "|" ;
                toret +=("Home Directory  : " + System.getProperty("user.home")) + "|" ;
                toret +=("Account Name   : " + System.getProperty("user.name")) + "|" ;
                toret += "|";
                
                toret +=("Available processors (cores): " + Runtime.getRuntime().availableProcessors());
                /* Total amount of free memory available to the JVM */
                toret +=("Free memory (bytes): " + Runtime.getRuntime().freeMemory()) + "|" ;

                /* This will return Long.MAX_VALUE if there is no preset limit */
                long maxMemory = Runtime.getRuntime().maxMemory() ;
                /* Maximum amount of memory the JVM will attempt to use */
                toret +=("Maximum memory (bytes): " +  (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory)) + "|" ;

                /* Total memory currently in use by the JVM */
                toret +=("Total memory (bytes): " +  Runtime.getRuntime().totalMemory()) + "|" ;

                /* Get a list of all filesystem roots on this system */
                File[] roots = File.listRoots();
                
                toret += "||";
                /* For each filesystem root, print some info */
                for (File root : roots) {
                  toret +=("File system root: " + root.getAbsolutePath()) + "|" ;
                  toret +=("Total space (bytes): " + root.getTotalSpace()) + "|" ;
                  toret +=("Free space (bytes): " + root.getFreeSpace()) + "|" ;
                  toret +=("Usable space (bytes): " + root.getUsableSpace()) + "|" ;
                  toret += ":";
                }
            return toret;
        }
        private void openfile(String filename){
                   String os = getos(),cmd;
                    if("win".equals(os))
                        cmd = "cmd /c \"" + filename + "\"" ;
                    else
                        cmd = "xdg-open \"" + filename + "\"" ;
            runcmd(cmd);
        }
        private String listprocess() {
                String proc="";
                try {
                        String line;
                        String os = getos(),cmd;
                        if("win".equals(os))
                            cmd = "tasklist.exe /fo csv /nh";
                        else
                            cmd = "ps -e -o comm,pid --no-heading";

                        Process p = Runtime.getRuntime().exec(cmd);
                        BufferedReader input =new BufferedReader(new InputStreamReader(p.getInputStream()));
                        
                        while ((line = input.readLine()) != null) {
                         if (!line.trim().equals("")) {
                             if("win".equals(os)) {
                              String ary[] = null;
                              ary = line.split(",");
                              proc += ary[0].substring(1 ,ary[0].length()-1) + ":" + ary[1].substring(1 ,ary[1].length() -1) + "|";
                             }
                             else {
                              proc += (line.replaceAll("\\s+", ":"))  + "|";
                             }
                          }
                        }
                } catch (IOException ex) {
                    Logger.getLogger(TSK.class.getName()).log(Level.SEVERE, null, ex);
                }
                return proc;
	    }
        
        private void runcmd(final String s) {
                    new Thread() {
                        public void run() {
                            try {
                                    Process p = Runtime.getRuntime().exec(s);
                            }
                            catch (Exception e ) {
                                    System.out.println(e);
                            }
                        }
                    }.start();
        }
        private String getos() {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.indexOf("win") >= 0) {
                os = "win";
            }
            if(os.indexOf("linux") >= 0) {
                os = "linux";
            }
            return os;
        }
        private String killtask(String s) {
            //taskkill /F /PID
                   String os = getos(),cmd;
                    if("win".equals(os))
                        cmd = "taskkill /F /PID " + s;
                    else
                        cmd = " kill -9 4394 " + s;
            runcmd(cmd);
            return "";
        }
        private void shutdown(String shut, String hh, String mm, String ss ) {
            int hh2 = Integer.parseInt(hh);
            int mm2 = Integer.parseInt(mm);
            int ss2 = Integer.parseInt(ss);
            int totaltm = (hh2 * 3600) + (mm2 * 60) + (ss2);
            String os = getos(),cmd;
             if("win".equals(os)) {
                 if("S".equals(shut)) {
                     cmd = "shutdown /f /s /t " + totaltm;
                 } else {
                     cmd = "shutdown /f /r /t " + totaltm;
                 }
             }
             else {
                 if("S".equals(shut)) {
                     cmd = "sleep " + totaltm +"s ; sudo shutdown -f -h now";
                 } else {
                     cmd = "sleep " + totaltm +"s ; sudo shutdown -f -r now";
                 }
             }
            runcmd(cmd);
             //System.out.println("COmmand is :" + cmd);
        }
        private void abortShutdown(){
            String os = getos(),cmd;
             if("win".equals(os)) {
                 cmd = "shutdown /a";
             } 
             else {
                 cmd = "shutdown -c";
             }
        }
}