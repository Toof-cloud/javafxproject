import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class AdmincustomertableController {

    @FXML
    private Button admintablebutton;

    @FXML
    private TableColumn<Customer, String> citycolumn;

    @FXML
    private TableColumn<Customer, String> contactnumbercolumn;

    @FXML
    private TableColumn<Customer, String> customerIDcolumn;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TextField customercitytextfield;

    @FXML
    private TextField customercontactnotextfield;

    @FXML
    private Button customercreatebutton;

    @FXML
    private Button customerdeletebutton;

    @FXML
    private TextField customeremailtextfield;

    @FXML
    private TableColumn<Customer, String> customerfullnamecolumn;

    @FXML
    private TextField customerfullnametextfield;

    @FXML
    private TextField customerpasstextfield;

    @FXML
    private TextField customerstreettextfield;

    @FXML
    private Button customerupdatebutton;

    @FXML
    private TextField customerziptextfield;

    @FXML
    private TableColumn<Customer, String> emailcolumn;

    @FXML
    private Button logoutButton;

    @FXML
    private Label nameLabel;

    @FXML
    private Label nameLabel1;

    @FXML
    private TableColumn<Customer, String> passwordcolumn;

    @FXML
    private Button ridertablebutton;

    @FXML
    private TableColumn<Customer, String> streetcolumn;

    @FXML
    private Label usernamedisplay;

    @FXML
    private TableColumn<Customer, String> zipcolumn;

    private ObservableList<Customer> customerData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        customerIDcolumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerfullnamecolumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        contactnumbercolumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        emailcolumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordcolumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        citycolumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        streetcolumn.setCellValueFactory(new PropertyValueFactory<>("street"));
        zipcolumn.setCellValueFactory(new PropertyValueFactory<>("zipCode"));

        loadCustomers();

        // Add selection listener for table row clicks
        customerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                customerfullnametextfield.setText(newSelection.getFullName());
                customercontactnotextfield.setText(newSelection.getContactNumber());
                customeremailtextfield.setText(newSelection.getEmail());
                customerpasstextfield.setText(newSelection.getPassword());
                customercitytextfield.setText(newSelection.getCity());
                customerstreettextfield.setText(newSelection.getStreet());
                customerziptextfield.setText(newSelection.getZipCode());
                showAlert(Alert.AlertType.INFORMATION, "Selection", "You are ready to update or delete a customer.");
            }
        });
    }

    private void loadCustomers() {
        customerData.clear();
        String query = "SELECT C.Customer_id, C.CustomerFullName, C.Password, L.City, L.Zip, L.Street, CT.ContactNum, CT.Email " +
                       "FROM CustomerTable C " +
                       "INNER JOIN LocationTable L ON C.Customer_id = L.Customer_id " +
                       "INNER JOIN ContactTable CT ON C.Customer_id = CT.Customer_id";

        try (Connection connection = DatabaseHandler.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                customerData.add(new Customer(
                    resultSet.getString("Customer_id"),
                    resultSet.getString("CustomerFullName"),
                    resultSet.getString("Email"),
                    resultSet.getString("ContactNum"),
                    resultSet.getString("Street"),
                    resultSet.getString("City"),
                    resultSet.getString("Zip"),
                    resultSet.getString("Password")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading customer data.");
        }
        customerTable.setItems(customerData);
    }

    @FXML
    void createCustomer(ActionEvent event) {
        String query = "INSERT INTO CustomerTable (Customer_id, CustomerFullName, Password) VALUES (?, ?, ?)";
        String locationQuery = "INSERT INTO LocationTable (Customer_id, City, Zip, Street) VALUES (?, ?, ?, ?)";
        String contactQuery = "INSERT INTO ContactTable (Customer_id, ContactNum, Email) VALUES (?, ?, ?)";
        
        try (Connection connection = DatabaseHandler.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             PreparedStatement locationStatement = connection.prepareStatement(locationQuery);
             PreparedStatement contactStatement = connection.prepareStatement(contactQuery)) {
            
            String customerId = getNextCustomerID(connection);
            statement.setString(1, customerId);
            statement.setString(2, customerfullnametextfield.getText());
            statement.setString(3, customerpasstextfield.getText());
            statement.executeUpdate();
            
            locationStatement.setString(1, customerId);
            locationStatement.setString(2, customercitytextfield.getText());
            locationStatement.setString(3, customerziptextfield.getText());
            locationStatement.setString(4, customerstreettextfield.getText());
            locationStatement.executeUpdate();
            
            contactStatement.setString(1, customerId);
            contactStatement.setString(2, customercontactnotextfield.getText());
            contactStatement.setString(3, customeremailtextfield.getText());
            contactStatement.executeUpdate();
            
            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer added successfully.");
            clearTextFields();
            loadCustomers();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add customer.");
        }
    }

    private String getNextCustomerID(Connection connection) throws SQLException {
        String query = "SELECT getNextCustomerID() AS id";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("id");
            }
        }
        return "CUSTOMER_0001";
    }

    @FXML
    void updateCustomer(ActionEvent event) {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a customer to update.");
            return;
        }

        String query = "UPDATE CustomerTable SET CustomerFullName = ?, Password = ? WHERE Customer_id = ?";
        String locationQuery = "UPDATE LocationTable SET City = ?, Zip = ?, Street = ? WHERE Customer_id = ?";
        String contactQuery = "UPDATE ContactTable SET ContactNum = ?, Email = ? WHERE Customer_id = ?";
        
        try (Connection connection = DatabaseHandler.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             PreparedStatement locationStatement = connection.prepareStatement(locationQuery);
             PreparedStatement contactStatement = connection.prepareStatement(contactQuery)) {
            
            statement.setString(1, customerfullnametextfield.getText());
            statement.setString(2, customerpasstextfield.getText());
            statement.setString(3, selectedCustomer.getCustomerId());
            statement.executeUpdate();
            
            locationStatement.setString(1, customercitytextfield.getText());
            locationStatement.setString(2, customerziptextfield.getText());
            locationStatement.setString(3, customerstreettextfield.getText());
            locationStatement.setString(4, selectedCustomer.getCustomerId());
            locationStatement.executeUpdate();
            
            contactStatement.setString(1, customercontactnotextfield.getText());
            contactStatement.setString(2, customeremailtextfield.getText());
            contactStatement.setString(3, selectedCustomer.getCustomerId());
            contactStatement.executeUpdate();
            
            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer updated successfully.");
            clearTextFields();
            loadCustomers();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update customer.");
        }
    }

    @FXML
    void deleteCustomer(ActionEvent event) {
    Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
    if (selectedCustomer == null) {
        showAlert(Alert.AlertType.ERROR, "Error", "Please select a customer to delete.");
        return;
    }

    String contactQuery = "DELETE FROM ContactTable WHERE Customer_id = ?";
    String locationQuery = "DELETE FROM LocationTable WHERE Customer_id = ?";
    String customerQuery = "DELETE FROM CustomerTable WHERE Customer_id = ?";

    try (Connection connection = DatabaseHandler.getDBConnection();
         PreparedStatement contactStatement = connection.prepareStatement(contactQuery);
         PreparedStatement locationStatement = connection.prepareStatement(locationQuery);
         PreparedStatement customerStatement = connection.prepareStatement(customerQuery)) {

        // Delete from ContactTable
        contactStatement.setString(1, selectedCustomer.getCustomerId());
        contactStatement.executeUpdate();

        // Delete from LocationTable
        locationStatement.setString(1, selectedCustomer.getCustomerId());
        locationStatement.executeUpdate();

        // Delete from CustomerTable
        customerStatement.setString(1, selectedCustomer.getCustomerId());
        customerStatement.executeUpdate();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Customer deleted successfully.");
        clearTextFields();
        loadCustomers();
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete customer.");
    }
}
    @FXML
    void logoutAdmin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Userfirstpage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void admintableHandler(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Admintable.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void ridertableHandler(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Adminridertable.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearTextFields() {
        customerfullnametextfield.clear();
        customercontactnotextfield.clear();
        customeremailtextfield.clear();
        customerpasstextfield.clear();
        customercitytextfield.clear();
        customerstreettextfield.clear();
        customerziptextfield.clear();
    }
}