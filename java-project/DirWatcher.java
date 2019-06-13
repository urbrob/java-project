import java.util.*;
import java.io.*;
import java.io.PrintWriter;

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

  public void send_message(String request_type, String file_name, String file_as_string){
    pw.println(request_type);
    pw.println(file_name);
    pw.println(file_as_string);
  }

  public void modified(File file){
    try{
      String file_as_string = new Coder().file_to_string(file);
      this.send_message("modified", file.getName(), file_as_string);
    }catch(Exception e){
      e.printStackTrace();
      System.out.println(e);
    }
  }

  public void deleted(File file){
    try{
      this.send_message("deleted", file.getName(), "Delete");
    }catch(Exception e){
      e.printStackTrace();
      System.out.println(e);
    }
  }

  public void created(File file){
    try{
      String file_as_string = new Coder().file_to_string(file);
      this.send_message("created", file.getName(), file_as_string);
    }catch(Exception e){
      e.printStackTrace();
      System.out.println(e);
    }
  }

  public void update_dir_and_modification(File new_files[]){
    for(File file : new_files){
    this.dir.put(file.getName(), file);
    this.modification.put(file.getName(), new Long(file.lastModified()));
    }
  }

  public void delete_removed_files(){
    for(Map.Entry<String, File> entry : this.dir.entrySet()) {
      this.deleted(entry.getValue());
      this.dir.remove(entry.getKey());
      this.modification.remove(entry.getKey());
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
      this.delete_removed_files();
      this.update_dir_and_modification(new_files);
      }
    }
  }
