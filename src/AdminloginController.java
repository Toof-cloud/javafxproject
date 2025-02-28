import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminloginController {

    @FXML
    private Button loginbuton;

    @FXML
    private Label passwordlabel;

    @FXML
    private TextField passwordtextfield;

    @FXML
    private Button returntouserbutton;

    @FXML
    private Label usernamelabel;

    @FXML
    private TextField usernametextfield;

    @FXML
    void loginbuttonhandler(ActionEvent event) {
        String username = usernametextfield.getText().trim();
        String password = passwordtextfield.getText().trim();

        if (DatabaseHandler.validateAdminLogin(username, password)) {
            // Show success alert
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");

            // Load Admintable.fxml
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Admintable.fxml"));
                Parent root = loader.load();

                // Get the current stage
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Set the scene to Admintable.fxml
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading the admin dashboard.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    @FXML
    void returntouserpagehandler(ActionEvent event) {
        try {
            // Load userfirstpage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userfirstpage.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene to userfirstpage.fxml
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading the user first page.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}