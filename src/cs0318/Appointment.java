/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author david.wilkins
 */
public class Appointment extends Entity {
    private int appointmentId;
    private int customerId;
    private int userId;
    private String title = "";
    private String description = "";
    private String location = "";
    private String contact = "";
    private String type = "";
    private String url = "";
    private ZonedDateTime start;
    private ZonedDateTime end;
    private User user;
    private Customer customer;
    
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.setUserId(user.getUserId());
            user.addAppointment(this);
        } else {
            this.setUserId(0);
        }
    }
    
    public User getUser() {
        return this.user;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null) {
            this.setCustomerId(customer.getCustomerId());
            customer.addAppointment(this);
        } else {
            this.setCustomerId(0);
        }
    }
    
    public Customer getCustomer() {
        return this.customer;
    }
    
    /**
     * @return the appointmentId
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * @param appointmentId the appointmentId to set
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the start
     */
    public ZonedDateTime getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public ZonedDateTime getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }
    
        @Override
    public String toString() {
        return start.format(DateTimeFormatter.ofPattern("h:mm a")) + "-" + end.format(DateTimeFormatter.ofPattern("h:mm a")) + " " + title;
    }
}
