import java.util.*;
import java.io.*;
import java.util.concurrent.TimeUnit;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DirWatcher{
    private String path;
    private File filesArray [];
    public HashMap<String, Long> modification = new HashMap<String, Long>();
    public HashMap<String, File> dir = new HashMap<String, File>();
    private PrintWriter pw;

    public DirWatcher(String path, PrintWriter ow) {
        this.path = path;
        this.pw = ow;
        filesArray = new File(path).listFiles();
        for(File file : filesArray){
            dir.put(file.getName(), file);
            modification.put(file.getName(), new Long(file.lastModified()));
        }
    }

    public String file_to_string(File file){
      String text = "DEADBEEF";
      try{
        text = new String(Files.readAllBytes(file.toPath()), "UTF-8").replace("\n", ":::n:::").replace("\r", ":::r:::");
      }catch(Exception e){
        System.out.println("Nie mogłem przekonwertować pliku do tekstu");
      }
      return text;
    }

    public void modified(File file){
      try{
        String fileString = this.file_to_string(file);
        pw.println("modified");
        pw.println(file.getName());
        pw.println(fileString);
      }catch(Exception e){
        System.err.println(e.getMessage());
      }
    }

    public void deleted(File file){
      try{
        pw.println("deleted");
        pw.println(file.getName());
        pw.println("Delete");
      }catch(Exception e){
        System.err.println(e.getMessage());
      }
    }

    public void created(File file){
      try{
        String fileString = this.file_to_string(file);
        System.out.println("Wysyłam plik: " + fileString);
        pw.println("created");
        pw.println(file.getName());
        pw.println(fileString);
      }catch(Exception e){
        System.out.println("Błąd wysyłania pliku na serv: " + e.getMessage());
      }
    }

    public void run() {
        while(true){
            File new_files [] = new File(path).listFiles();
            for(File file_new : new_files){
                File file_old = this.dir.remove(file_new.getName());
                Long old_time = this.modification.remove(file_new.getName());
                if(file_old == null){
                    this.created(file_new);
                }else {
                    long new_date = file_new.lastModified();
                    if(old_time != new_date){
                        this.modified(file_new);
                    }
                }
              }
              for(Map.Entry<String, File> entry : this.dir.entrySet()) {
                  this.deleted(entry.getValue());
                  this.dir.remove(entry.getKey());
                  this.modification.remove(entry.getKey());
              }
              for(File file : new_files){
                 this.dir.put(file.getName(), file);
                 this.modification.put(file.getName(), new Long(file.lastModified()));
              }
          }
      }
  }
