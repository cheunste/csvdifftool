package Controller;

import VarexpInterface.pcvue.fields.VarexpFactory;
import VarexpInterface.pcvue.fields.VarexpTuple;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
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


        /*
         * Create a varexp factory,
         * Use it get generate all the fields and have said field call the getFieldMap()
         */
        VarexpFactory factory = new VarexpFactory();

        for (String variable : factory.listOfTables) {
            Map<String, VarexpTuple> variableFieldMap = factory.declareNewVariable(variable).getFieldMap();

            for (String key : variableFieldMap.keySet()) {
                //Get the position
                int position = (Integer) variableFieldMap.get(key).getPosition();
                HBox filterHBox = new HBox();

                Label fieldNum = new Label(Integer.toString(position));
                fieldNum.setTextAlignment(TextAlignment.LEFT);

                Label fieldDescription = new Label(key);

                JFXCheckBox filterCheckbox = new JFXCheckBox();
                filterCheckbox.setAlignment(Pos.CENTER);

                //Add all the above widgets into the HBox
                filterHBox.getChildren().addAll(fieldNum, fieldDescription, filterCheckbox);
                //Add the Hbox onto the GridPane
            }

        }


    }

    //This is used to keep track of prvevious stages so I can close them later
    public void setCurrentWindow(Stage window) {
        this.currentWindow = window;
    }
}
