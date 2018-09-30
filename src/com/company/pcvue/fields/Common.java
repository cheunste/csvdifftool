package com.company.pcvue.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 4/27/2018.
 */
public class Common extends VarexpVariable {

    List<String> stringList;
    private ArrayList<List<String>> commonList;
    private String tableName;

    //Constructor, I guess
    public Common() {
        this.commonList = new ArrayList<>();
        setTableName("Common");
        setPositionList();
    }

    @Override
    void setPositionList() {
        for (int i = 0; i <= 28; i++) {
            varexpPositionList.add(i);
        }
        varexpPositionList.add(39);
        for (int i = 129; i <= 143; i++) {
            varexpPositionList.add(i);
        }
        varexpPositionList.add(173);
        varexpPositionList.add(177);
        varexpPositionList.add(178);
        varexpPositionList.add(240);
    }

    @Override
    List<Integer> getPositionList() {
        return varexpPositionList;
    }

    //This returns a map of all text field items, along with their VarexpTuple
    @Override
    public Map<String, VarexpTuple> getFieldMap() {
        //fieldMap.put("Variable Type", new VarexpTuple(1, "TF", new String[]{""}, new String[]{""}, true, 3));
        fieldMap.put("Variable Type", new VarexpTuple(1, "CB",
                new String[]{"Bit", "Bit and Command", "Alarm", "Alarm and command",
                        "Threshold", "Threshold and alarm Register", "Register and command", "Counter", "Chronometer Text",
                        "Text and command"},
                new String[]{"BIT", "CMD", "ALA", "ACM", "TSH", "ATS", "REG", "CTV", "CNT", "CHR", "TXT", "CXT"},
                true, 3));

        fieldMap.put("Variable Internal ID", new VarexpTuple(2, "TF", new String[]{""}, new String[]{"1", "30000"}, true, 12));
        fieldMap.put("1st element of Variable", new VarexpTuple(3, "TF", new String[]{""}, new String[]{""}, true, 255));
        fieldMap.put("2nd element of Variable", new VarexpTuple(4, "TF", new String[]{""}, new String[]{""}, true, 255));
        fieldMap.put("3rd element of Variable", new VarexpTuple(5, "TF", new String[]{""}, new String[]{""}, true, 255));
        fieldMap.put("4th element of Variable", new VarexpTuple(6, "TF", new String[]{""}, new String[]{""}, true, 255));
        fieldMap.put("5th element of Variable", new VarexpTuple(7, "TF", new String[]{""}, new String[]{""}, true, 255));
        fieldMap.put("6th element of Variable", new VarexpTuple(8, "TF", new String[]{""}, new String[]{""}, true, 255));
        fieldMap.put("7th-12th element of Variable", new VarexpTuple(9, "TF", new String[]{""}, new String[]{""}, true, 255));
        fieldMap.put("Variable description (1st lang)", new VarexpTuple(10, "TF", new String[]{""}, new String[]{""}, true, 255));
        fieldMap.put("Varialbe description (2nd lang)", new VarexpTuple(11, "TF", new String[]{""}, new String[]{""}, true, 255));
        fieldMap.put("Domain", new VarexpTuple(12, "TF", new String[]{""}, new String[]{""}, true, 255));
        fieldMap.put("Nature", new VarexpTuple(13, "TF", new String[]{""}, new String[]{""}, true, 255));
        fieldMap.put("Inhibit flag", new VarexpTuple(14, "CB", new String[]{"Y", "N"}, new String[]{"I", ""}, true, 100));
        fieldMap.put("Simulated flag", new VarexpTuple(15, "CB", new String[]{"Y", "N"}, new String[]{"S", ""}, true, 100));
        fieldMap.put("Permanent flag", new VarexpTuple(16, "CB", new String[]{"Y", "N"}, new String[]{"P", ""}, true, 1));
        fieldMap.put("Source of Variable", new VarexpTuple(17, "CB", new String[]{"Equipment", "Internal", "External", "DDE", "OPC", "LonWorks", "BACnet", "60870-5-104", "61850", "DNP3", "SNMP"}, new String[]{"E", "I", "X", "D", "O", "L", "B", "4", "8", "3", "S"}, true, 1));
        fieldMap.put("Diffusion Indicator", new VarexpTuple(18, "CB", new String[]{"Y", "N"}, new String[]{"1", "0"}, true, 2));
        fieldMap.put("Number of station", new VarexpTuple(19, "TF", new String[]{""}, new String[]{"1", "253"}, true, 2));
        fieldMap.put("reserved", new VarexpTuple(20, "TF", new String[]{""}, new String[]{""}, false, 0));
        fieldMap.put("Permanent subscription for mimics", new VarexpTuple(21, "CB", new String[]{"0: All stations", "1: None", "2: Server station"}, new String[]{"0", "1", "2"}, true, 2));
        fieldMap.put("Object Root", new VarexpTuple(22, "CB", new String[]{"Local", "Customized Layer"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("Variable with Extended Attributes", new VarexpTuple(23, "CB", new String[]{"Columns 130-140", "Columns 136-146"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("reserved2", new VarexpTuple(24, "TF", new String[]{""}, new String[]{""}, false, 0));
        fieldMap.put("Remote Access", new VarexpTuple(25, "CB", new String[]{"0", "1"}, new String[]{"0", "1"}, true, 2));
        fieldMap.put("reserved3", new VarexpTuple(26, "TF", new String[]{""}, new String[]{""}, false, 0));
        fieldMap.put("reserved4", new VarexpTuple(27, "TF", new String[]{""}, new String[]{""}, false, 0));
        fieldMap.put("Topology: Server list name", new VarexpTuple(28, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Topology: Client list name", new VarexpTuple(29, "TF", new String[]{""}, new String[]{""}, true, 40));
        fieldMap.put("Name of associated label", new VarexpTuple(40, "TF", new String[]{""}, new String[]{""}, true, 255));
        fieldMap.put("Binary attributes", new VarexpTuple(130, "TF", new String[]{""}, new String[]{"0", "4294967295"}, true, 4));
        fieldMap.put("Text Attribute 3", new VarexpTuple(131, "TF", new String[]{""}, new String[]{""}, true, 100));
        fieldMap.put("Text Attribute 4", new VarexpTuple(132, "TF", new String[]{""}, new String[]{""}, true, 100));
        fieldMap.put("Text Attribute 5", new VarexpTuple(133, "TF", new String[]{""}, new String[]{""}, true, 100));
        fieldMap.put("Text Attribute 6", new VarexpTuple(134, "TF", new String[]{""}, new String[]{""}, true, 100));
        fieldMap.put("Text Attribute 7", new VarexpTuple(135, "TF", new String[]{""}, new String[]{""}, true, 100));
        fieldMap.put("Text Attribute 8", new VarexpTuple(136, "TF", new String[]{""}, new String[]{""}, true, 100));
        fieldMap.put("Text Attribute 9", new VarexpTuple(137, "TF", new String[]{""}, new String[]{""}, true, 100));
        fieldMap.put("Text Attribute 10", new VarexpTuple(138, "TF", new String[]{""}, new String[]{""}, true, 100));
        fieldMap.put("Text Attribute 11", new VarexpTuple(139, "TF", new String[]{""}, new String[]{""}, true, 100));
        fieldMap.put("Text Attribute 12", new VarexpTuple(140, "TF", new String[]{""}, new String[]{""}, true, 100));
        fieldMap.put("Text Attribute 13", new VarexpTuple(141, "TF", new String[]{""}, new String[]{""}, true, 100));
        fieldMap.put("Text Attribute 14", new VarexpTuple(142, "TF", new String[]{""}, new String[]{""}, true, 100));
        fieldMap.put("Text Attribute 15", new VarexpTuple(143, "TF", new String[]{""}, new String[]{""}, true, 100));
        fieldMap.put("Text Attribute 16", new VarexpTuple(144, "TF", new String[]{""}, new String[]{""}, true, 100));
        fieldMap.put("Visualization Level", new VarexpTuple(174, "TF", new String[]{""}, new String[]{"0", "29"}, true, 2));
        fieldMap.put("Is Initial Value", new VarexpTuple(178, "CB", new String[]{"0", "1"}, new String[]{"0", "1"}, true, 1));
        fieldMap.put("Initial Value", new VarexpTuple(179, "TF", new String[]{""}, new String[]{""}, true, 255));
        fieldMap.put("resevered5", new VarexpTuple(241, "TF", new String[]{""}, new String[]{""}, false, 0));

        return fieldMap;
    }

    @Override
        //Should never be used for common;
    String empty() {
        return "";
    }

    @Override
        //Shouldn't ever be used for common
    String getJoinCmd() {
        return "";
    }

    @Override
    public String createTableCmd() {
        return
                "CREATE TABLE common(" +
                        "variable_id	 			INT UNSIGNED  PRIMARY KEY," +
                        "variable_type	 		    TEXT(50) NOT NULL	," +
                        "internal_id	 			TEXT(50) NOT NULL	," +
                        "1st_element	 			TEXT(50) NOT NULL	," +
                        "2nd_element	 			TEXT(50) NOT NULL	," +
                        "3rd_element	 			TEXT(50) NULL	," +
                        "4th_element	 			TEXT(50) NULL	," +
                        "5th_element	 			TEXT(50) NULL	," +
                        "6th_element	 			TEXT(50) NULL	," +
                        "7th_to_12th	 			TEXT(50) NULL	," +
                        "desc_1st_lang	 		    TEXT(50) NOT NULL	," +
                        "desc_2nd_lang	 		    TEXT(50) NOT NULL	," +
                        "domain	 				    TEXT(50) NOT NULL	," +
                        "nature	 				    TEXT(50) NOT NULL	," +
                        "inhibit_flag	 		    TEXT(50)  NULL	," +
                        "simulated_flag	 		    TEXT(50)  NULL	," +
                        "permanent_flag	 		    TEXT(50)  NULL	," +
                        "source	 				    TEXT(50) NOT  NULL	," +
                        "diffusion_indicator	 	TEXT(50) NOT NULL	," +
                        "station	 				TEXT(50) NOT NULL	," +
                        "reserved	 			    TEXT(50) NOT NULL	," +
                        "subscription	 		    TEXT(50) NOT NULL	," +
                        "object_root	 			TEXT(50) NOT NULL	," +
                        "extended_attribute	 	    TEXT(50) NOT NULL	," +
                        "reserved2	 			    TEXT(50) NOT NULL	," +
                        "remote_access	 		    TEXT(50) NOT NULL	," +
                        "reserved3	 			    TEXT(50) NOT NULL	," +
                        "reserved4	 			    TEXT(50) NOT NULL	," +
                        "topology_server	 		TEXT(50) NOT NULL	," +
                        "topology_client	 		TEXT(50) NULL	," +
                        "name_of_associated_label	TEXT(50) NULL	," +
                        "binary_attributes	 	    TEXT(50) NULL	," +
                        "Text_attribute_3	 	    TEXT(50) NULL	," +
                        "Text_attribute_4	 	    TEXT(50) NULL	," +
                        "Text_attribute_5	 	    TEXT(50) NULL	," +
                        "Text_attribute_6	 	    TEXT(50) NULL	," +
                        "Text_attribute_7	 	    TEXT(50) NULL	," +
                        "Text_attribute_8	 	    TEXT(50) NULL	," +
                        "Text_attribute_9	 	    TEXT(50) NULL	," +
                        "Text_attribute_10	 	    TEXT(5) NOT NULL	," +
                        "Text_attribute_11	 	    TEXT(5) NOT NULL	," +
                        "Text_attribute_12	 	    TEXT(5) NOT NULL	," +
                        "Text_attribute_13	 	    TEXT(5) NOT NULL	," +
                        "Text_attribute_14	 	    TEXT(5) NOT NULL	," +
                        "Text_attribute_15	 	    TEXT(5) NOT NULL	," +
                        "Text_attribute_16	 	    TEXT(5) NOT NULL	," +
                        "Visualization_level	 	TEXT(50) NULL	," +
                        "is_initial_value	 	    TEXT(50) NULL	," +
                        "initial_value	 		    TEXT(50) NULL	," +
                        "reserved5	 			    TEXT(50) NULL	" +
                        ");";


    }

    @Override
    public void setArrayList(String varexpString, int dbIndex) {
        setVarexpArrayList(varexpString);
        List<String> commonArrayList = new ArrayList<>();
        List<String> varexpArraySplit = this.getVarexpList();

        //Add the index Number. This will be used as the ID reference to the DB
        commonArrayList.add("" + dbIndex);

        for (int i : varexpPositionList) {
            commonArrayList.add(varexpArraySplit.get(i).trim());
        }

        this.commonList.add(commonArrayList);
        //this.acmList.add(acmList);
    }

    public ArrayList<List<String>> getArrayList() {
        return this.commonList;
    }

    @Override
    public String getVariableIdName() {
        return "common" + ID;
    }
}
