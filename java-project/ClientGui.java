import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.*;
import javafx.concurrent.Task;
import javafx.beans.property.*;
import javafx.concurrent.*;
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
import javafx.*;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import java.io.File;


public class ClientGui extends Application {
  public static CSVControler csv_controler = new CSVControler("notes.csv");
  public static SimpleStringProperty ssp = new SimpleStringProperty();
  public static String path;

  @Override
  public void start(Stage primaryStage) {
    BorderPane root = new BorderPane();
    Scene scene = new Scene(root, 600, 400, Color.WHITE);

    // Status
    HBox top = new HBox();
    top.setPadding(new Insets(10, 10, 10, 10));
    top.setSpacing(10);
    Label status_title = new Label("Status: ");
    Label status_value = new Label();
  //  status_value.textProperty().bind(ssp);
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

    // share
    HBox bottom = new HBox();
    Label share_label = new Label("User: ");
    ComboBox<String> shareComboBox = new ComboBox<String>();
    shareComboBox.getItems().addAll(csv_controler.load_user_list());
    final ObservableList<String> share = FXCollections.observableArrayList();

    Button shareButton = new Button(" Share ");
    shareButton.setOnAction((ActionEvent event) -> {
      for(String file : share){
        csv_controler.add_user_file(shareComboBox.getValue(), file);
      }
      csv_controler.run_circle();
    });
    bottom.getChildren().addAll(share_label, shareComboBox, shareButton);
    bottom.setPadding(new Insets(10, 10, 10, 10));
    bottom.setSpacing(10);
    root.setBottom(bottom);


    GridPane gridpane = new GridPane();
    gridpane.setPadding(new Insets(10));
    gridpane.setHgap(10);
    gridpane.setVgap(10);
    ColumnConstraints column1 = new ColumnConstraints(200, 200, Double.MAX_VALUE);
    ColumnConstraints column2 = new ColumnConstraints(50);
    ColumnConstraints column3 = new ColumnConstraints(200, 200, Double.MAX_VALUE);
    column1.setHgrow(Priority.ALWAYS);
    column3.setHgrow(Priority.ALWAYS);
    gridpane.getColumnConstraints().addAll(column1, column2, column3);


    Label filesLbl = new Label("Your Files");
    GridPane.setHalignment(filesLbl, HPos.CENTER);
    gridpane.add(filesLbl, 0, 1);

    Label shareLbl = new Label("To sharing");
    gridpane.add(shareLbl, 2, 1);
    GridPane.setHalignment(shareLbl, HPos.CENTER);

    // Tutaj trzeba dodać listę plików
    final ObservableList<String> files = FXCollections.observableArrayList(new File(path).list());
    new Thread(() -> {
        while (true) {
            try {
                Platform.runLater(() -> {
                  files.setAll(FXCollections.observableArrayList(new File(path).list()));
                });

                Thread.sleep(2000);
            }
            catch (Exception e) {}
        }
    }).start();

    final ListView<String> filesListView = new ListView<>(files);
    gridpane.add(filesListView, 0, 2);


    final ListView<String> shareListView = new ListView<>(share);
    gridpane.add(shareListView, 2, 2);

    Button sendRightButton = new Button(" > ");
    sendRightButton.setOnAction((ActionEvent event) -> {
      String your_file = filesListView.getSelectionModel().getSelectedItem();
      if (your_file != null) {
        filesListView.getSelectionModel().clearSelection();
        if (!share.contains(your_file)) {
            share.add(your_file);
        }
      }
    });

    Button sendLeftButton = new Button(" < ");
    sendLeftButton.setOnAction((ActionEvent event) -> {
      String share_file = shareListView.getSelectionModel().getSelectedItem();
      if (share_file != null) {
        shareListView.getSelectionModel().clearSelection();
        share.remove(share_file);
      }
    });
    VBox vbox = new VBox(5);
    vbox.getChildren().addAll(sendRightButton, sendLeftButton);

    gridpane.add(vbox, 1, 2);

    root.setCenter(gridpane);

    GridPane.setVgrow(root, Priority.ALWAYS);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Client");
    primaryStage.show();
  }


  public static void main(String[] args) {
    Client client = new Client();
    client.run(args[1], args[0]);
    path = args[1];
    ssp = client.status;
    launch(args);
  }
}
