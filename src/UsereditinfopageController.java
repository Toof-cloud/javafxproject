import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class UsereditinfopageController implements Initializable {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/moveit";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Blk3Llot19Lessandra";

    // FXML UI elements
    @FXML private Button customerlogoutbutton;
    @FXML private TextField changepasswordtextfield;
    @FXML private Button editCustomerButton;
    @FXML private TextField editcitytextfield;
    @FXML private TextField editemailtextfield;
    @FXML private TextField editnametextfield;
    @FXML private TextField editphonetextfield;
    @FXML private TextField editstreettextfield;
    @FXML private TextField editziptextfield;
    @FXML private Button returntoaccountbutton;
    @FXML private Button saveCustomerButton;

    private boolean isEditing = false;
    private String customerID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get the customer ID and load customer information
        customerID = getCustomerId();
        loadCustomerInfo();
        setEditableFields(false); // Disable editing by default
    }

    // Retrieve the customer ID from the appropriate controller
    private String getCustomerId() {
        String id = UsermobilenumController.getCustomerId();
        return (id != null) ? id : UsergoogleloginController.getCustomerId();
    }

    // Load customer information from the database
    private void loadCustomerInfo() {
        String query = "SELECT CustomerFullName, City, Zip, Street, ContactNum, Email FROM CustomerTable "
                     + "JOIN LocationTable ON CustomerTable.Customer_id = LocationTable.Customer_id "
                     + "JOIN ContactTable ON CustomerTable.Customer_id = ContactTable.Customer_id "
                     + "WHERE CustomerTable.Customer_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, customerID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                populateCustomerFields(rs);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading customer information.");
        }
    }

    // Populate the UI fields with customer information
    private void populateCustomerFields(ResultSet rs) throws SQLException {
        editnametextfield.setText(rs.getString("CustomerFullName"));
        editcitytextfield.setText(rs.getString("City"));
        editziptextfield.setText(rs.getString("Zip"));
        editstreettextfield.setText(rs.getString("Street"));
        editphonetextfield.setText(rs.getString("ContactNum"));
        editemailtextfield.setText(rs.getString("Email"));
    }

    // Handle the "Edit" button click event
    @FXML
    void handleEditCustomer(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Edit");
        alert.setHeaderText("Are you sure you want to edit your information?");
        alert.setContentText("Click OK to proceed.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                isEditing = !isEditing; // Toggle editing mode
                setEditableFields(isEditing);
            }
        });
    }

    // Handle the "Save" button click event
    @FXML
    void handleSaveCustomer(ActionEvent event) {
        if (!isEditing) return;

        // Show password confirmation dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Password Confirmation");
        dialog.setHeaderText("Please enter your password to save changes.");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        GridPane grid = new GridPane();
        grid.add(new Label("Password:  "), 0, 0);
        grid.add(passwordField, 1, 0);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return passwordField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(password -> {
            if (verifyPassword(password)) {
                saveCustomerInfo();
                changePasswordIfNeeded();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Incorrect password. Changes not saved.");
            }
        });
    }

    // Verify the entered password against the stored password
    private boolean verifyPassword(String password) {
        String query = "SELECT Password FROM CustomerTable WHERE Customer_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, customerID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("Password").equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Save the updated customer information to the database
    private void saveCustomerInfo() {
        String newFullName = editnametextfield.getText().trim();
        String newCity = editcitytextfield.getText().trim();
        String newZip = editziptextfield.getText().trim();
        String newStreet = editstreettextfield.getText().trim();
        String newContact = editphonetextfield.getText().trim();
        String newEmail = editemailtextfield.getText().trim();

        String updateCustomerQuery = "UPDATE CustomerTable SET CustomerFullName = ? WHERE Customer_id = ?";
        String updateLocationQuery = "UPDATE LocationTable SET City = ?, Zip = ?, Street = ? WHERE Customer_id = ?";
        String updateContactQuery = "UPDATE ContactTable SET ContactNum = ?, Email = ? WHERE Customer_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement updateCustomer = conn.prepareStatement(updateCustomerQuery);
             PreparedStatement updateLocation = conn.prepareStatement(updateLocationQuery);
             PreparedStatement updateContact = conn.prepareStatement(updateContactQuery)) {

            updateCustomerInfo(updateCustomer, newFullName);
            updateLocationInfo(updateLocation, newCity, newZip, newStreet);
            updateContactInfo(updateContact, newContact, newEmail);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer information updated successfully.");
            setEditableFields(false);
            isEditing = false;

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating information.");
        }
    }

    // Change the password if a new password is entered
    private void changePasswordIfNeeded() {
        String newPassword = changepasswordtextfield.getText().trim();
        if (!newPassword.isEmpty()) {
            String updatePasswordQuery = "UPDATE CustomerTable SET Password = ? WHERE Customer_id = ?";

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement updatePassword = conn.prepareStatement(updatePasswordQuery)) {

                updatePassword.setString(1, newPassword);
                updatePassword.setString(2, customerID);
                updatePassword.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Password updated successfully.");
                changepasswordtextfield.clear();

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating the password.");
            }
        }
    }

    // Update customer information in the database
    private void updateCustomerInfo(PreparedStatement stmt, String newFullName) throws SQLException {
        stmt.setString(1, newFullName);
        stmt.setString(2, customerID);
        stmt.executeUpdate();
    }

    // Update location information in the database
    private void updateLocationInfo(PreparedStatement stmt, String newCity, String newZip, String newStreet) throws SQLException {
        stmt.setString(1, newCity);
        stmt.setString(2, newZip);
        stmt.setString(3, newStreet);
        stmt.setString(4, customerID);
        stmt.executeUpdate();
    }

    // Update contact information in the database
    private void updateContactInfo(PreparedStatement stmt, String newContact, String newEmail) throws SQLException {
        stmt.setString(1, newContact);
        stmt.setString(2, newEmail);
        stmt.setString(3, customerID);
        stmt.executeUpdate();
    }

    // Enable or disable editing of the fields
    private void setEditableFields(boolean enable) {
        editnametextfield.setEditable(enable);
        editcitytextfield.setEditable(enable);
        editziptextfield.setEditable(enable);
        editstreettextfield.setEditable(enable);
        editphonetextfield.setEditable(enable);
        editemailtextfield.setEditable(enable);
    }

    // Handle the "Logout" button click event
    @FXML
    void logoutCustomer(ActionEvent event) {
        navigateToPage(event, "Userfirstpage.fxml");
    }

    // Handle the "Return to Account Page" button click event
    @FXML
    void returnaccountpageHandler(ActionEvent event) {
        navigateToPage(event, "Userhomepage.fxml");
    }

    // Navigate to a different page
    private void navigateToPage(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Show an alert dialog
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}