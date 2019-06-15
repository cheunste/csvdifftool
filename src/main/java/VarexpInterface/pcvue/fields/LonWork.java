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
        //TODO: Impliment this when user actually uses this
        return null;
    }

    @Override
    public String getVariableIdName() {
        return "lonwork" + ID;
    }
}
