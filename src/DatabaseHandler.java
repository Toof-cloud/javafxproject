import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {
    private static DatabaseHandler handler = null;
    private static Connection connection = null;
    private static PreparedStatement pstatement = null;

    // Singleton pattern to ensure only one instance of DatabaseHandler
    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    // Establishes a connection to the database
    public static Connection getDBConnection() {
        String dburl = "jdbc:mysql://localhost:3306/moveit";
        String userName = "root";
        String passWord = "Blk3Llot19Lessandra";

        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(dburl, userName, passWord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    // Executes a query and returns the result set
    public ResultSet execQuery(String query) {
        ResultSet result = null;

        try {
            Statement stmt = getDBConnection().createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
        }

        return result;
    }

    // Generates a new Customer_id
    private static String generateCustomerId() {
        String customerId = null;
        try {
            String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(Customer_id, 3, 3) AS UNSIGNED)), 0) + 1 AS next_id FROM CustomerTable";
            pstatement = getDBConnection().prepareStatement(query);
            ResultSet rs = pstatement.executeQuery();
            if (rs.next()) {
                int nextId = rs.getInt("next_id");
                customerId = String.format("C-%03d", nextId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerId;
    }

    // Adds a new customer to the database
    public static boolean addCustomer(Customer customer) {
        try {
            String customerId = generateCustomerId();
            if (customerId == null) {
                return false;
            }

            // Insert into CustomerTable
            String customerQuery = "INSERT INTO CustomerTable (Customer_id, CustomerFullName, Password) VALUES (?, ?, ?)";
            pstatement = getDBConnection().prepareStatement(customerQuery);
            pstatement.setString(1, customerId);
            pstatement.setString(2, customer.getFullName());
            pstatement.setString(3, customer.getPassword());
            pstatement.executeUpdate();

            // Insert into LocationTable
            String locationQuery = "INSERT INTO LocationTable (Customer_id, City, Zip, Street) VALUES (?, ?, ?, ?)";
            pstatement = getDBConnection().prepareStatement(locationQuery);
            pstatement.setString(1, customerId);
            pstatement.setString(2, customer.getCity());
            pstatement.setString(3, customer.getZipCode());
            pstatement.setString(4, customer.getStreet());
            pstatement.executeUpdate();

            // Insert into ContactTable
            String contactQuery = "INSERT INTO ContactTable (Customer_id, ContactNum, Email) VALUES (?, ?, ?)";
            pstatement = getDBConnection().prepareStatement(contactQuery);
            pstatement.setString(1, customerId);
            pstatement.setString(2, customer.getContactNumber());
            pstatement.setString(3, customer.getEmail());
            pstatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Retrieves combined customer data using joins
    public static ResultSet getCombinedCustomerData() {
        ResultSet result = null;

        try {
            String query = "SELECT C.Customer_id, C.CustomerFullName, L.City, L.Zip, L.Street, CT.ContactNum, CT.Email " +
                           "FROM CustomerTable C " +
                           "INNER JOIN LocationTable L ON C.Customer_id = L.Customer_id " +
                           "INNER JOIN ContactTable CT ON C.Customer_id = CT.Customer_id";
            pstatement = getDBConnection().prepareStatement(query);
            result = pstatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // Retrieves the fare from the fare_matrix table
    public static double getFare(String origin, String destination) throws SQLException {
        String query = "SELECT fare FROM fare_matrix WHERE origin = ? AND destination = ?";
        try (Connection connection = getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, origin);
            statement.setString(2, destination);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("fare");
                } else {
                    return -1; // Indicate that no fare was found
                }
            }
        }
    }

    // Retrieves the customer's full name based on their ID
    public static String getCustomerFullName(String customerId) throws SQLException {
        String query = "SELECT CustomerFullName FROM CustomerTable WHERE Customer_id = ?";
        try (Connection connection = getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("CustomerFullName");
                } else {
                    return null; // Indicate that no customer was found
                }
            }
        }
    }

    // Normalizes city names by removing spaces and converting to lowercase
    private static String normalizeCityName(String cityName) {
        return cityName.replaceAll("\\s+", "").toLowerCase();
    }

    // Retrieves the fare from the fare_matrix table with normalized city names
    public static double getFareNormalized(String origin, String destination) throws SQLException {
        String normalizedOrigin = normalizeCityName(origin);
        String normalizedDestination = normalizeCityName(destination);
        String query = "SELECT fare FROM fare_matrix WHERE REPLACE(LOWER(origin), ' ', '') = ? AND REPLACE(LOWER(destination), ' ', '') = ?";
        try (Connection connection = getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, normalizedOrigin);
            statement.setString(2, normalizedDestination);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("fare");
                } else {
                    return -1; // Indicate that no fare was found
                }
            }
        }
    }

    // Validates admin login credentials
    public static boolean validateAdminLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE Username = ? AND Password = ?";
        try (Connection connection = getDBConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Return true if a matching record is found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}