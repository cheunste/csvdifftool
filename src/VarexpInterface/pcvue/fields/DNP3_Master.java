package VarexpInterface.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/28/2018.
 */
public class DNP3_Master extends VarexpVariable {
    private ArrayList<List<String>> dnp3_masterList;

    public DNP3_Master() {
        this.dnp3_masterList = new ArrayList<>();
        setTableName("dnp3_master");
        setPositionList();
    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
    }
    @Override
    void setPositionList() {
        for (int i = 206; i <= 210; i++) {
            varexpPositionList.add(i);
        }
        varexpPositionList.add(212);
        varexpPositionList.add(214);
        for (int i = 219; i <= 228; i++) {
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

        return "RIGHT JOIN  dnp3_master on common.variable_id=dnp3_master.dnp3_master_variable_id";
    }

    @Override
    public String createTableCmd() {
        return
                "CREATE TABLE dnp3_master(" +
                        "dnp3_master_variable_id int unsigned  primary key," +
                        "dnp3_master_Network_name TEXT(50) NULL," +
                        "dnp3_master_device_name TEXT(50) NULL," +
                        "dnp3_master_type TEXT(50) NULL," +
                        "dnp3_master_point_address TEXT(50) NULL," +
                        "dnp3_master_AOB_PointVariation TEXT(50) NULL," +
                        "dnp3_master_Time_Tagging TEXT(50) NULL," +
                        "dnp3_master_Select_before_operate TEXT(50) NULL," +
                        "dnp3_master_enable_writing_add_val0 TEXT(50) NULL," +
                        "dnp3_master_add_val_0 TEXT(50) NULL," +
                        "dnp3_master_options_val0 TEXT(50) NULL," +
                        "dnp3_master_onTimeMs_val0 TEXT(50) NULL," +
                        "dnp3_master_EnableWriting_add_val_1 TEXT(50) NULL," +
                        "dnp3_master_add_val1 TEXT(50) NULL," +
                        "dnp3_master_options_val1 TEXT(50) NULL," +
                        "dnp3_master_onTimeMs_val1 TEXT(50) NULL," +
                        "dnp3_master_EnableWritingAdd_AOB TEXT(50) NULL," +
                        "dnp3_master_Add_AOB TEXT(50) NULL);";
    }

    public ArrayList<List<String>> getArrayList() {
        return this.dnp3_masterList;
    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> dnp3_masterList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        dnp3_masterList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            dnp3_masterList.add(varexpArraySplit.get(i).trim());
        }

        this.dnp3_masterList.add(dnp3_masterList);

    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Network Name", new VarexpTuple(207, "TF", new String[]{""}, new String[]{""}, true, 256));
        fieldMap.put("Device Name", new VarexpTuple(208, "TF", new String[]{""}, new String[]{""}, true, 256));
        fieldMap.put("Type", new VarexpTuple(209, "CB", new String[]{"Unknown", "BinaryInput", "BinaryOutput", "RunningCounter", "FrozenCounter", "AnalogInput", "AnalogOutput", "IIN", "ATT"}, new String[]{"0", "1", "10", "0", "21", "30", "40", "80", "1000"}, true, 2));
        fieldMap.put("Point Address", new VarexpTuple(210, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("AOB Point Variation", new VarexpTuple(211, "CB", new String[]{"32Bit", "16Bit", "Single precision Floating point", "Double precision Floating point"}, new String[]{"1", "2", "3", "4"}, true, 2));
        fieldMap.put("TIme Tagging", new VarexpTuple(213, "CB", new String[]{"Default", "Time tagged", "No time tagged"}, new String[]{"0", "1", "2"}, true, 2));
        fieldMap.put("Select Before Operate", new VarexpTuple(215, "CB", new String[]{"From global conf.", "Direct operate", "Direct op. no ack ", "Select"}, new String[]{"0", "1", "2", "3"}, true, 2));
        fieldMap.put("Enable Writing", new VarexpTuple(220, "CB", new String[]{"Enable different add for value 0", "disable different add for value 0"}, new String[]{"1", "0"}, true, 2));
        fieldMap.put("AddVal0", new VarexpTuple(221, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("OPtions+Val0", new VarexpTuple(222, "CB", new String[]{"Latch on", "Latch off", "Pulse on", "Trip and pulse on", "Close and pulse on"}, new String[]{"0", "1", "2", "3", "4"}, true, 0));
        fieldMap.put("OnTimeMS+Val0", new VarexpTuple(223, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("EnableWritingAdd+Val1", new VarexpTuple(224, "CB", new String[]{"Enable different add for value 1", "disable different add for value 1"}, new String[]{"1", "0"}, true, 2));
        fieldMap.put("Add+Val1", new VarexpTuple(225, "TF", new String[]{""}, new String[]{""}, true, 4));
        fieldMap.put("Options+Val1", new VarexpTuple(226, "CB", new String[]{""}, new String[]{""}, true, 0));
        fieldMap.put("OnTimeMS+Val1", new VarexpTuple(227, "TF", new String[]{"Latch on", "Latch off", "Pulse on", "Trip and pulse on", "Close and pulse on"}, new String[]{"0", "1", "2", "3", "4"}, true, 4));
        fieldMap.put("EnableWritingAdd+AOB", new VarexpTuple(228, "CB", new String[]{"Enable different add for value 1", "disable different add for value 1"}, new String[]{"1", "0"}, true, 2));
        fieldMap.put("Add_AOB", new VarexpTuple(229, "TF", new String[]{""}, new String[]{""}, true, 4));
        return fieldMap;
    }

    @Override
    public String getVariableIdName() {
        return "dnp3_master" + ID;
    }
}
