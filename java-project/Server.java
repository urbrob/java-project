import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;


public class Server{
    private static Socket socket;

    public static void main(String[] args){
        try{
            ServerSocket serverSocket = new ServerSocket(7998);
            while(true){
                socket = serverSocket.accept();
                CSVControler csv_c = new CSVControler("notes.csv");
                csv_c.run();
                new ClientThread(socket, csv_c).start();
                System.out.println("Przyjąłem nowego klienta");
            }
        }catch (Exception e){
          e.printStackTrace();
          System.out.println(e);
        }
    }
}
