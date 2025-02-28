import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserfirstpageController {

    // FXML UI elements
    @FXML
    private Button userloginbutton;

    @FXML
    private Button usersignupbutton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Handles the user login button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    @FXML
    private void handleUserLoginButton(ActionEvent event) throws IOException {
        // Load userloginpage1.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("userloginpage1.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the user signup button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    @FXML
    private void handleUserSignupButton(ActionEvent event) throws IOException {
        // Load usersignuppage1.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("usersignuppage1.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the user admin button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    @FXML
    private void handleAdminButton(ActionEvent event) throws IOException {
        // Load Adminlogin.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Adminlogin.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}