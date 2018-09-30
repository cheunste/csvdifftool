package com.company.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/28/2018.
 */
public class Equipment extends VarexpVariable {
    private ArrayList<List<String>> equipmentList;

    public Equipment() {
        this.equipmentList = new ArrayList<>();
        setTableName("equipment");
        setPositionList();
    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
    }
    @Override
    void setPositionList() {
        for (int i = 29; i <= 36; i++) {
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

        return "RIGHT JOIN  equipment on common.variable_id=equipment.equipment_variable_id";
    }

    @Override
    public String createTableCmd() {
        return
                "CREATE TABLE equipment(" +
                        "equipment_variable_id int unsigned  primary key," +
                        "equipment_Card_Number TEXT(50) NOT NULL," +
                        "Equipment_network_Name TEXT(50) NOT NULL," +
                        "Equipment_equipment_Name TEXT(50) NOT NULL," +
                        "equipment_Frame_Name TEXT(50) NOT NULL," +
                        "equipment_Format TEXT(50) NOT NULL," +
                        "equipment_Equipment_Index TEXT(50) NOT NULL," +
                        "equipment_Complementary_Index TEXT(50) NOT NULL," +
                        "equipment_Size TEXT(50) NOT NULL);";
    }

    public ArrayList<List<String>> getArrayList() {
        return this.equipmentList;
    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> equipmentList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        equipmentList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            equipmentList.add(varexpArraySplit.get(i).trim());
        }

        this.equipmentList.add(equipmentList);

    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Card Number", new VarexpTuple(30, "TF", new String[]{""}, new String[]{""}, true, 8));
        fieldMap.put("Network Name", new VarexpTuple(31, "TF", new String[]{""}, new String[]{""}, true, 8));
        fieldMap.put("Equipment Name", new VarexpTuple(32, "TF", new String[]{""}, new String[]{""}, true, 12));
        fieldMap.put("Frame Name", new VarexpTuple(33, "TF", new String[]{""}, new String[]{""}, true, 20));
        fieldMap.put("Format", new VarexpTuple(34, "CB",
                new String[]{"B:Bit (positive logic)", "b:Bit (negative logic)", "I:Integer 16 bits signed (capital i)",
                        "U:Integer 16 bits unsigned", "l:Integer 32 bits signed (lower case L)", "m:Integer 32 bits signed (swapped word)",
                        "L:Integer 32 bits unsigned", "M:Integer 32 bits unsigned (swapped word)", "F:Float 32 bits signed", "c:Byte 8 bits signed",
                        "C:Byte 8 bits unsigned", "S:Variable length string delimited by \0", "s:Variable length string delimited by \0 (swapped)",
                        "T:Fixed length string (=Size/8)", "t:Fixed length string (=Size/8) (swapped)", "d:Integer 2 bits unsigned (readonly)",
                        "Q:Integer 4 bits unsigned(readonly)", "q:Integer 4 bits signed(readonly)", "D:Float 64 bits signed"},
                new String[]{"B", "b", "I", "U", "I", "m", "L", "M", "F", "c", "C", "S", "s", "T", "t", "d", "Q", "q", "D"}, true, 1));
        fieldMap.put("Index", new VarexpTuple(35, "TF", new String[]{""}, new String[]{""}, true, 2));
        fieldMap.put("Complementary Index", new VarexpTuple(36, "TF", new String[]{""}, new String[]{""}, true, 2));
        fieldMap.put("Size", new VarexpTuple(37, "TF", new String[]{""}, new String[]{""}, true, 2));

        return fieldMap;
    }

    @Override
    public String getVariableIdName() {
        return "equipment" + ID;
    }
}

