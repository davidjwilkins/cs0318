/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.HashMap;
import javafx.collections.ListChangeListener;
/**
 *
 * @author david.wilkins
 */
public class User extends Entity {
    private int userId;
    private String userName;
    private String password;
    private boolean active;
    protected ObservableList<Appointment> appointments;
    protected HashMap<Integer, Appointment> appointmentIds;
    
    public User() {
        appointments = FXCollections.observableArrayList();
        appointmentIds = new HashMap<>();
        appointments.addListener((ListChangeListener<Appointment>) c -> {
            while (c.next()) {
                if (c.wasPermutated()) {
                    for (int i = c.getFrom(); i < c.getTo(); ++i) {
                         //permutate
                    }
                } else if (c.wasUpdated()) {
                         //update item
                } else {
                    for (Appointment removed : c.getRemoved()) {
                        appointmentIds.remove(removed.getAppointmentId());
                    }
                    for (Appointment added : c.getAddedSubList()) {
                        appointmentIds.put(added.getAppointmentId(), added);
                    }
                }
            }
        });
    }
    
    public void addAppointment(Appointment appointment) {
        if (!appointment.getUser().equals(this)) {
            appointment.setUser(this);
        }
        if (!this.appointments.contains(appointment)) {
            this.appointments.add(appointment);
        }
    }
    
    public Appointment getAppointment(int appointmentId) {
        return this.appointmentIds.get(appointmentId);
    }
    
    public void deleteAppointment(int appointmentId) {
        this.deleteAppointment(this.getAppointment(appointmentId));
    }
    
    public void deleteAppointment(Appointment appointment) {
        if (appointment.getUser().equals(this)) {
            appointment.setUser(null);
        }
        this.appointments.remove(appointment);
    }
    
    public ObservableList<Appointment> getAppointments() {
        return this.appointments;
    }
    
    public ObservableList<Appointment> getAppointments(LocalDate d) {
        return this.appointments.filtered((Appointment appointment) -> {
            return isSameDay(appointment.getStart(), d);
        }).sorted((Appointment a, Appointment b) -> {
            return a.getStart().compareTo(b.getStart());
        });
    }
    
    private boolean isSameDay(ZonedDateTime date1, LocalDate date2) {
        return date1.toLocalDate().getDayOfYear() == date2.getDayOfYear() &&
                date1.getYear() == date2.getYear();
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }
    
    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public boolean hasOverlappingAppointment(Appointment a) {
        for (Appointment b: this.getAppointments()) {
            if (a.equals(b)) {
                continue;
            }
            if (a.getStart().isEqual(b.getStart()) || b.getEnd().isEqual(b.getEnd())) {
                return true;
            }
            if (a.getEnd().isAfter(b.getStart()) && a.getEnd().isBefore(b.getEnd())) {
                return true;
            }
            if (a.getStart().isBefore(b.getEnd()) && a.getStart().isAfter(b.getStart())) {
                return true;
            }
        }
        return false;
    }
    
    public Appointment getUpcomingAppointment() {
        long current = System.currentTimeMillis();
        for (Appointment a: this.getAppointments()) {
            long appointment = a.getStart().toInstant().toEpochMilli();
            long difference = appointment - current;
            if (difference >= 0 && difference <= (15 * 60 * 1000)) {
                return a;
            }
        }
        return null;
    }
}
