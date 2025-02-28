import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the initial FXML file
        Parent root = FXMLLoader.load(getClass().getResource("Userfirstpage.fxml"));

        // Set the title of the main page
        primaryStage.setTitle("Log in");

        // Set the window size
        primaryStage.setScene(new Scene(root, 450, 700));

        // Lock the window size
        primaryStage.setResizable(false);

        // Display the window
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}