package com.company.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/20/2018.
 */

public class TXT extends VarexpVariable {
    private ArrayList<List<String>> txtList;

    public TXT() {
        this.txtList = new ArrayList<>();
        setTableName("txt");
        setPositionList();
    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
    }

    @Override
    void setPositionList() {
        varexpPositionList.add(99);
        varexpPositionList.add(100);
        varexpPositionList.add(102);
        varexpPositionList.add(156);

    }

    @Override
    String empty() {
        return "";
    }

    @Override
    String getJoinCmd() {
        return "RIGHT JOIN txt on common.variable_id = txt.txt_variable_id";
    }

    @Override
    public String createTableCmd() {
        return
                "CREATE TABLE txt(" +
                        "txt_variable_id int unsigned  primary key," +
                        "	txt_Maximum_number_of_characters TEXT(50) NULL," +
                        "	txt_write_authorised TEXT(50) NULL," +
                        "	txt_display_format TEXT(50) NULL," +
                        "	txt_VCR TEXT(50) NULL);";

    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> txtList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        txtList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            txtList.add(varexpArraySplit.get(i).trim());
        }
        this.txtList.add(txtList);
    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Max. Characters", new VarexpTuple(100, "TF", new String[]{""}, new String[]{""}, true, 2));
        fieldMap.put("Write Authorized", new VarexpTuple(101, "CB", new String[]{"Y", "N"}, new String[]{"1", "0"}, true, 2));
        fieldMap.put("Display Format", new VarexpTuple(103, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("VCR", new VarexpTuple(157, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        return fieldMap;
    }

    public ArrayList<List<String>> getArrayList() {
        return this.txtList;
    }

    @Override
    public String getVariableIdName() {
        return "txt" + ID;
    }
}
