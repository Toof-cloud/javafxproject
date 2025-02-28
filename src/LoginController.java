import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button; 
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class LoginController {

    @FXML
    private Label usernamelabel;

    @FXML
    private Label passwordlabel;

    @FXML
    private TextField usernametextfield;

    @FXML
    private TextField passwordtextfield;

    @FXML
    private Button loginbutton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Handles the login button action.
     * 
     * @param event The action event.
     * @throws IOException If an input or output exception occurred.
     */
    public void loginbuttonhandler(ActionEvent event) throws IOException {
        String uname = usernametextfield.getText();
        String pword = passwordtextfield.getText();

        System.out.println("Welcome to my app, " + uname);
        System.out.println("Show username: " + uname);
        System.out.println("Show password: " + pword);

        if (DatabaseHandler.validateLogin(uname, pword)) {
            System.out.println("Login successful");

            // Load homepage.fxml when login button is clicked
            FXMLLoader loader = new FXMLLoader(getClass().getResource("adminhomepage.fxml"));
            root = loader.load();

            HomeController homeController = loader.getController();
            // Pass username from textfield to displayName() method
            homeController.displayName(uname);

            // Load stage and scene
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Login unsuccessful");
        }
    }
}