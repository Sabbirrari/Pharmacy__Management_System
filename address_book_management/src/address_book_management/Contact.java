package address_book_management;

import javafx.beans.property.SimpleStringProperty;

public class Contact {
    private SimpleStringProperty name;
    private SimpleStringProperty address;
    private SimpleStringProperty phone;
    private SimpleStringProperty email;
    private SimpleStringProperty gender;

    public Contact(String name, String address, String phone, String email, String gender) {
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.gender = new SimpleStringProperty(gender);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String value) {
        address.set(value);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String value) {
        phone.set(value);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String value) {
        email.set(value);
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String value) {
        gender.set(value);
    }
}
