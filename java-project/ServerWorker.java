import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.*;
import java.io.*;


public class ServerWorker{
  ArrayList<String> storages;
  ServerWorker(){
    this.storages = new ArrayList<String>();
    String current_dir = System.getProperty("user.dir");
    this.storages.add(current_dir + "/1/");
    this.storages.add(current_dir + "/2/");
    this.storages.add(current_dir + "/3/");
    this.storages.add(current_dir + "/4/");
    this.storages.add(current_dir + "/5/");
  }

  public void create_file(String file_name, String content){
    new Thread(() -> {
      try{
        for(String storage_path : this.storages){
          File new_file = new File(storage_path + file_name);
          new_file.createNewFile();
          FileWriter fw = new FileWriter(storage_path + file_name, false);
          fw.write(content);
    	    fw.close();
        }
      }catch(Exception e) {
        System.err.println(e.getMessage());
      }
    }).start();

  }

  public void modify_file(String file_name, String content){
    new Thread(() -> {
      for(String storage_path : this.storages){
        try{
          File modify_file = new File(storage_path + file_name);
          modify_file.delete();
          modify_file.createNewFile();
          FileWriter fw = new FileWriter(storage_path + file_name, false);
          fw.write(content);
    	    fw.close();
        }catch(Exception e) {
          System.err.println(e.getMessage());
        }
      }
    }).start();
  }

  public void delete_file(String file_name){
    new Thread(() -> {
      for(String storage_path : this.storages){
        File deleting_file = new File(storage_path + file_name);
        deleting_file.delete();
      }
    }).start();

  }
}
