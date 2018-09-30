package com.company.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/28/2018.
 */
public class OPC extends VarexpVariable {
    private ArrayList<List<String>> opcList;

    public OPC() {
        this.opcList = new ArrayList<>();
        setTableName("opc");
        setPositionList();

    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
    }
    @Override
    void setPositionList() {
        for (int i = 144; i <= 150; i++) {
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

        return "RIGHT JOIN  opc on common.variable_id=opc.opc_variable_id";
    }

    @Override
    public String createTableCmd() {

        return "CREATE TABLE opc(" +
                "opc_variable_id int unsigned  primary key," +
                "opc_Server_alias TEXT(50) NULL," +
                "opc_Group_name TEXT(50) NULL," +
                "opc_Item_name TEXT(50) NULL," +
                "opc_access_path TEXT(50) NULL," +
                "opc_array_item TEXT(50) NULL," +
                "opc_array_item_index TEXT(50) NULL," +
                "opc_customizaiton_expression TEXT(50) NULL);";

    }

    public ArrayList<List<String>> getArrayList() {
        return this.opcList;
    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> opcList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        opcList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            opcList.add(varexpArraySplit.get(i).trim());
        }

        this.opcList.add(opcList);

    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Server alias", new VarexpTuple(145, "TF", new String[]{""}, new String[]{""}, true, 10));
        fieldMap.put("Group name", new VarexpTuple(146, "TF", new String[]{""}, new String[]{""}, true, 10));
        fieldMap.put("Item name", new VarexpTuple(147, "TF", new String[]{""}, new String[]{""}, true, 255));
        fieldMap.put("Access path", new VarexpTuple(148, "TF", new String[]{""}, new String[]{""}, true, 80));
        fieldMap.put("Array item", new VarexpTuple(149, "CB", new String[]{"1: Variable is mapped on an array", "0: Otherwise"}, new String[]{"1", "0"}, true, 2));
        fieldMap.put("Array item index", new VarexpTuple(150, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Customization expression", new VarexpTuple(151, "TF", new String[]{""}, new String[]{""}, true, 30));

        return fieldMap;
    }

    @Override
    public String getVariableIdName() {
        return "opc" + ID;
    }
}
