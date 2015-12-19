package tankgame.geoms;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

/**
 * Node for the tanks in game
 * 
 * @author MrIngelborn
 */
public class TankNode extends GeomNode {
	private ColorRGBA color;
	
	/**
	 * @param name Name of the Node
	 * @param color THe color of the tank
	 */
	public TankNode(String name, ColorRGBA color) {
		super(name);
		this.color = color;
	}
	
	/**
	 * Creates the nodes and geomeries under the main tank Node.
	 * 
	 * Called by the client only.
	 * 
	 * @param assetManager The games AssetManager for importing assets
	 */
	@Override
	public void createGeom(AssetManager assetManager) {
		//Import all the different tank parts
		Node tankBodyNode = (Node) assetManager.loadModel("Models/HoverTank/TankBodyGeom.mesh.xml");
		Node tankCannonBaseNode = (Node) assetManager.loadModel("Models/HoverTank/TankCannonGeom.mesh.xml");
		Node tankCannonLinkNode = (Node) assetManager.loadModel("Models/HoverTank/CannonPart2Geom.mesh.xml");
		Node tankCannonMussleNode = (Node) assetManager.loadModel("Models/HoverTank/CannonPart3Geom.mesh.xml");
		Node tankEngineNode = (Node) assetManager.loadModel("Models/HoverTank/EngineGeom.mesh.xml");
		
		//Attach the parts the their parents
		tankCannonLinkNode.attachChild(tankCannonMussleNode);
		tankCannonBaseNode.attachChild(tankCannonLinkNode);
		tankBodyNode.attachChild(tankEngineNode);
		tankBodyNode.attachChild(tankCannonBaseNode);
		this.attachChild(tankBodyNode);
		
		//Set the local translation for all parts
		tankCannonBaseNode.setLocalTranslation(0, 2.0985f, -2.14132f);
		tankCannonLinkNode.setLocalTranslation(0, 0.28472f, 2.08453f);
		tankCannonMussleNode.setLocalTranslation(0, -0.16652f, 1.50661f);
		tankEngineNode.setLocalTranslation(0, 0.32361f, -4.62786f);
		tankEngineNode.getChild(0).setLocalTranslation(0, 0, -0.08f);
		
		//Set the tank's material and color
		Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		mat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/MetalGalvanized.jpg"));
		//mat.setTexture("NormalMap", assetManager.loadTexture("Textures/MetalGalvanized.jpg"));
		//mat.setTexture("SpecularMap", assetManager.loadTexture("Models/HoverTank/tank_specular.jpg"));
		mat.setBoolean("UseMaterialColors",true); 
		mat.setColor("Diffuse", ColorRGBA.White);
		mat.setColor("Ambient", color.mult(0.5f));
		//mat.setColor("Specular", ColorRGBA.White);
		tankBodyNode.setMaterial(mat);
	}
	
}
