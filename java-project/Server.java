import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;


public class Server{
    private static Socket socket;

    public static void main(String[] args){
        try{
            ServerSocket serverSocket = new ServerSocket(7998);
            ServerWorker sw = new ServerWorker();
            while(true){
                socket = serverSocket.accept();
                new ClientThread(socket, sw).start();
                System.out.println("Przyjąłem nowego klienta");
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
}
