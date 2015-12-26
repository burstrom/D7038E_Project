package tankgame.client;

import com.jme3.input.controls.ActionListener;

/**
 *
 * @author MrIngelborn
 */
public class MyActionListener implements ActionListener {
	private boolean forwardPressed = false, backwardPressed = false;
	private IActionHandler handler;

	public MyActionListener(IActionHandler handler) {
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
		else if (name.equals("Fire")) {
			handler.setShooting(isPressed);
		}
		else if (name.startsWith("SetZoom")) {
			int zoomLevel = Character.digit(name.charAt(name.length()-1), 10);
			System.out.println("Name: " + name);
			System.out.println("zoom: " + zoomLevel);
			handler.setZoom(zoomLevel);
		}
	}
}
