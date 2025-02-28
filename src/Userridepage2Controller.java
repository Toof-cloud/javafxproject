import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

public class Userridepage2Controller {

    @FXML
    private TableColumn<RiderInfo, String> colname;

    @FXML
    private TableColumn<RiderInfo, String> contactcol;

    @FXML
    private TableColumn<RiderInfo, String> locationcol;

    @FXML
    private TableColumn<RiderInfo, String> platecol;

    @FXML
    private TableColumn<RiderInfo, Integer> ratingcol;

    @FXML
    private TableColumn<RiderInfo, String> vehiclecol;

    @FXML
    private TableView<RiderInfo> riderinfotable;

    @FXML
    private Button startridebutton;

    @FXML
    private Button endridebutton;

    @FXML
    private Button paybutton;

    @FXML
    private Button applybutton;

    @FXML
    private TextField timeintextfield;

    @FXML
    private TextField vouchercodetextfield;

    @FXML
    private Label moplabel;

    @FXML
    private Label etalabel;

    @FXML
    private Label paymentlabel;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private ObservableList<RiderInfo> riderData = FXCollections.observableArrayList();
    private boolean isPaid = false; // Flag to track if payment has been made
    private double fare = 0.0; // Store the fare to avoid recalculating

    @FXML
    public void initialize() {
        colname.setCellValueFactory(new PropertyValueFactory<>("riderFullname"));
        contactcol.setCellValueFactory(new PropertyValueFactory<>("riderContactNo"));
        locationcol.setCellValueFactory(new PropertyValueFactory<>("city"));
        platecol.setCellValueFactory(new PropertyValueFactory<>("plateNumber"));
        vehiclecol.setCellValueFactory(new PropertyValueFactory<>("vehicle"));
        ratingcol.setCellValueFactory(new PropertyValueFactory<>("rating"));

        loadRandomRiderData();

        // Add listener to timeintextfield to calculate ETA when time is entered
        timeintextfield.setOnAction(this::calculateETA);
    }

    private void loadRandomRiderData() {
        String url = "jdbc:mysql://localhost:3306/moveit";
        String user = "root";
        String password = "Blk3Llot19Lessandra";

        String query = "SELECT R.Rider_id, R.RiderFullname, R.RiderContactNo, RL.Zip, RL.City, RL.Street, V.PlateNumber, V.Vehicle, RR.Rating, RR.ShipOnTime " +
                       "FROM RiderTable R " +
                       "LEFT JOIN RiderLocationTable RL ON R.Rider_id = RL.Rider_id " +
                       "LEFT JOIN VehicleTable V ON R.Rider_id = V.Rider_id " +
                       "LEFT JOIN RiderRatingTable RR ON R.Rider_id = RR.Rider_id " +
                       "ORDER BY RAND() " +
                       "LIMIT 1";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String riderId = rs.getString("Rider_id");
                String riderFullname = rs.getString("RiderFullname");
                String riderContactNo = rs.getString("RiderContactNo");
                String zip = rs.getString("Zip");
                String city = rs.getString("City");
                String street = rs.getString("Street");
                String plateNumber = rs.getString("PlateNumber");
                String vehicle = rs.getString("Vehicle");
                int rating = rs.getInt("Rating");
                String shipOnTime = rs.getString("ShipOnTime");

                RiderInfo rider = new RiderInfo(riderId, riderFullname, riderContactNo, zip, city, street, plateNumber, vehicle, rating, shipOnTime);
                riderData.clear();
                riderData.add(rider);

                // Pre-select the rider
                riderinfotable.getSelectionModel().select(rider);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Rider Found", "No rider found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading rider data.");
        }

        riderinfotable.setItems(riderData);
    }

    @FXML
    void handleBackButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Userhomepage.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setModeOfPayment(String modeOfPayment) {
        moplabel.setText(modeOfPayment);
    }

    public void setFare(double fare) {
        this.fare = fare;
        paymentlabel.setText(String.format("₱%.2f", fare));
    }

    @FXML
    void calculateETA(ActionEvent event) {
        String origin = "Caloocan"; // Replace with actual origin
        String destination = "Manila"; // Replace with actual destination

        String url = "jdbc:mysql://localhost:3306/moveit";
        String user = "root";
        String password = "Blk3Llot19Lessandra";

        String query = "SELECT distance_km FROM fare_matrix WHERE origin = ? AND destination = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, origin);
            stmt.setString(2, destination);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double distanceKm = rs.getDouble("distance_km");
                double etaMinutes = distanceKm * 1.5;

                // Parse the time input
                String timeInput = timeintextfield.getText().trim();
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
                try {
                    LocalTime time = LocalTime.parse(timeInput, timeFormatter);
                    LocalTime etaTime = time.plusMinutes((long) etaMinutes);
                    etalabel.setText(String.format("ETA: %s (%.2f minutes)", etaTime.format(timeFormatter), etaMinutes));
                } catch (DateTimeParseException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid time format. Please use the format 'h:mm a' (e.g. 1:30 PM/AM ).");
                }
            } else {
                etalabel.setText("No route found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while calculating the ETA.");
        }
    }

    @FXML
    void handleStartRideButton(ActionEvent event) {
        // Set the current time as the start time
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        timeintextfield.setText(currentTime.format(timeFormatter));

        calculateETA(event);
    }

    @FXML
    void handleEndRideButton(ActionEvent event) throws IOException {
        if (!isPaid) {
            showAlert(Alert.AlertType.ERROR, "Payment Required", "Please complete the payment before ending the ride.");
            return;
        }

        System.out.println("handleEndRideButton called"); // Debugging

        // Get current customer ID from SessionManager
        String currentCustomerId = SessionManager.getCustomerId();

        if (currentCustomerId == null || currentCustomerId.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No logged-in customer found.");
            return;
        }

        // Parse pickup time
        String pickupTimeInput = timeintextfield.getText().trim();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        try {
            LocalTime pickupTime = LocalTime.parse(pickupTimeInput, timeFormatter);

            // Set the current time as the end time
            LocalTime endTime = LocalTime.now();
            LocalDateTime endDateTime = LocalDateTime.of(LocalDateTime.now().toLocalDate(), endTime);

            double amountPaid = Double.parseDouble(paymentlabel.getText().replace("₱", "").trim());

            // Get the rider ID from the pre-selected rider
            RiderInfo selectedRider = riderinfotable.getSelectionModel().getSelectedItem();
            if (selectedRider == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "No rider selected.");
                return;
            }
            String riderId = selectedRider.getRiderId();

            // Store booking in the database
            storeBooking(currentCustomerId, riderId, pickupTime, endDateTime, amountPaid);

            showAlert(Alert.AlertType.INFORMATION, "Ride Ended", "The ride has ended. Thank you for using our service!");

            // Navigate back to Userhomepage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Userhomepage.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid time format. Please use 'h:mm a' (e.g., 1:30 PM/AM).");
        }
    }

    private void storeBooking(String customerId, String riderId, LocalTime pickupTime, LocalDateTime arrivalTime, double amountPaid) {
        String transactionSql = "INSERT INTO TransactionTable (Transaction_id, Booking_id, Customer_id, Rider_id, pickup_time, arrival_time, amount_paid, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/moveit", "root", "Blk3Llot19Lessandra");
             PreparedStatement transactionStmt = conn.prepareStatement(transactionSql)) {

            // Generate the next Transaction_id and Booking_id
            String nextTransactionId = getNextTransactionId(conn);
            String nextBookingId = getNextBookingId(conn);

            // Set parameters for TransactionTable
            transactionStmt.setString(1, nextTransactionId); // Transaction_id
            transactionStmt.setString(2, nextBookingId); // Booking_id
            transactionStmt.setString(3, customerId);
            transactionStmt.setString(4, riderId);
            transactionStmt.setTimestamp(5, java.sql.Timestamp.valueOf(LocalDateTime.of(LocalDateTime.now().toLocalDate(), pickupTime)));
            transactionStmt.setTimestamp(6, java.sql.Timestamp.valueOf(arrivalTime));
            transactionStmt.setDouble(7, amountPaid);
            transactionStmt.setString(8, "Paid");

            // Execute the statement
            int transactionRowsInserted = transactionStmt.executeUpdate();

            if (transactionRowsInserted > 0) {
                System.out.println("Transaction stored successfully for customer: " + customerId);
            } else {
                System.out.println("Failed to store transaction.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while storing the transaction.");
        }
    }

    private String getNextTransactionId(Connection conn) throws SQLException {
        String query = "SELECT Transaction_id FROM TransactionTable ORDER BY Transaction_id DESC LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastTransactionId = rs.getString("Transaction_id");
                int nextId = Integer.parseInt(lastTransactionId.substring(1)) + 1;
                return String.format("T%03d", nextId);
            } else {
                return "T001"; // Default to T001 if no transactions exist
            }
        }
    }

    private String getNextBookingId(Connection conn) throws SQLException {
        String query = "SELECT Booking_id FROM TransactionTable ORDER BY Booking_id DESC LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastBookingId = rs.getString("Booking_id");
                int nextId = Integer.parseInt(lastBookingId.substring(1)) + 1;
                return String.format("B%03d", nextId);
            } else {
                return "B001"; // Default to B001 if no bookings exist
            }
        }
    }

    @FXML
    void handlePayButton(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Payment Successful", "Thank you for successfully paying.");
        isPaid = true; // Set the flag to true when payment is made
    }

    @FXML
    void handleApplyVoucher(ActionEvent event) {
        String paymentMethod = moplabel.getText(); // Assuming the user selects this
        if (paymentMethod.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a payment method first.");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/moveit";
        String user = "root";
        String password = "Blk3Llot19Lessandra";

        String fetchPromoQuery = "SELECT Promo FROM PaymentMethod WHERE PaymentMethod = ?";
        String fetchDiscountQuery = "SELECT Percentage FROM PromoTable WHERE Promo = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt1 = conn.prepareStatement(fetchPromoQuery);
             PreparedStatement stmt2 = conn.prepareStatement(fetchDiscountQuery)) {

            // Step 1: Get promo code based on payment method
            stmt1.setString(1, paymentMethod);
            ResultSet rs1 = stmt1.executeQuery();

            if (rs1.next()) {
                String promoCode = rs1.getString("Promo");

                // Step 2: Get discount percentage using promo code
                stmt2.setString(1, promoCode);
                ResultSet rs2 = stmt2.executeQuery();

                if (rs2.next()) {
                    int discountPercent = rs2.getInt("Percentage");

                    // Step 3: Apply discount
                    double originalFare = Double.parseDouble(paymentlabel.getText().replace("₱", "").trim());
                    double discountAmount = originalFare * (discountPercent / 100.0);
                    double finalFare = originalFare - discountAmount;

                    // Step 4: Update the UI with the discounted fare
                    paymentlabel.setText(String.format("₱%.2f", finalFare));
                    showAlert(Alert.AlertType.INFORMATION, "Voucher Applied", 
                              "Promo Code: " + promoCode + "\nDiscount: " + discountPercent + "%\nNew Fare: ₱" + finalFare);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Promo code not found in PromoTable.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No promo available for this payment method.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while applying the voucher.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}//working