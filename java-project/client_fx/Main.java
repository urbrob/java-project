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

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) {
    BorderPane root = new BorderPane();
    Scene scene = new Scene(root, 600, 400, Color.WHITE);

    // Status
    HBox top = new HBox();
    top.setPadding(new Insets(10, 10, 10, 10));
    top.setSpacing(10);
    Label status_title = new Label("Status: ");
    Label status_value = new Label("ZABIJ SIE");
    top.getChildren().addAll(status_title, status_value);
    root.setTop(top);

    // share
    HBox bottom = new HBox();
    Label share_label = new Label("User: ");
    ComboBox<String> shareComboBox = new ComboBox<String>();
    shareComboBox.getItems().addAll(
        "Highest",
        "High",
        "Normal",
        "Low",
        "Lowest"
    );
    Button shareButton = new Button(" Share ");
    shareButton.setOnAction((ActionEvent event) -> {

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
    final ObservableList<String> files = FXCollections.observableArrayList(new File("/Users/dawidcwiek/Desktop/hack/java-project/java-project").list());
    final ListView<String> filesListView = new ListView<>(files);
    gridpane.add(filesListView, 0, 2);

    final ObservableList<String> share = FXCollections.observableArrayList();
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
    launch(args);
  }
}
