package tankgame.demogame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;

/**
 * Some notes on the game:
 * Playing field is a square that's 600x600 units.
 * Tanks are 8 long and 6 wide boxes, with a turret on top.
 * 
 */

public class Main extends SimpleApplication {

    public static final int MAX_PLAYERS = 8;
    public static final int ROUND_THINGS_RES = 100;
    
    public static final float PLAYINGFIELD_SIDE = 600f;
    public static final float TANK_BODY_LENGTH = 8f;
    public static final float TANK_BODY_WIDTH = 6f;
    public static final float TANK_BODY_HEIGHT = 2.5f;
    public static final float TANK_TURRET_RADIUS = 4f;
    public static final float TANK_BARREL_RADIUS = 1f;
    public static final float TANK_BARREL_LENGTH = 3f;
    public static final float TANK_ACCELERATION = 5f;
    public static final float TANK_DECELERATION = 18f;
    public static final float TANK_MAX_SPEED = 30f;
    
    //Following are in degrees per sec.
    public static final float TANK_ROTATE_SPEED = 50f; 
    public static final float TURRET_ROTATE_SPEED = 50f; 
    public static final float TURRET_ELEVATE_SPEED = 5f;
    
    private float speed = 0;
    private Node tank;
    private boolean isMoving = false;
    
    
    public Node createTank(ColorRGBA color){
        //Tanks consist of two nodes.
        //The tanknode and the turretnode.
        //since the turret is actually a sphere lodged inside the tank
        //The whole shebang will be controllable.
        
        Node tankNode = new Node("Tank node");
        Box bodyGeom = new Box(TANK_BODY_WIDTH,TANK_BODY_HEIGHT,TANK_BODY_LENGTH);
        Geometry body = new Geometry("Tank body", bodyGeom);
        Material mat1 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Blue);
        body.setMaterial(mat1);
        
        tankNode.attachChild(body);
        
        Node turretNode = new Node("Turret node");
        Sphere turretSphere = new Sphere(ROUND_THINGS_RES, 
                ROUND_THINGS_RES, TANK_TURRET_RADIUS);
        Geometry turret = new Geometry("Tank turret", turretSphere);
        Material mat2 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", ColorRGBA.Yellow);
        turret.setMaterial(mat2);
        tankNode.attachChild(turretNode);
        turretNode.attachChild(turret);
        turretNode.setLocalTranslation(new Vector3f(0,TANK_BODY_HEIGHT+TANK_BARREL_RADIUS,0));
        
        Cylinder barrelCyl = new Cylinder(ROUND_THINGS_RES,
                ROUND_THINGS_RES,TANK_BARREL_RADIUS,TANK_BARREL_LENGTH,
                true);
        Geometry barrel = new Geometry("Tank barrel", barrelCyl);
        Material mat3 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat3.setColor("Color", ColorRGBA.Green);
        barrel.setMaterial(mat3);
        turretNode.attachChild(barrel);
        //barrel.rotate(90*FastMath.DEG_TO_RAD,0,0);
        barrel.move(0, 0, TANK_BARREL_LENGTH/2f+TANK_TURRET_RADIUS-0.1f);
        
        return tankNode;
    }
    
    private void moveForwardZ(Spatial element, float speed, float tpf ) {
        //This piece of code wizardry moves it in the direction it's pointing
        //Turns out it's a lot harder than you'd think.
        Vector3f forward = element.getLocalRotation().mult(Vector3f.UNIT_Z);
        element.move(forward.mult(speed).mult(tpf));
    }
    
    private void moveForwardZ(Spatial element, float distance ) {
        //This piece of code wizardry moves it in the direction it's pointing
        //Turns out it's a lot harder than you'd think.
        Vector3f forward = element.getLocalRotation().mult(Vector3f.UNIT_Z);
        element.move(forward.mult(distance));
    }
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    private void initKeys() {
        
        //toggle actions:
        //inputManager.addMapping("Toggle laser",  new KeyTrigger(KeyInput.KEY_L));
        //inputManager.addMapping("Shoot", new KeyTrigger(KeyInput.KEY_SPACE));
        //inputManager.addMapping("Restart", new KeyTrigger(KeyInput.KEY_R));
        
        //Analog actions
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Turn left",  new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Turn right",  new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Turret left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Turret right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Turret up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Turret down", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Fire", new KeyTrigger(KeyInput.KEY_SPACE));

        // Add the names to the action listener.
        //inputManager.addListener(actionListener, "Toggle laser", "Shoot","Restart");
        inputManager.addListener(actionListener,"Forward", "Backward", "Fire");
        inputManager.addListener(analogListener,"Forward", "Backward", 
                "Turn left", "Turn right", "Turret left", "Turret right", 
                "Turret up", "Turret down");
  };
    
    @Override
    public void simpleInitApp() {

        tank = createTank(ColorRGBA.Blue);
        rootNode.attachChild(tank);
        tank.move(0, -10, -50);
        flyCam.setMoveSpeed(0);
        initKeys();
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        //System.out.println("Updating.");
        if (!isMoving) {
            if (speed > 0) {
                speed -= TANK_DECELERATION*tpf;
            }
            if (speed < 0) {
                speed += TANK_DECELERATION*tpf;
            }
        }
        moveForwardZ(tank, speed, tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    

    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Forward")) {
                if (keyPressed){
                    System.out.println("Started moving forward.");
                    isMoving = true;
                } else {
                    System.out.println("Stopped moving forward.");
                    isMoving = false;
                }
            }
            else if (name.equals("Backward")) {
                if (keyPressed){
                    System.out.println("Started moving backward.");
                    isMoving = true;
                } else {
                    System.out.println("Stopped moving backward.");
                    isMoving = false;
                }
            }
        }
    };

    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("Forward")) {
                if (speed <= TANK_MAX_SPEED){
                    speed += TANK_ACCELERATION*tpf;
                }
            }
            else if (name.equals("Backward")) {
                if (speed >= -TANK_MAX_SPEED){
                    speed -= TANK_ACCELERATION*tpf;
                }
            }
            else if (name.equals("Turn left")) {
                tank.rotate(0, FastMath.DEG_TO_RAD*TANK_ROTATE_SPEED*tpf, 0);
            }
            else if (name.equals("Turn right")) {
                tank.rotate(0, -FastMath.DEG_TO_RAD*TANK_ROTATE_SPEED*tpf, 0);
            }
        }
    };
  
    
}
