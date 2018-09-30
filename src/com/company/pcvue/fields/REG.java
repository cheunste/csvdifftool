package com.company.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/20/2018.
 */
public class REG extends VarexpVariable {
    private ArrayList<List<String>> regList;

    public REG() {
        this.regList = new ArrayList<>();
        setTableName("reg");
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
        return "RIGHT JOIN reg on common.variable_id = reg.reg_variable_id";
    }

    @Override
    public String createTableCmd() {

        return "CREATE TABLE reg(" +
                "	reg_variable_id int unsigned  primary key," +
                "	reg_Measurement_Units TEXT(50) NULL," +
                "	reg_Deadband TEXT(50) NULL," +
                "	reg_Minimium_display_value TEXT(50) NULL," +
                "	reg_Maximum_display_value TEXT(50) NULL," +
                "	reg_Scaling TEXT(50) NULL," +
                "	reg_Minimum_equipment_value TEXT(50) NULL," +
                "	reg_Maximum_equipment_value TEXT(50) NULL," +
                "	reg_Display_format TEXT(50) NULL," +
                "	reg_Deadband_type TEXT(50) NULL," +
                "	reg_VCR TEXT(50) NULL);";

    }

    public ArrayList<List<String>> getArrayList() {
        return this.regList;
    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> acmList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        acmList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            acmList.add(varexpArraySplit.get(i).trim());
        }

        this.regList.add(acmList);
    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Measurement Units", new VarexpTuple(60, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Deadband", new VarexpTuple(61, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Min. Display Value", new VarexpTuple(62, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Max. Display Value", new VarexpTuple(63, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Scaling", new VarexpTuple(64, "CB", new String[]{"0", "1"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Min. Equip. Value", new VarexpTuple(65, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Max. Equip. Value", new VarexpTuple(66, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Display Format", new VarexpTuple(67, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Deadband Type", new VarexpTuple(156, "CB", new String[]{""}, new String[]{"0", "1", "3"}, true, 2));
        fieldMap.put("VCR", new VarexpTuple(157, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        return fieldMap;
    }

    @Override
    public String getVariableIdName() {
        return "reg" + ID;
    }
}
