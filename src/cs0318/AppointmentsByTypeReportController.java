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
import java.util.HashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;


/**
 * FXML Controller class
 *
 * @author david.wilkins
 */
public class AppointmentsByTypeReportController extends SceneChangerController implements Initializable {

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
        ObservableList<Report> report = FXCollections.observableArrayList();
        Set<String> types = new HashSet<>();
        this.appointments.forEach(a -> types.add(a.getType()));
        types.forEach((String t) -> report.add(new Report(t, this.appointments)));
        table.setItems(report);
    }
    
}
