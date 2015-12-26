/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.util;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Utilities for movment
 * @author MrIngelborn
 */
public class MovementHelper {
	
	/**
	 * Moves a spatial in the spatials Z-direction
	 * @param element The spatial to move
	 * @param speed Speed of the spatial
	 * @param tpf Time since last frame
	 */
    public static void moveForwardZ(Spatial element, float speed, float tpf ) {
		moveForwardZ(element, speed * tpf);
    }
    
	/**
	 * Moves a spatial in the spatials Z-direction
	 * @param element The spatial to move
	 * @param distance 
	 */
    public static void moveForwardZ(Spatial element, float distance ) {
        Vector3f forward = element.getLocalRotation().mult(Vector3f.UNIT_Z);
        element.move(forward.mult(distance));
    }
}
