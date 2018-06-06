/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import javafx.collections.ObservableList;
import javafx.util.StringConverter;

/**
 *
 * @author david
 */
public class CityStringConverter extends StringConverter<City> {

    @Override
    public String toString(City object) {
        return object.getCity();
    }

    @Override
    public City fromString(String string) {
            ObservableList<City> cities = Context.getInstance().getCountry().getCities();
            for(City city: cities) {
                if (city.getCity().equals(string)) {
                    return city;
                }
            }
            return null;
    }    
}
