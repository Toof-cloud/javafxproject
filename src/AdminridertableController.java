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

public class AdminridertableController {

    @FXML
    private Button admintablebutton;

    @FXML
    private TableColumn<AdminRiderData, String> cityColumn;

    @FXML
    private TextField citytextfield;

    @FXML
    private TextField contactnotextfield;

    @FXML
    private Button customertablebutton;

    @FXML
    private Button logoutButton;

    @FXML
    private Label nameLabel;

    @FXML
    private Label nameLabel1;

    @FXML
    private TableColumn<AdminRiderData, String> platenoColumn;

    @FXML
    private TextField platenotextfield;

    @FXML
    private TableColumn<AdminRiderData, Double> ratingColumn;

    @FXML
    private TextField ratingtextfield;

    @FXML
    private TableView<AdminRiderData> riderTable;

    @FXML
    private TableColumn<AdminRiderData, String> ridercontactnoColumn;

    @FXML
    private Button ridercreatebutton;

    @FXML
    private Button riderdeletebutton;

    @FXML
    private TableColumn<AdminRiderData, String> riderfullnameColumn;

    @FXML
    private TextField riderfullnametextfield;

    @FXML
    private TableColumn<AdminRiderData, String> rideridColumn;

    @FXML
    private Button riderupdatebutton;

    @FXML
    private TableColumn<AdminRiderData, String> shipontimeColumn;

    @FXML
    private TableColumn<AdminRiderData, String> streetColumn;

    @FXML
    private TextField streettextfield;

    @FXML
    private Label usernamedisplay;

    @FXML
    private TableColumn<AdminRiderData, String> vehicleColumn;

    @FXML
    private TextField vehicletextfield;

    @FXML
    private TableColumn<AdminRiderData, String> zipColumn;

    @FXML
    private TextField ziptextfield;

    private ObservableList<AdminRiderData> riderData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        rideridColumn.setCellValueFactory(new PropertyValueFactory<>("riderId"));
        riderfullnameColumn.setCellValueFactory(new PropertyValueFactory<>("riderName"));
        platenoColumn.setCellValueFactory(new PropertyValueFactory<>("plateNo"));
        vehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicle"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        streetColumn.setCellValueFactory(new PropertyValueFactory<>("street"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));
        ridercontactnoColumn.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        shipontimeColumn.setCellValueFactory(new PropertyValueFactory<>("shipOnTime"));
        
        loadRiders();

        // Add selection listener for table row clicks
        riderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                riderfullnametextfield.setText(newSelection.getRiderName());
                platenotextfield.setText(newSelection.getPlateNo());
                vehicletextfield.setText(newSelection.getVehicle());
                citytextfield.setText(newSelection.getCity());
                streettextfield.setText(newSelection.getStreet());
                ziptextfield.setText(newSelection.getZip());
                contactnotextfield.setText(newSelection.getContactNo());
                ratingtextfield.setText(String.valueOf(newSelection.getRating()));
                showAlert(Alert.AlertType.INFORMATION, "Selection", "You are ready to update or delete a rider.");
            }
        });
    }

    private void loadRiders() {
        riderData.clear();
        String query = "SELECT R.Rider_id, R.RiderFullname, R.RiderContactNo, RL.Zip, RL.City, RL.Street, V.PlateNumber, V.Vehicle, RR.Rating, RR.ShipOnTime " +
                       "FROM RiderTable R " +
                       "LEFT JOIN RiderLocationTable RL ON R.Rider_id = RL.Rider_id " +
                       "LEFT JOIN VehicleTable V ON R.Rider_id = V.Rider_id " +
                       "LEFT JOIN RiderRatingTable RR ON R.Rider_id = RR.Rider_id " +
                       "ORDER BY R.Rider_id ASC";

        try (Connection connection = DatabaseHandler.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                riderData.add(new AdminRiderData(
                    resultSet.getString("Rider_id"),
                    resultSet.getString("RiderFullname"),
                    resultSet.getString("PlateNumber"),
                    resultSet.getString("Vehicle"),
                    resultSet.getString("City"),
                    resultSet.getString("Street"),
                    resultSet.getString("Zip"),
                    resultSet.getString("RiderContactNo"),
                    resultSet.getDouble("Rating"),
                    resultSet.getString("ShipOnTime")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading rider data.");
        }
        riderTable.setItems(riderData);
    }

    @FXML
    void createRider(ActionEvent event) {
        String query = "INSERT INTO RiderTable (Rider_id, RiderFullname, RiderContactNo) VALUES (?, ?, ?)";
        String locationQuery = "INSERT INTO RiderLocationTable (Rider_id, Zip, City, Street) VALUES (?, ?, ?, ?)";
        String vehicleQuery = "INSERT INTO VehicleTable (PlateNumber, Vehicle, Rider_id) VALUES (?, ?, ?)";
        String ratingQuery = "INSERT INTO RiderRatingTable (Rider_id, Rating, ShipOnTime) VALUES (?, ?, ?)";
        
        try (Connection connection = DatabaseHandler.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             PreparedStatement locationStatement = connection.prepareStatement(locationQuery);
             PreparedStatement vehicleStatement = connection.prepareStatement(vehicleQuery);
             PreparedStatement ratingStatement = connection.prepareStatement(ratingQuery)) {
            
            String riderId = getNextRiderID(connection);
            statement.setString(1, riderId);
            statement.setString(2, riderfullnametextfield.getText());
            statement.setString(3, contactnotextfield.getText());
            statement.executeUpdate();
            
            locationStatement.setString(1, riderId);
            locationStatement.setString(2, ziptextfield.getText());
            locationStatement.setString(3, citytextfield.getText());
            locationStatement.setString(4, streettextfield.getText());
            locationStatement.executeUpdate();
            
            vehicleStatement.setString(1, platenotextfield.getText());
            vehicleStatement.setString(2, vehicletextfield.getText());
            vehicleStatement.setString(3, riderId);
            vehicleStatement.executeUpdate();
            
            ratingStatement.setString(1, riderId);
            ratingStatement.setDouble(2, Double.parseDouble(ratingtextfield.getText()));
            ratingStatement.setString(3, "0%");
            ratingStatement.executeUpdate();
            
            showAlert(Alert.AlertType.INFORMATION, "Success", "Rider added successfully.");
            clearTextFields();
            loadRiders();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add rider.");
        }
    }

    private String getNextRiderID(Connection connection) throws SQLException {
        String query = "SELECT getNextRiderID() AS id";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("id");
            }
        }
        return "RIDER_0001";
    }

    @FXML
    void updateRider(ActionEvent event) {
        AdminRiderData selectedRider = riderTable.getSelectionModel().getSelectedItem();
        if (selectedRider == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a rider to update.");
            return;
        }

        String query = "UPDATE RiderTable SET RiderFullname = ?, RiderContactNo = ? WHERE Rider_id = ?";
        String locationQuery = "UPDATE RiderLocationTable SET Zip = ?, City = ?, Street = ? WHERE Rider_id = ?";
        String vehicleQuery = "UPDATE VehicleTable SET PlateNumber = ?, Vehicle = ? WHERE Rider_id = ?";
        String ratingQuery = "UPDATE RiderRatingTable SET Rating = ? WHERE Rider_id = ?";
        
        try (Connection connection = DatabaseHandler.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             PreparedStatement locationStatement = connection.prepareStatement(locationQuery);
             PreparedStatement vehicleStatement = connection.prepareStatement(vehicleQuery);
             PreparedStatement ratingStatement = connection.prepareStatement(ratingQuery)) {
            
            statement.setString(1, riderfullnametextfield.getText());
            statement.setString(2, contactnotextfield.getText());
            statement.setString(3, selectedRider.getRiderId());
            statement.executeUpdate();
            
            locationStatement.setString(1, ziptextfield.getText());
            locationStatement.setString(2, citytextfield.getText());
            locationStatement.setString(3, streettextfield.getText());
            locationStatement.setString(4, selectedRider.getRiderId());
            locationStatement.executeUpdate();
            
            vehicleStatement.setString(1, platenotextfield.getText());
            vehicleStatement.setString(2, vehicletextfield.getText());
            vehicleStatement.setString(3, selectedRider.getRiderId());
            vehicleStatement.executeUpdate();
            
            ratingStatement.setDouble(1, Double.parseDouble(ratingtextfield.getText()));
            ratingStatement.setString(2, selectedRider.getRiderId());
            ratingStatement.executeUpdate();
            
            showAlert(Alert.AlertType.INFORMATION, "Success", "Rider updated successfully.");
            clearTextFields();
            loadRiders();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update rider.");
        }
    }

    @FXML
    void deleteRider(ActionEvent event) {
        AdminRiderData selectedRider = riderTable.getSelectionModel().getSelectedItem();
        if (selectedRider == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a rider to delete.");
            return;
        }

        String query = "DELETE FROM RiderTable WHERE Rider_id = ?";
        
        try (Connection connection = DatabaseHandler.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, selectedRider.getRiderId());
            statement.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Rider deleted successfully.");
            clearTextFields();
            loadRiders();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete rider.");
        }
    }

    @FXML
    void handleLogout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Userfirstpage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void handleAdminTable(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Admintable.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void handleCustomerTable(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Admincustomertable.fxml"));
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
        riderfullnametextfield.clear();
        platenotextfield.clear();
        vehicletextfield.clear();
        citytextfield.clear();
        streettextfield.clear();
        ziptextfield.clear();
        contactnotextfield.clear();
        ratingtextfield.clear();
    }
}