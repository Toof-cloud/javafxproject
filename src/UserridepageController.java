import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class UserridepageController {

    // FXML UI elements
    @FXML
    private Button backbutton; 

    @FXML
    private Button proceedbutton; 

    @FXML
    private Label estimatedfarelabel;

    @FXML
    private TextField locationField;

    @FXML
    private TextField locationField2;

    @FXML
    private TextField modeofpaymenttextfield; 

    @FXML
    private ImageView mapImageView;

    @FXML
    private ImageView mapImageView2;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private final Map<String, Image> locationImages = new HashMap<>();
    private Image defaultMap;

    private double fare; // Store the fare value

    @FXML
    public void initialize() {
        // Load default Metro Manila map once
        File defaultMapFile = new File("C:\\MetroManilaMapLocations\\Metro_Manila_location_map.png");
        if (defaultMapFile.exists()) {
            defaultMap = new Image(defaultMapFile.toURI().toString());
        } else {
            System.err.println("Default map not found! Ensure file exists: " + defaultMapFile.getAbsolutePath());
        }

        mapImageView.setImage(defaultMap);
        mapImageView2.setImage(defaultMap);

        // List of cities
        String[] cities = {
            "caloocan", "las piñas", "makati", "malabon", "mandaluyong", "manila",
            "marikina", "muntinlupa", "navotas", "parañaque", "pasay", "pasig",
            "pateros", "quezon city", "san juan", "taguig", "valenzuela"
        };

        String basePath = "C:\\MetroManilaMapLocations\\";

        // Load city maps
        for (String city : cities) {
            String normalizedCity = city.replace(" ", "").toLowerCase();
            File file = new File(basePath + city.replace(" ", "_") + "_in_Metro_Manila.svg.png");

            if (file.exists()) {
                locationImages.put(normalizedCity, new Image(file.toURI().toString()));
            } else {
                System.err.println("Warning: Missing map for " + city + " at " + file.getAbsolutePath());
            }
        }
    }

    /**
     * Handles the back button action.
     */
    @FXML
    void handleBackButton(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Userhomepage.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the proceed button action.
     */
    @FXML
    void handleProceedButton(ActionEvent event) throws IOException {
        String modeOfPayment = modeofpaymenttextfield.getText().trim();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Userridepage2.fxml"));
        root = loader.load();

        Userridepage2Controller userridepage2Controller = loader.getController();
        userridepage2Controller.setFare(fare); // Use the fare value from updateFare
        userridepage2Controller.setModeOfPayment(modeOfPayment);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Updates the image based on user input in the first text field.
     */
    @FXML
    void updateImage(KeyEvent event) {
        updateMapImage(locationField.getText(), mapImageView);
        updateFare();
    }

    /**
     * Updates the image based on user input in the second text field.
     */
    @FXML
    void updateImage2(KeyEvent event) {
        updateMapImage(locationField2.getText(), mapImageView2);
        updateFare();
    }

    /**
     * Helper method to update the ImageView.
     */
    private void updateMapImage(String location, ImageView imageView) {
        if (location == null || location.trim().isEmpty()) {
            imageView.setImage(defaultMap);
            return;
        }

        String normalizedLocation = location.trim().toLowerCase().replace(" ", "");

        if (locationImages.containsKey(normalizedLocation)) {
            imageView.setImage(locationImages.get(normalizedLocation));
        } else {
            imageView.setImage(defaultMap);
        }
    }

    /**
     * Updates the fare label based on the origin and destination.
     */
    private void updateFare() {
        String origin = locationField.getText().trim();
        String destination = locationField2.getText().trim();
    
        if (origin.isEmpty() || destination.isEmpty()) {
            estimatedfarelabel.setText("  Enter both origin and destination. ");
            adjustLabelWidth();
            fare = 0.0; // Reset fare value
            return;
        }
    
        try {
            fare = DatabaseHandler.getFareNormalized(origin, destination);
            if (fare >= 0) {
                estimatedfarelabel.setText("  Estimated Fare: ₱" + fare + "  ");
            } else {
                estimatedfarelabel.setText("  No fare found for the given route. ");
                fare = 0.0; // Reset fare value
            }
        } catch (SQLException e) {
            e.printStackTrace();
            estimatedfarelabel.setText("  Error calculating fare. ");
            fare = 0.0; // Reset fare value
        }
    
        adjustLabelWidth(); // Adjust label width dynamically
    }

    /**
     * Adjusts the width of the fare label dynamically.
     */
    private void adjustLabelWidth() {
        estimatedfarelabel.setPrefWidth(Region.USE_COMPUTED_SIZE); // Auto-compute width
        estimatedfarelabel.setWrapText(true); // Ensure text wraps if needed
    }

    /**
     * Shows an alert dialog.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}