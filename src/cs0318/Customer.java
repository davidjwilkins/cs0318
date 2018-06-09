/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.util.HashMap;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;

/**
 *
 * @author david.wilkins
 */
public class Customer extends Entity {
    private int customerId;
    private String customerName = "";
    private int addressId;
    private Address address;
    private boolean active = true;
    private ObservableList<Appointment> appointments;
    private HashMap<Integer, Appointment> appointmentIds;
    
    public Customer() {
        this.appointments = FXCollections.observableArrayList();
        this.appointmentIds = new HashMap<>();
        this.address = new Address();
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
    
    public void setAddress(Address address) {
        this.address = address;
        if (address != null) {
            this.setAddressId(address.getAddressId());
        }
    }
    
    public Address getAddress() {
        return this.address;
    }
    
    public void addAppointment(Appointment appointment) {
        if (!this.appointments.contains(appointment)) {
            this.appointments.add(appointment);
        }
        if (!appointment.getCustomer().equals(this)) {
            appointment.setCustomer(this);
        }
    }
    
    public void deleteAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
    }
    
    public void deleteAppointment(int appointmentId) {
        this.deleteAppointment(this.getAppointment(appointmentId));
    }
    
    public Appointment getAppointment(int appointmentId) {
        return this.appointmentIds.get(appointmentId);
    }
    
    public ObservableList<Appointment> getAppointments() {
        return this.appointments;
    }
    
    /**
     * @return the customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the addressId
     */
    public int getAddressId() {
        return addressId;
    }

    /**
     * @param addressId the addressId to set
     */
    public void setAddressId(int addressId) {
        this.addressId = addressId;
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
    
        @Override
    public String toString() {
        return this.getCustomerName();
    }
}
