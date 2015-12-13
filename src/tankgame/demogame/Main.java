package tankgame.demogame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
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
    
    
    public Node createTank(ColorRGBA color){
        //Tanks consist of two nodes.
        //The tanknode and the turretnode.
        //since the turret is actually a sphere lodged inside the tank
        //The whole shebang will be controllable.
        
        Node tankNode = new Node("Tank node");
        Box bodyGeom = new Box(TANK_BODY_LENGTH,TANK_BODY_HEIGHT,TANK_BODY_WIDTH);
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
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        //Box b = new Box(1, 1, 1);
        //Geometry geom = new Geometry("Box", b);

        //Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //mat.setColor("Color", ColorRGBA.Blue);
        //geom.setMaterial(mat);

        
        
        //rootNode.attachChild(geom);
        rootNode.attachChild(createTank(ColorRGBA.Blue));
        flyCam.setMoveSpeed(10);
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
