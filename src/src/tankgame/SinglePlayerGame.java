/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import tankgame.client.IInputHandler;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import tankgame.client.KeyBindings;
import tankgame.geoms.BulletNode;
import tankgame.geoms.TankNode;
import tankgame.settings.Constants;

/**
 * Single player game without network communication
 *
 * @author MrIngelborn
 */
public class SinglePlayerGame extends SimpleApplication implements IInputHandler {

	private KeyBindings keyBindings;
	private TankNode tank;
	private Node allBulletsNode;

	@Override
	public void simpleInitApp() {
		// Move camera a bit faster
		flyCam.setMoveSpeed(20);

		// Create the players tank
		tank = new TankNode("Tank", ColorRGBA.Blue);
		tank.initClient(assetManager);
		rootNode.attachChild(tank);

		//Create node for all bullets
		allBulletsNode = new Node("Bullets");
		rootNode.attachChild(allBulletsNode);

		//Add light
		DirectionalLight dl = new DirectionalLight();
		dl.setColor(ColorRGBA.White);
		rootNode.addLight(dl);

		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White);
		rootNode.addLight(al);

		//Init keybindings
		keyBindings = new KeyBindings(this, inputManager);
		keyBindings.init();
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
		tank.onUpdate(tpf);
		for (Spatial child : allBulletsNode.getChildren()) {
		}
	}

	@Override
	public void simpleRender(RenderManager rm) {
		super.simpleRender(rm);
	}

	public static void main(String[] args) {
		SinglePlayerGame game = new SinglePlayerGame();
		game.start();
	}

	public void shootBullet() {
		BulletNode bullet = new BulletNode("Bullet", tank.getColor());
		bullet.initClient(assetManager);
		allBulletsNode.attachChild(bullet);

		//Rotate correctly.
		Quaternion launchrotation = tank.getBarrelWorldDirection();
		bullet.rotate(launchrotation);

		Vector3f barrelPos = tank.getApertureWorldTranslation();
		bullet.setLocalTranslation(barrelPos);

		//System.out.println("Shot placed at: \n" + barrelPos);
	}

	/**
	 * Applys acceleration to the tank in the specified direction
	 *
	 * @param direction The direction of the acceleration force (normalized)
	 * @param acceleration The acceleration to be applied
	 */
	public void accelerateTank(float acceleration) {
		tank.accelerate(acceleration);
	}

	public void rotateTankY(float radians) {
		this.tank.rotate(0, radians, 0);
	}

	public void rotateCannon(float radians) {
		this.tank.addRotation(radians);
	}

	public void elevateCannon(float radians) {
		this.tank.addElevation(radians);
	}

	public void setTankAccelerating(boolean shouldAccelerate) {
		this.tank.setAccelerating(shouldAccelerate);
	}
}
