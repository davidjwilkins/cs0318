/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author david.wilkins
 */
public class CustomerController extends SceneChangerController implements Initializable {
    protected ResourceBundle rb;
    protected Country selectedCountry;
    protected City selectedCity;
    @FXML
    protected Label idLabel, nameLabel, addressLabel, address2Label, cityLabel,
            countryLabel, postalCodeLabel, phoneLabel;
    
    @FXML
    protected TextField idText, nameText, addressText, address2Text,
             postalCodeText, phoneText;
    
    @FXML
    protected ChoiceBox countrySelect, citySelect;
    
    @FXML RadioButton activeButton, inactiveButton;
    @FXML ToggleGroup active;
    
    @FXML Label title;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        // TODO
        idText.setDisable(true);
        countrySelect.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            Country country = (Country) countrySelect.getItems().get((Integer) newValue);
            citySelect.setValue(null);
            citySelect.setItems(country.getCities());
            citySelect.setDisable(false);
            this.selectedCountry = country;
        });
        citySelect.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (!newValue.equals(-1)) {
                this.selectedCity = (City) citySelect.getItems().get((Integer) newValue);
            }
        });
        title.setText(rb.getString("create") + " " + rb.getString("customer"));
        activeButton.setSelected(true);
    }    
    
    @FXML
    private void handleCancelAction(ActionEvent event) {
        this.setScene("Main");
    }
    
    @FXML
    private void handleSaveAction(ActionEvent event) {
        try {
            Customer customer = Context.getInstance().getCustomer();
            customer.setCustomerName(nameText.getText());
            customer.setActive(activeButton.isSelected());
            Address address = customer.getAddress();
            address.setAddress(addressText.getText());
            address.setAddress2(address2Text.getText());
            address.setCity((City) citySelect.getValue());
            address.setPostalCode(postalCodeText.getText());
            address.setPhone(phoneText.getText());
            if (customer.getCustomerName().isEmpty()) {
                throw new Exception("Customer name is required!");
            } else if (address.getAddress().isEmpty()) {
                throw new Exception("Address is required!");
            } else if (address.getCity() == null) {
                throw new Exception("City is required!");
            } else if (address.getPostalCode().isEmpty()) {
                throw new Exception("Postal code is required!");
            } else if (address.getPhone().isEmpty()) {
                throw new Exception("Phone number is required");
            }
            try {
                DB.connect().upsertAddress(address);
                customer.setAddress(address);
                DB.connect().upsertCustomer(customer);
            } catch(SQLException e) {
                errorMessage(rb.getString("couldNotSaveCustomer"), e);
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage(rb.getString("unknownError"), e);
            }
        } catch (Exception e) {
            errorMessage("Invalid Customer Data", e);
        }
        this.setScene("Main");
    }    
    
    @Override
    protected void refresh() {
        Customer c = Context.getInstance().getCustomer();
        if (c != null) {
            int id = c.getCustomerId();
            if (id == 0) {
                idText.setText(rb.getString("newRecord"));
                title.setText(rb.getString("create") + " " + rb.getString("customer"));
            } else {
                idText.setText(Integer.toString(id));
                title.setText(rb.getString("edit") + " " + rb.getString("customer"));
            }
            nameText.setText(c.getCustomerName());
            addressText.setText(c.getAddress().getAddress());
            address2Text.setText(c.getAddress().getAddress2());
            citySelect.setValue(c.getAddress().getCity());
            postalCodeText.setText(c.getAddress().getPostalCode());
            phoneText.setText(c.getAddress().getPhone());
            countrySelect.setItems(FXCollections.observableArrayList(Context.getInstance().getCountries()));
            countrySelect.setValue(c.getAddress().getCity().getCountry());
            citySelect.setItems(c.getAddress().getCity().getCountry().getCities());
            citySelect.setValue(c.getAddress().getCity());
            if (c.getAddress().getCity().getCountry().getCountryId() == 0) {
                citySelect.setDisable(true);
            } else {
                citySelect.setDisable(false);
            }
            activeButton.setSelected(c.isActive());
            inactiveButton.setSelected(!c.isActive());
        }
    }
}
