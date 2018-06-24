package com.company.pcvue.fields;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stephen on 5/28/2018.
 */
public class DDE extends VarexpVariable {
    private ArrayList<List<String>> ddeList;

    public DDE() {
        this.ddeList = new ArrayList<>();
        setTableName("dde");
        setPositionList();
    }

    @Override
    void setPositionList() {
        for (int i = 119; i <= 126; i++) {
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

        return "RIGHT JOIN  dde on common.variable_id=dde.dde_variable_id";
    }

    public ArrayList<List<String>> getArrayList() {
        return this.ddeList;
    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setvarexpArrayList(varexpString);
        List<String> ddeList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        ddeList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            ddeList.add(varexpArraySplit.get(i));
        }

        this.ddeList.add(ddeList);

    }
}