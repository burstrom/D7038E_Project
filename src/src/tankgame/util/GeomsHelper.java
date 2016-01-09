/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.util;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * Class for static methods that can be used to help with common geometric
 * tasks.
 * 
 * @author MrIngelborn
 */
public class GeomsHelper {
	public static Node createBox(AssetManager assetManager, String name,
			float length, float height, float width, ColorRGBA color) {
        Node boxNode = new Node(name);
        Box boxGeom = new Box(length,height,width);
        Geometry box = new Geometry(name+" geometry", boxGeom);
        //Material mat1 = new Material(assetManager, 
        //        "Common/MatDefs/Misc/Unshaded.j3md");
        //mat1.setColor("Color", color);
        Material mat1 = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        mat1.setBoolean("UseMaterialColors",true);
        mat1.setColor("Ambient", color); 
        mat1.setColor("Diffuse", color); 
        box.setMaterial(mat1);
        boxNode.attachChild(box);
        return boxNode;
	}
}
