package com.company.Database;

import com.company.matrikon.MatrikonVariable;
import com.company.pcvue.fields.VarexpFactory;
import com.company.pcvue.fields.VarexpVariable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Stephen on 4/20/2018.
 * <p>
 * This class is responsible for connecting to mySQL
 */
public class dbConnector {
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
                //System.out.println(" "+rsmd.getColumnName(i));
                resultString += resultSet.getString(i) + ",";
                //System.out.println(" "+rsmd.getString(i));
            }
            System.out.println(resultString);
        }

        return resultString;

    }

    public Connection openConnection(String databaseName) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connect = DriverManager
                    //.getConnection("jdbc:mysql://localhost/" + databaseName + "?" + "user=production&password=ZAQ!xsw2CDE#&useSSL=false");
                    .getConnection("jdbc:mysql://localhost/" + databaseName, "production", "ZAQ!xsw2CDE#");

        } catch (Exception e) {
            e.printStackTrace();
            //In the case where you get an error opening, this might mean the database does not exist, In this case
            //it will build you a new DATABAES instead

        }
        return this.connect;
    }

    private Connection newConnection(String site) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/", "production", "ZAQ!xsw2CDE#");

        } catch (Exception e) {
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

    public String sqlQuery(String databaseName, String sqlCmd) {

        try {
            openConnection(databaseName);
            setStatement(connect);
            ResultSet rs = statement.executeQuery(sqlCmd);
            String resultString = writeResultSet(rs);
            return resultString;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            close(connect);
        }

    }

    public void sqlExeucte(String databaseName, String sqlCmd) {
        try {
            openConnection(databaseName);
            setStatement(connect);
            boolean rs = statement.execute(sqlCmd);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(connect);
        }
    }

    public String getTableSize(String databaseName) {
        openConnection(databaseName);
        setStatement(connect);
        String sqlCmd = "SELECT COUNT(common.variable_id) FROM common;";
        try {
            ResultSet rs = statement.executeQuery(sqlCmd);
            System.out.println(rs);
            rs.next();
            String numVarexpVariable = rs.getString(1);
            return numVarexpVariable;
        } catch (Exception e) {
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
                if (databaseName.equals(dbName)) {
                    return true;
                }
            }
        } catch (SQLException e1) {
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
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
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


    public void createDB(String dbName) {
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
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
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
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
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
