/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
/**
 *
 * @author david.wilkins
 */
public class LoginController extends SceneChangerController implements Initializable {
    @FXML
    private Label userNameLabel, passwordLabel;
    @FXML
    private TextField userNameText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private Button loginButton;
    
    private DB db;
    
    @FXML
    private void handleLoginAction(ActionEvent event) {
        User user = new User();
        user.setUserName(userNameText.getText().trim());
        user.setPassword(passwordText.getText());
        loginButton.setDisable(true);
        String originalText = loginButton.getText();
        loginButton.setText("Logging in...");
        try {
            db.login(user);
            this.setScene("Main");
        } catch (SQLException e) {
            errorMessage("Could not connect to database", e);
        } catch (Exception e) {
            errorMessage("Invalid Login", e);
        }
        loginButton.setText(originalText);
        loginButton.setDisable(false);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.db = DB.connect();
    }    
    
    private void errorMessage(String title, Exception e) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(title);
        errorAlert.setContentText(e.getMessage());
        errorAlert.showAndWait();
    }    
    
    @Override
    protected void refresh() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}