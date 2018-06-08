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
            this.selectedCity = (City) citySelect.getItems().get((Integer) newValue);
        });
    }    
    
    @FXML
    private void handleCancelAction(ActionEvent event) {
        this.setScene("Main");
    }
    
    @FXML
    private void handleSaveAction(ActionEvent event) {
        Customer customer = Context.getInstance().getCustomer();
        Address address = customer.getAddress();
        address.setAddress(addressText.getText());
        address.setAddress2(address2Text.getText());
        address.setCity((City) citySelect.getValue());
        address.setPostalCode(postalCodeText.getText());
        address.setPhone(phoneText.getText());
        try {
            DB.connect().upsertAddress(address);
            customer.setCustomerName(nameText.getText());
            customer.setAddress(address);
            DB.connect().upsertCustomer(customer);
        } catch(SQLException e) {
            errorMessage(rb.getString("couldNotSaveCustomer"), e);
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage(rb.getString("unknownError"), e);
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
            } else {
                idText.setText(Integer.toString(id));
            }
            nameText.setText(c.getCustomerName());
            addressText.setText(c.getAddress().getAddress());
            address2Text.setText(c.getAddress().getAddress2());
            citySelect.setValue(c.getAddress().getCity());
            postalCodeText.setText(c.getAddress().getPostalCode());
            phoneText.setText(c.getAddress().getPhone());
            countrySelect.setItems(FXCollections.observableArrayList(Context.getInstance().getCountries()));
            citySelect.setItems(c.getAddress().getCity().getCountry().getCities());
            citySelect.setValue(c.getAddress().getCity());
            countrySelect.setValue(c.getAddress().getCity().getCountry());
            if (c.getAddress().getCity().getCountry().getCountryId() == 0) {
                citySelect.setDisable(true);
            } else {
                citySelect.setDisable(false);
            }
        }
    }
}
