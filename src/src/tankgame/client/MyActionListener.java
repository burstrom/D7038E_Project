package tankgame.client;

import com.jme3.input.controls.ActionListener;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import tankgame.geoms.BulletNode;

/**
 *
 * @author MrIngelborn
 */
public class MyActionListener implements ActionListener {
	private boolean isMoving = false;
	private IInputHandler handler;
	
	public MyActionListener(IInputHandler handler) {
		this.handler = handler;
	}

	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("Forward") || name.equals("Backward")) {
                if (isPressed){
                    //System.out.println("Started moving");
                    handler.setTankAccelerating(true);
                } else {
                    //System.out.println("Stopped moving");
                    handler.setTankAccelerating(false);
                }
            }
            if (name.equals("Fire") && isPressed){
				handler.shootBullet();
            }
	}
	
}
