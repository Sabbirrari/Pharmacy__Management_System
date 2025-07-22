package address_book_management;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    private TextField textfield3;  // Name
    @FXML
    private TextField textfield4;  // Email
    @FXML
    private TextField textfield5;  // Phone 
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
        String name = textfield3.getText().trim();
        String email = textfield4.getText().trim();
        String phone = textfield5.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter all fields");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO users (name, email, phone) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Welcome " + name + "!");
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not save user. Try again.");
        }
    }

    @FXML
    private void backlogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) btn4.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        textfield3.clear();
        textfield4.clear();
        textfield5.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
