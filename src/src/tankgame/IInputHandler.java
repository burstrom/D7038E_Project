/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

/**
 * Interface for the Action and Analog Listeners to hook on to
 * @author MrIngelborn
 */
public interface IInputHandler {

	public void shootBullet();

	public void accelerateTank(float acceleraton);

	public void rotateTankY(float radians);

	public void rotateCannon(float radians);

	public void elevateCannon(float radians);

	public void setTankAccelerating(boolean shouldAccelerate);
	
}
