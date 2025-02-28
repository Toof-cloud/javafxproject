import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UseraccountpageController {

    @FXML
    private Button csaccountbutton;

    @FXML
    private Button csactivitybutton;

    @FXML
    private Button cshomebutton;

    @FXML
    private Button csmessagesbutton;

    @FXML
    private Button userchangeinfobutton;

    @FXML
    private Label csfullname;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void initialize() {
        String customerId = UsergoogleloginController.customerId; // Get the customer ID from the login controller

        if (customerId != null) {
            try {
                String fullName = DatabaseHandler.getCustomerFullName(customerId);
                if (fullName != null) {
                    csfullname.setText(fullName);
                } else {
                    csfullname.setText("Customer not found");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                csfullname.setText("Error retrieving customer name");
            }
        } else {
            csfullname.setText("Customer ID not found");
        }
    }

    /**
     * Handles the account button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    @FXML
    private void handleAccountButton(ActionEvent event) throws IOException {
        // Load accountpage.fxml
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
        // Load activitypage.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Useractivitypage.fxml"));
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
    private void handleuserchangeinfobutton(ActionEvent event) throws IOException {
        // Load activitypage.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Usereditinfopage.fxml"));
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
        // Load homepage.fxml
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
        // Load messagespage.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Usermessagespage.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}