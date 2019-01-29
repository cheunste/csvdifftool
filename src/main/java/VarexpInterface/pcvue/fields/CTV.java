package VarexpInterface.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/20/2018.
 */
public class CTV extends VarexpVariable {
    private ArrayList<List<String>> ctvList;

    public CTV() {
        this.ctvList = new ArrayList<>();
        setTableName("ctv");
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
        for (int i = 69; i <= 71; i++) {
            varexpPositionList.add(i);
        }
        varexpPositionList.add(155);
        varexpPositionList.add(156);
    }

    @Override
    String empty() {
        return "";
    }

    @Override
    String getJoinCmd() {
        return "RIGHT JOIN ctv on common.variable_id = ctv.ctv_variable_id";
    }

    @Override
    public String createTableCmd() {
        return "CREATE TABLE ctv(" +
                "	ctv_variable_id int unsigned  primary key," +
                "	ctv_Measurement_Units TEXT(50) NULL," +
                "	ctv_Deadband TEXT(50) NULL," +
                "	ctv_Minimium_display_value TEXT(50) NULL," +
                "	ctv_Maximum_display_value TEXT(50) NULL," +
                "	ctv_Scaling TEXT(50) NULL," +
                "	ctv_Minimum_equipment_value TEXT(50) NULL," +
                "	ctv_Maximum_equipment_value TEXT(50) NULL," +
                "	ctv_Display_format TEXT(50) NULL," +
                "	ctv_Minimum_control_value TEXT(50) NULL," +
                "	ctv_maximum_control_value TEXT(50) NULL," +
                "	ctv_authorisation_level TEXT(50) NULL," +
                "	ctv_deadband_type TEXT(50) NULL," +
                "	ctv_VCR TEXT(50) NULL);";

    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> ctvList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        ctvList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            ctvList.add(varexpArraySplit.get(i).trim());
        }

        this.ctvList.add(ctvList);
    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Measurement Units", new VarexpTuple(60, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Deadband", new VarexpTuple(61, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Min Display Value", new VarexpTuple(62, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Max. Display Value", new VarexpTuple(63, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Scaling", new VarexpTuple(64, "CB", new String[]{"0", "1"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Min. Equip Value ", new VarexpTuple(65, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Max. Equip. Value", new VarexpTuple(66, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Display Format", new VarexpTuple(67, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Min Ctrl Value", new VarexpTuple(70, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Max. Ctrl Value", new VarexpTuple(71, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Authorisation Level", new VarexpTuple(72, "TF", new String[]{""}, new String[]{"0", "29"}, true, 2));
        fieldMap.put("Deadband Type", new VarexpTuple(156, "CB", new String[]{"0", "1", "3"}, new String[]{"0", "1", "3"}, true, 2));
        fieldMap.put("VCR", new VarexpTuple(157, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        return fieldMap;
    }

    public ArrayList<List<String>> getArrayList() {
        return this.ctvList;
    }

    @Override
    public String getVariableIdName() {
        return "ctv" + ID;
    }
}
