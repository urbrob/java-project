import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.io.*;
import javafx.beans.property.SimpleStringProperty;

/** Zadaniem klasy obiektu jest stworzenie połączenia między clientem a serwerem, pobranie plików których nie posiadamy lokalnie oraz rozpoczęcie monitorowanie folderu lokalnego */
public class Client{
  private static Socket socket;
  public static Scanner s;
  public static PrintWriter pw;
  public static FileControler fc;
  public SimpleStringProperty status = new SimpleStringProperty();

  /** Metoda Rozpoczyna komunikację z serwerem i wysyła do niego dane użytkownika*/
  public void establish_connection(String user, String location){
    try{
      InetAddress address = InetAddress.getByName("localhost");
      socket = new Socket(address, 7998);

      // Send User information
      pw = new PrintWriter(socket.getOutputStream(), true);
    }catch(Exception e){
      e.printStackTrace();
      System.out.println(e);
    }
    pw.println(user);
    pw.println(location);
    this.status.setValue("Connecting...");
    System.out.println("---- Connected with Server! ----");
  }

  /** Metoda otwiera nasłuchiwanie na serwer w celu synchronizacji plików z serwerem */
  public void download_files_from_server(Socket socket, String path){
    while(true){
      String file_name = s.nextLine();
      if (!file_name.equals("end;")){
        fc.create_file(path, file_name, s.nextLine());
      }else {
        break;
      }
    }
    this.status.setValue("Synch up...");
    System.out.println("---- Done Synchronization ----");
  }
  /** Metoda rozpoczyna obserwacje folderu klienta */
  public void start_watching_edition_of_files(String location, PrintWriter pw){
    new Thread(() -> {
      DirWatcher dw = new DirWatcher(location, pw, this.status);
      dw.run();
    }).start();
    this.status.setValue("Watching files...");
    System.out.println("---- Watching Files ----");
  }

  public void run(String location, String user){
    try {
      this.status.setValue("Starting");
      this.establish_connection(user, location);
      s = new Scanner(socket.getInputStream());
      fc = new FileControler();
      this.download_files_from_server(socket, location);
      this.start_watching_edition_of_files(location, pw);
    }catch (Exception e){
      e.printStackTrace();
      System.out.println(e);
    }
  }
}
