import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Userloginpage1Controller {

    @FXML
    private Button backbutton;

    @FXML
    private Button continuewithgooglebutton;

    @FXML
    private Button continuewithmobnum;

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Handles the back button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        // Load Userfirstpage.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Userfirstpage.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the continue with Google button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    @FXML
    private void handleContinueWithGoogleButton(ActionEvent event) throws IOException {
        // Load usergooglelogin.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("usergooglelogin.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the continue with Mobile Number button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    @FXML
    private void handleContinueWithMobileNumButton(ActionEvent event) throws IOException {
        // Load usermobilenumlogin.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Usermobilenumlogin.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}