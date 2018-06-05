/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.net.URL;
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
    
    @FXML
    private void handleLoginAction(ActionEvent event) {
        User user = new User();
        user.setUserName(userNameText.getText().trim());
        user.setPassword(passwordText.getText());
        Context.getInstance().setUser(user);
        loginButton.setDisable(true);
        loginButton.setText("Logging in...");
        this.setScene("Main");
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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