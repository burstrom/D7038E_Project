/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.settings;

/**
 *
 * @author MrIngelborn
 */
public class CameraSettings {
	/**
	 * How far back in relation to the height in the distance between the camera and the target node
	 */
	public static float HeightToBackRatio = 4;
	/**
	 * The deafualt index in the _zoomLevelHeights_ array
	 */
	public static final int DEAFULT_ZOOM_LEVEL= 1;
	/**
	 * The diffrent height levels in relation to zoom level
	 */
	public static float[] zoomLevelHeights = new float[]{
		5, 10, 15, 20, 25
	};
	
}
