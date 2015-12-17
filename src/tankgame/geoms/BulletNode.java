/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.geoms;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import static tankgame.settings.Constants.BULLET_RADIUS;
import static tankgame.settings.Constants.ROUND_THINGS_RES;

/**
 *
 * @author MrIngelborn
 */
public class BulletNode extends GeomNode {
	private ColorRGBA color;

	public BulletNode(String name, ColorRGBA color) {
		super(name);
		this.color = color;
	}

	@Override
	public void createGeom(AssetManager assetManager) {
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
	}
	
}
