/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DB {
    
    private static DB instance;
    private Connection connection = null;
    private HashMap<String, PreparedStatement> statements;
    private DB(){
        statements = new HashMap<>();
        System.out.println("Connecting to the database");
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://52.206.157.109:3306/U04pAX", "U04pAX", "53688308408");;
        }catch (ClassNotFoundException ce){
            ce.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Connected!");
         try {
            statements.put("login", connection.prepareStatement("SELECT * FROM user WHERE userName=? AND password=?"));
            statements.put("countries", connection.prepareStatement("SELECT * FROM country"));
            statements.put("cities", connection.prepareStatement("SELECT * FROM city"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //Returns Connection
    public static DB connect(){
        if (instance == null) {
            instance = new DB();
            System.out.println(instance);
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public void login(User user) throws SQLException, Exception {
        PreparedStatement s = (PreparedStatement) statements.get("login");
        s.setString(1, user.getUserName());
        s.setString(2, user.getPassword());
        ResultSet exists = s.executeQuery();
        if(exists.next()){
            user.setUserId(exists.getInt("userId"));
            Context.getInstance().setUser(user);
            populate();
        } else {
            throw new Exception("This username/password combination does not exist");
        }
    }
    
    protected void populate() throws SQLException, Exception {
        PreparedStatement countriesQuery = (PreparedStatement) statements.get("countries");
        PreparedStatement citiesQuery = (PreparedStatement) statements.get("cities");
        Context context = Context.getInstance();
        
        ResultSet countries = countriesQuery.executeQuery();
        HashMap<Integer, Country> countryMap = new HashMap<>();
        while(countries.next()) {
            Country c = new Country();
            c.setCountryId(countries.getInt("countryId"));
            c.setCountry(countries.getString("country"));
            context.getCountries().add(c);
            countryMap.put(c.getCountryId(), c);
        }
        
        ResultSet cities = citiesQuery.executeQuery();
        while(cities.next()) {
            City c = new City();
            Integer countryId = cities.getInt("countryId");
            c.setCityId(cities.getInt("cityId"));
            c.setCountryId(countryId);
            c.setCity(cities.getString("city"));
            ((Country) countryMap.get(countryId)).addCity(c);
        }
    }
    //Closes connections
    public void close(){
        try{
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            System.out.println("Connection closed.");
        }
    }
    
}