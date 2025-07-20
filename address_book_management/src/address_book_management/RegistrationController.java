package address_book_management;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class RegistrationController implements Initializable {

    @FXML
    private TextField textfield3;  // e.g. Name
    @FXML
    private TextField textfield4;  // e.g. Email
    @FXML
    private TextField textfield5;  // e.g. Phone or Password
    @FXML
    private Button btn3;  // Submit/Register button
    @FXML
    private Button btn4;  // Back to Login button

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization if needed
    }    

    @FXML
    private void submit(ActionEvent event) {
        String name = textfield3.getText();
        String email = textfield4.getText();
        String phone = textfield5.getText();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter all fields");
            return;
        }

        // Here, you can add actual registration logic like saving user to DB
        // For demo, just show success
        showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Welcome " + name + "!");

        // Clear fields after registration
        textfield3.clear();
        textfield4.clear();
        textfield5.clear();
    }

    @FXML
    private void backlogin(ActionEvent event) {
        try {
            // Load login screen and set scene
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) btn4.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
