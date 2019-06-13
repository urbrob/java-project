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
  public CSVControler csv_cont;

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

  ServerWorker(CSVControler csv_c){
    csv_cont = csv_c;
    this.storages = new ArrayList<String>();
    String current_dir = System.getProperty("user.dir");
    this.storages.add(current_dir + "/1");
    this.storages.add(current_dir + "/2");
    this.storages.add(current_dir + "/3");
    this.storages.add(current_dir + "/4");
    this.storages.add(current_dir + "/5");
    this.fc = new FileControler();
  }

  public void create_file(String file_name, String content){
    new Thread(() -> {
        for(String storage_path : this.storages){
          fc.create_file(storage_path, file_name, content);
        }
        csv_cont.add_user_file(user, file_name);
    }).start();


  }

  public void modify_file(String file_name, String content){
    new Thread(() -> {
      for(String storage_path : this.storages){
        fc.modify_file(storage_path, file_name, content);
      }
    }).start();
  }

  public void delete_file(String file_name){
    new Thread(() -> {
      for(String storage_path : this.storages){
        fc.delete_file(storage_path, file_name);
      }
      csv_cont.remove_user_file(user, file_name);
    }).start();

  }
}
