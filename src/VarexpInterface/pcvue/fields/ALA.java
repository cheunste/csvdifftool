package VarexpInterface.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/20/2018.
 */
public class ALA extends VarexpVariable {
    private ArrayList<List<String>> alaList;

    public ALA() {
        this.alaList = new ArrayList<>();
        setTableName("ala");
        setPositionList();
    }

    @Override
    void setPositionList() {
        for (int i = 40; i <= 42; i++) {
            this.varexpPositionList.add(i);
        }
        for (int i = 44; i <= 47; i++) {
            this.varexpPositionList.add(i);
        }
        this.varexpPositionList.add(156);
    }

    @Override
    List<Integer> getPositionList() {
        return this.varexpPositionList;
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
        return "RIGHT JOIN ala on common.variable_id = ala.ala_variable_id";
    }

    @Override
    public String createTableCmd() {
        return "CREATE TABLE ala(" +
                "ala_variable_id int unsigned  primary key," +
                "	ala_Log_bit_0_to_1 TEXT(50) NULL," +
                "	ala_Log_bit_1_to_0 TEXT(50) NULL," +
                "	ala_reserved TEXT(50) NULL," +
                "	ala_Alarm_Level TEXT(50) NULL," +
                "	ala_Alarm TEXT(50) NULL," +
                "	ala_Name_of_mask_bit TEXT(50) NULL," +
                "	alarm_temporization TEXT(50) NULL," +
                "	ala_VCR TEXT(50) NULL);";

    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {

        setVarexpArrayList(varexpString);
        List<String> alaList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        alaList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            alaList.add(varexpArraySplit.get(i).trim());
        }
        this.alaList.add(alaList);

    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Log bit (0 to 1)", new VarexpTuple(41, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Log bit (1 to 0)", new VarexpTuple(42, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("", new VarexpTuple(43, "TF", new String[]{""}, new String[]{""}, false, 0));
        fieldMap.put("Alarm Level", new VarexpTuple(45, "TF", new String[]{""}, new String[]{"0", "29"}, true, 2));
        fieldMap.put("Alarm", new VarexpTuple(46, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Name of bit for mask", new VarexpTuple(47, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Alarm Temporization", new VarexpTuple(48, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("VCR", new VarexpTuple(157, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        return fieldMap;
    }

    public ArrayList<List<String>> getArrayList() {
        return this.alaList;

    }

    @Override
    public String getVariableIdName() {
        return "ala" + ID;
    }
}
