import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.io.PrintWriter;
import java.util.Scanner;


public class Client{
    private static Socket socket;

    public static void main(String args[]){
        try {
            InetAddress address = InetAddress.getByName("localhost");
            socket = new Socket(address, 7998);
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            pw.println(args[0]);
            pw.println(args[1]);
            DirWatcher dw = new DirWatcher(args[1], pw);
            dw.run();
        }
        catch (Exception e){
          System.err.println(e.getMessage());
        }
    }
}
