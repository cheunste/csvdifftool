package VarexpInterface.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/28/2018.
 */
public class LonWork extends VarexpVariable {
    private ArrayList<List<String>> lonworkList;

    public LonWork() {
        this.lonworkList = new ArrayList<>();
        setTableName("lonwork");
        setPositionList();
    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
    }

    @Override
    void setPositionList() {
        for (int i = 109; i <= 117; i++) {
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

        return "RIGHT JOIN  lonwork on common.variable_id=lonwork.lonwork_variable_id";
    }

    @Override
    public String createTableCmd() {
        return
                "CREATE TABLE lonwork(" +
                        "lonwork_variable_id int unsigned  primary key," +
                        "lonwork_Network_alias TEXT(50) NULL," +
                        "lonwork_Node_alias TEXT(50) NULL," +
                        "lonwork_variable_name TEXT(50) NULL," +
                        "lonwork_network_scanning_mode TEXT(50) NULL," +
                        "lonwork_reserved9 TEXT(50) NULL," +
                        "lonwork_network_variable_field_name TEXT(50) NULL," +
                        "lonwork_reserved10 TEXT(50) NULL," +
                        "lonwork_monitoring_definition TEXT(50) NULL," +
                        "lonwork_monitoring_type TEXT(50) NULL);";

    }

    public ArrayList<List<String>> getArrayList() {
        return this.lonworkList;
    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> lonworkList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        lonworkList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            lonworkList.add(varexpArraySplit.get(i).trim());
        }
        this.lonworkList.add(lonworkList);

    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Network Alias", new VarexpTuple(110, "TF", new String[]{""}, new String[]{""}, true, 30));
        fieldMap.put("Node Alias", new VarexpTuple(111, "TF", new String[]{""}, new String[]{""}, true, 30));
        fieldMap.put("Variable Name", new VarexpTuple(112, "TF", new String[]{""}, new String[]{""}, true, 30));
        fieldMap.put("Network Scanning Mode", new VarexpTuple(113, "TF", new String[]{""}, new String[]{""}, true, 30));
        fieldMap.put("Reserved", new VarexpTuple(114, "TF", new String[]{""}, new String[]{""}, true, 30));
        fieldMap.put("Netowkr Variable Field Name", new VarexpTuple(115, "TF", new String[]{""}, new String[]{""}, true, 30));
        fieldMap.put("Reserved", new VarexpTuple(116, "TF", new String[]{""}, new String[]{""}, true, 30));
        fieldMap.put("Monitoring definition", new VarexpTuple(117, "CB", new String[]{""}, new String[]{""}, true, 30));
        fieldMap.put("Monitoring Type", new VarexpTuple(118, "CB", new String[]{""}, new String[]{""}, true, 30));
        return fieldMap;
    }

    @Override
    public String getVariableIdName() {
        return "lonwork" + ID;
    }
}
