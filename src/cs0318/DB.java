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
import java.sql.Statement;
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
            statements.put("customers", connection.prepareStatement("SELECT * FROM customer"));
            statements.put("addresses", connection.prepareStatement("SELECT * FROM address"));
            statements.put("appointments", connection.prepareStatement("SELECT * FROM appointments"));
            statements.put("insertCustomer", connection.prepareStatement("INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdateBy) "
                    + "VALUES(?, ?, ?, NOW(), ?, '')", Statement.RETURN_GENERATED_KEYS));
            statements.put("updateCustomer", connection.prepareStatement("UPDATE customers SET customerName = ?, addressId = ?, active = ?, lastUpdate = NOW(), lastUpdatedBy = ? "
                    + "WHERE id = ?"));
            statements.put("insertAddress", connection.prepareStatement("INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) "
                    + "VALUES(?, ?, ?, ?, ?, NOW(), ?, '')", Statement.RETURN_GENERATED_KEYS)); 
            statements.put("updateAddress", connection.prepareStatement("UPDATE address SET address = ?, address2 = ?, postalCode = ?, phone = ?, "
                    + "postalCode = ?, lastUpdate = NOW(), lastUpdateBy = ? WHERE id = ?"));
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
    
    public void upsertCustomer(Customer customer) throws SQLException, Exception {
        PreparedStatement s;
        if (customer.getCustomerId() == 0) {
            s = (PreparedStatement) statements.get("insertCustomer");
        } else {
            s = (PreparedStatement) statements.get("updateCustomer");
            s.setInt(5, customer.getCustomerId());
        }
            //customerName, addressId, active, NOW(), createdBy
        s.setString(1, customer.getCustomerName());
        s.setInt(2, customer.getAddress().getAddressId());
        s.setBoolean(3, customer.isActive());
        s.setString(4, Context.getInstance().getUser().getUserName());
        s.executeUpdate();
        if (customer.getCustomerId() == 0) {
            ResultSet exists = s.getGeneratedKeys();
            System.out.println(exists);
            if(exists.next()){
                customer.setAddressId(exists.getInt(1));
            } else {
                throw new Exception("Could not create/update customer");
            }
        }
        Context.getInstance().addCustomer(customer);
    }
    
    public void upsertAddress(Address address) throws SQLException, Exception {
        PreparedStatement s;
        if (address.getAddressId() == 0) {
            s = (PreparedStatement) statements.get("insertAddress");
        } else {
            s = (PreparedStatement) statements.get("updateAddress");
            s.setInt(7, address.getAddressId());
        }
        s.setString(1, address.getAddress());
        s.setString(2, address.getAddress2());
        s.setInt(3, address.getCityId());
        s.setString(4, address.getPostalCode());
        s.setString(5, address.getPhone());
        s.setString(6, Context.getInstance().getUser().getUserName());
        s.executeUpdate();
        if (address.getAddressId() == 0) {
            ResultSet exists = s.getGeneratedKeys();
            System.out.println(exists);
            if(exists.next()){
                address.setAddressId(exists.getInt(1));
            } else {
                System.out.println(exists);
                throw new Exception("Could not create/update address");
            }
        }
    }
    
    protected void populate() throws SQLException, Exception {
        PreparedStatement countriesQuery = (PreparedStatement) statements.get("countries");
        PreparedStatement citiesQuery = (PreparedStatement) statements.get("cities");
        PreparedStatement addressesQuery = (PreparedStatement) statements.get("addresses");
        PreparedStatement customersQuery = (PreparedStatement) statements.get("customers");
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
        
        HashMap<Integer, City> cityMap = new HashMap<>();
        ResultSet cities = citiesQuery.executeQuery();
        while(cities.next()) {
            City c = new City();
            Integer countryId = cities.getInt("countryId");
            c.setCityId(cities.getInt("cityId"));
            c.setCountryId(countryId);
            c.setCity(cities.getString("city"));
            ((Country) countryMap.get(countryId)).addCity(c);
            cityMap.put(c.getCityId(), c);
        }
        
        HashMap<Integer, Address> addressMap = new HashMap<>();
        ResultSet addresses = addressesQuery.executeQuery();
        while(addresses.next()) {
            Address a = new Address();
            Integer addressId = addresses.getInt("addressId");
            a.setAddressId(addressId);
            a.setAddress(addresses.getString("address"));
            a.setAddress2(addresses.getString("address2"));
            a.setCityId(addresses.getInt("cityId"));
            a.setCity(cityMap.get(a.getCityId()));
            a.setPhone(addresses.getString("phone"));
            a.setPostalCode(addresses.getString("postalCode"));
            addressMap.put(a.getAddressId(), a);
        }
        
        HashMap<Integer, Customer> customerMap = new HashMap<>();
        ResultSet customers = customersQuery.executeQuery();
        while(customers.next()) {
            Customer c = new Customer();
            Integer customerId = customers.getInt("customerId");
            c.setCustomerId(customerId);
            c.setActive(customers.getBoolean("active"));
            c.setAddressId(customers.getInt("addressId"));
            c.setAddress(addressMap.get(c.getAddressId()));
            c.setCustomerName(customers.getString("customerName"));
            customerMap.put(customerId, c);
            context.addCustomer(c);
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