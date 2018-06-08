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
public class Country extends Entity {
    private int countryId;
    private String country;
    protected ObservableList<City> cities;
    protected HashMap<Integer, City> cityIds;
    
    public Country() {
        this.cities = FXCollections.observableArrayList();
        this.cityIds = new HashMap<>();
        cities.addListener((ListChangeListener<City>) c -> {
            while (c.next()) {
                if (c.wasPermutated()) {
                    for (int i = c.getFrom(); i < c.getTo(); ++i) {
                         //permutate
                    }
                } else if (c.wasUpdated()) {
                         //update item
                } else {
                    for (City removed : c.getRemoved()) {
                        cityIds.remove(removed.getCityId());
                    }
                    for (City added : c.getAddedSubList()) {
                        cityIds.put(added.getCityId(), added);
                    }
                }
            }
        });
    }
    
    public void addCity(City city) {
        city.setCountry(this);
        this.cities.add(city);
    }
    
    public void deleteCity(City city) {
        if (city.getCountry().equals(this)) {
            city.setCountry(null);
        }
        this.cities.remove(city);
    }
    
    public void deleteCity(int cityId) {
        this.deleteCity(this.getCity(cityId));
    }
    
    public City getCity(int cityId) {
        return this.cityIds.get(cityId);
    }
    
    public ObservableList<City> getCities() {
        return this.cities;
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

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }
    
        @Override
    public String toString() {
        return this.getCountry();
    }
}
