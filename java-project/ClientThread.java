import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.*;

public class ClientThread extends java.lang.Thread {
  private Socket socket;
  private ServerWorker sw;
  public Scanner ser;
  public String user;
  public String local_addres;
  public CSVControler csv_c;

  public ClientThread(Socket s, CSVControler csv_cont){
    socket = s;
    csv_c = csv_cont;
    sw = new ServerWorker(csv_c);
    try {
      ser = new Scanner(socket.getInputStream());
      user = ser.nextLine();
      local_addres = ser.nextLine();
      sw.user = this.user;
    }catch(Exception e){
      e.printStackTrace();
      System.out.println(e);
    }
    send_server_files(s);
  }

  public void send_server_files(Socket socket){
    try{
      File files_array[] = new File("1").listFiles();
      Coder coding_machine = new Coder();
      PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
      // Send every file which belongs to User
      for(File file : files_array){
        String file_name = file.getName();
        if(!file_name.equals(csv_c.file_name) && csv_c.check_if_user_have_permission(this.user, file_name)){
          String file_as_string = coding_machine.code(coding_machine.file_to_string(file));
          pw.println(file_name);
          pw.println(file_as_string);
        }
      }
      pw.println("end;");
    }catch(Exception e){
      e.printStackTrace();
      System.out.println(e);
    }
  }

  public void run() {
    try{
      ser = new Scanner(socket.getInputStream());
      while(true){
        String request_type = ser.nextLine();
        String file_name = ser.nextLine();
        String content = new Coder().decode(ser.nextLine());
        switch(request_type){
          case "created":
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
      e.printStackTrace();
      System.out.println(e);
    }
  }
}
