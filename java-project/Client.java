import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.io.*;


public class Client{
  // Task of Client class is to establish connection and start DirWatcher work
  private static Socket socket;
  public static Scanner s;
  public static PrintWriter pw;
  public static FileControler fc;

  public static void establish_connection(String user, String location){
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
    System.out.println("---- Connected with Server! ----");
  }

  public static void download_files_from_server(Socket socket, String path){
    while(true){
      String file_name = s.nextLine();
      if (!file_name.equals("end;")){
        fc.create_file(path, file_name, s.nextLine());
      }else {
        break;
      }
    }
    System.out.println("---- Done Synchronization ----");
  }

  public static void start_watching_edition_of_files(String location){
    new Thread(() -> {
      DirWatcher dw = new DirWatcher(location, pw);
      dw.run();
    });
    System.out.println("---- Watching Files ----");
  }

  public static void main(String args[]){
    try {
      establish_connection(args[0], args[1]);
      s = new Scanner(socket.getInputStream());
      fc = new FileControler();
      download_files_from_server(socket, args[1]);
      DirWatcher dw = new DirWatcher(args[1], pw);
      dw.run();
    }
    catch (Exception e){
      e.printStackTrace();
      System.out.println(e);
    }
  }
}
