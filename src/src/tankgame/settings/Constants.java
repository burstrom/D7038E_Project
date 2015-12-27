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
    public static final float PLAYINGFIELD_HEIGHT = 1f;
    public static final float GRAVITY = 180f;
    
    public static final float OBSTACLE_HEIGHT = 15f;
    public static final float OBSTACLE_SIDE = 5f;
    
    //Tank constants
    public static final float TANK_BODY_LENGTH = 8f;
    public static final float TANK_BODY_WIDTH = 6f;
    public static final float TANK_BODY_HEIGHT = 2.5f;
    public static final float TANK_TURRET_RADIUS = 4f;
    public static final float TANK_BARREL_RADIUS = 1f;
    public static final float TANK_BARREL_LENGTH = 8f;
    public static final float TANK_ACCELERATION = 5f;
    public static final float TANK_DECELERATION = 25f;
    public static final float TANK_MAX_SPEED = 30f;
    public static final float TANK_MASS_OFFSET = TANK_BODY_HEIGHT;
    //Following are in degrees per sec.
    public static final float TANK_ROTATE_SPEED = 0.7f; // In rads
    public static final float CANNON_ROTATE_SPEED = 20f; // In rads
    public static final float TURRET_ROTATE_SPEED = 50f;
    public static final float TURRET_ELEVATE_SPEED = 20f; // In rads
    public static final float CANNON_MAX_ELEVATION = 0.35f; //In radians.
    public static final float TURRET_MAX_ELEVATION = 20f;
    
    //bullet constants
    public static final float BULLET_RADIUS = 0.2f;
    public static final float BULLET_SPEED = 30f;
    public static final float BULLET_MASS = 5f; //5 kg.
    public static final float BULLET_FORCE = 100f; //Energy?
    
    //Names of things.
    public static final String TANK_NODE_NAME = "Tank node";
    public static final String TANK_BODY_GEOM_NAME = "Tank body";
    public static final String TANK_TURRET_ROT_NODE_NAME = "Turret rotation node";
    public static final String TANK_TURRET_ELE_NODE_NAME = "Turret elevation node";
    public static final String TANK_TURRET_MUZZLE_NODE_NAME = "Turret muzzle node";
    public static final String TANK_TURRET_GEOM_NAME = "Tank turret";
    public static final String TANK_BARREL_GEOM_NAME = "Tank barrel";
    
    public static final String BULLET_GEOM_NAME = "Bullet geometry";
    public static final String BULLET_NODE_NAME = "Bullet node";
    
    public static final String ARENA_FLOOR_GEOM_NAME = "Playing field geometry";
    public static final String ARENA_FLOOR_NODE_NAME = "Playing field";
    
    public static final String ARENA_OBSTACLE_GEOM_NAME = "Obstacle geometry";
    public static final String ARENA_OBSTACLE_NODE_NAME = "Obstacle";
    
}
