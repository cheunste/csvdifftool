package com.company.Database;

import com.company.pcvue.fields.VarexpFactory;
import com.company.pcvue.fields.VarexpVariable;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Stephen on 6/23/2018.
 */
/*
The purpose of this class is to export the entire database from the user when the user gives an export command
 */
public class Export implements Runnable {

    private static String encoding = "UTF-8";
    private static String fileName = "\\Varexp.txt";
    //TODO: Use the BUffer class to pass on a ConcurrentLinkedQueue to this class and the dbConnector
    private String databaseName;
    private PrintWriter outputFile;
    private ArrayList<ArrayList<String>> outptuVarexpVariableList;
    private String outputFilePath;

    //Constructor
    public Export(String databaseName, Buffer buffer, String outputFilepath) {
        this.databaseName = databaseName;
        this.outputFilePath = outputFilepath;
    }

    //Removes '[', ']' and removes all empty spaces after a single comma
    private static String removeUnwantedCharacters(String varexpVariable) {
        return varexpVariable.replace("[", "").replace("]", "").replaceAll(",\\ *", ",").replace("\"", "");
        //return varexpVariable.replace("[", "").replace("]", "").trim();
    }

    public void exportDB() {

        //Verify that the DB exists first
        boolean exists = dbConnector.verifyDBExists(databaseName);

        if (exists) {
            createFile();
            fetch(databaseName);
        } else {
        }
    }

    /**
     * This function performs the actual fetching from MySQL.
     * @param databaseName Name of the database (ie twin_buttes_2)
     */
    private void fetch(String databaseName) {

        String numVarexpVariable = dbConnector.getTableSize(databaseName);
        String sqlCmd = "SELECT * FROM common ";
        ArrayList<ArrayList<String>> resultList = dbConnector.readDatabase(databaseName, sqlCmd);
        ArrayList<ArrayList<String>> csvListOutput = new ArrayList<ArrayList<String>>();


        VarexpFactory newVariable = new VarexpFactory();
        VarexpVariable common = newVariable.declareNewVariable("COMMON");
        for (ArrayList<String> innerList : resultList) {

            //Remove the id, which is always position 0
            //String id =innerList.remove(0);

            ArrayList<String> outputVarexpVariable = new ArrayList<String>();
            //sets up the new variable positions:
            for (int x = 1; x <= common.getFieldNum(); x++) {
                outputVarexpVariable.add("");
            }

            //Sets the item form the COMMON table to the outputVarexpVariable.
            int y = 1;
            for (int position : common.getVarexpPositionList()) {
                String temp = removeUnwantedCharacters(innerList.get(y));
                outputVarexpVariable.set(position, temp);
                y++;
            }
            csvListOutput.add(outputVarexpVariable);
        }

        for (String table : newVariable.listOfTables) {
            //Do nothing for the common table as it has already been taken care of
            if (table == "COMMON") {

            } else {
                VarexpVariable tempVar = newVariable.declareNewVariable(table);
                String tableSqlCmd = "SELECT * FROM " + tempVar.getTableName();

                ArrayList<ArrayList<String>> tableList = dbConnector.readDatabase(databaseName, tableSqlCmd);

                for (ArrayList<String> innerList : tableList) {
                    String id = innerList.remove(0);
                    ArrayList<String> commonTemp = csvListOutput.get(Integer.parseInt(id));

                    int a = 0;
                    for (int position : tempVar.getVarexpPositionList()) {
                        String difference = StringUtils.difference(commonTemp.get(position), innerList.get(a));
                        //Only set value at commonTemp if and only if innerList.get is NOT empty
                        if (!innerList.get(a).equals("")) {
                            commonTemp.set(position, innerList.get(a));
                        }
                        a++;
                    }

                }
            }
        }
        for (ArrayList<String> outputList : csvListOutput) {
            writeLine(removeUnwantedCharacters(outputList.toString()));
        }
        closeFile();
    }

    private void createFile() {
        try {
            //this.outputFile = new PrintWriter("C:\\Users\\Stephen\\Desktop\\varexp tool project\\Test-Varexp.csv", encoding);
            System.out.println(outputFilePath + fileName);
            this.outputFile = new PrintWriter(outputFilePath + fileName, encoding);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //This function writes a VarexpVariable line into the varexp file
    private void writeLine(String varexpLine) {
        this.outputFile.println(varexpLine);
    }

    private String formatVarexp(VarexpVariable varexp) {
        return null;
    }

    //Closes the file. After this call, you're done exporting the DB to file
    private void closeFile() {
        this.outputFile.close();
    }

    @Override
    public void run() {

    }
}
