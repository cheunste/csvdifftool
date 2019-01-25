package Controller;

import com.company.Comparison.Result;
import com.company.Database.*;
import com.company.PropertyManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ComparisonSceneController implements Initializable {

    static final Logger logger = LogManager.getLogger(ComparisonSceneController.class.getName());

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

        dbConnector dbc = new dbConnector();

        boolean dbExists = dbc.verifyDBExists(databaseName);

        if (dbExists) {

            //System.out.println("Database already exists. Overwrite? (Y/N)");
            //Scanner sc = new Scanner(System.in);
            //String choice = sc.next().toLowerCase();

            deleteDB(databaseName);
            createVarexpDB(databaseName);
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
        dbConnector dbc = new dbConnector();

        boolean dbExists = dbc.verifyDBExists(databaseName);

        if (dbExists) {
            deleteDB(databaseName);
            dbc.createMatrikonDB(databaseName);
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

    private static void deleteDB(String databaseName) {
        logger.info("Attempting to delete DB " + databaseName);
        dbConnector db = new dbConnector();
        db.deleteDB(databaseName);
        db.close();
    }

    private static void createVarexpDB(String databaseName) {
        dbConnector db = new dbConnector();
        db.createVarexpDB(databaseName);
        db.close();
    }

    //Overloaded method solely used for Matrikon imports
    private static void createMatrikonDB(String databaseName) {
        dbConnector db = new dbConnector();
        db.createMatrikonDB(databaseName);
        db.close();
    }

    private static void createOuptutDB(String databaseName) {

        dbConnector db = new dbConnector();
        db.close();

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
                //TODO: Add a screen to tell user to close the file
                logger.info("File is open. Please close to continue. Program will now shut exit");
                return false;
            }
            //If file isn't open, then delete it
            Result.deleteResultFile(filePath, fileName);
        }
        return true;
    }

    private void compareBtnVisibilityEnable() {

        logger.info("Old config File Path" + oldConfigFilePath.getText());
        logger.info("Old config File Path" + newConfigFilePath.getText());
        logger.info("Old config File Path" + matrikonFilePath.getText());


        if ((oldConfigFilePath.getText() != null &&
                newConfigFilePath.getText() != null &&
                matrikonFilePath.getText() != null)
        ) {
            logger.info("Compare Btn: " + compareBtn.isDisable());
            compareBtn.setDisable(false);
        } else {
            logger.info("Compare Btn: " + compareBtn.isDisable());
            compareBtn.setDisable(true);
        }
    }

    public void compare() throws IOException, SQLException {

        //Fetch fields from the config file
        PropertyManager pm = new PropertyManager();
        pm.getPropertyValues();


        //Debug Mode. In this mode, everything important should be logged but more importantly, the DB should not be deleted.
        boolean debugMode = debugModeBtn.isSelected();
        if (debugMode) {
            logger.debug("Debug Mode seelcted");
        }

        //These are names of the databases that will be created. Since these are going to be dleted
        //I really do not care if I hardcode them
        String oldDB = "oldVarexpDB";
        String newDB = "newVarexpDB";
        String matrikonDB = "matrikonDB";

        //Check to see if file is open. If it is, exit the program immediately
        if (!fileCheck()) {
            //TODO: Replace this with a popup for the user
            logger.info("Program will not run as the " +
                    PropertyManager.getDefaultFilePath() + PropertyManager.getDefaultFileName() +
                    " is still open");
            return;
        }


        //Result.exportResult();
        //System.out.println("derp");

        //Delete the DBs beforehand just in case. They should be deleted after everything is done, but might not
        //due to debug mode
        logger.info("Attempting to delete the three databases");
        deleteDB(oldDB);
        deleteDB(newDB);
        deleteDB(matrikonDB);

        //Create a reference to Result class. Result class is used for output. Instaniating it will create
        // the DB used to store results
        Result.deleteResultDB();
        Result.createResultDB();

        //Import the Old Varexp and New Varexp into a newVarexpDB and oldVarexpDB and create a finalVarexpDB
        logger.info("Creating the DB for the new and old config");
        createVarexpDB(oldDB);
        createVarexpDB(newDB);


        logger.info("Creating the DB for the matrikon config");
        createMatrikonDB(matrikonDB);

        logger.info("DBs created");

        //Import the varexps.
        //TODO: The following are the thread version of importFile...still a WIP
        //new Thread(new ImportThreadExecutor(oldConfigFilePath.getText(),oldDB)).start();
        //new Thread(new ImportThreadExecutor(newConfigFilePath.getText(),newDB)).start();

        logger.info("Importing the old config file to its DB ");
        importFile(oldConfigFilePath.getText(), oldDB);

        logger.info("Importing the new config file to its DB");
        importFile(newConfigFilePath.getText(), newDB);

        //Import the MatrikonFactory file
        logger.info("Importing the matrikon file to the DB");
        importMatrikon(matrikonFilePath.getText(), matrikonDB);

        //Get the number of items from the DB. If they do not match, then throw a warning and end the program.
        //TODO: Do something about matrikon.common as that doesn't exist, but the function stil uses it
        //TODO: You need to have a pop up to verify user if they want to continue or not
        boolean equalLines = Result.compareLines(oldDB, newDB, matrikonDB);
        if (!equalLines) {
            logger.info("Lines between the three files are not equal");
            //End the program
        } else {

        }

        //Create a result Database and a  Result Table.

        //Wait some time in order to let mysql set up all the DB
        //TODO: Replace this with a Future/Promise so you don't have to wait. Because that's bullshit.
        dbWait(FIVE_SECONDS);
        Result.executeTests(matrikonDB, newDB, oldDB);

        //Export the database.
        Result.exportResult();
        logger.info(PropertyManager.getDefaultFileName() + " created");

        //Drop the databases (becaues at this point, you're done)
        logger.info("DBs deleted");
        if (!debugMode) {
            logger.debug("Debug Mode is not selected. DBs will be deleted");
            deleteDB(oldDB);
            deleteDB(newDB);
            deleteDB(matrikonDB);
        } else {
            logger.debug("Debug Mode is enabled. DBs will not be deleted");
        }
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

    private static class ImportThreadExecutor implements Runnable {

        String dbName;
        String configFilePath;

        public ImportThreadExecutor(String configfilePath, String dbName) {
            this.dbName = dbName;
            this.configFilePath = configfilePath;
        }

        @Override
        public void run() {
            try {
                importFile(configFilePath, dbName);
            } catch (IOException e) {
            } catch (SQLException e) {
            }
        }

    }


}
