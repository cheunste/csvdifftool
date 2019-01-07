package Controller;

import com.company.Database.dbConnector;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class DiffFileController {


    private static JFXDialog dialog;
    private Stage importWindow;
    private String databaseName;
    private boolean databaseNameBoolean;
    private boolean chosenFileBoolean;
    private StackPane rootScene;

    @FXML
    private JFXButton browseButton;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton importButton;

    @FXML
    private JFXTextField importDatabaseName;

    @FXML
    private Label outputLabel;

    //Function to import the varexp file. Ask a user if they want to overwrite a DB first and then actually import it
    private void importFile(String fileLocation, String databaseName) throws IOException, SQLException {

        dbConnector dbc = new dbConnector();

        dbc.createVarexpDB(databaseName);

        //create a database
        //TODO: Consider inserting another alert here
        importHelper(fileLocation, databaseName);
    }

    //An assistant method to ImportHandler. This Does the actual work of importing the DB Really needs a better name
    private void importHelper(String fileLocation, String databaseName) throws IOException, SQLException {


    }

    //The following initializes all the buttons to their proper actions or functions
    public void display() throws Exception {

    }

    //Gets a file path from the user
    private void getFilePath() {

    }


    private void showDialog() {

    }
}
