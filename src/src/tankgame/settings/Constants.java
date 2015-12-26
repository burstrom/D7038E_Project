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
    
    //Environment constants
    public static final float PLAYINGFIELD_SIDE = 600f;
    public static final float GRAVITY = 10f;
    
    //Tank constants
    public static final float TANK_ACCELERATION = 15f; // units/s^2
    public static final float TANK_DECELERATION = 10f; // units/s^2
    public static final float TANK_MAX_SPEED = 30f; // units/s
    public static final float TANK_ROTATE_SPEED = 0.7f; // rad/s
	
    public static final float CANNON_ROTATE_SPEED = 0.7f; // rad/s
    public static final float CANNON_ELEVATE_SPEED = 0.4f; // rad/s
    public static final float CANNON_MAX_ELEVATION = 0.35f; // rads
    
	public static final float TANK_SHOOT_FREQ = 1;
	
    //bullet constants
    public static final int BULLET_RES = 50;
    public static final float BULLET_RADIUS = 0.2f;
    public static final float BULLET_SPEED = 60f;
}
