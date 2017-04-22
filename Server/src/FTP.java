import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class FTP {
     private static final int BUFFER_SIZE = 4096 * 4;
    public String handler(String[] s) throws IOException, InterruptedException {
        String send = "abc";
        switch(s[1]) {
            case "DRV" :
            {
                send = "FTP|DRV|" +listdrives() ;
            } break;
            case "DIR" :
            {
                send = "FTP|DIR|" + listfilefolders(s[2]) ;
            } break;
            case "REN" :
            {
                rename(s[2],s[3]);
                send = "FTP|DIR|REF";
            } break;
            case "DEL" :
            {
                delete(s[2],true);
                send = "FTP|REF|z";
            } break;
            case "CPD" :
            {
                copyDirectory(s[2],s[3]);
                send = "FTP|REF|z";
            } break;
            case "CPF" :
            {
                copyFile(s[2],s[3]);
                send = "FTP|REF|z";
            } break;
            case "MVD" :
            {
                copyDirectory(s[2],s[3]);
                delete(s[2],true);
                send = "FTP|REF|z";
            } break;
            case "MVF" :
            {
                copyFile(s[2],s[3]);
                delete(s[2],true);
                send = "FTP|REF|z";
            } break;
            case "MKD" :
            {
                mkdir(s[2]);
                send = "FTP|REF|z";
            } break;
            case "PRO" :
            {
                send = "FTP|PRO|" + properties(s[2]);
            } break;
            case "TSF" :
            {
                System.out.println("In the CODEEEEEEEEEEEE");
                //server.transfer.initNetwork();
                send = "FTP|TSF|z";
            } break;
            case "DNL" :
            {
                final String filename = s[2];
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            server.transfer.sendFile(filename);
                            System.out.println("After the recieve file code");
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }
                }.start(); 
            } break;
            case "UPL" :
            {
                String savename = s[3];
                final String savepath = s[2], downname = savename.substring(savename.lastIndexOf("/")+1);
                System.out.println("savepath : " + savepath + " downname :" + downname);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            server.transfer.recieveFile(savepath + downname);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }
                }.start(); 
                Thread.sleep(500);
                send = "FTP|UPL|" + s[3];
            } break;
                
        }
        return send;
    }
    private String properties(String s) throws IOException{
        String properties = "";
        String path = s;
        Path file = Paths.get(path);
        BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
        String creationTime= attr.creationTime().toString();
        String lastAccessTime= attr.lastAccessTime().toString();
        String lastModifiedTime= attr.lastModifiedTime().toString();

        String isDirectory= (new Boolean( attr.isDirectory())).toString();
        String isOther = (new Boolean( attr.isOther())).toString();
        String isRegularFile= (new Boolean( attr.isRegularFile())).toString();
        String isSymbolicLink= (new Boolean( attr.isSymbolicLink())).toString();
        String size= String.valueOf(attr.size() / 1000); 
        
        properties = s + "|" + creationTime + "|" + lastAccessTime + "|" + lastModifiedTime + "|" + isDirectory + "|" + isOther + "|" +  isRegularFile + "|" + isSymbolicLink + "|" +  size + "|" ;
        return properties;
    }
    private boolean mkdir(String s){
        delete(s,true);
        return new File(s).mkdir();
    }
    public static boolean delete(String filePath, boolean recursive) {
        File file = new File(filePath);
        if (!file.exists()) {
            return true;
        }
        if (!recursive || !file.isDirectory())
            return file.delete();

        String[] list = file.list();
        for (int i = 0; i < list.length; i++) {
            if (!delete(filePath + File.separator + list[i], true))
                return false;
        }

        return file.delete();
    }
    private void rename(final String oldname, final String newname) {
	File oldfile = new File(oldname);
	File newfile = new File(newname);
        boolean Rename = false;
	if(!oldfile.exists())
	{
		System.out.println("File or directory does not exist.");
		return ;
	}
        else
        {
            Rename = oldfile.renameTo(newfile);
        }
	if(!Rename)
	{
		System.out.println("Not Renamed");
	}
	else {
		System.out.println("Renamed");
	}

    }
    
    private String listfilefolders (String s) {
        System.out.println("Listing contents of :" + s);
        String files ="";
        try {
        File folder = new File(s);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            files +=  listOfFiles[i].getName() + "|";
          } else if (listOfFiles[i].isDirectory()) {
            files +=  listOfFiles[i].getName() + "/" + "|";
          }
        }
        }
        catch (Exception e) {
        }
        return files;
}
private String listdrives() {
    File[] roots = File.listRoots();
    String drives="";
    for(int i=0;i<roots.length;i++)
    {
        String f;
        f = roots[i].toString();
        drives +=  f.substring(0, f.length()-1) + "/" + "|";
    }

    return drives;
}


  public static boolean copyDirectory(File from, File to) {
    return copyDirectory(from, to, (byte[]) null, (String[]) null);
  }

  public static boolean copyDirectory(String from, String to) {
      delete(to,true);
    return copyDirectory(new File(from), new File(to));
  }

  /**
   * @param filter -
   *          array of names to not copy.
   */
  public static boolean copyDirectory(File from, File to, byte[] buffer, String[] filter) {
    //
    // System.out.println("copyDirectory("+from+","+to+")");

    if (from == null)
      return false;
    if (!from.exists())
      return true;
    if (!from.isDirectory())
      return false;

    if (to.exists()) {
      // System.out.println(to + " exists");
      return false;
    }
    if (!to.mkdirs()) {
      // System.out.println("can't make" + to);
      return false;
    }

    String[] list = from.list();

    // Some JVMs return null for File.list() when the
    // directory is empty.
    if (list != null) {

      if (buffer == null)
        buffer = new byte[BUFFER_SIZE]; // reuse this buffer to copy files

      nextFile: for (int i = 0; i < list.length; i++) {

        String fileName = list[i];

        if (filter != null) {
          for (int j = 0; j < filter.length; j++) {
            if (fileName.equals(filter[j]))
              continue nextFile;
          }
        }

        File entry = new File(from, fileName);

        // System.out.println("\tcopying entry " + entry);

        if (entry.isDirectory()) {
          if (!copyDirectory(entry, new File(to, fileName), buffer, filter))
            return false;
        } else {
          if (!copyFile(entry, new File(to, fileName), buffer))
            return false;
        }
      }
    }
    return true;
  }
  public static boolean copyFile(String from, String to){
      delete(to,true);
      return copyFile(new File(from), new File(to));
  }
  public static boolean copyFile(File from, File to) {
    return copyFile(from, to, (byte[]) null);
  }

  public static boolean copyFile(File from, File to, byte[] buf) {
    if (buf == null)
      buf = new byte[BUFFER_SIZE];

    //
    // System.out.println("Copy file ("+from+","+to+")");
    FileInputStream from_s = null;
    FileOutputStream to_s = null;

    try {
      from_s = new FileInputStream(from);
      to_s = new FileOutputStream(to);

      for (int bytesRead = from_s.read(buf); bytesRead != -1; bytesRead = from_s.read(buf))
        to_s.write(buf, 0, bytesRead);

      from_s.close();
      from_s = null;

      to_s.getFD().sync(); // RESOLVE: sync or no sync?
      to_s.close();
      to_s = null;
    } catch (IOException ioe) {
      return false;
    } finally {
      if (from_s != null) {
        try {
          from_s.close();
        } catch (IOException ioe) {
        }
      }
      if (to_s != null) {
        try {
          to_s.close();
        } catch (IOException ioe) {
        }
      }
    }

    return true;
  }
}