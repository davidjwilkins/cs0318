/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author david.wilkins
 */
public class ConsultantScheduleReportController extends SceneChangerController implements Initializable {

    @FXML
    DatePicker scheduleDate;
    @FXML
    GridPane schedule;
    
    @FXML
    private void setDate(ActionEvent event) {
        LocalDate date = scheduleDate.getValue();
        renderSchedule(date);
    }
    
    @FXML
    private void goBack(ActionEvent event) {
        this.setScene("Main");
    }
    
    public void renderSchedule(LocalDate date) {
        schedule.getChildren().remove(0, schedule.getChildren().size());
        ObservableList<User> users = Context.getInstance().users;
        ObservableList<Appointment> appointments;
        int row = 0;
        try {
            appointments = DB.connect().getAllAppointments();

            for (User u : users) {
                Text t = new Text();
                t.setText("Schedule for " + u.getUserName());
                schedule.add(t, 0, row++, 2, 1);
                ObservableList<Appointment> userAppointments = appointments.filtered((Appointment appointment) -> {
                    return appointment.getUserId() == u.getUserId();
                });
                u.setAppointments(userAppointments);
                ObservableList<Appointment> daysAppointments = u.getAppointments(date);
                for (Appointment a : u.getAppointments(date)) {
                    System.out.println("FOUND");
                    Label timeLabel = new Label();
                    timeLabel.setText(a.getTitle() + ": " +
                            a.getStart().format(DateTimeFormatter.ISO_LOCAL_TIME) + " - " +
                            a.getEnd().format(DateTimeFormatter.ISO_LOCAL_TIME) + 
                            " with " + 
                            a.getCustomer().getCustomerName());
                    schedule.add(timeLabel, 0, row++, 2, 1);
                };
                Text blank = new Text();
                blank.setText("\n");
                schedule.add(blank, 0, row++, 2, 1);
                row++;
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConsultantScheduleReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        refresh();
    }    

    @Override
    protected void refresh() {
        LocalDate now = LocalDate.now();
        scheduleDate.setValue(now);
        renderSchedule(now);
    }
    
}
