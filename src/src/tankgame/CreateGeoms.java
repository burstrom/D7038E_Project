/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;


import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/*
 * This class creates a white box and is used by both the server and 
 * the client.
 */
public class CreateGeoms {

    private Material m;
    private Box b;

    public CreateGeoms(SimpleApplication simpApp) {
        m = new Material(simpApp.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        b = new Box(Vector3f.ZERO, new Vector3f(1, 1, 1));
    }

    /*
     * We could add more methods for creating other kinds of geometries. 
     */
    public Geometry createBox() {
        Geometry box = new Geometry("box", b);
        box.setMaterial(m);
        return box;
    }
}
