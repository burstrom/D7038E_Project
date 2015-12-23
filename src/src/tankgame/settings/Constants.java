/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.settings;

/**
 *
 * @author MrIngelborn
 */
public class Constants {
	//General constants.
    public static final int MAX_PLAYERS = 8;
    public static final int ROUND_THINGS_RES = 100;
    
    //Environment constants
    public static final float PLAYINGFIELD_SIDE = 600f;
    public static final float GRAVITY = 10f;
    
    //Tank constants
    public static final float TANK_BODY_LENGTH = 8f;
    public static final float TANK_BODY_WIDTH = 6f;
    public static final float TANK_BODY_HEIGHT = 2.5f;
    public static final float TANK_TURRET_RADIUS = 4f;
    public static final float TANK_BARREL_RADIUS = 1f;
    public static final float TANK_BARREL_LENGTH = 8f;
    public static final float TANK_ACCELERATION = 0.4f;
    public static final float TANK_DECELERATION = 0.2f;
    public static final float TANK_MAX_SPEED = 30f;
    //Following are in degrees per sec.
    public static final float TANK_ROTATE_SPEED = 0.7f; // In rads
    public static final float CANNON_ROTATE_SPEED = 0.7f; // In rads
    public static final float TURRET_ELEVATE_SPEED = 0.4f; // In rads
    public static final float CANNON_MAX_ELEVATION = 0.35f; //In radians.
    
    //bullet constants
    public static final float BULLET_RADIUS = 0.2f;
    public static final float BULLET_SPEED = 30f;
}
