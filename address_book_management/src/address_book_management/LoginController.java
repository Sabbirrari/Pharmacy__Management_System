package address_book_management;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class LoginController implements Initializable {
    
    @FXML
    private TextField textfield1;  // Email
    @FXML
    private TextField textfield2;  // Phone 
    @FXML
    private Button btn1;  // Login button
    @FXML
    private Button btn2;  // Create new account button
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization if needed
    }    

    @FXML
    private void login(ActionEvent event) {
        String email = textfield1.getText().trim();
        String phone = textfield2.getText().trim();

        if (email.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Please enter email and phone");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE email = ? AND phone = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, phone);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome " + email + "!");

                Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                Stage stage = (Stage) btn1.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Dashboard");
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or phone");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not verify login");
        }
    }

    @FXML
    private void createnew(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("registration.fxml"));
            Stage stage = (Stage) btn2.getScene().getWindow();
            stage.setScene(new Scene(root));
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
