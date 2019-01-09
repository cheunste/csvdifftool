package Controller;

import com.company.Database.Buffer;
import com.company.Database.Import;
import com.company.Database.ImportHandler;
import com.company.Database.dbConnector;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Stephen on 8/13/2018.
 */
public class ImportSceneController {

    private static JFXDialog dialog;
    private Stage importWindow;
    private String PATH_TO_SCENE = "/View/ImportScene.fxml";
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

        try {
            Buffer buffer = new Buffer();
            ExecutorService executor = Executors.newFixedThreadPool(2);
            //This is the consumer. It consumes data in the queue
            Future<Boolean> future = executor.submit(new ImportHandler(buffer, databaseName));
            //This will be the producer (see producer-consumer problem if you're not familiar with hte term)
            executor.execute(new Import(fileLocation, databaseName, buffer));
            //Shtudown
            executor.shutdown();
            if (future.get()) {
                showDialog();
            } else {
            }
        } catch (Exception e) {

        } finally {
        }
    }

    //The following initializes all the buttons to their proper actions or functions
    public void display() throws Exception {

        importWindow = new Stage();
        importWindow.initModality(Modality.APPLICATION_MODAL);
        importDatabaseName = new JFXTextField();
        browseButton = new JFXButton();
        importButton = new JFXButton();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_TO_SCENE));
        rootScene = loader.load();

        importButton = (JFXButton) rootScene.lookup("#importButton");
        browseButton = (JFXButton) rootScene.lookup("#browseButton");
        cancelButton = (JFXButton) rootScene.lookup("#cancelButton");
        importDatabaseName = (JFXTextField) rootScene.lookup("#importDatabaseName");
        outputLabel = (Label) rootScene.lookup("#outputLabel");

        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

        browseButton.setOnAction(e -> {
            //String importFile = getFilePath();
            getFilePath();
        });

        importButton.disableProperty().bind(
                Bindings.or(importDatabaseName.textProperty().isEmpty(), outputLabel.textProperty().isEmpty())
        );
        importButton.setOnAction(e -> {
            try {
                importFile(outputLabel.getText(), importDatabaseName.getText());
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        importWindow.setScene(new Scene(rootScene));
        importWindow.setTitle("Choose Export Directory:");
        importWindow.setTitle("Import Varexp");

        importWindow.showAndWait();

    }

    //Gets a file path from the user
    private void getFilePath() {
        String filePath;
        FileChooser fc = new FileChooser();
        fc.setTitle("Import Varexp file");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Varexp File", "*.txt")
        );
        File selectedFile = fc.showOpenDialog(importWindow);
        if (selectedFile != null) {
            outputLabel.setText(selectedFile.getAbsolutePath());
        }
        //Reaching here doesn't really mean anything useful. It just means the user hit the cancel button for
        //whatever the reason
        else {
            outputLabel.setText("");
        }
    }


    private void showDialog() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_TO_SCENE));

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Import"));
        content.setBody(new Text(" File successfully imported"));

        //Create a new stackpane just for the dialog
        StackPane dialogPane = new StackPane();

        //Create a dialog container, which contains the dialog's layout, the stackpane and its transition)
        dialog = new JFXDialog(dialogPane, content, JFXDialog.DialogTransition.CENTER);

        //This is to add a button to the dialog
        JFXButton button = new JFXButton("OK");
        button.setOnAction(e -> {
            dialog.close();
            rootScene.getChildren().remove(dialogPane);
        });
        content.setActions(button);

        //Add the dialog to its stackpane
        rootScene.getChildren().add(dialogPane);
        dialog.show();
    }

}




