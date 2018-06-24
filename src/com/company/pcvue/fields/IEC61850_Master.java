package com.company.pcvue.fields;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stephen on 5/28/2018.
 */
public class IEC61850_Master extends VarexpVariable {
    private ArrayList<List<String>> iec61850_masterList;

    public IEC61850_Master() {
        this.iec61850_masterList = new ArrayList<>();
        setTableName("iec61850_master");
        setPositionList();
    }

    @Override
    void setPositionList() {
        for (int i = 189; i <= 199; i++) {
            varexpPositionList.add(i);
        }
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

        return "RIGHT JOIN  iec61850_master on common.variable_id=iec61850_master.iec61850_master_variable_id";
    }

    public ArrayList<List<String>> getArrayList() {
        return this.iec61850_masterList;
    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setvarexpArrayList(varexpString);
        List<String> iec61850_masterList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        iec61850_masterList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            iec61850_masterList.add(varexpArraySplit.get(i));
        }

        this.iec61850_masterList.add(iec61850_masterList);

    }
}