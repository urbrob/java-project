import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.*;
import java.io.*;


public class ServerWorker{
  public ArrayList<String> storages;
  public String user;
  public FileControler fc;

  ServerWorker(){
    this.storages = new ArrayList<String>();
    String current_dir = System.getProperty("user.dir");
    this.storages.add(current_dir + "/1");
    this.storages.add(current_dir + "/2");
    this.storages.add(current_dir + "/3");
    this.storages.add(current_dir + "/4");
    this.storages.add(current_dir + "/5");
    this.fc = new FileControler();
  }

  public String generate_new_csv(){
    File filesArray[] = new File("/Users/urbrob/Desktop/java-project/1").listFiles();
    String csv_string = "user,file_name\n";
    for(File file : filesArray){
      if(file.getName().equals("notes.csv")) continue;
      String file_name_as_array[] = file.getName().split("_");
      csv_string += file_name_as_array[0] + "," + file.getName().substring(file_name_as_array[0].length() + 1) + "\n";
    }
    return csv_string;
  }

  public void update_all_csv(){
    //String content = this.generate_new_csv();
    //this.modify_file("notes.csv", content);
  }

  public void create_file(String file_name, String content){
    new Thread(() -> {
        for(String storage_path : this.storages){
          fc.create_file(storage_path, file_name, content);
        }
        this.update_all_csv();
    }).start();

  }

  public void modify_file(String file_name, String content){
    new Thread(() -> {
      for(String storage_path : this.storages){
        fc.modify_file(storage_path, file_name, content);
      }
      this.update_all_csv();
    }).start();
  }

  public void delete_file(String file_name){
    new Thread(() -> {
      for(String storage_path : this.storages){
        fc.delete_file(storage_path, file_name);
      }
      this.update_all_csv();
    }).start();

  }
}
