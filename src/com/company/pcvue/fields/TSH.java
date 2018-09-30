package com.company.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/28/2018.
 */
public class TSH extends VarexpVariable {
    private ArrayList<List<String>> tshList;

    public TSH() {
        this.tshList = new ArrayList<>();
        setTableName("tsh");
        setPositionList();
    }


    @Override
    void setPositionList() {
        for (int i = 40; i <= 42; i++) {
            varexpPositionList.add(i);
        }
        for (int i = 49; i <= 54; i++) {
            varexpPositionList.add(i);
        }
        varexpPositionList.add(156);
    }

    @Override
    String empty() {
        String emptyString = "";

        for (int i : varexpPositionList) {
            emptyString += ",";
        }

        return emptyString;
    }

    @Override
    String getJoinCmd() {

        return "RIGHT JOIN  tsh on common.variable_id=tsh.tsh_variable_id";
    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
    }
    @Override
    public String createTableCmd() {

        return "CREATE TABLE tsh(" +
                "tsh_variable_id int unsigned  primary key," +
                "	tsh_Log_bit_0_to_1 TEXT(50) NULL," +
                "	tsh_Log_bit_1_to_0 TEXT(50) NULL," +
                "	tsh_reserved TEXT(50) NULL," +
                "	tsh_Hysteresis TEXT(50) NULL," +
                "	tsh_Value TEXT(50) NULL," +
                "	tsh_Threshold TEXT(50) NULL," +
                "	tsh_Name_of_REG TEXT(50) NULL," +
                "	tsh_Threshold_system TEXT(50) NULL," +
                "	tsh_Threshold_type TEXT(50) NULL," +
                "	tsh_VCR TEXT(50) NULL);";

    }

    public ArrayList<List<String>> getArrayList() {
        return this.tshList;
    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> tshList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        tshList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            tshList.add(varexpArraySplit.get(i).trim());
        }
        this.tshList.add(tshList);

    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Log bit (0 to 1)", new VarexpTuple(41, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Log bit (1 to 0)", new VarexpTuple(42, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("", new VarexpTuple(43, "TF", new String[]{""}, new String[]{""}, false, 0));
        fieldMap.put("Hysteresis", new VarexpTuple(50, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Value", new VarexpTuple(51, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Threshold", new VarexpTuple(52, "CB", new String[]{"Threshold High", "Threshold Low"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Register Variable Name", new VarexpTuple(53, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Threshold System", new VarexpTuple(54, "CB", new String[]{"0", "1", "2", "3", "4"}, new String[]{"0", "1", "2", "3", "4"}, true, 2));
        fieldMap.put("Threshold Type", new VarexpTuple(55, "CB", new String[]{"0", "1", "2", "3"}, new String[]{"0", "1", "2", "3"}, true, 2));
        fieldMap.put("VCR", new VarexpTuple(157, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        return fieldMap;
    }

    @Override
    public String getVariableIdName() {
        return "tsh" + ID;
    }
}
