import java.sql.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DatabaseHandler {
    private static DatabaseHandler handler = null;
    private static Connection connection = null;
    private static Statement stmt = null;
    private static PreparedStatement pstatement = null;

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    public static Connection getDBConnection() {
        Connection connection = null;

        String dburl = "jdbc:mysql://localhost:3306/moveit";
        String userName = "root";
        String passWord = "Blk3Llot19Lessandra";

        try {
            connection = DriverManager.getConnection(dburl, userName, passWord);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    public ResultSet execQuery(String query) {
        ResultSet result;

        try {
            stmt = getDBConnection().createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        } finally {
        }

        return result;
    }

    public static boolean validateLogin(String username, String password) {
        getInstance();
        String query = "SELECT * FROM users WHERE UserName = '" + username + "' AND Password = '" + password + "'";

        System.out.println(query);

        ResultSet result = handler.execQuery(query);
        try {
            if (result.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static ResultSet getUsers() {
        ResultSet result = null;

        try {
            String query = "SELECT * FROM users";
            result = handler.execQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean addUser(User user) {
        try {
            pstatement = getDBConnection().prepareStatement("INSERT INTO `users` (Username, Password) VALUES (?,?)");
            pstatement.setString(1, user.getUsername());
            pstatement.setString(2, user.getPassword());
            return pstatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean updateUser(User user) {
        try {
            pstatement = getDBConnection().prepareStatement("UPDATE `users` SET Username = ?, Password = ? WHERE User_id = ?");
            pstatement.setString(1, user.getUsername());
            pstatement.setString(2, user.getPassword());
            pstatement.setString(3, user.getUserid());

            int res = pstatement.executeUpdate();

            if (res > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean deleteUser(User user) {
        try {
            pstatement = getDBConnection().prepareStatement("DELETE FROM `users` WHERE Username=?");
            pstatement.setString(1, user.getUsername());

            int res = pstatement.executeUpdate();
            if (res > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean changePassword(String username, String password) {
        try {
            String query = "UPDATE users SET Password = '" + password + "' WHERE Username = '" + username + "'";

            System.out.println(query);

            pstatement = getDBConnection().prepareStatement(query);
            int result = pstatement.executeUpdate();

            if (result > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}