/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
/**
 *
 * @author david.wilkins
 */
public class MainController extends SceneChangerController implements Initializable {
    
    @FXML
    private Button addAppointmentButton, addCustomerButton;
    
    @FXML
    private void addAppointmentAction(ActionEvent event) {
        Context.getInstance().setAppointment(new Appointment());
        this.setScene("Appointment");
    }
    
    @FXML
    private void addCustomerAction(ActionEvent event) {
        Context.getInstance().setCustomer(new Customer());
        this.setScene("Customer");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @Override
    protected void refresh() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
