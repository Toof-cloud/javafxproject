import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserhomepageController {

    @FXML
    private Button csaccountbutton;

    @FXML
    private Button csactivitybutton;

    @FXML
    private Button cshomebutton;

    @FXML
    private Button csmessagesbutton;

    @FXML
    private Button csridebutton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Handles the account button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    @FXML
    private void handleAccountButton(ActionEvent event) throws IOException {
        // Load Useraccountpage.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Useraccountpage.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the activity button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    @FXML
    private void handleActivityButton(ActionEvent event) throws IOException {
        // Load Useractivitypage.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Useractivitypage.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the home button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    @FXML
    private void handleHomeButton(ActionEvent event) throws IOException {
        // Load Userhomepage.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Userhomepage.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the messages button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    @FXML
    private void handleMessagesButton(ActionEvent event) throws IOException {
        // Load Usermessagespage.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Usermessagespage.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the ride button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    @FXML
    private void handleRideButton(ActionEvent event) throws IOException {
        // Load Userridepage.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Userridepage.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}