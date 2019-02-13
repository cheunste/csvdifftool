package Controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class VarexpFilterSceneController implements Initializable {


    @FXML
    private JFXButton saveBtn;
    @FXML
    private JFXButton cancelBtn;
    @FXML
    private JFXButton clearBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        saveBtn.setOnAction(e -> {
        });
        cancelBtn.setOnAction(e -> {
        });
        clearBtn.setOnAction(e -> {
        });
    }
}
