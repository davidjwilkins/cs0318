/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import static java.time.temporal.TemporalQueries.localDate;
import java.util.ResourceBundle;
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
    protected ChoiceBox customerSelect;
    
    @FXML
    protected DatePicker startDate, endDate;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void handleCancelAction(ActionEvent event) {
        this.setScene("Main");
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
        app.setStart(java.sql.Date.valueOf(startDate.getValue()));
        app.setEnd(java.sql.Date.valueOf(endDate.getValue()));
        app.setCustomer((Customer) customerSelect.getValue());
        app.setUser(c.getUser());
        c.getUser().addAppointment(app);
        this.setScene("Main");
    }    
    
    @Override
    protected void refresh() {
        Context c = Context.getInstance();
        Appointment app = c.getAppointment();
        int id = app.getAppointmentId();
        if (id != 0) {
            idText.setText(Integer.toString(id));
        } else {
            idText.setText("New Record");
        }
        titleText.setText(app.getTitle());
        locationText.setText(app.getLocation());
        contactText.setText(app.getContact());
        typeText.setText(app.getType());
        urlText.setText(app.getUrl());
        descriptionText.setText(app.getDescription());
        
        if(app.getStart() != null) {
            LocalDate start = app.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            startDate.setValue(start);
        }
        if (app.getEnd() != null) {
            LocalDate end = app.getEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            endDate.setValue(end);
        }
        customerSelect.setValue(app.getCustomer());
    }
}
