/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.geoms;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import static demo.StandaloneTest.PLAYINGFIELD_SIDE;
import tankgame.util.GeomsHelper;

/**
 * Node for containing the ground and props in the world that creates the
 * play area for the tanks
 * 
 * @author MrIngelborn
 */
public class PlayFieldNode extends GeomNode {

	public PlayFieldNode(String name) {
		super(name);
	}
	
	@Override
	protected void createGeom(AssetManager assetManager) {
		// Create the ground
		Node field = GeomsHelper.createBox(assetManager, "Playing field", 
                PLAYINGFIELD_SIDE, 1f, PLAYINGFIELD_SIDE, ColorRGBA.Gray);
		
		// Create a few pillars
        Node obstacle1 = GeomsHelper.createBox(assetManager, "Obstacle 1",
				5f, 15f, 5f, ColorRGBA.LightGray);
        Node obstacle2 = GeomsHelper.createBox(assetManager, "Obstacle 2",
				5f, 15f, 5f, ColorRGBA.LightGray);
        Node obstacle3 = GeomsHelper.createBox(assetManager, "Obstacle 3",
				5f, 15f, 5f, ColorRGBA.LightGray);
        Node obstacle4 = GeomsHelper.createBox(assetManager, "Obstacle 4",
				5f, 15f, 5f, ColorRGBA.LightGray);
        
		// Attach the nodes
        this.attachChild(field);
        this.attachChild(obstacle1);
        this.attachChild(obstacle2);
        this.attachChild(obstacle3);
        this.attachChild(obstacle4);
		
		// Set the positions of the subnodes
        field.setLocalTranslation(0, -0.5f, 0);
        obstacle1.setLocalTranslation(100f, 15f, 0f);
        obstacle2.setLocalTranslation(-100f, 15f, 0f);
        obstacle3.setLocalTranslation(0f, 15f, 100f);
        obstacle4.setLocalTranslation(0f, 15f, -100f);
	}

	@Override
	public void onUpdate(float tpf) {
	}
	
}
