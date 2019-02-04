package VarexpInterface.Comparison;

import VarexpInterface.PropertyManager;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class CompareTask extends Task<Void> {


    static final Logger logger = LogManager.getLogger(CompareTask.class.getName());
    private static String oldDB;
    private static String newDB;
    private static String matrikonDB;
    private static String oldConfigFilePath;
    private static String newConfigFilePath;
    private static String matrikonFilePath;
    private static boolean debugMode;


    public CompareTask(String oldDB, String newDB, String matrikonDB,
                       String oldFilePath, String newFilePath, String matrikonFilePath,
                       boolean debugMode) {

        CompareTask.oldDB = oldDB;
        CompareTask.newDB = newDB;
        CompareTask.matrikonDB = matrikonDB;
        CompareTask.oldConfigFilePath = oldFilePath;
        CompareTask.newConfigFilePath = newFilePath;
        CompareTask.matrikonFilePath = matrikonFilePath;
        CompareTask.debugMode = debugMode;

    }

    @Override
    protected Void call() throws Exception {

        PropertyManager pm = new PropertyManager();

        try {
            pm.getPropertyValues();
        } catch (IOException e) {

        }

        try {
            Result.deleteResultDB();
            logger.info("Result DB Deleted");
        } catch (Exception e) {
            logger.info("Result DB already deleted");
        }

        //Create the result DB
        Result.createResultDB();
        exportResult();
        if (!debugMode) {
            //deleteDatabases();
        }
        return null;
    }

    private void exportResult() {
        Result.executeTests(matrikonDB, newDB, oldDB);
        Result.exportResult();
    }
}
