/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
/**
 *
 * @author david.wilkins
 */
public class LoginController extends SceneChangerController implements Initializable {
    protected Consumer<String> langChanger;
    @FXML
    private Label userNameLabel, passwordLabel;
    @FXML
    private TextField userNameText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private Button loginButton;
    
    @FXML RadioButton englishButton, russianButton;
    @FXML ToggleGroup lang;
    private DB db;
    private ResourceBundle rb;
    @FXML
    private void handleLoginAction(ActionEvent event) {
        User user = new User();
        user.setUserName(userNameText.getText().trim());
        user.setPassword(passwordText.getText());
        loginButton.setDisable(true);
        String originalText = loginButton.getText();
        loginButton.setText(rb.getString("loggingIn"));
        try {
            db.login(user);
            Appointment upcoming = user.getUpcomingAppointment();
            if (upcoming != null) {
                // using a lambda so I can change the message based on the
                // appointment
                this.warningMessage("Upcoming Appointment", () -> "You have an appointment with " +
                        upcoming.getCustomer().getCustomerName() + " in the next 15 minutes");
            }
            this.setScene("Main");
        } catch (SQLException e) {
            errorMessage(rb.getString("couldNotConnect"), e);
        } catch (Exception e) {
            errorMessage(rb.getString("invalidLogin"), e);
        } finally {
            Path path = Paths.get("./logins.log");
            try (BufferedWriter writer = Files.newBufferedWriter(path,
                     Charset.forName("UTF-16"), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
               writer.write(user.getUserName() + (user.getUserId() == 0 ? " failed to login" : " logged in") + 
                       " at " + ((Long)System.currentTimeMillis()).toString() + "\n");
            } catch (IOException e) {
               // Handle file I/O exception...
               errorMessage("Error writing to log", e);
            }
        }
        loginButton.setText(originalText);
        loginButton.setDisable(false);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.db = DB.connect();
        this.rb = rb;
        englishButton.setSelected(rb.getLocale().getLanguage().equals("en"));
        russianButton.setSelected(rb.getLocale().getLanguage().equals("ru"));
        lang.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (lang.getSelectedToggle() != null) {
                    String language = (String)lang.getSelectedToggle().getUserData();
                    langChanger.accept(language);
                }
            }
            
        });
        englishButton.setUserData("en");
        russianButton.setUserData("ru");
    }    
    
    public void setLangChanger(Consumer<String> langChanger) {
        this.langChanger = langChanger;
    }
    
    @Override
    protected void refresh() {
    }
}