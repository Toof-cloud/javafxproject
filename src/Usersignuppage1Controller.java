import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Usersignuppage1Controller {

    // FXML UI elements
    @FXML
    private TextField csfirstnametextfield;

    @FXML
    private TextField cslastnametextfield;

    @FXML
    private TextField csemailtextfield;

    @FXML
    private TextField cscontactnumtextfield;

    @FXML
    private TextField csstreettextfield;

    @FXML
    private TextField cscitytextfield;

    @FXML
    private TextField cszipcodetextfield;

    @FXML
    private TextField cspasswordtextfield;

    @FXML
    private Button backbutton;

    @FXML
    private Button donebutton;

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
     * Handles the done button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    @FXML
    private void handleDoneButton(ActionEvent event) throws IOException {
        // Retrieve user input from text fields
        String firstName = csfirstnametextfield.getText();
        String lastName = cslastnametextfield.getText();
        String fullName = firstName + " " + lastName;
        String email = csemailtextfield.getText();
        String contactNumber = cscontactnumtextfield.getText();
        String street = csstreettextfield.getText();
        String city = cscitytextfield.getText();
        String zipCode = cszipcodetextfield.getText();
        String password = cspasswordtextfield.getText();

        // Create a new Customer object
        Customer customer = new Customer(fullName, email, contactNumber, street, city, zipCode, password);

        // Add the customer to the database
        if (DatabaseHandler.addCustomer(customer)) {
            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Customer added successfully!");
            alert.showAndWait();
        } else {
            // Show error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to add customer.");
            alert.showAndWait();
        }
    }
}