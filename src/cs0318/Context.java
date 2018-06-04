/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs0318;

/**
 *
 * @author david.wilkins
 */
public class Context {
    protected static Context instance = null;
    protected User user;
    
    
    private Context() {
        if (instance == null) this.instance = this;        
    }
    
    public static Context getInstance()
    {
        if (instance == null)
            instance = new Context();
 
        return instance;
    }
}
