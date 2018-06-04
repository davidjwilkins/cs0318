/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.util.HashMap;
/**
 *
 * @author david.wilkins
 */
public class CS0318 extends Application {
    
    protected Scene scene;
    protected Stage stage;
    protected HashMap<String, Parent> scenes;
    @Override
    public void start(Stage stage) throws Exception {
        scenes = new HashMap<String, Parent>();
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        FXMLLoader customerLoader = new FXMLLoader(getClass().getResource("Customer.fxml"));
        FXMLLoader appointmentLoader = new FXMLLoader(getClass().getResource("Appointment.fxml"));
        scenes.put("Main", (Parent)mainLoader.load());
        ((MainController)mainLoader.getController()).setSceneChanger(s -> this.setScene(s));
        scenes.put("Login", (Parent)loginLoader.load());
        ((LoginController)loginLoader.getController()).setSceneChanger(s -> this.setScene(s));
        scenes.put("Customer", (Parent)customerLoader.load());
        ((CustomerController)customerLoader.getController()).setSceneChanger(s -> this.setScene(s));
        scenes.put("Appointment", (Parent)appointmentLoader.load());
        ((AppointmentController)appointmentLoader.getController()).setSceneChanger(s -> this.setScene(s));
        scene = new Scene(scenes.get("Login"));
        stage.setScene(scene);
        stage.show();
        this.stage = stage;
    }

    protected void setScene(String name) {
        try {
            if (!scenes.containsKey(name)) {
                throw new Exception(name + " not found in Scene list");
            }
            Parent root = scenes.get(name);
            scene.setRoot(root);
            stage.sizeToScene();
            
        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Scene not valid");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
