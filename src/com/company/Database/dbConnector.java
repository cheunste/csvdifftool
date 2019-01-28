package com.company.Database;

import com.company.PropertyManager;
import com.company.matrikon.MatrikonVariable;
import com.company.pcvue.fields.VarexpFactory;
import com.company.pcvue.fields.VarexpVariable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stephen on 4/20/2018.
 * <p>
 * This class is responsible for connecting to mySQL
 */
public class dbConnector {

    //Log
    static final Logger dbConnectionLogger = LogManager.getLogger(dbConnector.class.getName());

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private ArrayList<ArrayList<String>> resultSetArray;

    public dbConnector() {
    }

    public ArrayList<ArrayList<String>> readDatabase(String databaseName, String sqlCmd) {

        resultSetArray = new ArrayList<>();
        String temp = "";
        //ArrayList<String> tempArrayList = new ArrayList<>();
        try {
            openConnection(databaseName);
            setStatement(connect);

            resultSet = statement.executeQuery(sqlCmd);

            resultSetArray = new ArrayList<>();
            System.out.println(temp);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int colNum = rsmd.getColumnCount();

            while (resultSet.next()) {
                ArrayList<String> tempArrayList = new ArrayList<>();
                tempArrayList.clear();
                for (int j = 1; j <= colNum; j++) {
                    tempArrayList.add(resultSet.getString(j));
                }
                //System.out.println(tempArrayList);
                resultSetArray.add(tempArrayList);
                if (tempArrayList.size() == 0) {
                    System.out.println("Break");
                }
            }
            //System.out.println(resultSetArray);
            return resultSetArray;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        } finally {
            close(this.connect);
        }
    }

    public void writeDatabase(Connection connection, String insertToDBCmd, List<String> subArrayList) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println(insertToDBCmd);
            preparedStatement = connection.prepareStatement(insertToDBCmd);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //connect.close();
        }

    }

    private String writeResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnNum = rsmd.getColumnCount();
        String resultString = "";
        while (resultSet.next()) {
            for (int i = 1; i <= columnNum; i++) {
                resultString += resultSet.getString(i) + ",";
            }
        }

        return resultString;

    }

    //Open connection to DB. Warning: Requires property manager rto be used
    public Connection openConnection(String databaseName) {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            dbConnectionLogger.info("Opening connection to: " + PropertyManager.getDatabaseIP());
            this.connect = DriverManager
                    .getConnection("jdbc:mysql://" + PropertyManager.getDatabaseIP() + "/" + databaseName, PropertyManager.getUser(), PropertyManager.getPassword());

        } catch (Exception e) {
            e.printStackTrace();
            dbConnectionLogger.error("Error making connection to database " + databaseName);
            //In the case where you get an error opening, this might mean the database does not exist, In this case
            //it will build you a new DATABAES instead

        }
        return this.connect;
    }

    private Connection newConnection(String site) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            dbConnectionLogger.info("Making connection to: " + PropertyManager.getDatabaseIP());
            this.connect = DriverManager
                    .getConnection("jdbc:mysql://" + PropertyManager.getDatabaseIP() + "/", PropertyManager.getUser(), PropertyManager.getPassword());

        } catch (Exception e) {
            dbConnectionLogger.error("Error in making a new Connection. Error: " + e);
            e.printStackTrace();
        }
        return this.connect;
    }

    public void setStatement(Connection connection) {
        try {
            this.statement = connection.createStatement();
        } catch (Exception e) {
        }
    }

    public Statement getStatement(Connection connection) {
        try {
            this.statement = connection.createStatement();
        } catch (SQLException e) {
            dbConnectionLogger.error("Error in getting statement from DB. Error: " + e);
            e.printStackTrace();
        }
        return this.statement;
    }

    public void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

    public void close(Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connect != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public ResultSet sqlQueryResult(String databaseName, String sqlCmd) {
        try {
            openConnection(databaseName);
            setStatement(connect);
            ResultSet rs = statement.executeQuery(sqlCmd);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close(connect);
        }
    }

    public String sqlQuery(String databaseName, String sqlCmd) {

        try {
            openConnection(databaseName);
            setStatement(connect);
            ResultSet rs = statement.executeQuery(sqlCmd);
            String resultString = writeResultSet(rs);
            return resultString;
        } catch (Exception e) {
            dbConnectionLogger.error("Exception with sqlQuery. Error" + e);
            e.printStackTrace();
            return "";
        } finally {
            close(connect);
        }

    }

    public void sqlExecute(String databaseName, String sqlCmd) {
        try {
            openConnection(databaseName);
            setStatement(connect);
            boolean rs = statement.execute(sqlCmd);
            //System.out.println(rs);


        } catch (Exception e) {
            dbConnectionLogger.error("Exception with sqlQuery. Error" + e);
            e.printStackTrace();
        } finally {
            close(connect);
        }
    }

    public String getTableSizeMatrikon(String databaseName) {
        openConnection(databaseName);
        setStatement(connect);
        String sqlCmd = "SELECT COUNT(matrikon.matrikon_id) FROM matrikondb.matrikon;";
        try {
            ResultSet rs = statement.executeQuery(sqlCmd);
            rs.next();
            String numVarexpVariable = rs.getString(1);
            return numVarexpVariable;
        } catch (Exception e) {
            dbConnectionLogger.error("Exception with fetching Table Size for Matrikon table. Error" + e);
            e.printStackTrace();

        } finally {
            close();
        }

        return "0";
    }
    public String getTableSize(String databaseName) {
        openConnection(databaseName);
        setStatement(connect);
        String sqlCmd = "SELECT COUNT(common.variable_id) FROM common;";
        try {
            ResultSet rs = statement.executeQuery(sqlCmd);
            rs.next();
            String numVarexpVariable = rs.getString(1);
            return numVarexpVariable;
        } catch (Exception e) {
            dbConnectionLogger.error("Exception with fetching Table Size. Error" + e);
            e.printStackTrace();

        } finally {
            close();
        }

        return "0";
    }

    public boolean verifyDBExists(String dbName) {
        openConnection(dbName);
        setStatement(connect);
        try {
            ResultSet rs = this.connect.getMetaData().getCatalogs();
            while (rs.next()) {
                String databaseName = rs.getString(1);
                if (databaseName.equalsIgnoreCase(dbName)) {
                    return true;
                }
            }
        } catch (SQLException e1) {
            dbConnectionLogger.error("Exception with verifying DB's existance. Please panic");
        } finally {
            close();
        }
        return false;
    }

    //public ArrayList<ArrayList<String>> showDatabases(){
    public ArrayList<String> showDatabases() {
        String showDBSQL = "Show Databases;";
        ArrayList<String> listOfDatabase = new ArrayList<>();
        try {
            connect = newConnection("");
            setStatement(connect);

            resultSet = statement.executeQuery(showDBSQL);
            resultSet = statement.getResultSet();

            while (resultSet.next()) {
                String database = resultSet.getString("Database");
                listOfDatabase.add(database);
            }

            return listOfDatabase;

        } catch (Exception e) {

            dbConnectionLogger.error("Exception with showing Database. Error: " + e);
            e.printStackTrace();
            return null;
        } finally {
            close(this.connect);
        }
    }

    //This method creates a table specifically for the Matrikon tags
    public void createMatrikonDB(String dbName) {
        String createDBStatement = "CREATE DATABASE " + dbName;
        MatrikonVariable matrikonVariable = new MatrikonVariable(dbName);
        String createTableStatement;
        try {
            connect = newConnection(dbName);
            statement = connect.createStatement();
            statement.executeUpdate(createDBStatement);
            connect = openConnection(dbName);
            statement = connect.createStatement();

            createTableStatement = matrikonVariable.createTableCmd();
            statement.executeUpdate(createTableStatement);

        } catch (SQLException se) {
            //Handle errors for JDBC
            dbConnectionLogger.error("SQL Exception when attempting to create Matrikon DB." +
                    " Error: " + se);
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            dbConnectionLogger.error("Exception with creating MatrikonDB. Error: " + e);
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connect.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connect != null)
                    connect.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try

    }


    public void createVarexpDB(String dbName) {
        String createDBStatement = "CREATE DATABASE " + dbName;
        VarexpFactory factory = new VarexpFactory();
        String createTableStatement;
        String[] variableList = factory.listOfTables;

        try {
            connect = newConnection(dbName);
            statement = connect.createStatement();
            statement.executeUpdate(createDBStatement);

            connect = openConnection(dbName);
            statement = connect.createStatement();
            for (String var : variableList) {
                VarexpVariable temp = factory.declareNewVariable(var);
                createTableStatement = temp.createTableCmd();
                statement.executeUpdate(createTableStatement);
            }
        } catch (SQLException se) {
            //Handle errors for JDBC
            dbConnectionLogger.error("SQL Exception with creating VarexpDB. Error: " + se);
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            dbConnectionLogger.error("Error with creating VarexpDB. Error: " + e);
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connect.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connect != null)
                    connect.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
    }

    /*
    This is a generic function used to create a database given a name
     */
    public void createDatabase(String databaseName) {
        String createDBStatement = "CREATE DATABASE " + databaseName;
        try {
            connect = newConnection(databaseName);
            statement = connect.createStatement();
            statement.executeUpdate(createDBStatement);

            connect = openConnection(databaseName);
            statement = connect.createStatement();

        } catch (SQLException se) {
            //Handle errors for JDBC
            dbConnectionLogger.error("SQL Error with creating DB " + databaseName +
                    ". Error: " + se);
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            dbConnectionLogger.error("Error with creating DB " + databaseName + ". Error: " + e);
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connect.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connect != null)
                    connect.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
    }

    public void deleteDB(String DBName) {

        String deleteStatement = "DROP DATABASE IF EXISTS " + DBName;

        try {
            connect = openConnection(DBName);
            statement = connect.createStatement();
            statement.executeUpdate(deleteStatement);
        } catch (SQLException se) {
            //Handle errors for JDBC
            dbConnectionLogger.error("SQL Error with deleting DB " + DBName + ". Error: " + se);
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            dbConnectionLogger.error("Error with deleting DB " + DBName + ". Error: " + e);
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connect.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connect != null)
                    connect.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
