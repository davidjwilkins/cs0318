/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.util.function.Consumer;
import javafx.scene.control.Alert;

/**
 *
 * @author david.wilkins
 */
abstract public class SceneChangerController implements SceneChangerInterface {
    protected Consumer<String> sceneChanger;
    protected Context context;
    @Override
    public void setSceneChanger(Consumer<String> sceneChanger) {
        this.sceneChanger = sceneChanger;
    }
    public void setScene(String scene) {
        this.sceneChanger.accept(scene);
    }
    public void setContext(Context context) {
        this.context = context;
    }
    
    protected Context getContext() {
        return this.context;
    }
    
    protected void errorMessage(String title, Exception e) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(title);
        errorAlert.setContentText(e.getMessage());
        errorAlert.showAndWait();
    } 
    
    interface Message { String getMessage(); }
    protected void warningMessage(String title, Message e) {
        Alert errorAlert = new Alert(Alert.AlertType.WARNING);
        errorAlert.setHeaderText(title);
        errorAlert.setContentText(e.getMessage());
        errorAlert.showAndWait();
    }    
    abstract protected void refresh();
}
