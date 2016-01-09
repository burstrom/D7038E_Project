package demo;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import static demo.StandaloneTest.*;

/**
 *
 * @author sirfrog
 */
public class BulletControl extends RigidBodyControl 
    implements PhysicsCollisionListener {

    //Colliding with something generates a PhysicsCollisonEvent
    //You never which node is A or B. Must check both
    public void collision(PhysicsCollisionEvent event) {
        System.out.println("Collision!");
        if (event.getNodeA() != null){
            System.out.println("Node name: "+event.getNodeA().getName());
            if (event.getNodeA().getName().equals(BULLET_NODE_NAME)) {
                final Node bullet = (Node)event.getNodeA();
                collide(bullet);
            }
        }
        if (event.getNodeB() != null){
            System.out.println("Node name: "+event.getNodeB().getName());
            if (event.getNodeB().getName().equals(BULLET_NODE_NAME)) {
                final Node bullet = (Node)event.getNodeB();
                collide(bullet);
            }
        }
    }
    
    private void collide(Node bullet){
        Vector3f hitLocation = bullet.getWorldTranslation();
        bullet.removeFromParent();
        this.getPhysicsSpace().removeCollisionListener(this);
        //Create hit marker.
        System.out.println("Hit!");
        //TODO: Figure out how best to send info of this collision to the parent.
    }
    //Constructors
    public BulletControl(float mass, BulletAppState bulletAppState){
        super(mass);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }
    
    public BulletControl(CollisionShape shape, BulletAppState bulletAppState){
        super(shape);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }
    
    public BulletControl(CollisionShape shape, float mass, BulletAppState bulletAppState){
        super(shape, mass);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }
    
}
