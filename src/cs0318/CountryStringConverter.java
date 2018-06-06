/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.util.ArrayList;
import javafx.util.StringConverter;

/**
 *
 * @author david
 */
public class CountryStringConverter extends StringConverter<Country> {

    @Override
    public String toString(Country object) {
        return object.getCountry();
    }

    @Override
    public Country fromString(String string) {
            ArrayList<Country> countries = Context.getInstance().getCountries();
            for(Country country: countries) {
                if (country.getCountry().equals(string)) {
                    return country;
                }
            }
            return null;
    }    
}
