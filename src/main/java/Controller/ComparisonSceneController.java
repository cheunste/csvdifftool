package Controller;

import VarexpInterface.Comparison.CompareTask;
import VarexpInterface.Comparison.ConfigImportTask;
import VarexpInterface.Comparison.Result;
import VarexpInterface.Database.dbConnector;
import VarexpInterface.PropertyManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ComparisonSceneController implements Initializable {

    public static final String oldDB = "oldVarexpDB";
    public static final String newDB = "newVarexpDB";
    public static final String matrikonDB = "matrikonDB";
    static final Logger logger = LogManager.getLogger(ComparisonSceneController.class.getName());

    Stage currentWindow;

    //Main Buttons on GUI
    @FXML
    private JFXButton oldConfigImportBtn;
    @FXML
    private JFXButton newConfigImportBtn;
    @FXML
    private JFXButton matrikonConfigImportBtn;
    @FXML
    private JFXButton compareBtn;

    //ProgressBar
    @FXML
    private ProgressBar progressBar = new ProgressBar(0);

    //Debug Mode
    @FXML
    private JFXCheckBox debugModeBtn;

    //TextFields. These exists so I can display a file path to the user
    @FXML
    private JFXTextField oldConfigFilePath;
    @FXML
    private JFXTextField newConfigFilePath;
    @FXML
    private JFXTextField matrikonFilePath;


    //Function to perform a file check before running the actual execute code
    /*
    return: true: if file is not opened and file is deleted
            false: if file is opened
     */
    public boolean fileCheck() {

        String fileName = PropertyManager.getDefaultFileName();
        String filePath = PropertyManager.getDefaultFilePath();

        boolean fileExists = Result.resultFileExists(filePath, fileName);

        //If the file exists, check if it is open. If it is  open, then
        // inform user to close it. Program will not continue
        if (fileExists) {
            logger.info("File " + fileName + " Exists");
            boolean fileOpen = Result.resultFileOpen(filePath, fileName);
            if (fileOpen) {
                logger.info("File is open. Please close to continue. Program will now shut exit");
                return false;
            }
            //If file isn't open, then delete it
            Result.deleteResultFile(filePath, fileName);
        }
        return true;
    }

    private void compareBtnVisibilityEnable() {

        String oldConfigPathText = oldConfigFilePath.getText();
        String newConfigPathText = newConfigFilePath.getText();
        String matrikonConfigText = matrikonFilePath.getText();

        logger.info("Old config File Path" + oldConfigPathText);
        logger.info("Old config File Path" + newConfigPathText);
        logger.info("Old config File Path" + matrikonConfigText);


        if (
                (oldConfigPathText != null && !oldConfigPathText.equals("")) &&
                        (newConfigPathText != null && !newConfigPathText.equals("")) &&
                        (matrikonConfigText != null && !matrikonConfigText.equals(""))
        ) {
            compareBtn.setDisable(false);
            logger.info("CompareTask Btn: " + compareBtn.isDisable());
        } else {
            compareBtn.setDisable(true);
            logger.info("CompareTask Btn: " + compareBtn.isDisable());
        }
    }

    public void compare() throws IOException, SQLException {

        //Get the properties in the properties file
        PropertyManager pm = new PropertyManager();
        try {
            pm.getPropertyValues();
        } catch (IOException e) {
        }

        //Sett Progress bar to zero and unbind progress bar
        progressBar.progressProperty().unbind();

        //Check if the mysql server is alive. If not, show a popup to user
        if (dbConnector.serverAlive(PropertyManager.getDatabaseIP())) {
            //Doesn't do anything if server is alive
        } else {
            Alert serverAlert = new Alert(Alert.AlertType.ERROR);
            serverAlert.setTitle("Server Error");
            serverAlert.setHeaderText("Server Connection Error");
            serverAlert.setContentText("There was an issue in connection to " + PropertyManager.getDatabaseIP() + ". \nIs the server online?");
            serverAlert.showAndWait();
            return;
        }

        //Check to see if file is open. If it is, exit the program immediately
        if (!fileCheck()) {
            logger.info("Program will not run as the " +
                    PropertyManager.getDefaultFilePath() + PropertyManager.getDefaultFileName() +
                    " is still open");

            //ERROR Alert
            Alert openFileErrorAlert = new Alert(Alert.AlertType.ERROR);
            openFileErrorAlert.setTitle("Open File Error");
            openFileErrorAlert.setHeaderText("File Opened Error");
            openFileErrorAlert.setContentText("The output file: " + PropertyManager.getDefaultFileName() + " is still open." +
                    "Please close the file and try again");
            openFileErrorAlert.showAndWait();
            return;
        }

        //Declare the Task objects
        ConfigImportTask configImportTask = new ConfigImportTask(oldDB, newDB, matrikonDB,
                oldConfigFilePath.getText(), newConfigFilePath.getText(), matrikonFilePath.getText()
        );

        CompareTask compareTask = new CompareTask(oldDB, newDB, matrikonDB,
                oldConfigFilePath.getText(), newConfigFilePath.getText(), matrikonFilePath.getText(),
                debugModeBtn.isSelected());

        //Bind ProgressBar to ConfigImportTask
        progressBar.progressProperty().bind(configImportTask.progressProperty());

        /*
        Wait until the importing the config file is done.
        Then check the lines in the database (throw dialog if needed )
        Afterwards, if the equalLineCheck is true, then start a new thread to execute the compareTask thread
        */
        configImportTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        //If true, continue

                        progressBar.progressProperty().unbind();
                        progressBar.setProgress(0);
                        if (equalLineCheck()) {
                            progressBar.progressProperty().bind(compareTask.progressProperty());

                            new Thread(compareTask).start();
                        } else {
                            return;
                        }
                    }
                });

        compareTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                        logger.info("Call back from compareTask event handler");

                        progressBar.progressProperty().unbind();
                        progressBar.setProgress(0);

                        Alert completed = new Alert(Alert.AlertType.CONFIRMATION);
                        completed.setTitle("Comparison Done");
                        completed.setHeaderText("Config Comparison Completed!");
                        completed.setContentText("Comparison Done. Please see: " + PropertyManager.getDefaultFileName());
                        completed.showAndWait();
                    }
                });

        new Thread(configImportTask).start();
    }

    private void deleteDatabases() {
        dbConnector.deleteDB(oldDB);
        dbConnector.deleteDB(newDB);
        dbConnector.deleteDB(matrikonDB);
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        //Button mapping

        //The following three buttons allow user to select the old,new and matrikon config respectively
        oldConfigImportBtn.setOnAction((ActionEvent e) -> {
            try {

                logger.info("oldConfig Btn clicked");
                oldConfigFilePath.setText(getFilePath(currentWindow));
            } catch (Exception x) {

            }
            compareBtnVisibilityEnable();
        });
        newConfigImportBtn.setOnAction((ActionEvent e) -> {
            try {

                logger.info("newConfig Btn clicked");
                newConfigFilePath.setText(getFilePath(currentWindow));
            } catch (Exception x) {

            }
            compareBtnVisibilityEnable();
        });
        matrikonConfigImportBtn.setOnAction((ActionEvent e) -> {
            try {

                logger.info("MatrikonConfig Btn clicked");
                matrikonFilePath.setText(getFilePath(currentWindow));

            } catch (Exception x) {

            }
            compareBtnVisibilityEnable();
        });

        debugModeBtn.setOnAction((ActionEvent e) -> {
            oldConfigFilePath.setText("C:\\Users\\Stephen\\IdeaProjects\\databaseCrap\\out\\artifacts\\VarexpInterface\\Varexp_FE03_SHILO_OLD.csv");
            newConfigFilePath.setText("C:\\Users\\Stephen\\IdeaProjects\\databaseCrap\\out\\artifacts\\VarexpInterface\\Varexp_FE03_SHILO_NEW.csv");
            matrikonFilePath.setText("C:\\Users\\Stephen\\IdeaProjects\\databaseCrap\\out\\artifacts\\VarexpInterface\\Matrikon_FE03_SHILO.csv");
            compareBtn.setDisable(false);

        });

        //This  button fires the main function
        compareBtn.setOnAction((ActionEvent e) -> {
            try {
                logger.info("CompareTask Btn clicked");
                compare();
            } catch (IOException ioe) {
                logger.info("Error occured when comparing. Error: " + ioe);
                ioe.printStackTrace();
            } catch (SQLException sqe) {
                logger.info("SQL Error occured when comparing. Error: " + sqe);
                sqe.printStackTrace();
            }

        });

    }

    private boolean getDebugMode() {
        return debugModeBtn.isSelected();
    }

    //This is used to keep track of prvevious stages so I can close them later
    public void setCurrentWindow(Stage window) {
        this.currentWindow = window;
    }

    public String getFilePath(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Config");
        File configFile = fileChooser.showOpenDialog(stage);
        return configFile.getAbsolutePath();
    }

    public boolean databaseExists(String oldDB, String newDB, String matrikonDB) {
        dbConnector db = new dbConnector();
        return (dbConnector.verifyDBExists(oldDB) && dbConnector.verifyDBExists(newDB) && dbConnector.verifyDBExists(matrikonDB));
    }


    private boolean equalLineCheck() {
        boolean equalLines = Result.compareLines(oldDB, newDB, matrikonDB);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        if (!equalLines) {

            Alert equalLinesAlert = new Alert(Alert.AlertType.CONFIRMATION);
            equalLinesAlert.setTitle("Confirmation");
            equalLinesAlert.setHeaderText("Lines between the three config files are not equal.");
            equalLinesAlert.setContentText("Do you want to continue using the comparison tool?");
            logger.info("Lines between the three files are not equal");

            Optional<ButtonType> equalLinesAlertResult = equalLinesAlert.showAndWait();
            if (equalLinesAlertResult.get() == ButtonType.OK) {
                logger.info("Lines are not equal. Continue using the tool");
                return true;
            } else {
                logger.info("LInes are not equal. Cancelling the tool");
                return false;
            }
        }
        return true;
    }
}
