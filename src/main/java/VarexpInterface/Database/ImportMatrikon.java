package VarexpInterface.Database;

import VarexpInterface.fileHandler;
import VarexpInterface.matrikon.MatrikonVariable;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportMatrikon implements Runnable {
    private String[] args;
    private Buffer buffer;
    private String path;
    private String dbName;
    private int totalLines;


    public ImportMatrikon(String filePath, String dbName, Buffer buffer) throws IOException, SQLException {
        this.path = filePath;
        this.dbName = dbName;
        this.buffer = buffer;
    }

    public void importFile() throws IOException, ArrayIndexOutOfBoundsException, SQLException {
        String line;
        fileHandler fh = new fileHandler();
        //open file
        BufferedReader fileBR = fh.readInput(path);
        int totalLine = 0;

        /*
        For each line in the file do the following:
        1) Separate the line from String to fields (List or Arraylist)
        2) Add the newly converted list to another list. You will have a 2D List of lists<String>
        3) For each list in the list, get the variable parameter
        4) With the parameter in 3), map it to the correct database and write into the DB
         */

        //Create a fileLIst arrayList to hold all items
        ArrayList<List<String>> fullList = new ArrayList();

        //read the file one by one and store them into a list
        while ((line = fileBR.readLine()) != null) {
            String appendedString = line;
            List<String> tempItem = Arrays.asList(appendedString.split(","));
            fullList.add(tempItem);
            totalLine++;
        }
        this.totalLines = totalLine;

        //Call the Matrikon Insert function in the MatrikonVariable class
        MatrikonVariable matrikonVariable = new MatrikonVariable(dbName);

        for (int i = 0; i < fullList.size(); i++) {
            matrikonVariable.importMatrikonVariable(fullList.get(i));
        }

        //Actually store the tags into the DB
        //close file
        fh.closeFile(fileBR);
        buffer.setDoneFlag();
    }

    public int getTotalLine() {
        return this.totalLines;
    }

    @Override
    public void run() {
        try {
            importFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
