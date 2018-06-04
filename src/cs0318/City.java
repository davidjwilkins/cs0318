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
public class City extends Entity {
    private int cityId;
    private String city;
    private int countryId;
    private Country country;
    private final ObservableList<Address> addresses;
    private final HashMap<Integer, Address> addressIds;
    
    public City() {
        this.addresses = FXCollections.observableArrayList();
        
        this.addressIds = new HashMap<>();
        addresses.addListener((ListChangeListener<Address>) c -> {
            while (c.next()) {
                if (c.wasPermutated()) {
                    for (int i = c.getFrom(); i < c.getTo(); ++i) {
                         //permutate
                    }
                } else if (c.wasUpdated()) {
                         //update item
                } else {
                    for (Address removedAddress : c.getRemoved()) {
                        addressIds.remove(removedAddress.getAddressId());
                    }
                    for (Address addedAddress : c.getAddedSubList()) {
                        addressIds.put(addedAddress.getAddressId(), addedAddress);
                    }
                }
            }
        });
    }
    
    public void setCountry(Country country) {
        this.country = country;
        if (country != null) {
            this.setCountryId(country.getCountryId());
        } else {
            this.setCountryId(0);
        }
    }
    
    public Country getCountry() {
        return this.country;
    }
    
    public void addAddress(Address address) {
        address.setCity(this);
        this.addresses.add(address);
    }
    
    public void deleteAddress(Address address) {
        this.addresses.remove(address);
        if (address.getCity().equals(this)) {
            address.setCity(null);
        }
    }
    
    public Address getAddress(int addressId) {
        return this.addressIds.get(addressId);
    }
    
    public ObservableList<Address> getAddresses() {
        return this.addresses;
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
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the countryId
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * @param countryId the countryId to set
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
