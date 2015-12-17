package tankgame.geoms;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import static tankgame.settings.Constants.BULLET_RADIUS;
import static tankgame.settings.Constants.ROUND_THINGS_RES;
import static tankgame.settings.Constants.TANK_BARREL_LENGTH;
import static tankgame.settings.Constants.TANK_BARREL_RADIUS;
import static tankgame.settings.Constants.TANK_BODY_HEIGHT;
import static tankgame.settings.Constants.TANK_BODY_LENGTH;
import static tankgame.settings.Constants.TANK_BODY_WIDTH;
import static tankgame.settings.Constants.TANK_TURRET_RADIUS;

/**
 * Node for the tanks in game
 * 
 * @author MrIngelborn
 */
public class TankNode extends GeomNode {

	public TankNode(String name) {
		super(name);
	}
	
	/**
	 * Basically Sirfrog's kod from the Demo Game
	 * @param assetManager 
	 */
	@Override
	public void createGeom(AssetManager assetManager) {
		//Tanks consist of two nodes.
		//The tanknode and the turretnode.
        //since the turret is actually a sphere lodged inside the tank
        //The whole shebang will be controllable.
		
        Box bodyGeom = new Box(TANK_BODY_WIDTH,TANK_BODY_HEIGHT,TANK_BODY_LENGTH);
        Geometry body = new Geometry("Tank body", bodyGeom);
        Material mat1 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Blue);
        body.setMaterial(mat1);
        
        this.attachChild(body);
        
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
        this.attachChild(turretRotationalNode);
        
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
	}
	
}
