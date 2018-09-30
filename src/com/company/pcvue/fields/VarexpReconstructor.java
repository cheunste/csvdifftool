package com.company.pcvue.fields;

import java.util.List;

public class VarexpReconstructor {


    /**
     * This function reconstructs a Varexp Array from the database. It takes three Strings for each piece of the varexp.
     * @param commonVariable  The common varexp String from the database
     * @param sourceVariable  The source String. Can vary in length depending on variable
     * @param commandVariable The command String. Can vary in length depending on variable
     */
    public static List<String> reconstructVarexpArray(String[] commonVariable, String[] sourceVariable, String[] commandVariable) {


        /*
        TODO:
        1) Populate the reconstructedVarexpArray with empty Strings. Remember the FIELD_NUM member variable
        2) Fetch the position array (varexpPositionList) from the common Varexp subclass and then stuff the common
        variables into it
        3) Determine the source and command that was set up in Coomon. Remember that Source and Command can be empty
        if it is a new variable and user haven't implemented it yet
        4)
         */


        VarexpFactory factory = new VarexpFactory();

        VarexpVariable common = factory.declareNewVariable("COMMON");

        common.initializeVarexpArray();
        //Create an empty Varexp array
        List<String> reconstructedVarexpArray = common.getVarexpList();

        //Get the positino list of the Common Variable
        List<Integer> commonPositionList = common.getVarexpPositionList();

        int tempIndexCounter = 1;

        //Traverse the common variables pulled from the database
        for (String commonField : commonVariable) {
            //And insert them into the reconstructedVarexpArray
            reconstructedVarexpArray.set(commonPositionList.get(tempIndexCounter), commonField);
            //Increment the temp counter
            tempIndexCounter++;
        }

        /***************COMMAND***************/
        //Note that for tempIndexCounter starts at 1 in this case because you need to ignore the extra variable_ID that is at the begining
        tempIndexCounter = 0;
        String commandName = commonVariable[VarexpVariable.COMMAND_FIELD_NUM];
        VarexpVariable command = factory.declareNewVariable(commandName);
        List<Integer> commandPositionList = command.getVarexpPositionList();

        for (String commandField : commandVariable) {
            if (tempIndexCounter > 0 && (tempIndexCounter != commandVariable.length)) {
                //And insert them into the reconstructedVarexpArray
                reconstructedVarexpArray.set(commandPositionList.get(tempIndexCounter - 1), commandField);
            }
            //Increment the temp counter
            tempIndexCounter++;
        }


        /***************SOURCE***************/
        tempIndexCounter = 0;
        String sourceName = commonVariable[VarexpVariable.SOURCE_FIELD_NUM];
        VarexpVariable source = factory.declareNewVariable(sourceName);
        List<Integer> sourcePositionList = source.getVarexpPositionList();

        for (String sourceField : sourceVariable) {
            if (tempIndexCounter > 0 && (tempIndexCounter != sourceVariable.length)) {
                //And insert them into the reconstructedVarexpArray
                reconstructedVarexpArray.set(sourcePositionList.get(tempIndexCounter - 1), sourceField);
            }
            //Increment the temp counter
            tempIndexCounter++;
        }

        //In the reconstructed Variable
        return reconstructedVarexpArray;
    }

    public static String ListToString(List<String> reconstructedList) {
        String result = "";
        for (String temp : reconstructedList) {
            result += temp + ",";
        }

        return result;
    }
}
