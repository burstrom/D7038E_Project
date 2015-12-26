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
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Turn left",  new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Turn right",  new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Turret left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Turret right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Turret up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Turret down", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Fire", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping("SetZoom0", new KeyTrigger(KeyInput.KEY_1));
		inputManager.addMapping("SetZoom1", new KeyTrigger(KeyInput.KEY_2));
		inputManager.addMapping("SetZoom2", new KeyTrigger(KeyInput.KEY_3));
		inputManager.addMapping("SetZoom3", new KeyTrigger(KeyInput.KEY_4));
		inputManager.addMapping("SetZoom4", new KeyTrigger(KeyInput.KEY_5));

        // Add the names to the action listener
        inputManager.addListener(actionListener,"Forward", "Backward", "Fire", 
				"SetZoom0", "SetZoom1", "SetZoom2", "SetZoom3", "SetZoom4");
        inputManager.addListener(analogListener,"Forward", "Backward", 
                "Turn left", "Turn right", "Turret left", "Turret right", 
                "Turret up", "Turret down");
	}
}
