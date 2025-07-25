package address_book_management;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

    @FXML
    private TextField c_name;
    @FXML
    private TextField c_address;
    @FXML
    private TextField c_phone;
    @FXML
    private TextField c_email;
    @FXML
    private TextField c_gender;
    @FXML
    private Button btn5;  // Add
    @FXML
    private Button btn6;  // Delete
    @FXML
    private Button btn7;  // Update
    @FXML
    private TableView<Contact> tableview;
    @FXML
    private TextField c_search;
    @FXML
    private Button btn8; // logout

    ObservableList<Contact> contactList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadContactsFromDB();

        // Live search implementation
        FilteredList<Contact> filteredData = new FilteredList<>(contactList, p -> true);
        c_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(contact -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return contact.getName().toLowerCase().contains(lowerCaseFilter)
                        || contact.getPhone().toLowerCase().contains(lowerCaseFilter)
                        || contact.getEmail().toLowerCase().contains(lowerCaseFilter)
                        || contact.getAddress().toLowerCase().contains(lowerCaseFilter)
                        || contact.getGender().toLowerCase().contains(lowerCaseFilter);
            });
            tableview.setItems(filteredData);
        });

        tableview.setOnMouseClicked(event -> {
            Contact selected = tableview.getSelectionModel().getSelectedItem();
            if (selected != null) {
                c_name.setText(selected.getName());
                c_address.setText(selected.getAddress());
                c_phone.setText(selected.getPhone());
                c_email.setText(selected.getEmail());
                c_gender.setText(selected.getGender());
            }
        });
    }

    private void setupTable() {
        TableColumn<Contact, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Contact, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Contact, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Contact, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Contact, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

        tableview.getColumns().clear();
        tableview.getColumns().addAll(nameCol, addressCol, phoneCol, emailCol, genderCol);
        tableview.setItems(contactList);
    }

    private void loadContactsFromDB() {
        contactList.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM contacts";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Contact c = new Contact(
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("gender")
                );
                contactList.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void added(ActionEvent event) {
        String name = c_name.getText().trim();
        String address = c_address.getText().trim();
        String phone = c_phone.getText().trim();
        String email = c_email.getText().trim();
        String gender = c_gender.getText().trim();

        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Name is required");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO contacts (name, address, phone, email, gender) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, gender);
            stmt.executeUpdate();

            contactList.add(new Contact(name, address, phone, email, gender));
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add contact");
        }
    }

    @FXML
    private void delete(ActionEvent event) {
        Contact selected = tableview.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a contact to delete");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM contacts WHERE name = ? AND phone = ?"; 
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, selected.getName());
            stmt.setString(2, selected.getPhone());
            int affected = stmt.executeUpdate();

            if (affected > 0) {
                contactList.remove(selected);
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Delete Error", "Could not delete contact from database");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete contact");
        }
    }

    @FXML
    private void update(ActionEvent event) {
        Contact selected = tableview.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a contact to update");
            return;
        }

        String name = c_name.getText().trim();
        String address = c_address.getText().trim();
        String phone = c_phone.getText().trim();
        String email = c_email.getText().trim();
        String gender = c_gender.getText().trim();

        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Name is required");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE contacts SET name=?, address=?, phone=?, email=?, gender=? WHERE name=? AND phone=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, gender);
            stmt.setString(6, selected.getName());
            stmt.setString(7, selected.getPhone());
            int affected = stmt.executeUpdate();

            if (affected > 0) {
                selected.setName(name);
                selected.setAddress(address);
                selected.setPhone(phone);
                selected.setEmail(email);
                selected.setGender(gender);
                tableview.refresh();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Error", "Could not update contact in database");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update contact");
        }
    }

    private void clearFields() {
        c_name.clear();
        c_address.clear();
        c_phone.clear();
        c_email.clear();
        c_gender.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
