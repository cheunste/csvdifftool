package VarexpInterface.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/2/2018.
 */
public class ACM extends VarexpVariable {
    private ArrayList<List<String>> acmList;

    public ACM() {
        this.acmList = new ArrayList<>();
        setPositionList();
        setTableName("acm");
    }


    @Override
    void setPositionList() {
        for (int i = 40; i <= 46; i++) {
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

        return "RIGHT JOIN  acm on common.variable_id=acm.acm_variable_id";
    }

    @Override
    public String createTableCmd() {
        return "CREATE TABLE acm(" +
                "acm_variable_id int unsigned  primary key," +
                "	acm_Log_bit_0_to_1 TEXT(50) NULL," +
                "	acm_Log_bit_1_to_0 TEXT(50) NULL," +
                "	acm_reserved TEXT(50) NULL," +
                "	acm_Authoisation_Level TEXT(50) NULL," +
                "	acm_Alarm_Level TEXT(50) NULL," +
                "	acm_Alarm TEXT(50) NULL," +
                "	acm_Name_of_mask_bit TEXT(50) NULL," +
                "	acm_VCR TEXT(50) NULL);";

    }

    @Override
    public ArrayList<List<String>> getArrayList() {
        return this.acmList;
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
        this.acmList.add(acmList);

    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Log bit (0 to 1)", new VarexpTuple(41, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Log bit (1 to 0)", new VarexpTuple(42, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("", new VarexpTuple(43, "TF", new String[]{""}, new String[]{""}, false, 0));
        fieldMap.put("Authorisation Level", new VarexpTuple(44, "TF", new String[]{""}, new String[]{"0", "29"}, true, 2));
        fieldMap.put("Alarm Level", new VarexpTuple(45, "TF", new String[]{""}, new String[]{"0", "29"}, true, 2));
        fieldMap.put("Alarm", new VarexpTuple(46, "CB", new String[]{"Y", "N"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Name of bit for mask", new VarexpTuple(47, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("VCR", new VarexpTuple(157, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));

        return fieldMap;
    }

    @Override
    public String getVariableIdName() {
        return "acm" + ID;
    }
}
