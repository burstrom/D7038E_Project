/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.client;

import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Vector3f;
import static tankgame.settings.Constants.TANK_ACCELERATION;
import static tankgame.settings.Constants.TANK_ROTATE_SPEED;
import static tankgame.settings.Constants.TURRET_ELEVATE_SPEED;
import static tankgame.settings.Constants.CANNON_ROTATE_SPEED;
import tankgame.IInputHandler;

/**
 *
 * @author MrIngelborn
 */
public class MyAnalogListener implements AnalogListener {
	IInputHandler game;

	public MyAnalogListener(IInputHandler game) {
		this.game = game;
	}

	public void onAnalog(String name, float value, float tpf) {
		if (name.equals("Forward")) {
			game.accelerateTank(TANK_ACCELERATION * tpf * value);
		} else if (name.equals("Backward")) {
			game.accelerateTank(-TANK_ACCELERATION * tpf * value);
		} else if (name.equals("Turn left")) {
			game.rotateTankY(TANK_ROTATE_SPEED * tpf * value);
		} else if (name.equals("Turn right")) {
			game.rotateTankY(-TANK_ROTATE_SPEED * tpf * value);
		} else if (name.equals("Turret left")) {
			game.rotateCannon(CANNON_ROTATE_SPEED * tpf * value);
		} else if (name.equals("Turret right")) {
			game.rotateCannon(-CANNON_ROTATE_SPEED * tpf * value);
		} else if (name.equals("Turret up")) {
			game.elevateCannon(TURRET_ELEVATE_SPEED * tpf * value);
		} else if (name.equals("Turret down")) {
			game.elevateCannon(-TURRET_ELEVATE_SPEED * tpf * value);
		}
	}
}
