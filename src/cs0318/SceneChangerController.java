/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

import java.util.function.Consumer;

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
    
    abstract protected void refresh();
}
