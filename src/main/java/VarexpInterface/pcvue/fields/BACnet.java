package VarexpInterface.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/28/2018.
 */
public class BACnet extends VarexpVariable {

    private ArrayList<List<String>> bacList;

    public BACnet() {
        this.bacList = new ArrayList<>();
        setTableName("bacnet");
        setPositionList();
    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
    }

    @Override
    void setPositionList() {
        for (int i = 162; i <= 172; i++) {
            getVarexpPositionList().add(i);
        }
        getVarexpPositionList().add(203);
        getVarexpPositionList().add(204);
        getVarexpPositionList().add(217);
        getVarexpPositionList().add(218);
    }

    @Override
    String empty() {
        String emptyString = "";
        for (int i : getVarexpPositionList()) {
            emptyString += ",";
        }

        return emptyString;
    }

    @Override
    String getJoinCmd() {
        return "RIGHT JOIN  bac on common.variable_id=bac.bac_variable_id";
    }

    @Override
    public String createTableCmd() {
        return
                "CREATE TABLE bacnet(" +
                        "bacnet_variable_id int unsigned  primary key," +
                        "bacnet_Network_alias TEXT(50) NULL," +
                        "bacnet_device_alias TEXT(50) NULL," +
                        "bacnet_object_type TEXT(50) NULL," +
                        "bacnet_object_instance TEXT(50) NULL," +
                        "bacnet_property TEXT(50) NULL," +
                        "bacnet_fields TEXT(50) NULL," +
                        "bacnet_frequency TEXT(50) NULL," +
                        "bacnet_change_of_value_type TEXT(50) NULL," +
                        "bacnet_priority TEXT(50) NULL," +
                        "bacnet_EDE_file TEXT(50) NULL," +
                        "bacnet_Rserved TEXT(50) NULL," +
                        "bacnet_EDE_file_name TEXT(50) NULL," +
                        "bacnet_EDE_Keyname TEXT(50) NULL," +
                        "BACnet_variable_type TEXT(50) NULL," +
                        "BACnet_alarm_type TEXT(50) NULL);";

    }

    public ArrayList<List<String>> getArrayList() {
        return this.bacList;
    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> bacList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        bacList.add("" + dbIndex);

        for (int i : getVarexpPositionList()) {
            bacList.add(varexpArraySplit.get(i).trim());
        }

        this.bacList.add(bacList);
    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        //TODO: Impliment this when user actually uses this
        return null;
    }

    @Override
    public String getVariableIdName() {
        return "bacnet" + ID;
    }
}
