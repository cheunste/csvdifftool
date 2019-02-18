package Controller;

import VarexpInterface.pcvue.fields.VarexpFactory;
import VarexpInterface.pcvue.fields.VarexpTuple;
import VarexpInterface.pcvue.fields.VarexpVariable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/*

Controller to the Varexp Filter Scene.

This class is the controller that will allow users to filter out certain Varexp Fields before comparing
the old PcVue config and the new PcVue configs

 */
public class VarexpFilterSceneController implements Initializable {

    Stage currentWindow;

    @FXML
    private JFXButton saveBtn;
    @FXML
    private JFXButton cancelBtn;
    @FXML
    private JFXButton clearBtn;
    private final int OFFSET = 1;

    //Member variables. All private
    private final int COLUMN_NUM = 4;
    private final int ITEMS_PER_COLUMN = VarexpVariable.getFieldNum() / COLUMN_NUM;
    private final int FIELD_NUM_INDEX = 1;
    private final int DESCRIPTION_INDEX = 2;
    private final int CHECK_BOX_INDEX = 3;
    @FXML
    private JFXButton selectAllBtn;

    //Grid panes
    @FXML
    private GridPane col1;
    @FXML
    private GridPane col2;
    @FXML
    private GridPane col3;
    @FXML
    private GridPane col4;

    //Scroll Pane
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private HBox columnContainer;

    //This is an ObservableList for filtering

    /**
     * This method initializes the GUI  by setting up button actions, event listeners, etc.
     * <p>
     * More importantly, it also creates a HBox that stores a varexp field num, field name and a check box
     * <p>
     * Should look something like this
     * || Field Number || Field Name || Filter ||
     * || 1            || [Name]     ||   []   ||
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        saveBtn.setOnAction(e -> {
        });
        cancelBtn.setOnAction(e -> {
        });
        clearBtn.setOnAction(e -> {
        });
        selectAllBtn.setOnAction(e -> {
        });

        //Set up all the varexp field HBoxes
        /*
        TODO: THe following
        1) Get the field number as well as field name
        2) Create a HBox
        3) Stuff the HBoxes into the Grid View. Note that there are four columns, so divide them equally
        4) Have a way to map and store all of the filters into the property file...or another file
         */

        /*
        Todo: Things to think about
        - How would you fetch both the id and the field description?
        - How would you actually ignore certain fields and make SQL queries more dynamic and less coupled?
        - Recall that there are multiple tables that can share the same field names.

        -How to export this into a result file?
         */

        /*
        TODO: Tests to think about:
        - Missing tags. How would you even match this up? After all, the ids become diferent
        - Missing field content (Like in the old varexp has 1.0 but new varexp has nothing) or vice versa
        - Missing
         */


        //TODO: Set up an observable list
        /*
        TODO: Think about filtering.
        - You need to filter all of the columns
        - Retain the checkboxes (this is kinda tricky as I'm assuming you'll have to recreate the column views after an onclick is executed);
        - You need an observablist List. But what exactly are you adding into this list? The map you get from the call to VarexpVariable? Or something more?
        - Do you ACTUALLY need this? Or are you trying to satisfy your ego? After all, this is most likely a one time deal

         */

        ObservableList<Map<String, VarexpTuple>> data = FXCollections.observableArrayList();
        ObservableList<Map<String, VarexpTuple>> sortedData = FXCollections.observableArrayList();

        //Scroll pane set up
        scrollPane.setContent(columnContainer);

        /*
         * Create a varexp factory,
         * Use it get generate all the fields and have said field call the getFieldMap()
         */
        VarexpFactory factory = new VarexpFactory();

        int columnCount = 0;
        int temp = 0;

        for (String variable : factory.listOfTables) {
            Map<String, VarexpTuple> variableFieldMap = factory.declareNewVariable(variable).getFieldMap();
            data.add(variableFieldMap);

            for (String key : variableFieldMap.keySet()) {
                //Get the position
                int position = (Integer) variableFieldMap.get(key).getPosition();

                Label fieldNum = new Label(Integer.toString(position));
                fieldNum.setTextAlignment(TextAlignment.LEFT);

                Label fieldDescription = new Label(key);
                fieldDescription.setTextAlignment(TextAlignment.CENTER);

                JFXCheckBox filterCheckbox = new JFXCheckBox();
                filterCheckbox.setAlignment(Pos.CENTER_LEFT);


                //Add all the above widgets into the VBOXes
                if (columnCount <= ITEMS_PER_COLUMN) {

                    System.out.println("Column 1: " + columnCount + " : " + key);
                    //col1.add(filterHBox,1,columnCount);
                    col1.add(fieldNum, FIELD_NUM_INDEX, columnCount);
                    col1.add(fieldDescription, DESCRIPTION_INDEX, columnCount);
                    col1.add(filterCheckbox, CHECK_BOX_INDEX, columnCount);
                } else if (columnCount <= (2 * ITEMS_PER_COLUMN) &&
                        columnCount > ITEMS_PER_COLUMN

                ) {
                    System.out.println("Column 2: " + columnCount + " : " + (columnCount - ITEMS_PER_COLUMN) + " : " + key);

                    //col2.add(filterHBox,1,0);
                    col2.add(fieldNum, FIELD_NUM_INDEX, columnCount - (ITEMS_PER_COLUMN) - OFFSET);
                    col2.add(fieldDescription, DESCRIPTION_INDEX, columnCount - (ITEMS_PER_COLUMN) - OFFSET);
                    col2.add(filterCheckbox, CHECK_BOX_INDEX, columnCount - (ITEMS_PER_COLUMN) - OFFSET);
                } else if (columnCount <= (3 * ITEMS_PER_COLUMN) &&
                        columnCount > (2 * ITEMS_PER_COLUMN)
                ) {
                    System.out.println("Column 3: " + columnCount + " : " + (columnCount - (2 * ITEMS_PER_COLUMN)) + " : " + key);
                    //col3.add(filterHBox,1,0);

                    col3.add(fieldNum, FIELD_NUM_INDEX, columnCount - (2 * ITEMS_PER_COLUMN) - OFFSET);
                    col3.add(fieldDescription, DESCRIPTION_INDEX, columnCount - (2 * ITEMS_PER_COLUMN) - OFFSET);
                    col3.add(filterCheckbox, CHECK_BOX_INDEX, columnCount - (2 * ITEMS_PER_COLUMN) - OFFSET);
                } else {
                    System.out.println("Column 4: " + columnCount + " : " + (columnCount - (3 * ITEMS_PER_COLUMN)) + " : " + key);
                    //col4.add(filterHBox,1,0);
                    col4.add(fieldNum, FIELD_NUM_INDEX, columnCount - (3 * ITEMS_PER_COLUMN) - OFFSET);
                    col4.add(fieldDescription, DESCRIPTION_INDEX, columnCount - (3 * ITEMS_PER_COLUMN) - OFFSET);
                    col4.add(filterCheckbox, CHECK_BOX_INDEX, columnCount - (3 * ITEMS_PER_COLUMN) - OFFSET);
                }
                columnCount++;
            }
        }

        System.out.println(data);
    }

    //This is used to keep track of prvevious stages so I can close them later
    public void setCurrentWindow(Stage window) {
        this.currentWindow = window;
    }


    //function that clears up the Observable Array
    private void dataCleanup() {

    }
}
