import java.util.*;
import java.io.*;


class FileControler{
  Coder coder;
  FileControler(){
    coder = new Coder();
  }

  public String get_full_path(String path, String file_name){
    return path + "/" + file_name;
  }

  public void create_file(String path, String file_name, String content){
    String full_path = this.get_full_path(path, file_name);
    new Thread(() -> {
      try{
        new File(full_path).createNewFile();
        FileWriter fw = new FileWriter(full_path, false);
        fw.write(this.coder.decode(content));
        fw.close();
      }catch(Exception e) {
        e.printStackTrace();
        System.out.println(e);
      }
    }).start();
  }

  public void delete_file(String path, String file_name){
    String full_path = this.get_full_path(path, file_name);
    new Thread(() -> {
      new File(full_path).delete();
    }).start();
  }

  public void modify_file(String path, String file_name, String content){
    String full_path = this.get_full_path(path, file_name);
    new File(full_path).delete();
    this.create_file(path, file_name, content);
  }
}
