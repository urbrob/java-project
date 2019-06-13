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


public class Main extends Application {

  @Override
  public void start(Stage primaryStage) {
    BorderPane root = new BorderPane();
    Scene scene = new Scene(root, 400, 250, Color.WHITE);

    HBox hbox = new HBox();
    hbox.setPadding(new Insets(15, 12, 15, 12));
    hbox.setSpacing(10);
    root.setTop(hbox);

    Label status = new Label("Status: ");


    Label x = new Label("chujowo");
    hbox.getChildren().addAll(status, x);


    GridPane gridpane = new GridPane();
    gridpane.setPadding(new Insets(5));
    gridpane.setHgap(10);
    gridpane.setVgap(10);
    ColumnConstraints column1 = new ColumnConstraints(150, 150, Double.MAX_VALUE);
    ColumnConstraints column2 = new ColumnConstraints(50);
    ColumnConstraints column3 = new ColumnConstraints(150, 150, Double.MAX_VALUE);
    column1.setHgrow(Priority.ALWAYS);
    column3.setHgrow(Priority.ALWAYS);
    gridpane.getColumnConstraints().addAll(column1, column2, column3);


    Label filesLbl = new Label("Your Files");
    GridPane.setHalignment(filesLbl, HPos.CENTER);
    gridpane.add(filesLbl, 0, 1);

    Label shareLbl = new Label("Share");
    gridpane.add(shareLbl, 2, 1);
    GridPane.setHalignment(shareLbl, HPos.CENTER);

    // Tutaj trzeba dodać listę plików
    final ObservableList<String> files = FXCollections.observableArrayList("Z", "A", "B", "C", "D");
    final ListView<String> filesListView = new ListView<>(files);
    gridpane.add(filesListView, 0, 2);

    final ObservableList<String> share = FXCollections.observableArrayList();
    final ListView<String> shareListView = new ListView<>(share);
    gridpane.add(shareListView, 2, 2);

    Button sendRightButton = new Button(" > ");
    sendRightButton.setOnAction((ActionEvent event) -> {
      String potential = filesListView.getSelectionModel()
          .getSelectedItem();
      if (potential != null) {
        filesListView.getSelectionModel().clearSelection();
        files.remove(potential);
        share.add(potential);
      }
    });

    Button sendLeftButton = new Button(" < ");
    sendLeftButton.setOnAction((ActionEvent event) -> {
      String s = shareListView.getSelectionModel().getSelectedItem();
      if (s != null) {
        shareListView.getSelectionModel().clearSelection();
        share.remove(s);
        files.add(s);
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
    launch(args);
  }
}
