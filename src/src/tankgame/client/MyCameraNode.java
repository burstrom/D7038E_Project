/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.client;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;
import tankgame.settings.CameraSettings;
import static tankgame.settings.CameraSettings.DEAFULT_ZOOM_LEVEL;

/**
 *
 * @author MrIngelborn
 */
public class MyCameraNode extends CameraNode {
	private int zoomLevel = CameraSettings.DEAFULT_ZOOM_LEVEL;
	
	/**
	 * 
	 * @param name name of the node
	 * @param camera the camera
	 * @param target The target to follow
	 */
	public MyCameraNode(String name, Camera camera, Node target) {
		super(name, camera);
		
		// This mode means that camera copies the movements of the target:
		setControlDir(CameraControl.ControlDirection.SpatialToCamera);
		// Attach the camera node to the target:
		target.attachChild(this);
		updatePosition();
		// Rotate the camNode to look at the target:
		lookAt(target.getLocalTranslation(), Vector3f.UNIT_Y);
	}
	
	public void setZoom(int zoomLevel) {
		if (this.zoomLevel != zoomLevel) {
			if (zoomLevel >= 0 && zoomLevel < CameraSettings.zoomLevelHeights.length) {
				this.zoomLevel = zoomLevel;
				updatePosition();
			}
		}
		//System.out.println("old zoom level: " + this.zoomLevel);
		//System.out.println("new zoom level: " + zoomLevel);
	}
	
	private void updatePosition() {
		this.setLocalTranslation(0, CameraSettings.zoomLevelHeights[zoomLevel],
				- (CameraSettings.zoomLevelHeights[zoomLevel] * CameraSettings.HeightToBackRatio));
	}
	
}
