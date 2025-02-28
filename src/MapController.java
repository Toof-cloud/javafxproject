import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapController {

    @FXML
    private TextField locationField;

    @FXML
    private ImageView mapImageView;

    public void updateImage() {
        String location = locationField.getText().trim().toLowerCase();

        // Example: Change the image based on the entered location
        if (location.equals("makati")) {
            mapImageView.setImage(new Image(getClass().getResource("/images/makati.png").toExternalForm()));
        } else if (location.equals("manila")) {
            mapImageView.setImage(new Image(getClass().getResource("/images/manila.png").toExternalForm()));
        } else if (location.equals("paranaque") || location.equals("parañaque")) {
            mapImageView.setImage(new Image("file:C:/MetroManilaMapLocations/Paranaque_in_Metro_Manila.svg.png"));
        } else if (location.equals("laspinas") || location.equals("las piñas")) {
            mapImageView.setImage(new Image("file:C:/MetroManilaMapLocations/Las_Pinas_in_Metro_Manila.svg.png"));
        } else {
            // Default image or handle other locations
            mapImageView.setImage(new Image(getClass().getResource("/images/default.png").toExternalForm()));
        }
    }
}