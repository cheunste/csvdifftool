package com.company.matrikon;

import com.company.Database.dbConnector;

import java.util.*;

public class MatrikonVariable {

    private ArrayList<List<String>> matrikonVariableList;
    private String dbName;
    private String tableName;
    private int maxDataSize = 11;
    //This is a map that maps the position of the the Matrikon CSV file to a description.
    //It beats having magic numbers around
    private Map<String, Integer> matrikonMap;

    public MatrikonVariable(String dbName) {
        matrikonMap = new LinkedHashMap<>();
        this.dbName = dbName;
        this.tableName = "matrikon";

        matrikonMap.put("groupName", 0);
        matrikonMap.put("name", 1);
        matrikonMap.put("itemPath", 2);
        matrikonMap.put("dataType", 3);
        matrikonMap.put("readOnly", 4);
        matrikonMap.put("pollAlways", 5);
        matrikonMap.put("updateRate", 6);
        matrikonMap.put("scaling", 7);
        matrikonMap.put("hiRaw", 8);
        matrikonMap.put("lowRaw", 9);
        matrikonMap.put("hiScaled", 10);
        matrikonMap.put("loScaled", 11);

    }

    public String createTableCmd() {
        //Note that group name is something like "SHILO.ST.DB1.MMXU1"
        //While name is 'TotW'
        return "CREATE TABLE " + this.tableName + "(" +
                "matrikon_id int unsigned primary key NOT NULL auto_increment," +
                "matrikon_group_name TEXT(50) NULL," +
                "matrikon_name TEXT(50) NULL," +
                "matrikon_item_path TEXT(50) NULL," +

                "matrikon_dataType TINYINT NULL," +
                "matrikon_readOnly TINYINT NULL," +
                "matrikon_pollAlways TINYINT NULL," +
                "matrikon_updateRate SMALLINT NULL," +
                "matrikon_scaling SMALLINT NULL," +

                "matrikon_hiRaw   SMALLINT NULL," +
                "matrikon_lowRaw  SMALLINT NULL," +
                "matrikon_hiScaled  SMALLINT NULL," +
                "matrikon_loScaled  SMALLINT NULL" +
                ");";
    }

    public void importMatrikonVariable(List<String> data) {

        String insertCmd = "";

        List<String> newDataList = new ArrayList<String>();

        System.out.println(data);
        if (data.size() < this.maxDataSize) {

            for (int i = 0; i < data.size(); i++) {
                newDataList.add(data.get(i));
            }
            while (newDataList.size() <= this.maxDataSize) {
                newDataList.add("0");
            }
            Collections.replaceAll(newDataList, "", "0");
            insertCmd = " INSERT INTO matrikon VALUES(" +
                    null + "," +
                    "'" + newDataList.get(matrikonMap.get("groupName")) + "'," +
                    "'" + newDataList.get(matrikonMap.get("name")) + "'," +
                    "'" + newDataList.get(matrikonMap.get("itemPath")) + "'," +
                    newDataList.get(matrikonMap.get("dataType")) + "," +
                    newDataList.get(matrikonMap.get("readOnly")) + "," +
                    newDataList.get(matrikonMap.get("pollAlways")) + "," +
                    newDataList.get(matrikonMap.get("updateRate")) + "," +
                    newDataList.get(matrikonMap.get("scaling")) + "," +
                    newDataList.get(matrikonMap.get("hiRaw")) + "," +
                    newDataList.get(matrikonMap.get("lowRaw")) + "," +
                    newDataList.get(matrikonMap.get("hiScaled")) + "," +
                    newDataList.get(matrikonMap.get("loScaled")) +
                    ");";
        } else {

            insertCmd = " INSERT INTO matrikon VALUES(" +
                    null + "," +
                    "'" + data.get(matrikonMap.get("groupName")) + "'," +
                    "'" + data.get(matrikonMap.get("name")) + "'," +
                    "'" + data.get(matrikonMap.get("itemPath")) + "'," +
                    data.get(matrikonMap.get("dataType")) + "," +
                    data.get(matrikonMap.get("readOnly")) + "," +
                    data.get(matrikonMap.get("pollAlways")) + "," +
                    data.get(matrikonMap.get("updateRate")) + "," +
                    data.get(matrikonMap.get("scaling")) + "," +
                    data.get(matrikonMap.get("hiRaw")) + "," +
                    data.get(matrikonMap.get("lowRaw")) + "," +
                    data.get(matrikonMap.get("hiScaled")) + "," +
                    data.get(matrikonMap.get("loScaled")) +
                    ");";
        }
        System.out.println(insertCmd);

        dbConnector db = new dbConnector();
        System.out.println(insertCmd);
        db.sqlExeucte(this.dbName, insertCmd);
    }

}
