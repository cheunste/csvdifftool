package VarexpInterface.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/28/2018.
 */
public class IEC61850_Master extends VarexpVariable {
    private ArrayList<List<String>> iec61850_masterList;

    public IEC61850_Master() {
        this.iec61850_masterList = new ArrayList<>();
        setTableName("iec61850_master");
        setPositionList();
    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
    }
    @Override
    void setPositionList() {
        for (int i = 189; i <= 199; i++) {
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

        return "RIGHT JOIN  iec61850_master on common.variable_id=iec61850_master.iec61850_master_variable_id";
    }

    @Override
    public String createTableCmd() {
        return "CREATE TABLE iec61850_master(" +
                "iec61850_variable_id int unsigned  primary key," +
                "iec61850_Mapping_Type TEXT(50) NULL," +
                "iec61850_61850_master_network_alias TEXT(50) NULL," +
                "iec61850_61850_master_physical_device_alias TEXT(50) NULL," +
                "iec61850_61850_master_data_group_alias TEXT(50) NULL," +
                "iec61850_master_data_group_alias TEXT(50) NULL," +
                "iec61850_field TEXT(50) NULL," +
                "iec61850_reserved11 TEXT(50) NULL," +
                "iec61850_data_object_identifier TEXT(50) NULL," +
                "iec61850_control_model TEXT(50) NULL," +
                "iec61850_time_provided TEXT(50) NULL," +
                "iec61850_quality_provided TEXT(50) NULL);";
    }

    public ArrayList<List<String>> getArrayList() {
        return this.iec61850_masterList;
    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> iec61850_masterList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        iec61850_masterList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            iec61850_masterList.add(varexpArraySplit.get(i).trim());
        }

        this.iec61850_masterList.add(iec61850_masterList);

    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        //TODO: Impliment this when user actually uses this
        return null;
    }

    @Override
    public String getVariableIdName() {
        return "iec61850" + ID;
    }
}
