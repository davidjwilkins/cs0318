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

    protected void initializeForLanguage(String language) throws Exception {
        Locale locale = new Locale(language, language.toUpperCase());
        SceneChangerController appointmentController, customerController,
                loginController, mainController, listCustomersController;
        scenes = new HashMap<>();
        controllers = new HashMap<>();
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        mainLoader.setResources(ResourceBundle.getBundle("resources.main", locale));
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        loginLoader.setResources(ResourceBundle.getBundle("resources.login", locale));
        FXMLLoader customerLoader = new FXMLLoader(getClass().getResource("Customer.fxml"));
        customerLoader.setResources(ResourceBundle.getBundle("resources.customer", locale));
        FXMLLoader listCustomersLoader = new FXMLLoader(getClass().getResource("ListCustomers.fxml"));
        listCustomersLoader.setResources(ResourceBundle.getBundle("resources.customer", locale));
        FXMLLoader appointmentLoader = new FXMLLoader(getClass().getResource("Appointment.fxml"));
        appointmentLoader.setResources(ResourceBundle.getBundle("resources.appointment", locale));
        scenes.put("Main", (Parent)mainLoader.load());
        scenes.put("Login", (Parent)loginLoader.load());
        scenes.put("Customer", (Parent)customerLoader.load());
        scenes.put("List Customers", (Parent)listCustomersLoader.load());
        scenes.put("Appointment", (Parent)appointmentLoader.load());
        mainController = ((MainController)mainLoader.getController());
        loginController = ((LoginController)loginLoader.getController());
        customerController = ((CustomerController)customerLoader.getController());
        listCustomersController = ((ListCustomersController)listCustomersLoader.getController());
        appointmentController = ((AppointmentController)appointmentLoader.getController());
        
        mainController.setSceneChanger(s -> this.setScene(s));
        loginController.setSceneChanger(s -> this.setScene(s));
        ((LoginController) loginController).setLangChanger(lang -> {
            try {
                ResourceBundle.clearCache();
                initializeForLanguage(lang);
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
        customerController.setSceneChanger(s -> this.setScene(s));
        listCustomersController.setSceneChanger(s -> this.setScene(s));
        appointmentController.setSceneChanger(s -> this.setScene(s));
        
        controllers.put("Main", mainController);
        controllers.put("Login", loginController);
        controllers.put("Customer", customerController);
        controllers.put("Appointment", appointmentController);
        controllers.put("List Customers", listCustomersController);
        
        scene = new Scene(scenes.get("Login"));
        System.out.println("Setting scene...");
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        initializeForLanguage("en");
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
