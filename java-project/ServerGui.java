import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Separator;
import javafx.geometry.VPos;
import javafx.scene.layout.HBox;
import javafx.scene.control.ComboBox;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.beans.property.*;
import java.io.File;


public class ServerGui extends Application {
    public static SimpleStringProperty ssp = new SimpleStringProperty();

    public void generate_list_file(int i, GridPane gridpane){
      Label filesLbl = new Label("Drive " + i);
      GridPane.setHalignment(filesLbl, HPos.CENTER);
      gridpane.add(filesLbl, 0, i);

      ObservableList<String> files = FXCollections.observableArrayList(new File("/Users/roberturbaniak/Desktop/java-project/java-project/" + i).list());
      ListView<String> filesListView = new ListView<>(files);
      filesListView.setMaxWidth(Double.MAX_VALUE);
      gridpane.add(filesListView, 1 , i);
      new Thread(() -> {
          while (true) {
              try {
                  Platform.runLater(() -> {
                    files.setAll(FXCollections.observableArrayList(new File("/Users/roberturbaniak/Desktop/java-project/java-project/" + i).list()));
                  });

                  Thread.sleep(2000);
              }
              catch (Exception e) {}
          }
      }).start();
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 500, 800, Color.WHITE);

        // Status
        HBox top = new HBox();
        top.setPadding(new Insets(10, 10, 10, 10));
        top.setSpacing(10);
        Label status_title = new Label("Status: ");
        Label status_value = new Label("Starting");
        new Thread(() -> {
            while (true) {
                try {
                    Platform.runLater(() -> {
                      status_value.setText(ssp.toString());
                    });
                    Thread.sleep(300);
                }
                catch (Exception e) {}
            }
        }).start();
        top.getChildren().addAll(status_title, status_value);
        root.setTop(top);


        GridPane gridpane = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints(100);
        ColumnConstraints column2 = new ColumnConstraints(100,100,Double.MAX_VALUE);
        column2.setHgrow(Priority.ALWAYS);
        gridpane.getColumnConstraints().addAll(column1, column2);

        for(int i = 1; i < 6; i++){
          generate_list_file(i, gridpane);
        }

        gridpane.setPadding(new Insets(10, 10, 10, 10));
        gridpane.setHgap(10);
        gridpane.setVgap(10);
        root.setCenter(gridpane);

        GridPane.setVgrow(root, Priority.ALWAYS);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Server");
        primaryStage.show();
    }



    public static void main(String[] args) {
        Server server = new Server();
        new Thread(() -> {server.run();}).start();
        ssp = server.status;
        launch(args);
    }
}
