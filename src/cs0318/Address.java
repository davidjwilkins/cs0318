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
public class Address extends Entity {
    private int addressId;
    private String address;
    private String address2;
    private int cityId;
    protected City city;
    private String postalCode;
    private String phone;
    private final ObservableList<Customer> customers;
    private final HashMap<Integer, Customer> customerIds;
    
    
    public Address() {
        this.city = new City();
        this.customers = FXCollections.observableArrayList();
        this.customerIds = new HashMap<>();
        customers.addListener((ListChangeListener<Customer>) c -> {
            while (c.next()) {
                if (c.wasPermutated()) {
                    for (int i = c.getFrom(); i < c.getTo(); ++i) {
                         //permutate
                    }
                } else if (c.wasUpdated()) {
                         //update item
                } else {
                    for (Customer removed : c.getRemoved()) {
                        customerIds.remove(removed.getCustomerId());
                    }
                    for (Customer added : c.getAddedSubList()) {
                        customerIds.put(added.getCustomerId(), added);
                    }
                }
            }
        });
    }
    
    public void setCity(City city) {
        this.city = city;
        if (city != null) {
            this.setCityId(city.getCityId());
        } else {
            this.setCityId(0);
        }
    }
    
    public City getCity() {
        return this.city;
    }
    
    public void addCustomer(Customer customer) {
        this.customers.add(customer);
    }
    
    public Customer getCustomer(int customerId) {
        return this.customerIds.get(customerId);
    }
    
    public void deleteCustomer(Customer customer) {
        if (customer.getAddress().equals(this)) {
            customer.setAddress(null);
        }
        this.customers.remove(customer);
    }
    
    public void deleteCustomer(int customerId) {
        this.deleteCustomer(this.getCustomer(customerId));
    }
    
    public ObservableList<Customer> getCustomers() {
        return this.customers;
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
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the address2
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @param address2 the address2 to set
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return the cityId
     */
    public int getCityId() {
        return cityId;
    }

    /**
     * @param cityId the cityId to set
     */
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
