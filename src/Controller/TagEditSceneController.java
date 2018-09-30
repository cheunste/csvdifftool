package Controller;

import com.company.Database.dbConnector;
import com.company.pcvue.fields.*;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen on 8/25/2018.
 * <p>
 * This is a controller class that is responsible for setting up various textfields, combo boxes and other widget
 * listeners for the TagEidt.fxml. Sadly, there are a LOT of widgets on this page.
 * <p>
 * To future me, for the love of Arceus, break this down somehow
 */
public class TagEditSceneController {

    //Constants
    private final int COMMON_ITEM_LIMIT = 25;
    private final int PREF_SIZE_WIDTH = 225;

    private final String VARIABLE_TYPE_COMBOBOX = "1";
    private final String SOURCE_OF_VARIABLE_COMBOBOX = "17";

    //List of new TExtInput objects
    //Member variables
    private String databaseName;
    private String TEXT_FIELD = "TF";
    private String COMBO_BOX = "CB";

    private ObservableList<String> tagList;
    private Map<String, String> idMap;
    private Stage currentWindow;
    //List of either textField or comboBoxes. You'll need these for reference
    private List<TextInput> commonTextFieldList = new ArrayList<>();
    private List<ComboBox> commonComboBoxList = new ArrayList<>();

    private List<TextInput> commandTextFieldList = new ArrayList<>();
    private List<ComboBox> commandComboBoxList = new ArrayList<>();

    private List<TextInput> sourceTextFieldList = new ArrayList<>();
    private List<ComboBox> sourceComboBoxList = new ArrayList<>();

    private int DOUBLE_CLICK = 2;

    //FXML widgets
    @FXML
    private ListView TagListView;
    @FXML
    private GridPane commonViewLeftGridPane;
    @FXML
    private GridPane commonViewRightGridPane;

    @FXML
    private VBox SourceView;
    @FXML
    private VBox CommandView;
    @FXML
    private Button btnGetInputText;
    @FXML
    private TextField TagFilterField;

    @FXML
    private GridPane CommandGridPane;
    @FXML
    private GridPane SourceGridPane;


    /**
     * Constructor. Takes a name of the Database
     *
     * @param databaseName
     */
    public TagEditSceneController(String databaseName) {
        this.databaseName = databaseName;
        idMap = new HashMap<String, String>();

        tagList = FXCollections.observableArrayList();
    }

    @FXML
    /**
     * The intialize function. Sets up widgets and listeners. Pretty much the entire purpose fo why this class exists
     */
        /*
    TODO:
        In the initialize function, the goal here is to
        7) Highlight the first item of the listview. If there are none, then you have to handle it
        8) display the appropriate information
        9) Imiplement the text filter function

         */
    void initialize() {
        //Before doing anything, get the size of the DB and see if there are any elements in it.
        //If there are no elements, you're working with a new project/DB
        if (getTableSize() > 0) {
            //Get the tagnames and load them to observable list
            getVariableNames();

            //Set up the filter
            filterSetUp();

            //Highlight first item
            TagListView.getFocusModel().focus(0);

            //Populate the GUI with the First item
            //TODO: implement this
        }

        //set up onclick listener for the items in the tagLIstVIew
        //Unsure if this needs to be separated...for now

        TagListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == DOUBLE_CLICK) {
                    System.out.println(TagListView.getSelectionModel().getSelectedItems());
                    //For whatever reason, the String comes with square brackets. Probably of how you implement
                    //The DB function
                    String temp = TagListView.getSelectionModel().getSelectedItems().toString().replace("[", "").replace("]", "");
                    String id = idMap.get(temp);
                    System.out.println(id);

                    //At this point, call a function to populate the GUI
                    populateTagValues(id);
                }
            }
        });

        //Here we will generate 40 new TextINput objects
        Common common = new Common();

        //Call the Common Varexp's fieldMap.
        Map<String, VarexpTuple> commonMap = common.getFieldMap();

        //Traverse its map for its keys and then using the key values, create text input as well as their labels.
        //Note that the fxid is the varexp position
        int columnCount = 0;

        //This loop populates the GUI widget to the GUI itself. Can't really be in a separate function since
        //The Common Field is so big that it needs to take up two columns
        for (String key : commonMap.keySet()) {
            String widgetType = (String) commonMap.get(key).getWidgetType();
            int position = (Integer) commonMap.get(key).getPosition();
            Boolean visible = (Boolean) commonMap.get(key).getVisibility();
            //Create text Field items for common variable fields
            if (widgetType.equals(TEXT_FIELD) && visible) {
                TextInput input = new TextInput(
                        "" + position,
                        new Label(key),
                        new JFXTextField(),
                        (Integer) commonMap.get(key).getPosition(),
                        commonMap.get(key)
                );
                if (columnCount < COMMON_ITEM_LIMIT) {
                    //Now, add the new TextInput Label and TExtField to the GridPane
                    commonViewLeftGridPane.add(input.getLabel(), 0, columnCount);
                    commonViewLeftGridPane.add(input.getTextField(), 1, columnCount);
                } else {
                    //Now, add the new TextInput Label and TExtField to the GridPane
                    commonViewRightGridPane.add(input.getLabel(), 0, columnCount - COMMON_ITEM_LIMIT);
                    commonViewRightGridPane.add(input.getTextField(), 1, columnCount - COMMON_ITEM_LIMIT);
                }
                //Finaly, add the input to the list so they can be retrieved later using the input's Name
                commonTextFieldList.add(input);
            }
            //Create Combobox Item for common variable fields
            else if (widgetType.equals(COMBO_BOX) && visible) {
                ComboBox cb = new ComboBox(
                        "" + position,
                        new Label(key),
                        new JFXComboBox(),
                        (Integer) commonMap.get(key).getPosition(),
                        commonMap.get(key)
                );
                if (columnCount < COMMON_ITEM_LIMIT) {
                    //Now, add the new TextInput Label and TExtField to the GridPane
                    commonViewLeftGridPane.add(cb.getLabel(), 0, columnCount);
                    commonViewLeftGridPane.add(cb.getComboBox(), 1, columnCount);
                } else {
                    //Now, add the new TextInput Label and TExtField to the GridPane
                    commonViewRightGridPane.add(cb.getLabel(), 0, columnCount - COMMON_ITEM_LIMIT);
                    commonViewRightGridPane.add(cb.getComboBox(), 1, columnCount - COMMON_ITEM_LIMIT);
                }
                //Finaly, add the input to the list so they can be retrieved later using the input's Name
                commonComboBoxList.add(cb);
            }
            columnCount++;
        }

        //Now load the variable's common fields and set up the listeners. This should be a function as you'll need to call it everytime
        //another variable is clicked on the listview
        //TODO: Implement this
        setUpCommonVariableListener();

        //Highlight the first item in the tagList observableList...if it isn't empty
        //TODO: Implement this

        //Get the ID from of the Variable in the tagList
        //TODO: Implement this

        //Fetch the data from the DB
        //TODO: Implement this

        //Populate the data in the GUI
        //TODO: Implement this

        //Now using the variable's command and source field, load the Source and Command textfields
        //TODO: Implement this

        //Finally, load the Source and Command field parameters
        //TODO: Implement this
    }

    /**
     * This function fetches the values from the tables in the MySQL DB and then
     * compiles it to an array. That array is then looped and its values are filled into the table
     *
     * @param id This is the ID of the tag. This ID is from the MySQL DB and it will be used to call tables from the DB
     */
    /*
    TODO:
        1) Get the Common Variable data from the DB using the id
        2) from 1) Get the source and command field name of the Common Varexp Variable
        3) Create a factory for the source and the command and using the tablename and variableIdName, query their tables
        4) Pass the three noted resultStrings into a method and reconstruct the Varexp Variable array in this method

        5) Compile the returned data from the three tables into an array
        6) Set a pointer called "currentValue" to this array.
        You will create a "newValue" pointer when the save button is hit, but that's much MUCH later in the project
        7) Each of the textfields or combobox has an unique ID that's associated with the position of the VarexpArray
        HOWEVER, remember that "user display text" is different than the value of the variable

     */
    private void populateTagValues(String id) {
        //TODO: Verify how much you can leverage out of the Export function
        dbConnector connector = new dbConnector();

        String commonResult = connector.sqlQuery(databaseName, "SELECT * FROM COMMON WHERE variable_id=" + id + ";");
        String[] commonStringArray = commonResult.split(",");
        String commandVariable = commonStringArray[Integer.parseInt(VARIABLE_TYPE_COMBOBOX)];
        String sourceVariable = commonStringArray[Integer.parseInt(SOURCE_OF_VARIABLE_COMBOBOX)];

        VarexpFactory factory = new VarexpFactory();

        /***************COMMAND***************/
        VarexpVariable command = factory.declareNewVariable(commandVariable);
        String commandResult = connector.sqlQuery(databaseName, "SELECT * FROM " + command.getTableName() + " WHERE " +
                command.getVariableIdName() +
                "=" + id + ";");
        String[] commandStringArray = commandResult.split(",");

        /***************SOURCE***************/
        VarexpVariable source = factory.declareNewVariable(sourceVariable);
        String sourceResult = connector.sqlQuery(databaseName, "SELECT * FROM " + source.getTableName() + " WHERE " +
                source.getVariableIdName() +
                "=" + id + ";");
        String[] sourceStringArray = sourceResult.split(",");


        List<String> reconstructVarexpArray = VarexpReconstructor.reconstructVarexpArray(commonStringArray, sourceStringArray, commandStringArray);
        String temp = VarexpReconstructor.ListToString(reconstructVarexpArray);
        System.out.println(temp);
    }

    /**
     * This function is used to update the tag that's already in the DB
     *
     * @param id       The id of the tag in the DB
     * @param newValue The new VarexpArray that will be updated
     */
    private void updateTagValue(String id, String newValue) {

    }

    private int getTableSize() {
        dbConnector connector = new dbConnector();
        String rowNum = connector.getTableSize(databaseName);
        return Integer.parseInt(rowNum);
    }

    public void setCurrentWindow(Stage stage) {
        this.currentWindow = stage;
    }

    public void saveCurrentTag() {

    }

    //This function gets the variable names and their ID given the database (set in constructor) and formats it.
    // It returns a void as it will be set to a member variable dictionary (You'll need this when making edits)
    private void getVariableNames() {
        dbConnector conn = new dbConnector();
        //Form the variable name
        String getTagNameCmd = "select variable_id,1st_element, 2nd_element, 3rd_element,4th_element,5th_element,6th_element,7th_to_12th from common;";
        ArrayList<ArrayList<String>> temp;

        //Query the tag name in the selected DB
        temp = conn.readDatabase(databaseName, getTagNameCmd);

        //Traverse the arraylist
        for (ArrayList<String> item : temp) {
            int arraySize = item.size();
            String tagName = "";
            for (int i = 1; i < arraySize; i++) {
                tagName += item.get(i) + ".";
            }
            //This line is a  way of removing multiple ".." from the end of the String. I hope you know regex...Java regex
            tagName = tagName.replaceAll("\\.{1,}\\Z", "");
            idMap.put(tagName, item.get(0));
            tagList.add(tagName);
        }
    }

    /**
     * This function sets up the filtering function for the tagList. However, it does NOT use any regex or wildcard characters
     */
    private void filterSetUp() {
        FilteredList<String> filteredData = new FilteredList<>(tagList, s -> true);
        TagListView.setItems(filteredData);

        TagFilterField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchTerm = TagFilterField.getText();
            if (searchTerm == null || searchTerm.length() == 0) {
                filteredData.setPredicate(s -> true);
            } else {
                filteredData.setPredicate(s -> s.contains(searchTerm));
            }
        });
    }

    //This function fills out the comboBox Choices for the Common Varexp Variable
    private void fillComboBoxChoices(List<ComboBox> comboBoxList) {
        for (ComboBox comboBoxItems : comboBoxList) {
            for (String choice : (String[]) comboBoxItems.getTupleData().getUserText()) {
                comboBoxItems.addDropDownChoice(choice);
            }
        }
    }

    //This function sets up listeners to all the Common Variable's textfield and combo boxes
    //For textfields, you need to either check to see if a value is within the value limit or if it is within the byte limit
    // Not all textfields have a value limit, if it doesn't have a value limit, then go with the byte limit
    private void setUpCommonVariableListener() {
        setTextFieldListener(commonTextFieldList);
        fillComboBoxChoices(commonComboBoxList);
        for (ComboBox comboBoxInput : commonComboBoxList) {
            comboBoxInput.getComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                    System.out.println(comboBoxInput.getName() + " " + newValue);

                    String[] userDisplayText = (String[]) comboBoxInput.getTupleData().getUserText();
                    String[] valueStringArray = (String[]) comboBoxInput.getTupleData().getValueOptions();

                    //Find the index of the combobox that the user selected
                    int index = -1;
                    for (int i = 0; i < userDisplayText.length; i++) {
                        if (userDisplayText[i].equals(newValue)) {
                            index = i;
                            break;
                        }
                    }
                    String comboBoxValueItem = valueStringArray[index];

                    if (comboBoxInput.getName().equals(VARIABLE_TYPE_COMBOBOX)) {
                        VarexpFactory factory = new VarexpFactory();
                        VarexpVariable commandVariable = factory.declareNewVariable(comboBoxValueItem);
                        populateNonCommonVariables(commandVariable.getFieldMap(), CommandGridPane, commandTextFieldList, commandComboBoxList, "Command: " + newValue);
                    }
                    if (comboBoxInput.getName().equals(SOURCE_OF_VARIABLE_COMBOBOX)) {
                        VarexpFactory factory = new VarexpFactory();
                        VarexpVariable sourceVariable = factory.declareNewVariable(comboBoxValueItem);
                        populateNonCommonVariables(sourceVariable.getFieldMap(), SourceGridPane, sourceTextFieldList, sourceComboBoxList, "Source: " + newValue);
                    }
                }
            });
        }
    }

    /**
     * This method sets up listeners to textfields. Note that there is priority with this
     * <p>
     * The method takes the 'value limit' of the variable first and determines if it is out of range.
     * Not all Varexp Variables that uses a textfield have a 'value limit' with their variable, and in this case
     * the byte size of the variable will be used to see if it is out of range or not
     * <p>
     * Method will highlight a textfield **YELLOW** if it is out of the value scope of a variable
     * Method will highlight a textfield **RED** if it is the wrong type (ie using a char for an int field, or vice versa)
     * <p>
     * This method will also remove the CSS classes applied when the text clears
     *
     * @param textFieldList List of TExtInput classes
     */
    private void setTextFieldListener(List<TextInput> textFieldList) {
        for (TextInput textFieldInput : textFieldList) {

            //These two lines determine the value limit of a text field...if available. Some fields isn't given a value limit
            //But instead a Byte limit. However, this should take priority, if it exists
            String[] valueLimit = (String[]) textFieldInput.getTupleData().getValueOptions();
            int valueLimitLength = valueLimit.length;

            //This is to get the byteLimit. Almost all variables have a byte limit. If it is zero, then it is most likely
            //A reserved variable and that shouldn't be touched in the first place
            int byteLimit = (int) textFieldInput.getTupleData().getByteSize();

            textFieldInput.getTextField().textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    System.out.println(textFieldInput.getName() + " " + newValue);
                    if (newValue.length() > 0) {
                        if (valueLimitLength > 1) {
                            int lowerLimit = Integer.parseInt(valueLimit[0]);
                            int upperLimit = Integer.parseInt(valueLimit[1]);
                            int newValueInt = 0;
                            try {
                                newValueInt = Integer.parseInt(newValue);
                                //Tests to see if the number is within the value limits
                                if (newValueInt < lowerLimit || newValueInt > upperLimit) {
                                    System.out.println("Warning: Limit exceeded for: " + textFieldInput.getLabel().getText());
                                    textFieldInput.addWarning();
                                } else {
                                    System.out.println("Within limit. Remove class");
                                    textFieldInput.clear();
                                }
                            }
                            //Reaching here means that a user entered in letters in a TF meant for numbers
                            catch (NumberFormatException e) {
                                textFieldInput.clear();
                                textFieldInput.addError();
                            }
                        }
                        //For length verification
                        else {
                            int newValueLength = newValue.length();
                            if (newValueLength > byteLimit) {
                                System.out.println("Warning: Byte Limit exceeded: " + textFieldInput.getLabel().getText());
                                textFieldInput.addWarning();
                            } else {
                                System.out.println("Within byte limit");
                                textFieldInput.clear();
                            }
                        }
                    }
                    //Reaching here means the textfield itself is empty. Nothing should be thrown here
                    else if (newValue.length() <= 0) {
                        System.out.println("Empty Field " + textFieldInput.getLabel().getText());
                        textFieldInput.clear();
                    }
                }
            });
        }
    }

    /**
     * This method will populate all non Common Variables into the GUI
     *
     * @param fieldMap  the Map variable of a Varexp Varialbe. THis is retrieved from a VarexpVariable's getFieldMap()
     * @param pane      This is a GridPane. This should either be commonViewLeftGridPane, commonViewRightGridPane, SourceGridPane or CommandGridPane
     * @param tfList    A list of TextInput classes. Class is defined as an inner class here
     * @param cbList    A list of ComboBox Classes. Class is defined as an inner class here
     * @param labelText This is the text that will be displayed
     */
    private void populateNonCommonVariables(Map<String, VarexpTuple> fieldMap, GridPane pane, List<TextInput> tfList, List<ComboBox> cbList, String labelText) {

        //Traverse its map for its keys and then using the key values, create text input as well as their labels.
        //Note that the fxid is the varexp position
        int columnCount = 1;

        //Removes everything from the GridPane. Chances are if you've reached here, you need to populate new items into
        //a gridpane

        //Remove all items from the GridPane
        pane.getChildren().clear();

        //Removes all items from the (Souce or COmmand) TextViewList and ComboBoxList
        tfList.clear();
        cbList.clear();

        //Create a header label. This will tell the user what Command or Source Variable they're working on
        Label headerLabel = new Label(labelText);
        headerLabel.getStyleClass().add("header");
        pane.add(headerLabel, 0, 0);


        for (String key : fieldMap.keySet()) {
            String widgetType = (String) fieldMap.get(key).getWidgetType();
            int position = (Integer) fieldMap.get(key).getPosition();
            Boolean visible = (Boolean) fieldMap.get(key).getVisibility();

            if (widgetType.equals(TEXT_FIELD) && visible) {
                TextInput input = new TextInput(
                        "" + position,
                        new Label(key),
                        new JFXTextField(),
                        (Integer) fieldMap.get(key).getPosition(),
                        fieldMap.get(key)
                );

                pane.add(input.getLabel(), 0, columnCount);
                pane.add(input.getTextField(), 1, columnCount);
                tfList.add(input);
            }
            //Create Combobox Item for common variable fields
            else if (widgetType.equals(COMBO_BOX) && visible) {
                ComboBox cb = new ComboBox(
                        "" + position,
                        new Label(key),
                        new JFXComboBox(),
                        (Integer) fieldMap.get(key).getPosition(),
                        fieldMap.get(key)
                );
                pane.add(cb.getLabel(), 0, columnCount);
                pane.add(cb.getComboBox(), 1, columnCount);
                cbList.add(cb);
            }
            columnCount++;
        }

        //Populate the comboboxes choices
        fillComboBoxChoices(cbList);

        // Apply text field listeners to all the textfields
        setTextFieldListener(tfList);
    }

    //This is a public inner class  to create a textfield item along with its assocated label
    class TextInput {
        private final String name;
        private final Label label;
        private final JFXTextField textField;
        private final VarexpTuple tupleData;

        public TextInput(String name, Label label, JFXTextField textField, int varexpId, VarexpTuple tupleData) {
            this.name = name;
            this.label = label;
            this.textField = textField;
            this.textField.setId(String.valueOf(varexpId));
            this.textField.setPrefWidth(PREF_SIZE_WIDTH);
            this.tupleData = tupleData;
        }

        public String getName() {
            return name;
        }

        public Label getLabel() {
            return label;
        }

        public TextField getTextField() {
            return textField;
        }

        public VarexpTuple getTupleData() {
            return tupleData;
        }

        //This method clears the CSS highlight for the textfield
        public void clear() {
            textField.getStyleClass().removeAll("error", "warning");
        }

        public void addError() {
            textField.getStyleClass().add("error");
        }

        public void addWarning() {
            textField.getStyleClass().add("warning");
        }
    }

    /**
     * Public Class that creates a Combo Box group (consists of label, name, combo box and id for combo box)
     */
    class ComboBox {
        private final String name;
        private final Label label;
        private final JFXComboBox comboBox;
        private final VarexpTuple tupleData;

        public ComboBox(String name, Label label, JFXComboBox comboBox, int varexpId, VarexpTuple tupleData) {
            this.name = name;
            this.label = label;
            this.comboBox = comboBox;
            this.comboBox.setId(String.valueOf(varexpId));
            this.comboBox.setPrefWidth(PREF_SIZE_WIDTH);
            this.tupleData = tupleData;
        }

        public String getName() {
            return name;
        }

        public Label getLabel() {
            return label;
        }

        public JFXComboBox getComboBox() {
            return comboBox;
        }

        public void setComboBox() {
        }

        public void addDropDownChoice(String choice) {
            comboBox.getItems().add(choice);
        }

        public VarexpTuple getTupleData() {
            return tupleData;
        }
    }
}
