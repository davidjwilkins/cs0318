/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.util.ArrayList;

/**
 *
 * @author david.wilkins
 */
public class Context {
    protected static Context instance = null;
    protected User user;
    protected Appointment appointment;
    protected Customer customer;
    protected ArrayList<Country> countries;
    
    private Context() {
        if (instance == null) this.instance = this;       
        this.countries = new ArrayList<>();
    }
    
    public static Context getInstance()
    {
        if (instance == null)
            instance = new Context();
 
        return instance;
    }
    
    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
    }
    
    public ArrayList<Country> getCountries() {
        return this.countries;
    }
    
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    
    public Appointment getAppointment() {
        return appointment;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public User getUser() {
        return user;
    }
}
