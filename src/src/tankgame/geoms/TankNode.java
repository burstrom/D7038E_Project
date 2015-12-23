package tankgame.geoms;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import tankgame.settings.Constants;
import tankgame.util.Movement;

/**
 * Node for the tanks in game
 * 
 * @author MrIngelborn
 */
public class TankNode extends GeomNode {
	private ColorRGBA color;
	private float rotation = 0, elevation = 0, speed = 0;
	private Node bodyNode, cannonNode, cannonLinkNode, cannonBarrelNode, engineNode, apertureNode;
	private boolean accelerating = false;
	
	/**
	 * @param name Name of the Node
	 * @param color THe color of the tank
	 */
	public TankNode(String name, ColorRGBA color) {
		super(name);
		this.color = color;
	}
	
	/**
	 * Creates the nodes and geomeries under the main tank Node.
	 * 
	 * Called by the client only.
	 * 
	 * @param assetManager The games AssetManager for importing assets
	 */
	@Override
	protected void createGeom(AssetManager assetManager) {
		//Import all the different tank parts
		bodyNode = (Node) assetManager.loadModel("Models/HoverTank/TankBodyGeom.mesh.xml");
		cannonNode = (Node) assetManager.loadModel("Models/HoverTank/TankCannonGeom.mesh.xml");
		cannonLinkNode = (Node) assetManager.loadModel("Models/HoverTank/CannonPart2Geom.mesh.xml");
		cannonBarrelNode = (Node) assetManager.loadModel("Models/HoverTank/CannonPart3Geom.mesh.xml");
		engineNode = (Node) assetManager.loadModel("Models/HoverTank/EngineGeom.mesh.xml");
		apertureNode = new Node("apperture");
		
		//Attach the parts the their parents
		cannonBarrelNode.attachChild(apertureNode);
		cannonLinkNode.attachChild(cannonBarrelNode);
		cannonNode.attachChild(cannonLinkNode);
		bodyNode.attachChild(engineNode);
		bodyNode.attachChild(cannonNode);
		this.attachChild(bodyNode);
		
		//Set the local translation for all parts
		cannonNode.setLocalTranslation(0, 2.0985f, -2.14132f);
		cannonLinkNode.setLocalTranslation(0, 0.28472f, 2.08453f);
		cannonBarrelNode.setLocalTranslation(0, -0.16652f, 1.50661f);
		engineNode.setLocalTranslation(0, 0.32361f, -4.62786f);
		engineNode.getChild(0).setLocalTranslation(0, 0, -0.08f);
		apertureNode.setLocalTranslation(0, -0.45f, 4.3f);
		
		//fix the rotation of the barrel
		cannonBarrelNode.getChild(0).rotate(0.05f, 0, 0);
		
		//Set the tank's material and color
		Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		mat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/MetalGalvanized.jpg"));
		//mat.setTexture("NormalMap", assetManager.loadTexture("Textures/MetalGalvanized.jpg"));
		//mat.setTexture("SpecularMap", assetManager.loadTexture("Models/HoverTank/tank_specular.jpg"));
		mat.setBoolean("UseMaterialColors",true); 
		mat.setColor("Diffuse", ColorRGBA.White);
		mat.setColor("Ambient", color.mult(0.5f));
		//mat.setColor("Specular", ColorRGBA.White);
		bodyNode.setMaterial(mat);
	}
	
	/**
	 * Updates the geomeries depending on the time that has gone by.
	 * 
	 * @param tpf Time since last frame
	 */
	public void onUpdate(float tpf) {
		this.engineNode.rotate(0, tpf*20, 0);
		
		//Deaccelerate if not accelerating
		if (!accelerating) {
			if (speed > 0) {
				// Make sure the speed don't make it to the other way of the zero
				float deceleration = Math.min(Constants.TANK_DECELERATION* tpf, speed);
				this.speed -= deceleration;
			}
			else if (speed < 0){
				// Make sure the speed don't make it to the other way of the zero
				float deceleration = Math.max(-Constants.TANK_DECELERATION* tpf, speed);
				this.speed -= deceleration;
			}
		}
		// Move the tank according to its speed
		if (speed != 0) {
			Movement.moveForwardZ(this, speed, tpf);
		}
		//System.out.println("Speed: " + speed);
	}
	
	/**
	 * Set the rotation of the cannon on top of the tank
	 * @param rotation The rotation on radians
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation % (FastMath.PI * 2);
		//Apply changes to nodes if geams are initialized
		if (this.hasGeoms) {
			System.out.println("Rotation: " + this.rotation);
			this.cannonNode.setLocalRotation(new Quaternion(new float[] {0, this.rotation/2, 0}));
		}
	}
	
	/**
	 * Add to the rotation of the cannon on top of the tank.
	 * Use negative values to rotate the other way around
	 * @param rotationDiff The difference in rotation on radians
	 */
	public void addRotation(float rotationDiff) {
		// Set new rotation
		this.setRotation(this.getRotation() + rotationDiff);
	}
	
	/**
	 * @return The current rotation of the cannon
	 */
	public float getRotation() {
		return this.rotation;
	}

	public Quaternion getBarrelWorldDirection() {
		return cannonBarrelNode.getWorldRotation();
	}

	public Vector3f getApertureWorldTranslation() {
		return apertureNode.getWorldTranslation();
	}

	public ColorRGBA getColor() {
		return this.color;
	}
	
	/**
	 * The speed of the dank in a specified direction.
	 * @param direction Direction to measure the speed in
	 * @return The speed of the tank in the direction
	 */
	public float getSpeed() {
		return this.speed;
	}
	
	/**
	 * Applys an acceleration force onto the tank
	 * @param direction Direction of the acceleration
	 * @param acceleration magnitude of the acceleration
	 */
	public void accelerate(float acceleration) {
		// Calculate the new speed
		float newSpeed = this.speed + acceleration;
		// Make dure the new speed in within bounds
		newSpeed = Math.max(newSpeed, -Constants.TANK_MAX_SPEED);
		newSpeed = Math.min(newSpeed, Constants.TANK_MAX_SPEED);
		// Set the speed to the new speed
		this.speed = newSpeed;
 	}
	
	/**
	 * Set the elevation (or tilt) of the tank's cannon barrel
	 * @param angle The anlge in radians
	 */
	public void setElevation(float angle) {
		//Make sure the elevation is within bounds
		angle = Math.max(angle, 0);
		angle = Math.min(angle, Constants.CANNON_MAX_ELEVATION);
		//Set elevation to new value
		this.elevation = angle;
		//Update graphics if geoms are initialized
		if (this.hasGeoms) {
			this.cannonLinkNode.setLocalRotation(new Quaternion(new float[] {-this.elevation/2, 0, 0}));
			this.cannonBarrelNode.setLocalRotation(new Quaternion(new float[] {-this.elevation/2, 0, 0}));
		}
	}
	
	/**
	 * Add to the elevation (or tilt) of the tank's cannon barrel
	 * @param angle The elevation
	 */
	public void addElevation(float angle) {
		this.setElevation(elevation+angle);
	}

	public void setAccelerating(boolean shouldAccelerate) {
		this.accelerating = shouldAccelerate;
	}
	
}
