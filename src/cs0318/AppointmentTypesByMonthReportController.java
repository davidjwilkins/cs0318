/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import java.text.DateFormatSymbols;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;


/**
 * FXML Controller class
 *
 * @author david.wilkins
 */
public class AppointmentTypesByMonthReportController extends SceneChangerController implements Initializable {

    @FXML
    protected TableView table;
    
    protected ObservableList<Appointment> appointments;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.refresh();
    }    
    
    @FXML
    private void goBack(ActionEvent event) {
        this.setScene("Main");
    }
    
    @Override
    protected void refresh() {
        try {
            this.appointments = DB.connect().getAllAppointments();
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentTypesByMonthReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObservableList<Report> months = FXCollections.observableArrayList();
        for (int i = 1; i <= 12; i++) {
            Report m = new Report(i, this.appointments);
            months.add(m);
        }
        table.setItems(months);
    }
    
}
