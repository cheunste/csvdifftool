package VarexpInterface.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/28/2018.
 */
public class SNMP extends VarexpVariable {
    private ArrayList<List<String>> snmpList;

    public SNMP() {
        this.snmpList = new ArrayList<>();
        setTableName("snmp");
        setPositionList();

    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
    }
    @Override
    void setPositionList() {
        for (int i = 229; i <= 238; i++) {
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

        return "RIGHT JOIN  snmp on common.variable_id=snmp.snmp_variable_id";
    }

    @Override
    public String createTableCmd() {
        return
                "CREATE TABLE snmp(" +
                        "snmp_variable_id int unsigned  primary key," +
                        "snmp_Network_Name TEXT(50) NULL," +
                        "snmp_Device_name TEXT(50) NULL," +
                        "snmp_polling_group TEXT(50) NULL," +
                        "snmp_data_type TEXT(50) NULL," +
                        "snmp_OID TEXT(50) NULL," +
                        "snmp_Disable_reading TEXT(50) NULL," +
                        "snmp_with_initial_value TEXT(50) NULL," +
                        "snmp_initial_value TEXT(50) NULL," +
                        "snmp_offset TEXT(50) NULL," +
                        "snmp_extraction_field TEXT(50) NULL);";

    }

    public ArrayList<List<String>> getArrayList() {
        return this.snmpList;
    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> snmpList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        snmpList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            snmpList.add(varexpArraySplit.get(i).trim());
        }

        this.snmpList.add(snmpList);

    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Network Name", new VarexpTuple(230, "TF", new String[]{""}, new String[]{""}, true, 10));
        fieldMap.put("Device Name", new VarexpTuple(231, "TF", new String[]{""}, new String[]{""}, true, 10));
        fieldMap.put("Polling Group", new VarexpTuple(232, "TF", new String[]{""}, new String[]{""}, true, 10));
        fieldMap.put("Data Type", new VarexpTuple(233, "TF", new String[]{""}, new String[]{""}, true, 10));
        fieldMap.put("OID", new VarexpTuple(234, "TF", new String[]{""}, new String[]{""}, true, 10));
        fieldMap.put("Disable reading", new VarexpTuple(235, "TF", new String[]{""}, new String[]{""}, true, 10));
        fieldMap.put("With initial value", new VarexpTuple(236, "TF", new String[]{""}, new String[]{""}, true, 10));
        fieldMap.put("Initial value", new VarexpTuple(237, "TF", new String[]{""}, new String[]{""}, true, 10));
        fieldMap.put("Offset ", new VarexpTuple(238, "TF", new String[]{""}, new String[]{""}, true, 10));
        fieldMap.put("Extraction Field", new VarexpTuple(239, "TF", new String[]{""}, new String[]{""}, true, 10));
        return fieldMap;
    }

    @Override
    public String getVariableIdName() {
        return "snmp" + ID;
    }
}
