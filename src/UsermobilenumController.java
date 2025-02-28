import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class UsermobilenumController {

    // FXML UI elements
    @FXML
    private Button backbutton;

    @FXML
    private TextField csmobilenumtextfield;

    @FXML
    private TextField cspasswordtextfield;

    @FXML
    private Button loginbutton;

    @FXML
    private Button cssignupbutton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Static variable to store the customer ID
    public static String customerId;

    /**
     * Handles the back button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    @FXML
    void handleBackButton(ActionEvent event) throws IOException {
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
     * Handles the Sign Up button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    @FXML
    private void handlesignupbutton(ActionEvent event) throws IOException {
        // Load Usersignuppage1.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Usersignuppage1.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the login button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    @FXML
    void handleLoginButton(ActionEvent event) throws IOException {
        String mobileNumber = csmobilenumtextfield.getText();
        String password = cspasswordtextfield.getText();

        if (validateLogin(mobileNumber, password)) {
            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Login successful!");
            alert.showAndWait();

            // Load userhomepage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Userhomepage.fxml"));
            root = loader.load();

            // Set the customerId in the SessionManager
            SessionManager.setCustomerId(customerId);

            // Load stage and scene
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            // Show error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Invalid mobile number or password.");
            alert.showAndWait();
        }
    }

    /**
     * Validates the login credentials.
     * 
     * @param mobileNumber The mobile number.
     * @param password The password.
     * @return true if the credentials are valid, false otherwise.
     */
    private boolean validateLogin(String mobileNumber, String password) {
        Connection connection = DatabaseHandler.getDBConnection();
        String query = "SELECT C.Customer_id " +
                       "FROM CustomerTable C " +
                       "INNER JOIN ContactTable CT ON C.Customer_id = CT.Customer_id " +
                       "WHERE CT.ContactNum = ? AND C.Password = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, mobileNumber);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                customerId = resultSet.getString("Customer_id"); // Store the customer ID
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Gets the customer ID.
     * 
     * @return The customer ID.
     */
    public static String getCustomerId() {
        return customerId;
    }
}