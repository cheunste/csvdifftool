package com.company.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/20/2018.
 */
public class CMD extends VarexpVariable {
    private ArrayList<List<String>> cmdList;

    public CMD() {
        this.cmdList = new ArrayList<>();
        setTableName("cmd");
        setPositionList();
    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
    }
    @Override
    void setPositionList() {
        for (int i = 40; i <= 43; i++) {
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
        return "RIGHT JOIN cmd on common.variable_id = cmd.cmd_variable_id";
    }

    @Override
    public String createTableCmd() {
        return "CREATE TABLE cmd(" +
                "cmd_variable_id int unsigned  primary key," +
                "cmd_Log_bit_0_to_1 TEXT(50) NULL," +
                "cmd_Log_bit_1_to_0 TEXT(50) NULL," +
                "cmd_reserved TEXT(50) NULL," +
                "cmd_Authorisation_Level TEXT(50) NULL," +
                "cmd_VCR TEXT(50) NULL);";

    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> cmdList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        cmdList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            cmdList.add(varexpArraySplit.get(i).trim());
        }

        this.cmdList.add(cmdList);
    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Log bit (0 to 1)", new VarexpTuple(41, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Log big (1 to 0)", new VarexpTuple(42, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("", new VarexpTuple(43, "TF", new String[]{""}, new String[]{""}, false, 0));
        fieldMap.put("Authorisation level", new VarexpTuple(44, "TF", new String[]{""}, new String[]{"0", "29"}, true, 2));
        fieldMap.put("VCR", new VarexpTuple(157, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        return fieldMap;
    }

    public ArrayList<List<String>> getArrayList() {
        return this.cmdList;
    }

    @Override
    public String getVariableIdName() {
        return "cmd" + ID;
    }
}
