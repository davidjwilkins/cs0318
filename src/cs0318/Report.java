/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.text.DateFormatSymbols;
import javafx.collections.ObservableList;

/**
 *
 * @author david.wilkins
 */
public class Report {
    protected int month;
    protected String type;
    protected ObservableList<Appointment> appointments;
    
    public Report(int month, ObservableList appointments) {
        this.month = month;
        this.appointments = appointments;
    }

    public Report(String type, ObservableList appointments) {
        this.type = type;
        this.appointments = appointments;
    }
    
    public String getMonth() {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    public Integer getNumberOfAppointmentTypes() {
        return appointments.filtered((Appointment a) -> a.getStart().getMonthValue() == month).size();
    }
    
    public String getType() {
        return this.type;
    }
    
    public Integer getNumberOfAppointments() {
        return appointments.filtered((Appointment a) -> a.getType().equals(this.type)).size();
    }
}
