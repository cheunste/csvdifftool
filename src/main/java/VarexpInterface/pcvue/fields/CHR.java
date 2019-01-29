package VarexpInterface.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/20/2018.
 */
public class CHR extends VarexpVariable {

    private ArrayList<List<String>> chrList;

    public CHR() {
        this.chrList = new ArrayList<>();
        setTableName("chr");
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
        for (int i = 89; i <= 94; i++) {
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
        return "RIGHT JOIN chr on common.variable_id = chr.chr_variable_id";
    }

    @Override
    public String createTableCmd() {
        return
                "CREATE TABLE chr(" +
                        "chr_variable_id int unsigned  primary key," +
                        "	chr_Measurement_Units TEXT(50) NULL," +
                        "	chr_Deadband TEXT(50) NULL," +
                        "	chr_Minimium_display_value TEXT(50) NULL," +
                        "	chr_Maximum_display_value TEXT(50) NULL," +
                        "	chr_Scaling TEXT(50) NULL," +
                        "	chr_Minimum_equipment_value TEXT(50) NULL," +
                        "	chr_Maximum_equipment_value TEXT(50) NULL," +
                        "	chr_Display_format TEXT(50) NULL," +
                        "	chr_Chronometer_period TEXT(50) NULL," +
                        "	chr_Chronometer_increment TEXT(50) NULL," +
                        "	chr_chrono_enable_bit_variable TEXT(50) NULL," +
                        "	chr_Enable_on TEXT(50) NULL," +
                        "	chr_chrono_reset_bit_variable TEXT(50) NULL," +
                        "	chr_reset_on_transition TEXT(50) NULL," +
                        "	chr_deadband_type TEXT(50) NULL," +
                        "	chr_VCR TEXT(50) NULL);";

    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> chrList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        chrList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            chrList.add(varexpArraySplit.get(i).trim());
        }
        this.chrList.add(chrList);
    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Measurement Units", new VarexpTuple(60, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Deadband", new VarexpTuple(61, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Min Display Value", new VarexpTuple(62, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Max Display Value", new VarexpTuple(63, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Scaling", new VarexpTuple(64, "CB", new String[]{"0", "1"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Min. Equip Value", new VarexpTuple(65, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Max Equip. Value", new VarexpTuple(66, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Display Format", new VarexpTuple(67, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Chronometer Period (in second)", new VarexpTuple(90, "TF", new String[]{""}, new String[]{""}, true, 2));
        fieldMap.put("Chronometer inc/dec", new VarexpTuple(91, "TF", new String[]{"Decrement", "Increment"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Name of bit to enable chrono", new VarexpTuple(92, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Enable On", new VarexpTuple(93, "CB", new String[]{"0", "1"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Name of bit to reset chrono", new VarexpTuple(94, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Reset on Transition", new VarexpTuple(95, "CB", new String[]{"0", "1"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Deadband Type", new VarexpTuple(156, "CB", new String[]{"0", "1", "3"}, new String[]{"0", "1", "3"}, true, 2));
        fieldMap.put("VCR", new VarexpTuple(157, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        return fieldMap;
    }

    public ArrayList<List<String>> getArrayList() {
        return this.chrList;
    }

    @Override
    public String getVariableIdName() {
        return "chr" + ID;
    }
}
