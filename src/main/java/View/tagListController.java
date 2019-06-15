package View;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * This controller class isn't for a particular FXML file, but for the tagList in the tagEdit.fxml itself
 */
public class tagListController {

    private ListView TagListView;
    private TextField TagFilterField;

    /**
     * Constructor
     *
     * @param tagList This is the tagList reference passed from the TagEditSceneController class
     */
    public tagListController(ObservableList<String> tagList) {

    }

}
