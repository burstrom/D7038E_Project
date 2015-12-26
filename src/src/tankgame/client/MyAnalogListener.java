/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.client;

import com.jme3.input.controls.AnalogListener;
import static tankgame.settings.Constants.TANK_ACCELERATION;
import static tankgame.settings.Constants.TANK_ROTATE_SPEED;
import static tankgame.settings.Constants.CANNON_ELEVATE_SPEED;
import static tankgame.settings.Constants.CANNON_ROTATE_SPEED;

/**
 *
 * @author MrIngelborn
 */
public class MyAnalogListener implements AnalogListener {
	IActionHandler handler;

	public MyAnalogListener(IActionHandler handler) {
		this.handler = handler;
	}

	public void onAnalog(String name, float value, float tpf) {
		//System.out.println("Value:" + value);
		//System.out.println("TPF:" + tpf);
		if (name.equals("Forward")) {
			handler.accelerateTank(TANK_ACCELERATION * value);
		} else if (name.equals("Backward")) {
			handler.accelerateTank(-TANK_ACCELERATION * value);
		} else if (name.equals("Turn left")) {
			handler.rotateTankY(TANK_ROTATE_SPEED * value);
		} else if (name.equals("Turn right")) {
			handler.rotateTankY(-TANK_ROTATE_SPEED * value);
		} else if (name.equals("Turret left")) {
			handler.rotateCannon(CANNON_ROTATE_SPEED * value);
		} else if (name.equals("Turret right")) {
			handler.rotateCannon(-CANNON_ROTATE_SPEED * value);
		} else if (name.equals("Turret up")) {
			handler.elevateCannon(CANNON_ELEVATE_SPEED * value);
		} else if (name.equals("Turret down")) {
			handler.elevateCannon(-CANNON_ELEVATE_SPEED * value);
		}
	}
}
