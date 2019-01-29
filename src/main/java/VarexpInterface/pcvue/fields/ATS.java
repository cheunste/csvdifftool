package VarexpInterface.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/20/2018.
 */
public class ATS extends VarexpVariable {

    private ArrayList<List<String>> atsList;

    public ATS() {
        this.atsList = new ArrayList<>();
        setTableName("ats");
        setPositionList();
    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
    }

    @Override
    void setPositionList() {
        for (int i = 40; i <= 42; i++) {
            this.varexpPositionList.add(i);
        }
        for (int i = 44; i <= 46; i++) {
            this.varexpPositionList.add(i);
        }
        for (int i = 49; i <= 54; i++) {
            this.varexpPositionList.add(i);
        }
        this.varexpPositionList.add(156);
    }

    @Override
    String empty() {
        String emptyString = "";
        for (int i = 40; i <= 46; i++) {
            emptyString += ",";
        }
        for (int i = 49; i <= 54; i++) {
            emptyString += ",";
        }
        emptyString += ",";
        return emptyString;
    }

    public ArrayList<List<String>> getArrayList() {
        return this.atsList;
    }

    @Override
    String getJoinCmd() {
        return "RIGHT JOIN ats on common.variable_id = ats.ats_variable_id";
    }

    @Override
    public String createTableCmd() {
        return "CREATE TABLE ats(" +
                "ats_variable_id int unsigned  primary key," +
                "	ats_Log_bit_0_to_1 TEXT(50) NULL," +
                "	ats_Log_bit_1_to_0 TEXT(50) NULL," +
                "	ats_reserved TEXT(50) NULL," +
                "	ats_Alarm_Level TEXT(50) NULL," +
                "	ats_Alarm TEXT(50) NULL," +
                "	ats_Name_of_mask_bit TEXT(50) NULL," +
                "	ats_Hysteresis TEXT(50) NULL," +
                "	ats_Value TEXT(50) NULL," +
                "	ats_Threshold TEXT(50) NULL," +
                "	ats_Name_of_REG TEXT(50) NULL," +
                "	ats_Threshold_system TEXT(50) NULL," +
                "	ats_Threshold_type TEXT(50) NULL," +
                "	ats_VCR TEXT(50) NULL);";

    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> atsList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        atsList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            atsList.add(varexpArraySplit.get(i).trim());
        }
        this.atsList.add(atsList);
    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Log bit (0 to 1)", new VarexpTuple(41, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Log bit (1 to 0)", new VarexpTuple(42, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("", new VarexpTuple(43, "TF", new String[]{""}, new String[]{""}, false, 0));
        fieldMap.put("Alarm Level", new VarexpTuple(45, "TF", new String[]{""}, new String[]{"0", "29"}, true, 2));
        fieldMap.put("Alarm", new VarexpTuple(46, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Name of mask bit", new VarexpTuple(47, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Hysteresis", new VarexpTuple(50, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Value", new VarexpTuple(51, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Threshold", new VarexpTuple(52, "CB", new String[]{"Threshold High", "Threshold Low"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Register Variable Name", new VarexpTuple(53, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Threshold System", new VarexpTuple(54, "TF", new String[]{""}, new String[]{""}, true, 2));
        fieldMap.put("Threshold Type", new VarexpTuple(55, "TF", new String[]{""}, new String[]{""}, true, 2));
        fieldMap.put("VCR", new VarexpTuple(157, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));

        return fieldMap;
    }

    @Override
    public String getVariableIdName() {
        return "ats" + ID;
    }
}
