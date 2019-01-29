package VarexpInterface.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/21/2018.
 */
public class All_Alarms extends VarexpVariable {
    private ArrayList<List<String>> allAlarmsList;

    public All_Alarms() {
        this.allAlarmsList = new ArrayList<>();
        setTableName("all_alarms");
        setPositionList();

    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
    }

    @Override
    void setPositionList() {
        for (int i = 157; i <= 159; i++) {
            varexpPositionList.add(i);
        }
        for (int i = 174; i <= 176; i++) {
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
        return "RIGHT JOIN  all_alarms on common.variable_id=all_alarms.all_alarms_variable_id";
    }

    @Override
    public String createTableCmd() {
        return "CREATE TABLE all_alarms(" +
                "all_alarms_variable_id int unsigned  primary key," +
                "	all_alarms_Expression_Branch TEXT(50) NULL," +
                "	all_alarms_Expression_template_for_alarm_mask TEXT(50) NULL," +
                "	all_alarms_Latch_behavior TEXT(50) NULL," +
                "	all_alarms_Acknowledgement_level TEXT(50) NULL," +
                "	all_alarms_Mask_level TEXT(50) NULL," +
                "	all_alarms_Maintenance_level TEXT(50) NULL);";

    }

    public ArrayList<List<String>> getArrayList() {
        return this.allAlarmsList;
    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> all_alarmsList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        all_alarmsList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            all_alarmsList.add(varexpArraySplit.get(i).trim());
        }
        this.allAlarmsList.add(all_alarmsList);
    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("", new VarexpTuple(158, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("", new VarexpTuple(159, "TF", new String[]{""}, new String[]{""}, true, 80));
        fieldMap.put("", new VarexpTuple(160, "CB", new String[]{"N", "Y"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("", new VarexpTuple(175, "TF", new String[]{""}, new String[]{"-1", "29"}, true, 2));
        fieldMap.put("", new VarexpTuple(176, "TF", new String[]{""}, new String[]{"-2", "29"}, true, 2));
        fieldMap.put("", new VarexpTuple(177, "TF", new String[]{""}, new String[]{"-2", "29"}, true, 2));
        return fieldMap;
    }

    @Override
    public String getVariableIdName() {
        return "all_alarms" + ID;
    }
}
