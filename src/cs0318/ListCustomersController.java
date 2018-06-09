/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author david.wilkins
 */
public class ListCustomersController extends SceneChangerController implements Initializable {
    @FXML
    protected TableView customersList;
    @FXML
    protected Button editCustomerButton, deleteCustomerButton;
    
    protected ResourceBundle rb;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        this.customersList.setItems(Context.getInstance().getCustomers());
        System.out.println(Context.getInstance().getCustomers().size());
        this.customersList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {;
            boolean disabled = newValue == null;
            System.out.println(disabled);
            editCustomerButton.setDisable(disabled);
            deleteCustomerButton.setDisable(disabled);
        });
    }    

    @FXML
    private void handleEditAction(ActionEvent event) {
        Object selected = customersList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Customer c = (Customer) selected;
            Context.getInstance().setCustomer(c);
            this.setScene("Customer");
        }
    }
    
    @FXML
    private void handleDeleteAction(ActionEvent event) {
        try {
            Customer customer = (Customer) customersList.getSelectionModel().getSelectedItem();
            DB.connect().deleteCustomer(customer);
            for (Appointment a: customer.getAppointments()) {
                Context.getInstance().getUser().getAppointments().remove(a);
            }
            Context.getInstance().getCustomers().remove(customer);
        } catch (SQLException e) {
            errorMessage(rb.getString("couldNotDeleteCustomer"), e);
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage(rb.getString("unknownError"), e);
        }
    }
    
    @FXML
    private void handleCancelAction(ActionEvent event) {
        this.setScene("Main");
    }
    @Override
    protected void refresh() {
        editCustomerButton.setDisable(true);
        deleteCustomerButton.setDisable(true);
        this.customersList.getSelectionModel().clearSelection();
        
        System.out.println(Context.getInstance().getCustomers().size());
    }
    
}
