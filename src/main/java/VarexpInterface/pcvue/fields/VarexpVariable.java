package VarexpInterface.pcvue.fields;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 4/20/2018.
 * This is an abstract class that represents a Varexp Variable. A Varexp Variable have three main parts
 * 1) Fields (or parameters ) that are common to all Varexp Variables. This is known as the **common** fields
 * 2) Command fields.
 * 3) Source fields.
 * <p>
 * In a varexp's common field contains the information for the command and source field as well
 *
 * <p>
 * IMPORTANT: This is an abstract class that is meant to be inheritaned from other class, but have its own methods
 * to make things easier.
 * <p>
 * This abstract class handles anything relating ot the varexp file
 * <p>
 * This means anything include separating a varexp variable to its respective fields. If this doesn't make
 * any sense, then you need to read the varexp manual.
 */

public abstract class VarexpVariable {
    public static int SOURCE_FIELD_NUM = 17;
    public static int COMMAND_FIELD_NUM = 1;
    //Member variable. This represents the maximum amount of fields that PcVue will have.
    //IMPORTANT: If you do by chance need to add additional functionality, you MUST expand this FIELD_NUM field
    private static int FIELD_NUM = 250;
    //This is a member variable to the VarexpMap, fieldMap. This is used to store and pass certain details of the VarexpVariable
    public Map<String, VarexpTuple> fieldMap = new LinkedHashMap<>();
    //The name of the table. This is limited to 12 options (BIT, CMD, ALA, ACM, TSH, ATS, REG, CTV, CNT, CHR, TXT ,CXT), etc. Refer to the SQL file for details
    protected String tableName;
    //This is the list you'll be using to keep track of what position a varexp variable uses;
    //TODO: Implement the associated method
    protected List<Integer> varexpPositionList = new ArrayList<Integer>();
    //return command and source positions
    //THis is used for the getVariableIdName. I'm too lazy to constantly type _variable_id over and over again
    protected String ID = "_variable_id";
    //This is the same varexpArrayList, but splitted based on ','
    private ArrayList<String> varexpArrayList = new ArrayList<>(FIELD_NUM);

    //return FIELD_NUM
    public static int getFieldNum() {
        return FIELD_NUM;
    }

    //return the "variable id name". This is needed for SQL queries and it varies by subclasses. It should return something like "acm_variable_id"
    public abstract String getVariableIdName();

    //Returns the varexp Array List
    public List<String> getVarexpList() {
        return this.varexpArrayList;
    }

    public ArrayList<List<String>> getArrayList() {
        return null;
    }

    //Abstract method to set the position numbers of a varexp variable (aka sub-variables)
    public List<Integer> getVarexpPositionList() {
        return this.varexpPositionList;
    }


    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    //This is the method all sub class will have to implement in order to get an array of position indexes
    abstract void setPositionList();

    abstract List<Integer> getPositionList();

    //abstract method for returning empty fields. This is needed as not all fields are used
    abstract String empty();

    // abstract method for joining. This is so you can insert a variable in a joined table
    abstract String getJoinCmd();

    //abstract method for getting commands to create table. You need this in order to create tables in a fresh install
    public abstract String createTableCmd();

    //abstract method to set the ArrayList of the varexpString
    public abstract void setArrayList(String varexpString, int dbIndex);

    //abstract method that sets up a variable. This function returns a Map that contains the Varexp Variable's Name
    // Along with its VarexpTuple (essetially, its important data such as widget Type, comoboBox choices, etc.
    public abstract Map<String, VarexpTuple> getFieldMap();

    //This function splits a varexp variable into its respective fields and then
    //set the varexpArrayPlist variable
    /*
        Todo:
        - split string based on fields
        - append more fields if the number of fields is less than FIELD_NUM
        - Create a tempList arraylist<String> and copy all the crap from above to it
        - set varexpLIst to the new temp list
     */
    protected void setVarexpArrayList(String varexpString) {

        String[] temp = varexpString.replace("[", "").replace("]", "").split(",");
        this.varexpArrayList.clear();
        if (temp.length < FIELD_NUM) {
            try {
                for (int i = 0; i < temp.length; i++) {
                    this.varexpArrayList.add(i, temp[i]);
                }
                //This will pad the variable with extra commas until it reaches the field number.
                //This will ensure that all variables have the exact number of fields as FIELD_NUM
                for (int j = temp.length + 1; j <= FIELD_NUM; j++) {
                    this.varexpArrayList.add(" ");
                }
            } catch (Exception e) {
                throw e;
            } finally {
            }
        }
    }

    public void print(String string) {
        System.out.println(string);
    }

    /*
    This method checks to see if an array is empty. You use this to check if a sub array have fields or not
     */
    public boolean isEmpty(String subArray) {
        Boolean isEmpty = true;

        //Traverse throguh the array, if everything in the array is of length 90, then that means it is empty
        String[] tempList = subArray.split(",");
        for (String s : tempList) {
            if (s.length() != 0) {
                isEmpty = !isEmpty;
                return isEmpty;
            }
        }
        return isEmpty;
    }

    //This method is to add the query to the database queue. However, ti does NOT execute the batch itesm.
    public void addToDatabaseQueue(ArrayList<List<String>> fileList, String databaseName, String tableName) {

    }


    /**
     * This function creates an empty varexpArrayList, initializes it with empty fields and returns it to the user.
     */
    public void initializeVarexpArray() {
        for (int i = 0; i <= FIELD_NUM; i++) {
            varexpArrayList.add(i, "");
        }
    }


}
