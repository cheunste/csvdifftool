package com.company.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/28/2018.
 */
public class CNT extends VarexpVariable {
    private ArrayList<List<String>> cntList;

    public CNT() {
        this.cntList = new ArrayList<>();
        setTableName("cnt");
        setPositionList();
    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
    }
    @Override
    void setPositionList() {
        for (int i = 59; i <= 66; i++) {
            varexpPositionList.add(i);
        }
        for (int i = 79; i <= 84; i++) {
            varexpPositionList.add(i);
        }
        varexpPositionList.add(155);
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

        return "RIGHT JOIN  cnt on common.variable_id=cnt.cnt_variable_id";
    }

    @Override
    public String createTableCmd() {
        return
                "CREATE TABLE cnt(" +
                        "	cnt_variable_id int unsigned  primary key," +
                        "	cnt_Measurement_Units TEXT(50) NULL," +
                        "	cnt_Deadband TEXT(50) NULL," +
                        "	cnt_Minimium_display_value TEXT(50) NULL," +
                        "	cnt_Maximum_display_value TEXT(50) NULL," +
                        "	cnt_Scaling TEXT(50) NULL," +
                        "	cnt_Minimum_equipment_value TEXT(50) NULL," +
                        "	cnt_Maximum_equipment_value TEXT(50) NULL," +
                        "	cnt_Display_format TEXT(50) NULL," +
                        "	cnt_Size_of_step TEXT(50) NULL," +
                        "	cnt_Decrement_increment_counter TEXT(50) NULL," +
                        "	cnt_Name_of_bit_on_which_to_count TEXT(50) NULL," +
                        "	cnt_Count_on_transition TEXT(50) NULL," +
                        "	cnt_Name_of_bit_on_which_to_reset_count TEXT(50) NULL," +
                        "	cnt_reset_on_transition TEXT(50) NULL," +
                        "	cnt_deadband_type TEXT(50) NULL," +
                        "	cnt_VCR TEXT(50) NULL);";

    }

    public ArrayList<List<String>> getArrayList() {
        return this.cntList;
    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> cntList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        cntList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            cntList.add(varexpArraySplit.get(i).trim());
        }

        this.cntList.add(cntList);

    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Measurement Units", new VarexpTuple(60, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Deadband", new VarexpTuple(61, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Min. Display Value", new VarexpTuple(62, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Max. Display Value", new VarexpTuple(63, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Scaling", new VarexpTuple(64, "CB", new String[]{"0", "1"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Min Equip. Value", new VarexpTuple(65, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Max. Equip. Value", new VarexpTuple(66, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Display Format", new VarexpTuple(67, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Step Size", new VarexpTuple(80, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Dec/Inc. Counter", new VarexpTuple(81, "CB", new String[]{"Decrement", "Increment"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Bit to count", new VarexpTuple(82, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Count on transition to 0 or 1", new VarexpTuple(83, "CB", new String[]{"0", "1"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Bit to reset", new VarexpTuple(84, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Reset on transition to 0 or 1", new VarexpTuple(85, "CB", new String[]{"0", "1"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Deadband Type", new VarexpTuple(156, "CB", new String[]{"0", "1", "3"}, new String[]{"0", "1", "3"}, true, 2));
        fieldMap.put("VCR", new VarexpTuple(157, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        return fieldMap;
    }

    @Override
    public String getVariableIdName() {
        return "chr" + ID;
    }
}

