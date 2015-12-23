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
	 * Called by the initClient method to create the geometires and child nodes.
	 * @param assetManager The Game's Asset manager
	 */
	protected abstract void createGeom(AssetManager assetManager);
	
	/**
	 * Called by the client, but not the server.
	 * 
	 * @param assetManager The Game's Asset manager
	 */
	public void initClient(AssetManager assetManager) {
		this.hasGeoms = true;
		createGeom(assetManager);
	} 
	
	public abstract void onUpdate(float tpf);
}
