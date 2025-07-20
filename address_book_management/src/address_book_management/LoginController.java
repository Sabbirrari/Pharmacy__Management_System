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

public class LoginController implements Initializable  {
    
    @FXML
    private TextField textfield1;  // email
    @FXML
    private TextField textfield2;  // password
    @FXML
    private Button btn1;  // login button
    @FXML
    private Button btn2;  // create new account button
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization if needed
    }    

    @FXML
    private void login(ActionEvent event) {
        String email = textfield1.getText().trim();
        String password = textfield2.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Please enter email and password");
            return;
        }

        if (email.equals("user@gmail.com") && password.equals("1234")) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome " + email + "!");

            try {
                Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                Stage stage = (Stage) btn1.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Dashboard");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Loading Error", "Could not open dashboard");
            }

        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password");
        }
    }

    @FXML
    private void createnew(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("registration.fxml"));
            Stage stage = (Stage) btn2.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Registration");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Could not open registration form");
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
