package tankgame.client;

import com.jme3.input.controls.ActionListener;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import tankgame.geoms.CannonBallNode;

/**
 *
 * @author MrIngelborn
 */
public class MyActionListener implements ActionListener {
	private boolean forwardPressed = false, backwardPressed = false;
	private IInputHandler handler;

	public MyActionListener(IInputHandler handler) {
		this.handler = handler;
	}

	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("Forward") || name.equals("Backward")) {
			if (name.equals("Forward")) {
				forwardPressed = isPressed;
			}
			else if (name.equals("Backward")) {
				backwardPressed = isPressed;
			}
			if (isPressed) {
				handler.setTankAccelerating(true);
			} else {
				if (!forwardPressed && !backwardPressed) {
					handler.setTankAccelerating(false);
				}
			}
		}
		else if (name.equals("Fire") && isPressed) {
			handler.shootBullet();
		}
	}
}
