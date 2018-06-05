/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
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
}
