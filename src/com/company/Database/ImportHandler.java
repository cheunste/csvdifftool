package com.company.Database;

import com.company.pcvue.fields.VarexpVariable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * This class handles the formatting of sql requests before passing them onto the dbConnector class.
 * <p>
 * This class is responsible for fetching table names,
 * joining tables before performming reads or updates
 * Handling basic CRUD operations
 */
public class ImportHandler implements Callable<Boolean> {

    //These are member variables that have to do with mysql connections
    Connection connection;
    Statement statement;
    //These are used for Buffer class.
    private Buffer buffer;

    //Member varible
    private String databaseName;

    public ImportHandler() {

    }

    public ImportHandler(Buffer buffer, String databaseName) throws SQLException {
        this.buffer = buffer;
        this.connection = dbConnector.openConnection(databaseName);
        this.connection.setAutoCommit(false);
        this.statement = dbConnector.getStatement(connection);
        this.databaseName = databaseName;
    }

    /*
    This function sets up insert command parameters before hadning it off to the dbConnector for the actually
    insert
     */
    public void addToBatch(ArrayList<List<String>> fileList, String databaseName, String tableName) throws SQLException {

        //You need to insert other queries here as well
        //Recall the first 0 after VALUES is suppose to be the auto increment id for the common table
        String query;
        if (tableName.toLowerCase().equals("common")) {
            query = "INSERT INTO " + tableName + " VALUES ('";
        } else {
            query = "INSERT INTO " + tableName + " VALUES ('";
        }

        for (List<String> item : fileList) {
            for (String temp : item) {

                query += "" + temp + "','";
            }
        }
        String finalQuery = query.substring(0, query.length() - 2) + ")";
        finalQuery.replace('[', ' ').replace(']', ' ');
        //System.out.println(finalQuery);
        try {
            statement.addBatch(finalQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void executeBatch() {
        try {
            //then execute said batch statement
            this.statement.executeBatch();
            this.connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean call() {
        try {
            while (true) {
                if (buffer.getSize() >= buffer.getQueueLimit()) {
                    for (int i = 0; i <= buffer.getQueueLimit(); i++) {
                        //for(int i = 0; i<=THREAD_LIMIT; i++){
                        try {
                            VarexpVariable var = buffer.get();
                            if (var != null) {
                                addToBatch(var.getArrayList(), databaseName, var.getTableName());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        executeBatch();
                    } catch (Exception e) {

                    }
                } else {
                    //Add to batch
                    try {
                        VarexpVariable var = buffer.get();
                        if (var != null) {
                            addToBatch(var.getArrayList(), databaseName, var.getTableName());
                            executeBatch();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //If all the items have been processed, then we're done and this call should return true
                if (buffer.isDone == true && buffer.isEmpty()) {

                    //System.out.println("SQL Handler: Done");
                    System.out.println("Done with Importing to database.");
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
