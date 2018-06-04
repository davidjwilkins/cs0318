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
public interface SceneChangerInterface {
    public void setSceneChanger(Consumer<String> func);
}
