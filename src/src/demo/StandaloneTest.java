package demo;

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
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import java.util.List;

/**
 * Some notes on the game:
 * Playing field is a square that's 600x600 units.
 * Tanks are 8 long and 6 wide boxes, with a turret on top.
 * 
 */

public class StandaloneTest extends SimpleApplication {
    //General constants.
    public static final int MAX_PLAYERS = 8;
    public static final int ROUND_THINGS_RES = 100;
    
    //Environment constants
    public static final float PLAYINGFIELD_SIDE = 600f;
    public static final float GRAVITY = 180f;
    
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
    //Following are in degrees per sec.
    public static final float TANK_ROTATE_SPEED = 40f; 
    public static final float TURRET_ROTATE_SPEED = 50f; 
    public static final float TURRET_ELEVATE_SPEED = 10f;
    public static final float TURRET_MAX_ELEVATION = 20f; //In degrees.
    
    //bullet constants
    public static final float BULLET_RADIUS = TANK_BARREL_RADIUS;
    public static final float BULLET_SPEED = 800f;
    
    //available variables.
    private CameraNode camNode;
    private Node arena = new Node("Arena");
    private Node tank = new Node("Tank");
    private Node allBullets = new Node("All bullets");
    
    private boolean isMoving = false;
    private float speed = 0;
    private float turretAngle = 0;
    
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
        
        Node turretRotationalNode = new Node("Turret rotation node");
        Node turretElevationNode = new Node("Turret elevation node");
        Node turretAperture = new Node("Turret aperture");
        
        Sphere turretSphere = new Sphere(ROUND_THINGS_RES, 
                ROUND_THINGS_RES, TANK_TURRET_RADIUS);
        Geometry turret = new Geometry("Tank turret", turretSphere);
        Material mat2 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", ColorRGBA.Yellow);
        turret.setMaterial(mat2);
        tankNode.attachChild(turretRotationalNode);
        
        turretRotationalNode.attachChild(turretElevationNode);
        turretElevationNode.attachChild(turretAperture);
        turretElevationNode.attachChild(turret);
        
        turretRotationalNode.setLocalTranslation(new Vector3f(0,TANK_BODY_HEIGHT+TANK_BARREL_RADIUS,0));
        turretAperture.setLocalTranslation(0,TANK_BARREL_LENGTH/2-BULLET_RADIUS, 0);
        
        Cylinder barrelCyl = new Cylinder(ROUND_THINGS_RES,
                ROUND_THINGS_RES,TANK_BARREL_RADIUS,TANK_BARREL_LENGTH,
                true);
        Geometry barrel = new Geometry("Tank barrel", barrelCyl);
        Material mat3 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat3.setColor("Color", ColorRGBA.Green);
        barrel.setMaterial(mat3);
        turretElevationNode.attachChild(barrel);
        //barrel.rotate(90*FastMath.DEG_TO_RAD,0,0);
        barrel.move(0, 0, TANK_BARREL_LENGTH/2f+TANK_TURRET_RADIUS-0.1f);
        
        return tankNode;
    }
    
    //For testing purposes.
    public Node createBox
            (String name, float length, float height, float width, ColorRGBA color) {
        Node boxNode = new Node(name);
        Box boxGeom = new Box(length,height,width);
        Geometry box = new Geometry(name+" geometry", boxGeom);
        Material mat1 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", color);
        box.setMaterial(mat1);
        boxNode.attachChild(box);
        return boxNode;
    }
    
    public Node createBullet(ColorRGBA color){
        Sphere bulletSphere = new Sphere(ROUND_THINGS_RES, 
                ROUND_THINGS_RES, BULLET_RADIUS);
        Geometry bullet = new Geometry("Bullet", bulletSphere);
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
        
        Node bulletNode = new Node("Bullet node");
        bulletNode.setUserData("Gravity force", 0f);
        bulletNode.attachChild(bullet);
        return bulletNode;
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
        
        Node field = createBox("Playing field", 
                PLAYINGFIELD_SIDE, 1f, PLAYINGFIELD_SIDE, ColorRGBA.Gray);
        Node obstacle1 = createBox("Obstacle 1", 5f,15f,5f, ColorRGBA.LightGray);
        Node obstacle2 = createBox("Obstacle 2", 5f,15f,5f, ColorRGBA.LightGray);
        Node obstacle3 = createBox("Obstacle 3", 5f,15f,5f, ColorRGBA.LightGray);
        Node obstacle4 = createBox("Obstacle 4", 5f,15f,5f, ColorRGBA.LightGray);
        
        arena.attachChild(field);
        arena.attachChild(obstacle1);
        arena.attachChild(obstacle2);
        arena.attachChild(obstacle3);
        arena.attachChild(obstacle4);
        field.setLocalTranslation(0, -0.5f, 0);
        obstacle1.setLocalTranslation(100f, 15f, 0f);
        obstacle2.setLocalTranslation(-100f, 15f, 0f);
        obstacle3.setLocalTranslation(0f, 15f, 100f);
        obstacle4.setLocalTranslation(0f, 15f, -100f);
    }
    
    public static void main(String[] args) {
        StandaloneTest app = new StandaloneTest();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {

        tank = createTank(ColorRGBA.Blue);
        rootNode.attachChild(tank);
        tank.setLocalTranslation(0,TANK_BODY_HEIGHT,0);
        //flyCam.setMoveSpeed(40);
        initKeys();
        Node turret = (Node)tank.getChild("Turret rotation node");
        initCamera(turret);
        initPlayingField();
        rootNode.attachChild(allBullets);
        rootNode.attachChild(arena);
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
        
        List<Spatial> bullets = allBullets.getChildren();
        
        for (int i = 0; i < bullets.size(); i++) {
            Spatial bullet = bullets.get(i);
            
            //This condition should be entered for all nodes in AllProjectiles,
            //but we're checking anyway.
            if (bullet.getName().equals("Bullet node")) {    
                moveForwardZ(bullet, BULLET_SPEED, tpf);
                float grav = bullet.getUserData("Gravity force");
                grav += GRAVITY*tpf;
                bullet.move(0, -grav*tpf, 0);
                bullet.setUserData("Gravity force", grav);
                
                if (bullet.getWorldTranslation().getY() <= -1f) {
                    bullet.removeFromParent();
                }
            }
        }
        
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
            if (name.equals("Backward")) {
                if (keyPressed){
                    System.out.println("Started moving backward.");
                    isMoving = true;
                } else {
                    System.out.println("Stopped moving backward.");
                    isMoving = false;
                }
            }
            if (name.equals("Fire") && keyPressed){
                
                Node bullet = createBullet(ColorRGBA.White);
                allBullets.attachChild(bullet);
                
                //Rotate correctly.
                Node turret = (Node)tank.getChild("Turret elevation node");
                Quaternion launchrotation = turret.getWorldRotation();
                bullet.rotate(launchrotation);
                
                Vector3f barrelPos = tank.getChild("Turret aperture").getWorldTranslation();
                bullet.setLocalTranslation(barrelPos);
                
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
            else if (name.equals("Turret left")) {
                tank.getChild("Turret rotation node").rotate(
                        0f,FastMath.DEG_TO_RAD*TURRET_ROTATE_SPEED*tpf,0f);
            }
            else if (name.equals("Turret right")) {
                tank.getChild("Turret rotation node").rotate(
                        0f,-FastMath.DEG_TO_RAD*TURRET_ROTATE_SPEED*tpf,0f);
            }
            else if (name.equals("Turret up")) {
                if (turretAngle <= TURRET_MAX_ELEVATION*FastMath.DEG_TO_RAD) {
                    turretAngle += FastMath.DEG_TO_RAD*TURRET_ELEVATE_SPEED*tpf;
                    tank.getChild("Turret elevation node").rotate(
                        -FastMath.DEG_TO_RAD*TURRET_ELEVATE_SPEED*tpf,0,0);
                }
            }
            else if (name.equals("Turret down")) {
                if (turretAngle >= 0) {
                    turretAngle -= FastMath.DEG_TO_RAD*TURRET_ELEVATE_SPEED*tpf;
                    tank.getChild("Turret elevation node").rotate(
                        FastMath.DEG_TO_RAD*TURRET_ELEVATE_SPEED*tpf,0,0);
                }
            }
        }
    };
  
    
}
