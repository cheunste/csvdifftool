package Controller;

import VarexpInterface.Comparison.Compare;
import VarexpInterface.Comparison.ConfigImportTask;
import VarexpInterface.Comparison.Result;
import VarexpInterface.Database.*;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ComparisonSceneController implements Initializable {

    static final Logger logger = LogManager.getLogger(ComparisonSceneController.class.getName());
    public static final String oldDB = "oldVarexpDB";
    public static final String newDB = "newVarexpDB";
    public static final String matrikonDB = "matrikonDB";

    private static int FIVE_SECONDS = 5000;
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

    //Function to import the varexp file. Ask a user if they want to overwrite a DB first and then actually import it
    private static void importFile(String fileLocation, String databaseName) throws IOException, SQLException {

        boolean dbExists = dbConnector.verifyDBExists(databaseName);

        if (dbExists) {

            //System.out.println("Database already exists. Overwrite? (Y/N)");
            //Scanner sc = new Scanner(System.in);
            //String choice = sc.next().toLowerCase();

            dbConnector.deleteDB(databaseName);
            dbConnector.createVarexpDB(databaseName);
            importHelper(fileLocation, databaseName);

            //if (choice.equals("yes")) {
            //} else {
            //    System.out.println("No change will be made. Exiting tool");
            //}
        } else {
            importHelper(fileLocation, databaseName);
        }
    }

    //An assistant method to importFile. This Does the actual work of importing the DB Really needs a better name
    private static void importHelper(String fileLocation, String databaseName) throws IOException, SQLException {

        Buffer buffer = new Buffer();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        //This is the consumer. It consumes data in the queue
        Future<Boolean> future = executor.submit(new ImportHandler(buffer, databaseName));
        //This will be the producer (see producer-consumer problem if you're not familiar with hte term)
        executor.execute(new Import(fileLocation, databaseName, buffer));
        //Shtudown
        executor.shutdown();
    }

    //This function imports a MatrikonFactory configuration file
    private static void importMatrikon(String fileLocation, String databaseName) throws IOException, SQLException {

        boolean dbExists = dbConnector.verifyDBExists(databaseName);

        if (dbExists) {
            dbConnector.deleteDB(databaseName);
            dbConnector.createMatrikonDB(databaseName);
        }
        matrikonHelper(fileLocation, databaseName);
    }

    //An assistant method to importFile. This Does the actual work of importing the DB Really needs a better name
    private static void matrikonHelper(String fileLocation, String databaseName) throws IOException, SQLException {
        Buffer buffer = new Buffer();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        //This is the consumer. It consumes data in the queue
        //Future<Boolean> future = executor.submit(new ImportHandler(buffer, databaseName));
        //This will be the producer (see producer-consumer problem if you're not familiar with hte term)
        executor.execute(new ImportMatrikon(fileLocation, databaseName, buffer));
        //Shtudown
        executor.shutdown();
    }

    private static void dbWait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
            //Result.executeTests(matrikonDB, newDB, oldDB);
        } catch (InterruptedException e) {
        }

    }

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
            logger.info("Compare Btn: " + compareBtn.isDisable());
        } else {
            compareBtn.setDisable(true);
            logger.info("Compare Btn: " + compareBtn.isDisable());
        }
    }

    public void compare() throws IOException, SQLException {


        //Import the varexps.
        //TODO: The following are the thread version of importFile...still a WIP

        //TODO: Create a runnable, thread this to the other class, Compare and then
        // perform a callback when it is done. In said callback, you then do the other crap


        //Get the properties in the properties file
        PropertyManager pm = new PropertyManager();
        try {
            pm.getPropertyValues();
        } catch (IOException e) {
        }

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


        Compare compareTask = new Compare(oldDB, newDB, matrikonDB,
                oldConfigFilePath.getText(), newConfigFilePath.getText(), matrikonFilePath.getText(),
                debugModeBtn.isSelected());

        ConfigImportTask configImportTask = new ConfigImportTask(oldDB, newDB, matrikonDB,
                oldConfigFilePath.getText(), newConfigFilePath.getText(), matrikonFilePath.getText()
        );

        //Wait until the importing the config file is done.
        //Then check the lines in the database (throw dialog if needed )
        //Then
        configImportTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        //If false, return to user.
                        // this means user doesn't want to continue or something happened.
                        if (equalLineCheck() == false) {
                            return;
                        }
                        //Else, you now call upon the comparison class and perform
                        // the comparision portion. This means new threading
                        else {
                            new Thread(compareTask).start();
                        }
                    }
                });

        compareTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {

                    }
                });

        new Thread(configImportTask).start();
        //ExecutorService importPool = Executors.newSingleThreadExecutor();

        //logger.info("Importing the old config file to its DB ");
        //importPool.submit(new ImportThreadExecutor(oldConfigFilePath.getText(), oldDB, false));
        //logger.info("Importing the new config file to its DB");
        //importPool.submit(new ImportThreadExecutor(newConfigFilePath.getText(), newDB, false));
        //logger.info("Importing the matrikon file to the DB");
        //importPool.submit(new ImportThreadExecutor(matrikonFilePath.getText(), matrikonDB, true));
        //importPool.shutdown();

        ////Blocks until all import tasks are completed
        //while (!importPool.isTerminated()) {
        //}
        ////Create a result Database and a  Result Table.

        //Wait some time in order to let mysql set up all the DB
        //TODO: Replace this with a Future/Promise or a fork/join so you don't have to wait. Because that's bullshit.
        //dbWait(FIVE_SECONDS);
        //Result.executeTests(matrikonDB, newDB, oldDB);
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

        //This  button fires the main function
        compareBtn.setOnAction((ActionEvent e) -> {
            try {
                logger.info("Compare Btn clicked");
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
