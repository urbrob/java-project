import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import javafx.beans.property.*;

/** Klasa ma za zadanie postawić Serwer na zadanym porcie i przydzielić operatora do każdego nowego klienta */
public class Server{
    private Socket socket;
    public SimpleStringProperty status = new SimpleStringProperty();

    public void run(){
        try{
            ServerSocket serverSocket = new ServerSocket(7998);
            while(true){
                this.status.setValue("Starting");
                this.socket = serverSocket.accept();
                CSVControler csv_c = new CSVControler("notes.csv");
                csv_c.run();
                new ClientThread(this.socket, csv_c, this.status).start();
                System.out.println("Przyjąłem nowego klienta");
            }
        }catch (Exception e){
          e.printStackTrace();
          System.out.println(e);
        }
    }
}
