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
	private IInputHandler game;
	
	public MyActionListener(IInputHandler game) {
		this.game = game;
	}

	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("Forward") || name.equals("Backward")) {
                if (isPressed){
                    System.out.println("Started moving");
                    game.setTankAccelerating(true);
                } else {
                    System.out.println("Stopped moving");
                    game.setTankAccelerating(false);
                }
            }
            if (name.equals("Fire") && isPressed){
				game.shootBullet();
            }
	}
	
}
