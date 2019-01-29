package VarexpInterface.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 5/28/2018.
 */
public class DDE extends VarexpVariable {
    private ArrayList<List<String>> ddeList;

    public DDE() {
        this.ddeList = new ArrayList<>();
        setTableName("dde");
        setPositionList();
    }

    @Override
    void setPositionList() {
        for (int i = 119; i <= 126; i++) {
            varexpPositionList.add(i);
        }
    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
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

        return "RIGHT JOIN  dde on common.variable_id=dde.dde_variable_id";
    }

    @Override
    public String createTableCmd() {

        return "CREATE TABLE dde(" +
                "dde_variable_id int unsigned  primary key," +
                "dde_Conversation_name TEXT(50) NOT NULL," +
                "dde_Item_Name TEXT(50) NOT NULL," +
                "dde_reserved6 TEXT(50) NOT NULL," +
                "dde_reserved7 TEXT(50) NOT NULL," +
                "dde_Item_format TEXT(50) NOT NULL," +
                "dde_reserved TEXT(50) NOT NULL," +
                "dde_Auto_Format_Flag TEXT(50) NOT NULL," +
                "dde_Label  TEXT(50) NOT NULL);";


    }

    public ArrayList<List<String>> getArrayList() {
        return this.ddeList;
    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> ddeList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        ddeList.add("" + dbIndex);
        for (int i : varexpPositionList) {
            ddeList.add(varexpArraySplit.get(i).trim());
        }

        this.ddeList.add(ddeList);

    }

    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        fieldMap.put("Conversation Name", new VarexpTuple(120, "TF", new String[]{""}, new String[]{""}, true, 20));
        fieldMap.put("Item Name", new VarexpTuple(121, "TF", new String[]{""}, new String[]{""}, true, 20));
        fieldMap.put("", new VarexpTuple(122, "TF", new String[]{""}, new String[]{""}, false, 0));
        fieldMap.put("", new VarexpTuple(123, "TF", new String[]{""}, new String[]{""}, false, 0));
        fieldMap.put("Item Format", new VarexpTuple(124, "TF", new String[]{""}, new String[]{""}, true, 1));
        fieldMap.put("", new VarexpTuple(125, "TF", new String[]{""}, new String[]{""}, false, 0));
        fieldMap.put("Auto Format Flag", new VarexpTuple(126, "CB", new String[]{"Auto", "NA"}, new String[]{"1", ""}, true, 1));
        fieldMap.put("Label", new VarexpTuple(127, "TF", new String[]{""}, new String[]{""}, true, 80));

        return fieldMap;
    }

    @Override
    public String getVariableIdName() {
        return "dde" + ID;
    }
}
