/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.geoms;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import tankgame.settings.Constants;
import static tankgame.settings.Constants.BULLET_RADIUS;
import static tankgame.settings.Constants.ROUND_THINGS_RES;
import tankgame.util.Movement;

/**
 *
 * @author MrIngelborn
 */
public class BulletNode extends GeomNode {
	private ColorRGBA color;
	private float gravityVelocity;
	private Vector3f ownerVelocity;
	
	/**
	 * 
	 * @param name Name of the node
	 * @param color Color overlay on the tank
	 * @param ownerVelocity The owner's velocity
	 */
	public BulletNode(String name, ColorRGBA color, Vector3f ownerVelocity) {
		super(name);
		this.color = color;
		gravityVelocity = 0;
		this.ownerVelocity = ownerVelocity;
	}

	@Override
	protected void createGeom(AssetManager assetManager) {
		Sphere bulletSphere = new Sphere(
				ROUND_THINGS_RES,
				ROUND_THINGS_RES,
				BULLET_RADIUS);
		Geometry bullet = new Geometry("Bullet", bulletSphere);

		Material bulletMat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		bulletMat.setColor("Color", color);
		bullet.setMaterial(bulletMat);

		this.attachChild(bullet);
	}

	@Override
	public void onUpdate(float tpf) {
		//Move the bullet in it's rotation direction
		Movement.moveForwardZ(this, Constants.BULLET_SPEED, tpf);

		//Apply gravity physics
		gravityVelocity += Constants.GRAVITY * tpf;
		this.move(0, -gravityVelocity * tpf, 0);
		
		// Continue initial velocity
		this.move(ownerVelocity.mult(tpf));

		if (this.getWorldTranslation().getY() <= -1f) {
			this.removeFromParent();
		}
	}
}
