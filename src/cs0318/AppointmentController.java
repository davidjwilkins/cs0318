/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

/**
 * FXML Controller class
 *
 * @author david.wilkins
 */
public class AppointmentController extends SceneChangerController implements Initializable {
    protected Context context;
    protected Customer selectedCustomer;
    protected ResourceBundle rb;
    @FXML
    protected Label idLabel, customerLabel, titleLabel, descriptionLabel,
            locationLabel, contactLabel, typeLabel, urlLabel, startLabel,
            endLabel;
    
    @FXML
    protected TextField idText, titleText, locationText,
            contactText, typeText, urlText;
    
    @FXML
    protected TextArea descriptionText;
    
    @FXML
    protected ChoiceBox customerSelect, startDateHour, endDateHour, 
            startDateMinute, startDateMeridian, endDateMinute, endDateMeridian;
    
    @FXML
    protected DatePicker startDate;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        customerSelect.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.equals(-1)) {
                return;
            }
            this.selectedCustomer = (Customer) customerSelect.getItems().get((Integer) newValue);
        });
        idText.setDisable(true);
        ObservableList<String> hours = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        ObservableList<String> minutes = FXCollections.observableArrayList("05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60");
        ObservableList<String> meridians = FXCollections.observableArrayList(rb.getString("am"), rb.getString("pm"));
        startDateHour.setItems(hours);
        endDateHour.setItems(hours);
        startDateMinute.setItems(minutes);
        endDateMinute.setItems(minutes);
        startDateMeridian.setItems(meridians);
        endDateMeridian.setItems(meridians);
    }    
    
    @FXML
    private void handleCancelAction(ActionEvent event) {
        this.setScene("Main");
    }
    
    @FXML
    private void gotoCustomer(ActionEvent event) {
        Context.getInstance().setCustomer(selectedCustomer);
        this.setScene("Customer");
    }
    
    @FXML
    private void handleSaveAction(ActionEvent event) {
        Context c = Context.getInstance();
        Appointment app = c.getAppointment();
        app.setTitle(titleText.getText());
        app.setLocation(locationText.getText());
        app.setContact(contactText.getText());
        app.setType(typeText.getText());
        app.setUrl(urlText.getText());
        app.setDescription(descriptionText.getText());
        LocalDate appointmentStarts = startDate.getValue();
        LocalDate appointmentEnds = appointmentStarts;

        boolean isPM = startDateMeridian.getValue().equals(rb.getString("pm"));
        int hour = Integer.parseInt((String) startDateHour.getValue());
        int minute = Integer.parseInt((String) startDateMinute.getValue());
        if (hour == 12 && !isPM) {
            hour = 0;
        } else if (isPM) {
            hour += 12;
        }
        if (hour == 24) {
            hour = 12;
        }

        app.setStart(appointmentStarts.atTime(hour, minute, 0).atZone(ZoneId.systemDefault()));

       
        isPM = endDateMeridian.getValue().equals(rb.getString("pm"));
        hour = Integer.parseInt((String) endDateHour.getValue());
        minute = Integer.parseInt((String) endDateMinute.getValue());
        if (hour == 12 && !isPM) {
            hour = 0;
        }
        if (isPM) {
            hour += 12;
        }
        if (hour == 24) {
            hour = 12;
        }
        app.setEnd(appointmentEnds.atTime(hour, minute, 0).atZone(ZoneId.systemDefault()));
        try {
            if (app.getStart().getHour() < 9 || app.getStart().getHour() > 6|| (
                     app.getStart().getHour() == 6 && app.getStart().getMinute() > 0)) {
                throw new Exception("Start time must be during normal business hours (9AM - 5PM)");
            }
             if (app.getEnd().getHour() < 9 || app.getEnd().getHour() > 6 || (
                     app.getEnd().getHour() == 6 && app.getEnd().getMinute() > 0)) {
                throw new Exception("End time must be during normal business hours (9AM - 5PM)");
            }
            app.setCustomer((Customer) customerSelect.getValue());
            app.setUser(c.getUser());
            if (c.getUser().hasOverlappingAppointment(app)) {
                c.getUser().getAppointments().remove(app);
                throw new Exception("This appointment would overlap an existing one!");
            }
            if (app.getStart().isAfter(app.getEnd())) {
                throw new Exception("Appointment must end after it starts");
            }
            try {
                DB.connect().upsertAppointment(app);
            } catch (SQLException e) {
                errorMessage(rb.getString("couldNotSaveAppointment"), e);
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage(rb.getString("unknownError"), e);
            }
            c.getUser().addAppointment(app);
            this.setScene("Main");
        } catch(Exception e) {
            errorMessage("Invalid Appointment", e);
        }
    }    
    
    @Override
    protected void refresh() {
        Context c = Context.getInstance();
        Appointment app = c.getAppointment();
        int id = app.getAppointmentId();
        if (id != 0) {
            idText.setText(Integer.toString(id));
        } else {
            idText.setText(rb.getString("newRecord"));
        }
        titleText.setText(app.getTitle());
        locationText.setText(app.getLocation());
        contactText.setText(app.getContact());
        typeText.setText(app.getType());
        urlText.setText(app.getUrl());
        descriptionText.setText(app.getDescription());
        
        if(app.getStart() != null) {
            startDate.setValue(app.getStart().toLocalDate());
            startDateHour.setValue(app.getStart().format(DateTimeFormatter.ofPattern("h")));
            startDateMinute.setValue(app.getStart().format(DateTimeFormatter.ofPattern("m")));
            startDateMeridian.setValue(rb.getString(app.getStart().format(DateTimeFormatter.ofPattern("a")).toLowerCase()));
        }
        if (app.getEnd() != null) {
            endDateHour.setValue(app.getEnd().format(DateTimeFormatter.ofPattern("h")));
            endDateMinute.setValue(app.getEnd().format(DateTimeFormatter.ofPattern("m")));
            endDateMeridian.setValue(rb.getString(app.getEnd().format(DateTimeFormatter.ofPattern("a")).toLowerCase()));
        }
        if (app.getCustomer() != null) {
            customerSelect.setValue(app.getCustomer());
        }
        
        customerSelect.setItems(Context.getInstance().getCustomers());
    }
}
