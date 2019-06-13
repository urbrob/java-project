import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;

public class ClientController {

    @FXML
    private Label status;

    @FXML
    void initialize() {
        status.setText("Wysyłam");
    }

    @FXML
    void setStatus(String stat) {
        status.setText(stat);
    }
}
