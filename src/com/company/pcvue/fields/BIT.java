package com.company.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/20/2018.
 */
public class BIT extends VarexpVariable {
    private ArrayList<List<String>> bitList;

    public BIT() {
        this.bitList = new ArrayList<>();
        setTableName("bit");
        setPositionList();
    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
    }
    @Override
    void setPositionList() {
        for (int i = 40; i <= 42; i++) {
            varexpPositionList.add(i);
        }
        varexpPositionList.add(156);
    }

    @Override
    String empty() {
        String emptyString = "";

        for (int i : varexpPositionList) {

            emptyString += "";
        }
        return emptyString;
    }

    @Override
    String getJoinCmd() {
        return "RIGHT JOIN bit on common.variable_id = bit.bit_variable_id";
    }

    @Override
    public String createTableCmd() {
        return
                "CREATE TABLE bit(" +
                        "bit_variable_id int unsigned  primary key," +
                        "bit_Log_bit_0_to_1 TEXT(50) NULL," +
                        "bit_Log_bit_1_to_0 TEXT(50) NULL," +
                        "bit_reserved TEXT(50) NULL," +
                        "bit_VCR TEXT(50) NULL);";

    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> bitList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        bitList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            bitList.add(varexpArraySplit.get(i).trim());
        }
        this.bitList.add(bitList);
    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Log bit (0 to 1)", new VarexpTuple(41, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Log bit (1 to 0)", new VarexpTuple(42, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("", new VarexpTuple(43, "TF", new String[]{""}, new String[]{""}, false, 0));
        fieldMap.put("VCR", new VarexpTuple(157, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 1));
        return fieldMap;
    }

    public ArrayList<List<String>> getArrayList() {
        return this.bitList;
    }

    @Override
    public String getVariableIdName() {
        return "bit" + ID;
    }
}
