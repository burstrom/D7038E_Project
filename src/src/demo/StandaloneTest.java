package demo;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

/**
 * Some notes on the game: Playing field is a square that's 600x600 units. Tanks
 * are 8 long and 6 wide boxes, with a turret on top.
 *
 */
public class StandaloneTest extends SimpleApplication {
	//General constants.
	public static final int MAX_PLAYERS = 8;
	public static final int ROUND_THINGS_RES = 100;
	//Environment constants
	public static final float PLAYINGFIELD_SIDE = 600f;
	public static final float PLAYINGFIELD_HEIGHT = 1f;
	public static final float GRAVITY = 180f;
	public static final float OBSTACLE_HEIGHT = 15f;
	public static final float OBSTACLE_SIDE = 5f;
	//Tank constants
	public static final float TANK_BODY_LENGTH = 8f;
	public static final float TANK_BODY_WIDTH = 6f;
	public static final float TANK_BODY_HEIGHT = 2.5f;
	public static final float TANK_TURRET_RADIUS = 4f;
	public static final float TANK_BARREL_RADIUS = 1f;
	public static final float TANK_BARREL_LENGTH = 8f;
	public static final float TANK_ACCELERATION = 5f;
	public static final float TANK_DECELERATION = 25f;
	public static final float TANK_MAX_SPEED = 30f;
	public static final float TANK_MASS_OFFSET = TANK_BODY_HEIGHT;
	//Following are in degrees per sec.
	public static final float TANK_ROTATE_SPEED = 0.7f; // In rads
	public static final float CANNON_ROTATE_SPEED = 20f; // In rads
	public static final float TURRET_ROTATE_SPEED = 50f;
	public static final float TURRET_ELEVATE_SPEED = 20f; // In rads
	public static final float CANNON_MAX_ELEVATION = 0.35f; //In radians.
	public static final float TURRET_MAX_ELEVATION = 20f;
	//bullet constants
	public static final float BULLET_RADIUS = 0.2f;
	public static final float BULLET_SPEED = 30f;
	public static final float BULLET_MASS = 5f; //5 kg.
	public static final float BULLET_FORCE = 100f; //Energy?
	//Names of things.
	public static final String TANK_NODE_NAME = "Tank node";
	public static final String TANK_BODY_GEOM_NAME = "Tank body";
	public static final String TANK_TURRET_ROT_NODE_NAME = "Turret rotation node";
	public static final String TANK_TURRET_ELE_NODE_NAME = "Turret elevation node";
	public static final String TANK_TURRET_MUZZLE_NODE_NAME = "Turret muzzle node";
	public static final String TANK_TURRET_GEOM_NAME = "Tank turret";
	public static final String TANK_BARREL_GEOM_NAME = "Tank barrel";
	public static final String BULLET_GEOM_NAME = "Bullet geometry";
	public static final String BULLET_NODE_NAME = "Bullet node";
	public static final String ARENA_FLOOR_GEOM_NAME = "Playing field geometry";
	public static final String ARENA_FLOOR_NODE_NAME = "Playing field";
	public static final String ARENA_OBSTACLE_GEOM_NAME = "Obstacle geometry";
	public static final String ARENA_OBSTACLE_NODE_NAME = "Obstacle";
	//available variables.
	private CameraNode camNode; //following camera
	private Node arena = new Node("Arena");
	private Node tank = new Node("Tank");
	private Node allBullets = new Node("All bullets");
	private float speed = 0;
	private float steeringValue = 0; //Current steering
	private float steeringForce = 0.3f; //how much to steer
	private float accelerationValue = 0; // current acceleration
	private float accelerationForce = 35000; //how much to accelerate each time unit
	private float reverseForce = 30000; //Force to reverse
	private float brakeForce = 70000; //Force for braking.
	private float friction = 10000; //slowing force. Not implemented.
	private float turretAngle = 0; //Turret rotation (unused?)
	private int isMoving = 0; //if tank is under power.
	private BulletAppState bulletAppState; //Contains physics state
	private VehicleControl vehicle; //Vechicle physics

	public VehicleControl attachTankPhysics(Node tankNode) {
		//All shapes are positioned at TANK_MASS_OFFSET to get a lower
		//centre of gravity.
		CompoundCollisionShape compoundShape = new CompoundCollisionShape();
		BoxCollisionShape box = new BoxCollisionShape(
				new Vector3f(TANK_BODY_WIDTH, TANK_BODY_HEIGHT, TANK_BODY_LENGTH));
		compoundShape.addChildShape(box, new Vector3f(0, TANK_MASS_OFFSET, 0));
		SphereCollisionShape sphere = new SphereCollisionShape(
				TANK_TURRET_RADIUS);
		compoundShape.addChildShape(sphere, new Vector3f(
				0, TANK_TURRET_RADIUS + TANK_MASS_OFFSET, 0));

		vehicle = new VehicleControl(compoundShape, 1000);
		tankNode.addControl(vehicle);

		//BEGIN COPYPASTA CODE
		//These lines define the car-like suspension. They're strictly approximate
		float stiffness = 120f;
		float compValue = .3f;
		float dampValue = .4f;
		vehicle.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
		vehicle.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
		vehicle.setSuspensionStiffness(stiffness);
		vehicle.setMaxSuspensionForce(10000.0f);
		Vector3f wheelDirection = new Vector3f(0, -1, 0);
		Vector3f wheelAxle = new Vector3f(-1, 0, 0);
		float radius = 0.5f;
		float restLength = 0.3f;
		float yOff = -TANK_BODY_HEIGHT + TANK_MASS_OFFSET;
		float xOff = TANK_BODY_WIDTH * 0.9f;
		float zOff = TANK_BODY_LENGTH * 0.9f;

		//Time to add wheels. These are also temporary
		Cylinder wheelMesh = new Cylinder(16, 16, radius, radius * 0.6f, true);

		Material mat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.DarkGray);

		Node node1 = new Node("wheel 1 node");
		Geometry wheels1 = new Geometry("wheel 1", wheelMesh);
		node1.attachChild(wheels1);
		wheels1.rotate(0, FastMath.HALF_PI, 0);
		wheels1.setMaterial(mat);

		vehicle.addWheel(node1, new Vector3f(-xOff, yOff, zOff),
				wheelDirection, wheelAxle, restLength, radius, true);

		Node node2 = new Node("wheel 2 node");
		Geometry wheels2 = new Geometry("wheel 2", wheelMesh);
		node2.attachChild(wheels2);
		wheels2.rotate(0, FastMath.HALF_PI, 0);
		wheels2.setMaterial(mat);

		vehicle.addWheel(node2, new Vector3f(xOff, yOff, zOff),
				wheelDirection, wheelAxle, restLength, radius, true);

		Node node3 = new Node("wheel 3 node");
		Geometry wheels3 = new Geometry("wheel 3", wheelMesh);
		node3.attachChild(wheels3);
		wheels3.rotate(0, FastMath.HALF_PI, 0);
		wheels3.setMaterial(mat);

		vehicle.addWheel(node3, new Vector3f(-xOff, yOff, -zOff),
				wheelDirection, wheelAxle, restLength, radius, false);

		Node node4 = new Node("wheel 4 node");
		Geometry wheels4 = new Geometry("wheel 4", wheelMesh);
		node4.attachChild(wheels4);
		wheels4.rotate(0, FastMath.HALF_PI, 0);
		wheels4.setMaterial(mat);

		vehicle.addWheel(node4, new Vector3f(xOff, yOff, -zOff),
				wheelDirection, wheelAxle, restLength, radius, false);

		tankNode.attachChild(node1);
		tankNode.attachChild(node2);
		tankNode.attachChild(node3);
		tankNode.attachChild(node4);
		//END COPYPASTA CODE

		//Gravity 
		vehicle.setGravity(new Vector3f(0f, -9.81f, 0f));
		//This sets physics resolution.
		vehicle.setCcdMotionThreshold(TANK_BODY_HEIGHT);

		return vehicle;
	}

	public Node createTank(ColorRGBA color) {
		//Tanks consist of four nodes, body, rotation, elevation and muzzle

		//Create body of tank.
		Node tankNode = new Node(TANK_NODE_NAME);
		Box bodyGeom = new Box(TANK_BODY_WIDTH, TANK_BODY_HEIGHT, TANK_BODY_LENGTH);
		Geometry body = new Geometry(TANK_BODY_GEOM_NAME, bodyGeom);
		Material mat1 = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		mat1.setColor("Color", ColorRGBA.Blue);
		body.setMaterial(mat1);
		//Move geom down to match offset
		body.setLocalTranslation(0, TANK_MASS_OFFSET, 0);

		tankNode.attachChild(body);

		//Create turret nodes. Rotational handles turret rotation, elevation
		//handles tilting up and down, aperture(muzzle) makes placing bullets easier
		Node turretRotationalNode = new Node(TANK_TURRET_ROT_NODE_NAME);
		Node turretElevationNode = new Node(TANK_TURRET_ELE_NODE_NAME);
		Node turretAperture = new Node(TANK_TURRET_MUZZLE_NODE_NAME);

		//Create turret sphere
		Sphere turretSphere = new Sphere(ROUND_THINGS_RES,
				ROUND_THINGS_RES, TANK_TURRET_RADIUS);
		Geometry turret = new Geometry(TANK_TURRET_GEOM_NAME, turretSphere);
		Material mat2 = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		mat2.setColor("Color", ColorRGBA.Yellow);
		turret.setMaterial(mat2);
		turret.setLocalTranslation(0, TANK_MASS_OFFSET, 0);

		tankNode.attachChild(turretRotationalNode);

		//Attach turret nodes
		turretRotationalNode.attachChild(turretElevationNode);
		turretElevationNode.attachChild(turretAperture);
		turretElevationNode.attachChild(turret);

		//Move turret nodes.
		turretRotationalNode.setLocalTranslation(
				new Vector3f(0, TANK_BODY_HEIGHT + TANK_BARREL_RADIUS, 0));
		turretAperture.setLocalTranslation(
				0, 0, 2f * TANK_BARREL_LENGTH - BULLET_RADIUS - TANK_BARREL_RADIUS);

		//Make turret barrel cylinder.
		Cylinder barrelCyl = new Cylinder(ROUND_THINGS_RES,
				ROUND_THINGS_RES, TANK_BARREL_RADIUS, TANK_BARREL_LENGTH,
				true);
		Geometry barrel = new Geometry(TANK_BARREL_GEOM_NAME, barrelCyl);
		Material mat3 = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		mat3.setColor("Color", ColorRGBA.Green);
		barrel.setMaterial(mat3);
		barrel.setLocalTranslation(0, TANK_MASS_OFFSET, 0);
		turretElevationNode.attachChild(barrel);
		//barrel.rotate(90*FastMath.DEG_TO_RAD,0,0);
		barrel.move(0, 0, TANK_BARREL_LENGTH + TANK_TURRET_RADIUS - TANK_BARREL_RADIUS);

		return tankNode;
	}

	//For testing purposes.
	//Shorthand function for generic box.
	public Node createBox(String name, float length, float height, float width, ColorRGBA color) {
		Node boxNode = new Node(name);
		Box boxGeom = new Box(length, height, width);
		Geometry box = new Geometry(name + " geometry", boxGeom);
		Material mat1 = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		mat1.setColor("Color", color);
		box.setMaterial(mat1);
		boxNode.attachChild(box);
		return boxNode;
	}

	//Creates a bullet node.
	public Node createBullet(ColorRGBA color) {
		Sphere bulletSphere = new Sphere(ROUND_THINGS_RES,
				ROUND_THINGS_RES, BULLET_RADIUS);
		Geometry bullet = new Geometry(BULLET_GEOM_NAME, bulletSphere);
		/*
		 Material stone = new Material(assetManager,
		 "Common/MatDefs/Light/Lighting.j3md");
		 stone.setTexture("DiffuseMap",assetManager.loadTexture(
		 "Textures/Terrain/Rock/Rock.PNG"));
		 stone.setTexture("NormalMap",assetManager.loadTexture(
		 "Textures/Terrain/Rock/Rock_normal.png"));
		 stone.setBoolean("UseMaterialColors",true);
		 stone.setColor("Ambient", ColorRGBA.DarkGray); 
		 stone.setColor("Diffuse", ColorRGBA.White); 
		 stone.setColor("Specular", ColorRGBA.White);
		 */
		Material bulletMat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		bulletMat.setColor("Color", color);
		bullet.setMaterial(bulletMat);

		Node bulletNode = new Node(BULLET_NODE_NAME);
		bulletNode.attachChild(bullet);
		return bulletNode;
	}

	//Attach physics to the bullet
	private RigidBodyControl attachBulletPhysics(Node bulletNode) {
		//Create collision shape
		SphereCollisionShape sphereColl = new SphereCollisionShape(BULLET_RADIUS);
		//Create the physics control.
		BulletControl control = new BulletControl(sphereColl, BULLET_MASS, bulletAppState);
		//Stick to bullet node
		bulletNode.addControl(control);
		//Set gravity and physics threshold.
		control.setGravity(new Vector3f(0f, 9.81f, 0f));
		control.setCcdMotionThreshold(BULLET_RADIUS);
		return control;
	}

	private void moveForwardZ(Spatial element, float speed, float tpf) {
		//This piece of code wizardry moves it in the direction it's pointing
		//Turns out it's a lot harder than you'd think.
		Vector3f forward = element.getLocalRotation().mult(Vector3f.UNIT_Z);
		element.move(forward.mult(speed).mult(tpf));
	}

	private void moveForwardZ(Spatial element, float distance) {
		//This piece of code wizardry moves it in the direction it's pointing
		//Turns out it's a lot harder than you'd think.
		Vector3f forward = element.getLocalRotation().mult(Vector3f.UNIT_Z);
		element.move(forward.mult(distance));
	}

	private void initKeys() {
		inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Turn left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Turn right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Turret left", new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping("Turret right", new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping("Turret up", new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("Turret down", new KeyTrigger(KeyInput.KEY_DOWN));
		inputManager.addMapping("Fire", new KeyTrigger(KeyInput.KEY_SPACE));

		// Add the names to the action listener.
		inputManager.addListener(actionListener, "Forward", "Backward",
				"Turn left", "Turn right", "Turret left", "Turret right",
				"Turret up", "Turret down", "Fire");
		inputManager.addListener(analogListener, "Forward", "Backward",
				"Turn left", "Turn right", "Turret left", "Turret right",
				"Turret up", "Turret down", "Fire");
	}

	;
    
    private void initCamera(Node target) {
		flyCam.setEnabled(false);
		//create the camera Node
		camNode = new CameraNode("Camera Node", cam);
		//This mode means that camera copies the movements of the target:
		camNode.setControlDir(ControlDirection.SpatialToCamera);
		//Attach the camNode to the target:
		target.attachChild(camNode);
		//Move camNode, e.g. behind and above the target:
		camNode.setLocalTranslation(new Vector3f(0, 20, -60));
		//Rotate the camNode to look at the target:
		camNode.lookAt(target.getLocalTranslation(), Vector3f.UNIT_Y);
	}

	private void initPlayingField() {
		//Set up the nodes.
		Node field = createBox(ARENA_FLOOR_NODE_NAME,
				PLAYINGFIELD_SIDE, 1f, PLAYINGFIELD_SIDE, ColorRGBA.Gray);
		Node obstacle1 = createBox(
				ARENA_OBSTACLE_NODE_NAME, 5f, 15f, 5f, ColorRGBA.LightGray);
		Node obstacle2 = createBox(
				ARENA_OBSTACLE_NODE_NAME, 5f, 15f, 5f, ColorRGBA.LightGray);
		Node obstacle3 = createBox(
				ARENA_OBSTACLE_NODE_NAME, 5f, 15f, 5f, ColorRGBA.LightGray);
		Node obstacle4 = createBox(
				ARENA_OBSTACLE_NODE_NAME, 5f, 15f, 5f, ColorRGBA.LightGray);
		//attach nodes
		arena.attachChild(field);
		arena.attachChild(obstacle1);
		arena.attachChild(obstacle2);
		arena.attachChild(obstacle3);
		arena.attachChild(obstacle4);
		//place nodes.
		field.setLocalTranslation(0, -0.5f, 0);
		obstacle1.setLocalTranslation(100f, 15f, 0f);
		obstacle2.setLocalTranslation(-100f, 15f, 0f);
		obstacle3.setLocalTranslation(0f, 15f, 100f);
		obstacle4.setLocalTranslation(0f, 15f, -100f);

		//Create collision for the arena.
		//Generate collision from mesh
		CompoundCollisionShape arenaColl =
				(CompoundCollisionShape) CollisionShapeFactory.createMeshShape((Node) arena);
		//Create control
		RigidBodyControl arenaPhys =
				new RigidBodyControl(arenaColl, 0.0f);
		//attach control
		bulletAppState.getPhysicsSpace().add(arenaPhys);
		//Attach listener (?)
		//bulletAppState.getPhysicsSpace().addCollisionListener(arenaPhys);
	}

	public static void main(String[] args) {
		StandaloneTest app = new StandaloneTest();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		//Start physics space.
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		//Get debug printouts
		bulletAppState.getPhysicsSpace().enableDebug(assetManager);

		//Set and move tank
		tank = createTank(ColorRGBA.Blue);
		tank.setLocalTranslation(0, PLAYINGFIELD_HEIGHT / 2f + 0.2f, 0);
		//Generates physics
		VehicleControl vehicle = attachTankPhysics(tank);
		bulletAppState.getPhysicsSpace().add(vehicle);

		rootNode.attachChild(tank);
		tank.setLocalTranslation(0, TANK_BODY_HEIGHT, 0);
		//flyCam.setMoveSpeed(40);
		initKeys();
		Node turret = (Node) tank.getChild(TANK_TURRET_ROT_NODE_NAME);
		initCamera(turret);
		initPlayingField();
		rootNode.attachChild(allBullets);
		rootNode.attachChild(arena);
	}

	@Override
	public void simpleUpdate(float tpf) {
		//TODO: add update code
		//System.out.println("Updating.");
	}

	@Override
	public void simpleRender(RenderManager rm) {
		//TODO: add render code
	}
	private ActionListener actionListener = new ActionListener() {
		//TODO: Fix acceleration, braking, infinite rolling (?) etc.
		public void onAction(String name, boolean keyPressed, float tpf) {
			//Accelerates when pressed. Kind of iffy at the moment.
			if (name.equals("Forward")) {
				if (keyPressed) {
					System.out.println("Started moving forward.");
					accelerationValue += accelerationForce;
					isMoving = 1;
				} else {
					System.out.println("Stopped moving forward.");
					accelerationValue -= accelerationForce;
					isMoving = 0;
				}
			} else if (name.equals("Backward")) {
				if (keyPressed) {
					System.out.println("Started moving backward.");
					accelerationValue -= reverseForce;
					isMoving = -1;
				} else {
					System.out.println("Stopped moving backward.");
					accelerationValue += reverseForce;
					isMoving = 0;
				}
			}
			if (name.equals("Turn left")) {
				if (keyPressed) {
					System.out.println("Started turning left.");
					steeringValue += steeringForce;
				} else {
					System.out.println("Stopped turning left.");
					steeringValue -= steeringForce;
				}
			} else if (name.equals("Turn right")) {
				if (keyPressed) {
					System.out.println("Started turning right.");
					steeringValue -= steeringForce;
				} else {
					System.out.println("Stopped turning right.");
					steeringValue += steeringForce;
				}
			}
			if (name.equals("Fire") && keyPressed) {
				//Create bullet node
				Node bulletNode = createBullet(ColorRGBA.White);
				//Bullet control.
				RigidBodyControl bulletControl = attachBulletPhysics(bulletNode);
				//Attach to world
				allBullets.attachChild(bulletNode);
				bulletAppState.getPhysicsSpace().add(bulletControl);
				//Get direction for launching bullet 
				Vector3f turretMuzzle = tank.getChild(
						TANK_TURRET_MUZZLE_NODE_NAME).getWorldTranslation();
				Vector3f turretBase = tank.getChild(TANK_TURRET_ROT_NODE_NAME)
						.getWorldTranslation();
				Vector3f launchDirection = turretMuzzle.subtract(turretBase);
				//Set location for physics
				bulletControl.setPhysicsLocation(turretMuzzle);
				//apply force impulse
				bulletControl.applyImpulse(
						launchDirection.mult(BULLET_FORCE), turretMuzzle);
			}
		}
	};
	private AnalogListener analogListener = new AnalogListener() {
		public void onAnalog(String name, float value, float tpf) {
			if (name.equals("Forward")) {
				vehicle.accelerate(accelerationValue * tpf);
			} else if (name.equals("Backward")) {
				vehicle.accelerate(accelerationValue * tpf);
			}
			if (name.equals("Turn left")) {
				vehicle.steer(steeringValue);
			} else if (name.equals("Turn right")) {
				vehicle.steer(steeringValue);
			}
			if (name.equals("Turret left")) {
				tank.getChild("Turret rotation node").rotate(
						0f, FastMath.DEG_TO_RAD * TURRET_ROTATE_SPEED * tpf, 0f);
			} else if (name.equals("Turret right")) {
				tank.getChild("Turret rotation node").rotate(
						0f, -FastMath.DEG_TO_RAD * TURRET_ROTATE_SPEED * tpf, 0f);
			}
			if (name.equals("Turret up")) {
				if (turretAngle <= TURRET_MAX_ELEVATION * FastMath.DEG_TO_RAD) {
					turretAngle += FastMath.DEG_TO_RAD * TURRET_ELEVATE_SPEED * tpf;
					tank.getChild("Turret elevation node").rotate(
							-FastMath.DEG_TO_RAD * TURRET_ELEVATE_SPEED * tpf, 0, 0);
				}
			} else if (name.equals("Turret down")) {
				if (turretAngle >= 0) {
					turretAngle -= FastMath.DEG_TO_RAD * TURRET_ELEVATE_SPEED * tpf;
					tank.getChild("Turret elevation node").rotate(
							FastMath.DEG_TO_RAD * TURRET_ELEVATE_SPEED * tpf, 0, 0);
				}
			}
		}
	};
}
