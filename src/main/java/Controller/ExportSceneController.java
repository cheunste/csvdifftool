package Controller;

import VarexpInterface.Database.Buffer;
import VarexpInterface.Database.Export;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Created by Stephen on 8/12/2018.
 */
public class ExportSceneController {
    private final int WINDOW_WIDTH = 250;
    private final int SPACING = 10;
    private final String PATH_TO_SCENE = "/View/ExportScene.fxml";

    private Stage exportWindow;
    private String databaseSelected = "";
    private String outputDirectory;
    private StackPane rootScene;
    private JFXDialog dialog;
    private StackPane dialogPane;

    @FXML
    private Label outputLabel;

    @FXML
    private JFXButton exportButton;

    @FXML
    private JFXButton selectButton;

    @FXML
    private JFXButton cancelButton;

    public ExportSceneController(String database) {

        this.databaseSelected = database;

    }

    @FXML
    /*
    private void initialize(){
        databaseSelected=getdatabaseSelected();
        outputDirectory=null;
        exportButton.disableProperty().bind(
            outputLabel.textProperty().isEmpty()
        );
        exportButton.setOnAction(e->{
            Buffer buffer = new Buffer();
            Export exp = new Export(getdatabaseSelected(), buffer, outputDirectory);
            exp.exportDB();
        });

        selectButton.setOnAction(e->{
            outputDirectory=directorySelector(exportWindow);
            outputLabel.setText(outputDirectory);
        });

        cancelButton.setOnAction(e->{
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }
    */


    private String directorySelector(Stage stage) {
        String chosenDirectory = "";
        //Using the try catch block because if the user hits cancel, then that throws a nullpointerexception
        try {
            DirectoryChooser exportDirectoryChooser = new DirectoryChooser();
            exportDirectoryChooser.setTitle("Export Varexp File");
            exportDirectoryChooser.getInitialDirectory();
            chosenDirectory = exportDirectoryChooser.showDialog(stage).getAbsolutePath();
        } catch (NullPointerException e) {
            //Ignore all exception. It just means the user clicked cancel
        } finally {
            return chosenDirectory;
        }
    }

    public void display() throws Exception {

        System.out.println(databaseSelected);
        exportWindow = new Stage();
        exportWindow.initModality(Modality.APPLICATION_MODAL);
        //Parent rootScene = FXMLLoader.load(getClass().getResource(PATH_TO_SCENE));
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_TO_SCENE));
        //loader.setController(this);
        rootScene = loader.load();

        exportButton = (JFXButton) rootScene.lookup("#exportButton");
        selectButton = (JFXButton) rootScene.lookup("#selectButton");
        cancelButton = (JFXButton) rootScene.lookup("#cancelButton");
        outputLabel = (Label) rootScene.lookup("#outputLabel");

        exportButton.disableProperty().bind(
                outputLabel.textProperty().isEmpty()
        );
        exportButton.setOnAction(e -> {
            Buffer buffer = new Buffer();
            Export exp = new Export(getdatabaseSelected(), buffer, outputDirectory);
            exp.exportDB();

            showDialog();
            //closeStage(exportButton.getScene().getWindow());
        });

        selectButton.setOnAction(e -> {
            outputDirectory = directorySelector(exportWindow);
            outputLabel.setText(outputDirectory);
        });

        cancelButton.setOnAction(e -> {

            closeStage(cancelButton.getScene().getWindow());
        });

        exportWindow.setScene(new Scene(rootScene));
        exportWindow.setTitle("Choose Export Directory:");
        exportWindow.setTitle("Export Varexp");
        exportWindow.showAndWait();
    }

    public String getdatabaseSelected() {
        System.out.println(this.databaseSelected);
        return this.databaseSelected;
    }

    private void closeStage(Window window) {
        Stage stage = (Stage) window;
        stage.close();

    }

    private void showDialog() {
        //create a new dialogLayout along with its content
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Export"));
        content.setBody(new Text(" File successfully exported"));

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

    public void setDatabaseSelected(String databaseSelected) {
        this.databaseSelected = databaseSelected;
        System.out.println(databaseSelected);
    }

}
