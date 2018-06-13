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
import java.time.ZoneId;
import java.util.HashMap;
import java.util.function.BiConsumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
            statements.put("users", connection.prepareStatement("SELECT * FROM user"));
            statements.put("login", connection.prepareStatement("SELECT * FROM user WHERE userName=? AND password=?"));
            statements.put("countries", connection.prepareStatement("SELECT * FROM country"));
            statements.put("cities", connection.prepareStatement("SELECT * FROM city"));
            statements.put("customers", connection.prepareStatement("SELECT * FROM customer"));
            statements.put("addresses", connection.prepareStatement("SELECT * FROM address"));
            statements.put("appointments", connection.prepareStatement("SELECT * FROM appointment"));
            statements.put("userAppointments", connection.prepareStatement("SELECT * FROM appointment WHERE userId = ?"));
            statements.put("insertCustomer", connection.prepareStatement("INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdateBy) "
                    + "VALUES(?, ?, ?, NOW(), ?, '')", Statement.RETURN_GENERATED_KEYS));
            statements.put("updateCustomer", connection.prepareStatement("UPDATE customer SET customerName = ?, addressId = ?, active = ?, lastUpdate = NOW(), lastUpdateBy = ? "
                    + "WHERE customerId = ?"));
            statements.put("insertAddress", connection.prepareStatement("INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) "
                    + "VALUES(?, ?, ?, ?, ?, NOW(), ?, '')", Statement.RETURN_GENERATED_KEYS)); 
            statements.put("updateAddress", connection.prepareStatement("UPDATE address SET address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ?, "
                    + "lastUpdate = NOW(), lastUpdateBy = ? WHERE addressId = ?"));
            statements.put("insertAppointment", connection.prepareStatement("INSERT INTO appointment(customerId, title, description, location, contact, url, start, end, createDate,"
                    + " createdBy, lastUpdateBy, userId, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, '', ?, ?)", Statement.RETURN_GENERATED_KEYS));
            statements.put("updateAppointment", connection.prepareStatement("UPDATE appointment SET customerId = ?, title = ?, description = ?, location = ?, contact = ?, url = ?, "
                    + "start = ?, end = ?, lastUpdate = NOW(), lastUpdateBy = ?, type = ? WHERE appointmentId = ?"));
            statements.put("deleteAppointment", connection.prepareStatement("DELETE FROM appointment WHERE appointmentId = ?"));
            statements.put("deleteCustomer", connection.prepareStatement("DELETE FROM customer WHERE customerId = ?"));
            statements.put("deleteCustomerAppointments", connection.prepareStatement("DELETE FROM appointment WHERE customerId = ?"));
            statements.put("deleteAddress", connection.prepareStatement("DELETE FROM address WHERE addressId = ?"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //Returns Connection
    public static DB connect(){
        if (instance == null) {
            instance = new DB();
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
        // Try with resources...
        try (ResultSet exists = s.executeQuery()) {
            if(exists.next()){
                user.setUserId(exists.getInt("userId"));
                exists.close();
                Context.getInstance().setUser(user);
                populate();
            } else {
                exists.close();
                throw new Exception("This username/password combination does not exist");
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception("Error: " + e.getMessage());
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
            if(exists.next()){
                customer.setAddressId(exists.getInt(1));
                exists.close();
            } else {
                exists.close();
                throw new Exception("Could not create/update customer");
            }
        }
        if (!Context.getInstance().getCustomers().contains(customer))
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
            if(exists.next()){
                address.setAddressId(exists.getInt(1));
                exists.close();
            } else {
                exists.close();
                throw new Exception("Could not create/update address");
            }
        }
    }
    
    public void upsertAppointment(Appointment appointment) throws SQLException, Exception {
        PreparedStatement s;
        if (appointment.getAppointmentId() == 0) {
            s = (PreparedStatement) statements.get("insertAppointment");
            s.setInt(10, Context.getInstance().getUser().getUserId());
            s.setString(11, appointment.getType());
        } else {
            s = (PreparedStatement) statements.get("updateAppointment");
            s.setString(10, appointment.getType());
            s.setInt(11, appointment.getAppointmentId());
        }
        //(customerId, title, description, location, contact, url, start, end, createDate, createdBy)
        s.setInt(1, appointment.getCustomerId());
        s.setString(2, appointment.getTitle());
        s.setString(3, appointment.getDescription());
        s.setString(4, appointment.getLocation());
        s.setString(5, appointment.getContact());
        s.setString(6, appointment.getUrl());
        s.setTimestamp(7, java.sql.Timestamp.valueOf(appointment.getStart().toLocalDateTime()));
        s.setTimestamp(8, java.sql.Timestamp.valueOf(appointment.getEnd().toLocalDateTime()));
        s.setString(9, Context.getInstance().getUser().getUserName());
        
        s.executeUpdate();
        if (appointment.getAppointmentId() == 0) {
            ResultSet exists = s.getGeneratedKeys();
            if(exists.next()){
                appointment.setAppointmentId(exists.getInt(1));
                exists.close();
            } else {
                exists.close();
                throw new Exception("Could not create/update appointment");
            }
        }
    }
    
    public void deleteCustomer(Customer c) throws SQLException, Exception {
        try {
            connection.setAutoCommit(false);
            PreparedStatement s = statements.get("deleteAddress");
            s.setInt(1, c.getAddressId());
            s.executeUpdate();
            s = statements.get("deleteCustomer");
            s.setInt(1, c.getCustomerId());
            s.executeUpdate();
            s = statements.get("deleteCustomerAppointments");
            s.setInt(1, c.getCustomerId());
            s.executeUpdate();
            connection.commit();
        } catch (SQLException e ) {
            if (connection != null) {
                System.err.print("Transaction is being rolled back");
                connection.rollback(); 
            }
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
    
    public void deleteAppointment(Appointment a) throws SQLException, Exception {                
        PreparedStatement s = statements.get("deleteAppointment");
        s.setInt(1, a.getAppointmentId());
        s.executeUpdate();
    }
    
    protected void populate() throws SQLException, Exception {
        PreparedStatement countriesQuery = (PreparedStatement) statements.get("countries");
        PreparedStatement citiesQuery = (PreparedStatement) statements.get("cities");
        PreparedStatement addressesQuery = (PreparedStatement) statements.get("addresses");
        PreparedStatement customersQuery = (PreparedStatement) statements.get("customers");
        PreparedStatement appointmentsQuery = (PreparedStatement) statements.get("userAppointments");
        PreparedStatement usersQuery = (PreparedStatement) statements.get("users");
        
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
        countries.close();
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
        cities.close();
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
        addresses.close();
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
        customers.close();
        
        
        appointmentsQuery.setInt(1, Context.getInstance().getUser().getUserId());
        ResultSet appointments = appointmentsQuery.executeQuery();
        while(appointments.next()) {
            Appointment a = new Appointment();
            a.setAppointmentId(appointments.getInt("appointmentId"));
            a.setContact(appointments.getString("contact"));
            a.setCreateDate(appointments.getDate("createDate"));
            a.setCreatedBy(appointments.getString("createdBy"));
            a.setCustomer(customerMap.get(appointments.getInt("customerId")));
            a.setDescription(appointments.getString("description"));
            a.setEnd(appointments.getTimestamp("end").toLocalDateTime().atZone(ZoneId.systemDefault()));
            a.setStart(appointments.getTimestamp("start").toLocalDateTime().atZone(ZoneId.systemDefault()));
            a.setTitle(appointments.getString("title"));
            a.setUrl(appointments.getString("url"));
            a.setType(appointments.getString("type"));
            a.setUser(context.getUser());
        }
        appointments.close();
        
        ResultSet users = usersQuery.executeQuery();
        while(users.next()) {
            User u = new User();
            u.setUserId(users.getInt("userId"));
            u.setUserName(users.getString("userName"));
            Context.getInstance().addUser(u);
        }
        users.close();
    }
    
    public ObservableList getAllAppointments() throws SQLException {
        PreparedStatement appointmentsQuery = statements.get("appointments");
         ObservableList<Appointment> list = FXCollections.observableArrayList();
        try (ResultSet appointments = appointmentsQuery.executeQuery()) {
            while(appointments.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(appointments.getInt("appointmentId"));
                a.setContact(appointments.getString("contact"));
                a.setCreateDate(appointments.getDate("createDate"));
                a.setCreatedBy(appointments.getString("createdBy"));
                a.setCustomerId(appointments.getInt("customerId"));
                ObservableList<Customer> customerList = Context.getInstance().getCustomers().filtered(c -> c.getCustomerId() == a.getCustomerId());
                if (customerList.size() > 0) {
                    Customer customer = (Customer) customerList.get(0);
                    a.setCustomer(customer);
                }
                a.setDescription(appointments.getString("description"));
                a.setEnd(appointments.getTimestamp("end").toLocalDateTime().atZone(ZoneId.systemDefault()));
                a.setStart(appointments.getTimestamp("start").toLocalDateTime().atZone(ZoneId.systemDefault()));
                a.setTitle(appointments.getString("title"));
                a.setUrl(appointments.getString("url"));
                a.setUserId(appointments.getInt("userId"));
                a.setType(appointments.getString("type"));
                list.add(a);
            }
        }
        return list;
    }
    public void closeStatement(String key, PreparedStatement s) throws SQLException {
        s.close();
    }
    //Closes connections
    public void close(){
        try{
            connection.close();
            for (HashMap.Entry<String, PreparedStatement> key : statements.entrySet()) {
                key.getValue().close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        finally{
            System.out.println("Connection closed.");
        }
    }
    
}