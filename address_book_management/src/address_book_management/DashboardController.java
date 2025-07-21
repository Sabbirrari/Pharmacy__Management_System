package address_book_management;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

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
    private Button btn5;
    @FXML
    private Button btn6;
    @FXML
    private Button btn7;
    @FXML
    private TableView<Contact> tableview;

    ObservableList<Contact> contactList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

        tableview.getColumns().addAll(nameCol, addressCol, phoneCol, emailCol, genderCol);
        tableview.setItems(contactList);

        tableview.setOnMouseClicked((MouseEvent event) -> {
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

    @FXML
    private void added(ActionEvent event) {
        Contact contact = new Contact(
                c_name.getText(),
                c_address.getText(),
                c_phone.getText(),
                c_email.getText(),
                c_gender.getText()
        );
        contactList.add(contact);
        clearFields();
    }

    @FXML
    private void delete(ActionEvent event) {
        Contact selected = tableview.getSelectionModel().getSelectedItem();
        if (selected != null) {
            contactList.remove(selected);
            clearFields();
        }
    }

    @FXML
    private void update(ActionEvent event) {
        Contact selected = tableview.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setName(c_name.getText());
            selected.setAddress(c_address.getText());
            selected.setPhone(c_phone.getText());
            selected.setEmail(c_email.getText());
            selected.setGender(c_gender.getText());
            tableview.refresh();
            clearFields();
        }
    }

    private void clearFields() {
        c_name.clear();
        c_address.clear();
        c_phone.clear();
        c_email.clear();
        c_gender.clear();
    }
} 
