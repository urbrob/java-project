import java.util.*;
import java.io.*;

/** Klasa pomocnicza wspierające asynchroniczny zapis plików */
class FileControler{
  Coder coder;
  FileControler(){
    coder = new Coder();
  }

  /** Metoda pomocnicza łącząca ścieżkę i nazwę pliku */
  public String get_full_path(String path, String file_name){
    return path + "/" + file_name;
  }

  /** Metoda wykonuje asynchroniczny zapis pliku na podanej ścieżce */
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

  /** Metoda wykonuje asynchroniczny usunięcie pliku na podanej ścieżce */
  public void delete_file(String path, String file_name){
    String full_path = this.get_full_path(path, file_name);
    new Thread(() -> {
      new File(full_path).delete();
    }).start();
  }

  /** Metoda wykonuje asynchroniczną modyfikacje pliku na podanej ścieżce */
  public void modify_file(String path, String file_name, String content){
    String full_path = this.get_full_path(path, file_name);
    new File(full_path).delete();
    this.create_file(path, file_name, content);
  }
}
