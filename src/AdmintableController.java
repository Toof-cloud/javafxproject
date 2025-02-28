import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdmintableController {
    private UserController userController = new UserController();
    private ObservableList<AdminUserData> userData = FXCollections.observableArrayList();

    @FXML
    private TableView<AdminUserData> userstable;

    @FXML
    private TableColumn<AdminUserData, String> useridcolumn, usernamecolumn, passwordcolumn;

    @FXML
    private TableColumn<AdminUserData, LocalDateTime> accountcreatedcolumn;

    @FXML
    private TextField usernametextfield, passwordtextfield;

    @FXML
    private Label usernamedisplay, nameLabel, nameLabel1;

    @FXML
    private Button createButton, updateButton, deleteButton, logoutButton, customertablebutton, ridertablebutton;

    // 🔹 Initialize the table and load user data
    @FXML
    public void initialize() {
        useridcolumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernamecolumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordcolumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        accountcreatedcolumn.setCellValueFactory(new PropertyValueFactory<>("accountCreated"));

        loadUsers();

        // 🔹 Add selection listener for table row clicks
        userstable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                usernametextfield.setText(newSelection.getUsername());
                passwordtextfield.setText(newSelection.getPassword());
            }
        });
    }

    // 🔹 Load users from the database into the table
    private void loadUsers() {
        userData.clear();
        String query = "SELECT * FROM users";

        try (Connection connection = DatabaseHandler.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String userId = resultSet.getString("User_id");
                String username = resultSet.getString("Username");
                String password = resultSet.getString("Password");
                LocalDateTime accountCreated = resultSet.getTimestamp("AccountCreated").toLocalDateTime();

                userData.add(new AdminUserData(userId, username, password, accountCreated));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading user data.");
        }

        userstable.setItems(userData);
    }

    // 🔹 Create a new user
    @FXML
    void createUser(ActionEvent event) {
        String userId = getNextAdminID(); // Fetch next available ID from MySQL
        String username = usernametextfield.getText();
        String password = passwordtextfield.getText();

        if (!username.isEmpty() && !password.isEmpty()) {
            if (userController.addUser(userId, username, password)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully with ID: " + userId);
                loadUsers();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add user.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Username and Password cannot be empty.");
        }
    }

    // 🔹 Get the next available admin ID from MySQL
    private String getNextAdminID() {
        String query = "SELECT getNextAdminID()";
        try (Connection conn = DatabaseHandler.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Update user password
    @FXML
    void updateUser(ActionEvent event) {
        AdminUserData selectedUser = userstable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No user selected. Please select a user first.");
            return;
        }

        String userId = selectedUser.getUserId();
        String newPassword = passwordtextfield.getText();

        if (!newPassword.isEmpty()) {
            if (userController.updateUser(userId, newPassword)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully.");
                loadUsers();
                usernametextfield.clear();  // Clear text fields
                passwordtextfield.clear();

            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Password cannot be empty.");
        }
    }

    // 🔹 Delete selected user
    @FXML
    void deleteUser(ActionEvent event) {
        AdminUserData selectedUser = userstable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No user selected. Please select a user first.");
            return;
        }

        String userId = selectedUser.getUserId();

        if (userController.deleteUser(userId)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully.");
            loadUsers();
            usernametextfield.clear();  // Clear text fields
            passwordtextfield.clear();

        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user.");
        }
    }

    // 🔹 Check if user exists in the database
    private boolean doesUserExist(String userId) {
        String query = "SELECT COUNT(*) FROM users WHERE User_id = ?";
        try (Connection connection = DatabaseHandler.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();

            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Logout and return to login page
    @FXML
    void logoutAdmin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userfirstpage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

            showAlert(Alert.AlertType.INFORMATION, "Logged Out", "Admin has been logged out.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while logging out.");
        }
    }

    // 🔹 Load customer table
    @FXML
    void customertableHandler(ActionEvent event) {
        loadTable(event, "admincustomertable.fxml", "An error occurred while loading the customer table.");
    }

    // 🔹 Load rider table
    @FXML
    void ridertableHandler(ActionEvent event) {
        loadTable(event, "adminridertable.fxml", "An error occurred while loading the rider table.");
    }

    // 🔹 Generic method to load different tables
    private void loadTable(ActionEvent event, String fxmlFile, String errorMessage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", errorMessage);
        }
    }

    // 🔹 Show alerts for messages
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}