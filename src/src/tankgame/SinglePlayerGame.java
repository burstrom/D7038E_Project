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
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import tankgame.client.KeyBindings;
import tankgame.geoms.CannonBallNode;
import tankgame.geoms.PlayFieldNode;
import tankgame.geoms.TankNode;

/**
 * Single player game without network communication
 *
 * @author MrIngelborn
 */
public class SinglePlayerGame extends SimpleApplication implements IInputHandler {

	private KeyBindings keyBindings;
	private TankNode tank;
	private PlayFieldNode playField;
	private Node allBulletsNode;
	private CameraNode camNode;

	@Override
	public void simpleInitApp() {
		// Create the playing field
		playField = new PlayFieldNode("PlayField");
		playField.initClient(assetManager);
		rootNode.attachChild(playField);

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

		//Init camera
		initCamera(tank.getCannonNode());
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
		tank.onUpdate(tpf);
		for (Spatial child : allBulletsNode.getChildren()) {
			CannonBallNode bullet = (CannonBallNode) child;
			bullet.onUpdate(tpf);
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
		CannonBallNode bullet = tank.shootBullet(this.getTimer().getTimeInSeconds());
		if (bullet != null) {
			bullet.initClient(assetManager);
			allBulletsNode.attachChild(bullet);
		}
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

	private void initCamera(Node target) {
		flyCam.setEnabled(false);
		//create the camera Node
		camNode = new CameraNode("Camera Node", cam);
		//This mode means that camera copies the movements of the target:
		camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
		//Attach the camNode to the target:
		target.attachChild(camNode);
		//Move camNode, e.g. behind and above the target:
		camNode.setLocalTranslation(new Vector3f(0, 10, -40));
		//Rotate the camNode to look at the target:
		camNode.lookAt(target.getLocalTranslation(), Vector3f.UNIT_Y);
	}
}
