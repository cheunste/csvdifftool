package VarexpInterface.Comparison;

import VarexpInterface.Database.dbConnector;
import VarexpInterface.PropertyManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/*
This class is the result of the SQL comparision Test.

It does the following:
1) Create a table called "resultoutput" in Mysql
2) Execute the numerous tests from Victor
3) Exports the resulting table to a spreadsheet
 */
public class Result {
    private static String resultTable = "resultTable";
    private static String resultDatabase = "resultOutput";

    //Log
    private static final Logger log = LogManager.getLogger(Result.class);

    //Delete and recreate the DB and table
    public Result() {

        //Delete the DB first
        dbConnector.deleteDB(resultDatabase);

        //Create a resultTable database and table
        try {
            log.info("Attempting to insert header to the " + PropertyManager.getDefaultFileName());
            dbConnector.createDatabase(resultDatabase);
            dbConnector.sqlExecute(resultDatabase,

                    "CREATE TABLE `" + resultTable + "` (\n" +
                            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                            "  `TagName` varchar(45) NOT NULL,\n" +
                            "  `Tag Name Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Description Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Digitals Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Units Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Analogs Minimum Ratio Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Analogs Maximum Ratio Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Type Test` varchar(45) DEFAULT NULL,\n" +
                            "  `OPC DNP3 Source Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Commandable Range Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Internal Type Check Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Producer Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Comment` longtext,\n" +
                            "  PRIMARY KEY (`id`)\n" +
                            ") ENGINE=InnoDB AUTO_INCREMENT=1022 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");
        } catch (Exception e) {
            log.error("Warning: Cannot create headers to " + PropertyManager.getDefaultFileName() +
                    "error: " + e);

        } finally {
            dbConnector.close();
        }
    }

    public static String resultDatabaseName() {
        return resultDatabase + "." + resultTable;
    }

    //Delete and recreate the DB and table
    public static void createResultDB() {

        //Create a resultTable database and table
        try {
            log.info("Creating Result Database");
            dbConnector.createDatabase(resultDatabase);
            dbConnector.sqlExecute(resultDatabase,

                    "CREATE TABLE `" + resultTable + "` (\n" +
                            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                            "  `TagName` varchar(45) NOT NULL,\n" +
                            "  `Tag Name Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Description Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Digitals Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Units Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Analogs Minimum Ratio Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Analogs Maximum Ratio Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Type Test` varchar(45) DEFAULT NULL,\n" +
                            "  `OPC DNP3 Source Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Commandable Range Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Internal Type Check Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Producer Test` varchar(45) DEFAULT NULL,\n" +
                            "  `Comment` longtext,\n" +
                            "  PRIMARY KEY (`id`)\n" +
                            ") ENGINE=InnoDB AUTO_INCREMENT=1022 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");
        } catch (Exception e) {
            log.error("Failed to create the Result Database. Error: " + e);

        } finally {
            dbConnector.close();
        }
    }


    public static String getResultTable() {
        return resultTable;
    }

    public static String getResultDatabase() {
        return resultDatabase;
    }

    //Calls the numerous test's execute method
    /*
    prereq: Check to see if the three tables exists
    - matrkondb
    - newconfigDB
    - oldconfigDB

    If it does exist, then create a class for all tests and have them execute each test individually
     */
    public static void executeTests(String matrikonDB, String newConfigDB, String oldConfigDB) {
        ComparisonTest comparisonTest = new ComparisonTest(matrikonDB, newConfigDB, oldConfigDB, getResultDatabase());
        if (comparisonTest.checkDBExistance()) {
            ComparisonTest.executeTest();
        }
    }

    /*
        Checks the DBs to see if the line in the files are equal.
        SHould be called after importing the files, before calling export
     */
    public static boolean compareLines(String oldDB, String newDB, String matrikonDB) {

        int oldDBSize = Integer.parseInt(dbConnector.getTableSize(oldDB));
        int newDBSize = Integer.parseInt(dbConnector.getTableSize(newDB));
        int matrikonDBSize = Integer.parseInt(dbConnector.getTableSizeMatrikon(matrikonDB));
        log.info("lines in old Config:" + oldDBSize + " new config: " + newDBSize + " matrikon config: " + matrikonDBSize);

        return (oldDBSize == newDBSize && matrikonDBSize == newDBSize);
    }


    //Export result to a csv or excel spreadsheet
    /*
    This function exports the resultoutput database to a csv file.
     */
    public static void exportResult() {

        String fileName = PropertyManager.getDefaultFileName();
        String fileLocation = PropertyManager.getDefaultFilePath();

        //Query to output content from the DB
        String fullPath = (fileLocation + fileName).replace("\\", "\\\\");
        log.info("Exporting the Result Table to " + PropertyManager.getDefaultFileName());

        //"into outfile 'C:\\\\Users\\\\Stephen\\\\Documents\\\\ComparisonTool\\\\" + fileName + "'\n" +
        String saveToFile = "select \"Tag Name\",\"Tag Name Test\", \"Description Test\", \"Digitals Test\", \"Units Test\",\"Analogs Minimum Ratio Test\",\"Analogs Maximum Ratio Test\",\"Type Test\" ,\"OPC DNP3 Source Test\",\"Commandable Range Test\",\n" +
                "\t\"Internal Type Check Test\",\"Producer Test\",\"Comment\"\n" +
                "union all\n" +
                "(\n" +
                "Select `TagName`,`Tag Name Test`, `Description Test`, `Digitals Test`, `Units Test`,`Analogs Minimum Ratio Test`,`Analogs Maximum Ratio Test`,`Type Test` ,`OPC DNP3 Source Test`,`Commandable Range Test`,\n" +
                "\t`Internal Type Check Test`,`Producer Test`,`Comment`\n" +
                "into outfile '" + fullPath + "'\n" +
                "fields terminated by ','\n" +
                "enclosed by '\"'\n" +
                "escaped by '\\\\'\n" +
                "lines terminated by \"\\r\"\n" +
                "from resultoutput.resulttable\n" +
                ");";

        log.debug("Export query: " + saveToFile);

        //call query to save to file. Note that there isn't any file checks here
        //Because that's already handled in the ComparionsScneeController's fileCheck()
        try {
            dbConnector.sqlExecute("", saveToFile);
        } catch (Exception e) {
            log.error("Not able to export to " + PropertyManager.getDefaultFileName() + ". error: " + e);
        }

    }

    public static boolean resultFileExists(String filePath, String fileName) {
        File file = getFile(filePath, fileName);
        return file.exists();
    }

    public static boolean deleteResultFile(String filePath, String fileName) {
        log.info("Attempting to delete  " + fileName);
        File file = getFile(filePath, fileName);
        boolean fileDeleted = file.delete();
        //System.out.println(fileDeleted ? "File has been deleted" : "File couldn't be dleeted");
        String deleteMessage = (fileDeleted ? "File has been deleted" : "File couldn't be dleeted");
        log.info(deleteMessage);
        return fileDeleted;
    }

    //Tests to see if a file is opened
    public static boolean resultFileOpen(String filePath, String fileName) {
        File file = getFile(filePath, fileName);
        boolean fileIsLocked = !file.renameTo(file);
        //System.out.println(fileIsLocked ? "File is Locked and not usable" : "File usable");
        String fileOpenMessage = (fileIsLocked ? "File is Locked and not usable" : "File usable");
        log.info(fileOpenMessage);
        //Remember to log
        return fileIsLocked;
    }


    //Returns a file object needed for resultFile
    private static File getFile(String filePath, String fileName) {
        log.debug("In getFile. Returning " + filePath + fileName);
        return new File(filePath + fileName);
    }

    public static void deleteResultDB() {
        dbConnector.deleteDB(resultDatabase);
    }
}
