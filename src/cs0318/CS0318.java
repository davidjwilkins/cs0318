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
import java.util.Locale;
import java.util.ResourceBundle;
/**
 *
 * @author david.wilkins
 */
public class CS0318 extends Application {
    
    protected Scene scene;
    protected Stage stage;
    protected HashMap<String, Parent> scenes;
    protected HashMap<String, SceneChangerController> controllers;

    @Override
    public void start(Stage stage) throws Exception {
        Locale english = new Locale("en", "EN");
        SceneChangerController appointmentController, customerController, loginController, mainController;
        scenes = new HashMap<>();
        controllers = new HashMap<>();
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        mainLoader.setResources(ResourceBundle.getBundle("resources.main", english));
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        loginLoader.setResources(ResourceBundle.getBundle("resources.login", english));
        FXMLLoader customerLoader = new FXMLLoader(getClass().getResource("Customer.fxml"));
        customerLoader.setResources(ResourceBundle.getBundle("resources.customer", english));
        FXMLLoader appointmentLoader = new FXMLLoader(getClass().getResource("Appointment.fxml"));
        appointmentLoader.setResources(ResourceBundle.getBundle("resources.appointment", english));
        scenes.put("Main", (Parent)mainLoader.load());
        scenes.put("Login", (Parent)loginLoader.load());
        scenes.put("Customer", (Parent)customerLoader.load());
        scenes.put("Appointment", (Parent)appointmentLoader.load());
        
        mainController = ((MainController)mainLoader.getController());
        loginController = ((LoginController)loginLoader.getController());
        customerController = ((CustomerController)customerLoader.getController());
        appointmentController = ((AppointmentController)appointmentLoader.getController());
        
        mainController.setSceneChanger(s -> this.setScene(s));
        loginController.setSceneChanger(s -> this.setScene(s));
        customerController.setSceneChanger(s -> this.setScene(s));
        appointmentController.setSceneChanger(s -> this.setScene(s));
        
        controllers.put("Main", mainController);
        controllers.put("Login", loginController);
        controllers.put("Customer", customerController);
        controllers.put("Appointment", appointmentController);
        
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
       } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Scene not valid");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
        }
        Parent root = scenes.get(name);
        SceneChangerController controller = controllers.get(name);
        controller.refresh();
        scene.setRoot(root);
        stage.sizeToScene();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
