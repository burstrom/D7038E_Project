/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame.client;

/**
 * Interface for handling changes in the game. 
 * The Action and Analog Listeners hook on to this.
 * 
 * @author MrIngelborn
 */
public interface IActionHandler {

	public void accelerateTank(float acceleraton);

	public void rotateTankY(float radians);

	public void rotateCannon(float radians);

	public void elevateCannon(float radians);

	public void setTankAccelerating(boolean shouldAccelerate);

	public void setShooting(boolean shooting);
	
	public void shootCannonBall();
	
}
