package com.company.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/20/2018.
 */
public class CXT extends VarexpVariable {

    private ArrayList<List<String>> cxtList;

    public CXT() {
        this.cxtList = new ArrayList<>();
        setTableName("cxt");
        setPositionList();
    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
    }
    @Override
    void setPositionList() {
        for (int i = 99; i <= 102; i++) {
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
        return "RIGHT JOIN cxt on common.variable_id = cxt.cxt_variable_id";
    }

    @Override
    public String createTableCmd() {
        return "CREATE TABLE cxt(" +
                "	cxt_variable_id int unsigned  primary key," +
                "	cxt_Maximum_number_of_characters TEXT(50) NULL," +
                "	cxt_write_authorised TEXT(50) NULL," +
                "	cxt_authorisation_level TEXT(50) NULL," +
                "	cxt_display_format TEXT(50) NULL, cxt_VCR TEXT(50) NULL);";

    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> cxtList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        cxtList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            cxtList.add(varexpArraySplit.get(i).trim());
        }
        this.cxtList.add(cxtList);
    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Max. Char", new VarexpTuple(100, "TF", new String[]{""}, new String[]{""}, true, 2));
        fieldMap.put("Write Authorized", new VarexpTuple(101, "TF", new String[]{""}, new String[]{"1"}, true, 2));
        fieldMap.put("Authorisation Level", new VarexpTuple(102, "TF", new String[]{""}, new String[]{"0", "29"}, true, 2));
        fieldMap.put("Display Format", new VarexpTuple(103, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("VCR", new VarexpTuple(157, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        return fieldMap;
    }


    public ArrayList<List<String>> getArrayList() {
        return this.cxtList;
    }

    @Override
    public String getVariableIdName() {
        return "cxt" + ID;
    }
}
