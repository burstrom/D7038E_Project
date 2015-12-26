package tankgame.client;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;

/**
 *
 * @author MrIngelborn
 */
public class KeyBindings {
	private MyActionListener actionListener;
	private MyAnalogListener analogListener;
	private InputManager inputManager;
	
	public KeyBindings(IActionHandler handler, InputManager inputManager) {
		actionListener = new MyActionListener(handler);
		analogListener = new MyAnalogListener(handler);
		this.inputManager = inputManager;
	}
	
	public void init() {
        //toggle actions:
        //inputManager.addMapping("Toggle laser",  new KeyTrigger(KeyInput.KEY_L));
        //inputManager.addMapping("Shoot", new KeyTrigger(KeyInput.KEY_SPACE));
        //inputManager.addMapping("Restart", new KeyTrigger(KeyInput.KEY_R));
        
        //Analog actions
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Turn left",  new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Turn right",  new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Turret left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Turret right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Turret up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Turret down", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Fire", new KeyTrigger(KeyInput.KEY_SPACE));

        // Add the names to the action listener.
        //inputManager.addListener(actionListener, "Toggle laser", "Shoot","Restart");
        inputManager.addListener(actionListener,"Forward", "Backward", "Fire");
        inputManager.addListener(analogListener,"Forward", "Backward", 
                "Turn left", "Turn right", "Turret left", "Turret right", 
                "Turret up", "Turret down");
	}
}
