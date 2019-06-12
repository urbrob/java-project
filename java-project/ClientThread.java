import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.io.PrintWriter;

public class ClientThread extends java.lang.Thread {
  private Socket socket;
  private ServerWorker sw;
  public Scanner ser;
  public String user;
  public String local_addres;


  public ClientThread(Socket s, ServerWorker sw){
    this.socket = s;
    this.sw = sw;
    try {
      ser = new Scanner(socket.getInputStream());
      this.user = ser.nextLine();
      this.local_addres = ser.nextLine();
    }catch(Exception e){
      System.out.println("Scanner could not be loaded");
    }
  }

  public void run() {
    try{
      while(true){
        String request_type = ser.nextLine();
        String file_name = ser.nextLine();
        String content = ser.nextLine().replace(":::n:::", "\n").replace(":::r:::", "\r");
        switch(request_type){
          case "created":
            System.out.println("Tworzę nowy plik");
            sw.create_file(file_name, content);
            break;
          case "modified":
            sw.modify_file(file_name, content);
            break;
          case "deleted":
            sw.delete_file(file_name);
            break;
          default:
            System.out.println("Bad Request 400 error");
            break;
        }
      }
    }catch(Exception e){
      System.err.println(e.getMessage());
    }
  }
}
