import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateUserController {

    @FXML
    private TextField newUsernameField;

    @FXML
    private TextField newPasswordField;

    @FXML
    private Label currentuname;

    @FXML
    private Label currentpword;

    private User selectedUser;
    private HomeController homeController;

    /**
     * Sets the selected user and updates the current username and password labels.
     * 
     * @param user The selected user.
     */
    public void setUser(User user) {
        this.selectedUser = user;
        currentuname.setText(user.getUsername());
        currentpword.setText(user.getPassword());
    }

    /**
     * Sets the HomeController instance.
     * 
     * @param homeController The HomeController instance.
     */
    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    /**
     * Handles the update action. Updates the user's username and/or password.
     */
    @FXML
    private void handleUpdate() {
        String newUsername = newUsernameField.getText().trim();
        String newPassword = newPasswordField.getText().trim();

        if (!newUsername.isEmpty()) {
            selectedUser.setUsername(newUsername);
        }
        if (!newPassword.isEmpty()) {
            selectedUser.setPassword(newPassword);
        }

        if (DatabaseHandler.updateUser(selectedUser)) {
            homeController.displayUsers();
            Stage stage = (Stage) newUsernameField.getScene().getWindow();
            stage.close();
        } else {
            // Handle update failure
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to update user.");
            alert.showAndWait();
        }
    }
}