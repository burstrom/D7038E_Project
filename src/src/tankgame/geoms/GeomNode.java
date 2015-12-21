/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.geoms;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

/**
 * Common Class for all geometries in game. 
 * @author MrIngelborn
 */
public abstract class GeomNode extends Node {
	protected boolean hasGeoms = false;
	
	public GeomNode (String name) {
		super(name);
	}
	
	/**
	 * Create the actual geometry under the node.
	 * Called by the client, but not the server.
	 * 
	 * @param assetManager The Games Asset manager
	 */
	public void createGeom(AssetManager assetManager) {
		this.hasGeoms = true;
	}
	public abstract void onUpdate(float tpf);
}
