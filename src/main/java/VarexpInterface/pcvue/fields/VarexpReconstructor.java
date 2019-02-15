package VarexpInterface.pcvue.fields;

import java.util.Arrays;
import java.util.List;

public class VarexpReconstructor {


    public static String ListToString(List<String> reconstructedList) {
        String result = "";
        for (String temp : reconstructedList) {
            result += temp + ",";
        }

        return result;
    }

    /**
     * This function reconstructs a Varexp Array from the database. It takes three Strings for each piece of the varexp.
     *
     * @param commonResult        The common varexp String from the database...along with its ID
     * @param sourceResult        The source String. Can vary in length depending on variable
     * @param sourceVariableName  The name of the source variable. This is so I don't have to perform another rquery
     * @param commandResult       The command String. Can vary in length depending on variable
     * @param commandVariableName The name of the command variable.
     * @param allAlarmResult      This is the all_alarms string from the all_alamrms table
     */
    public static List<String> reconstructVarexpArray(String commonResult,
                                                      String commandResult, String commandVariableName,
                                                      String sourceResult, String sourceVariableName,
                                                      String allAlarmResult) {
        /*
        TODO:
        1) Populate the reconstructedVarexpArray with empty Strings. Remember the FIELD_NUM member variable
        2) Fetch the position array (varexpPositionList) from the common Varexp subclass and then stuff the common
        variables into it

        3) Remove the variable Id from commandResult and sourceResult. this one is very debatable

        4) fetch teh position array from the source variable
        5) populate reconsturctedVarexpArray with the source data. Remember that Source can be empty (newly created)

        6)fetch teh position array from the command variable
        7) populate reconsturctedVarexpArray with the source data. Remember that Command variable can be empty (newly created)

        8) return the reconstructecVarexpArray
         */

        /***************COMMON***************/
        VarexpFactory factory = new VarexpFactory();
        VarexpVariable common = factory.declareNewVariable("COMMON");

        common.initializeVarexpArray();
        List<String> reconstructedVarexpArray = common.getVarexpList();

        //Get the positino list of the Common Variable
        List<Integer> commonPositionList = common.getVarexpPositionList();

        String[] temp = commonResult.split(",");

        int tempIndexCounter = 0;
        String[] commonVariable = Arrays.copyOfRange(temp, 1, temp.length);


        //Traverse the common variables pulled from the database
        for (String commonField : commonVariable) {
            //And insert them into the reconstructedVarexpArray
            reconstructedVarexpArray.set(commonPositionList.get(tempIndexCounter), commonField);
            //Increment the temp counter
            tempIndexCounter++;
        }

        /***************COMMAND***************/
        tempIndexCounter = 0;
        VarexpVariable command = factory.declareNewVariable(commandVariableName);
        List<Integer> commandPositionList = command.getVarexpPositionList();

        temp = commandResult.split(",");
        String[] commandVariable = Arrays.copyOfRange(temp, 1, temp.length);

        for (String commandField : commandVariable) {
            //And insert them into the reconstructedVarexpArray
            reconstructedVarexpArray.set(commandPositionList.get(tempIndexCounter), commandField);
            //Increment the temp counter
            tempIndexCounter++;
        }


        /***************SOURCE***************/
        tempIndexCounter = 0;
        VarexpVariable source = factory.declareNewVariable(sourceVariableName);
        List<Integer> sourcePositionList = source.getVarexpPositionList();

        temp = sourceResult.split(",");
        String[] sourceVariable = Arrays.copyOfRange(temp, 1, temp.length);

        for (String sourceField : sourceVariable) {
            //And insert them into the reconstructedVarexpArray
            reconstructedVarexpArray.set(sourcePositionList.get(tempIndexCounter), sourceField);
            //Increment the temp counter
            tempIndexCounter++;
        }

        /***************ALL_ALARMS***************/
        tempIndexCounter = 0;
        VarexpVariable allAlarms = factory.declareNewVariable("ALL");
        List<Integer> allAlarmsPositionList = allAlarms.getVarexpPositionList();

        temp = allAlarmResult.split(",");
        String[] allAlarmsVariable = Arrays.copyOfRange(temp, 1, temp.length);

        for (String alarmField : allAlarmsVariable) {
            //And insert them into the reconstructedVarexpArray
            reconstructedVarexpArray.set(allAlarmsPositionList.get(tempIndexCounter), alarmField);
            //Increment the temp counter
            tempIndexCounter++;
        }

        //In the reconstructed Variable
        return reconstructedVarexpArray;
    }

    /**
     * Reconstruct a VarexpVariable given a the table id and the databaseName
     *
     * @param idList:       A list of ids from the database that houses the VarexpVariable that needs to be reconstructed
     * @param databaseName: Name of the database. For this case, it most likely would be "oldvarexpdb" and "newvarexpdb"
     * @return A List of VarexpVariables
     */
    public List<String> reconstructVarexpTag(List<Integer> idList, String databaseName) {

        return null;
    }
}
