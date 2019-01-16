package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ComparisonSceneController {
    private static final int DOUBLE_CLICK = 2;

    Stage currentWindow;

    @FXML
    private JFXButton oldConfigImportBtn;
    @FXML
    private JFXButton newConfigImportBtn;
    @FXML
    private JFXButton matrikonConfigImportBtn;
    @FXML
    private JFXButton compareBtn;

    @FXML
    private JFXCheckBox debugModeBtn;

    @FXML
    private JFXTextField oldConfigFilePath;
    @FXML
    private JFXTextField newConfigFilePath;
    @FXML
    private JFXTextField matrikonFilePath;

    //Initialize the window by adding listeners and other binding calls

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        //Button mapping
        oldConfigImportBtn.setOnAction((ActionEvent e) -> {
            try {

            } catch (Exception x) {

            }
        });
        newConfigImportBtn.setOnAction((ActionEvent e) -> {
            try {

            } catch (Exception x) {

            }
        });
        matrikonConfigImportBtn.setOnAction((ActionEvent e) -> {
            try {

            } catch (Exception x) {

            }
        });
    }

    private void compare() {

        //If checked, then it should be in debug mode
        boolean checked = debugModeBtn.isSelected();

    }

    //This is used to keep track of prvevious stages so I can close them later
    public void setCurrentWindow(Stage window) {
        this.currentWindow = window;
    }


}
