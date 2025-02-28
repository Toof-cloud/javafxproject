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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UseractivitypageController {

    @FXML
    private TableColumn<Transaction, Double> amountpaidcol;

    @FXML
    private TableColumn<Transaction, LocalDateTime> arrivaltimecol;

    @FXML
    private TableColumn<Transaction, String> bookingidcol;

    @FXML
    private Button csaccountbutton;

    @FXML
    private Button csactivitybutton;

    @FXML
    private Button cshomebutton;

    @FXML
    private Button csmessagesbutton;

    @FXML
    private TableColumn<Transaction, String> customeridcol;

    @FXML
    private TableColumn<Transaction, String> paymentstatuscol;

    @FXML
    private TableColumn<Transaction, LocalDateTime> pickuptimecol;

    @FXML
    private TableColumn<Transaction, LocalDateTime> transactiondatecol;

    @FXML
    private TableColumn<Transaction, String> transactionidcol;

    @FXML
    private TableView<Transaction> transactoiontable;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private ObservableList<Transaction> transactionData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        transactionidcol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        bookingidcol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        customeridcol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        pickuptimecol.setCellValueFactory(new PropertyValueFactory<>("pickupTime"));
        arrivaltimecol.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        amountpaidcol.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));
        paymentstatuscol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        transactiondatecol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        // Set cell factory to format LocalDateTime values with spaces
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringConverter<LocalDateTime> converter = new LocalDateTimeStringConverter(formatter, null);

        pickuptimecol.setCellFactory(column -> new TableCell<Transaction, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(converter.toString(item));
                }
            }
        });

        arrivaltimecol.setCellFactory(column -> new TableCell<Transaction, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(converter.toString(item));
                }
            }
        });

        transactiondatecol.setCellFactory(column -> new TableCell<Transaction, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(converter.toString(item));
                }
            }
        });

        // Retrieve customerId from SessionManager
        String customerId = SessionManager.getCustomerId();
        if (customerId != null && !customerId.isEmpty()) {
            loadTransactionData(customerId);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No logged-in customer found.");
        }
    }

    private void loadTransactionData(String customerId) {
        String url = "jdbc:mysql://localhost:3306/moveit";
        String user = "root";
        String password = "Blk3Llot19Lessandra";

        String query = "SELECT * FROM TransactionTable WHERE Customer_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String transactionId = rs.getString("Transaction_id");
                String bookingId = rs.getString("Booking_id");
                String riderId = rs.getString("Rider_id");
                LocalDateTime pickupTime = rs.getTimestamp("pickup_time").toLocalDateTime();
                LocalDateTime arrivalTime = rs.getTimestamp("arrival_time").toLocalDateTime();
                double amountPaid = rs.getDouble("amount_paid");
                String paymentStatus = rs.getString("payment_status");
                LocalDateTime transactionDate = rs.getTimestamp("transaction_date").toLocalDateTime();

                Transaction transaction = new Transaction(transactionId, bookingId, customerId, riderId, pickupTime, arrivalTime, amountPaid, paymentStatus, transactionDate);
                transactionData.add(transaction);
            }

            System.out.println("Loaded " + transactionData.size() + " transactions."); // Debug statement

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading transaction data.");
        }

        transactoiontable.setItems(transactionData);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleAccountButton(ActionEvent event) throws IOException {
        // Load accountpage.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Useraccountpage.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleActivityButton(ActionEvent event) throws IOException {
        // Load activitypage.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Useractivitypage.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleHomeButton(ActionEvent event) throws IOException {
        // Load homepage.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Userhomepage.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleMessagesButton(ActionEvent event) throws IOException {
        // Load messagespage.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Usermessagespage.fxml"));
        root = loader.load();

        // Load stage and scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}