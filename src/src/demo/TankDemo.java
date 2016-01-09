package demo;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author MrIngelborn
 */
public class TankDemo extends SimpleApplication{
	private Node tankNode;
	private float rotation = 0;
	private boolean rotateUp = true;
	
	public static void main(String[] args) {
		TankDemo main = new TankDemo();
		main.start();
	}

	@Override
	public void simpleInitApp() {
		flyCam.setMoveSpeed(20);
		
		tankNode = createTank();
                rootNode.attachChild(tankNode);
		/*
		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		BloomFilter bf = new BloomFilter(BloomFilter.GlowMode.Objects);
		fpp.addFilter(bf);
		viewPort.addProcessor(fpp);
		*/
		DirectionalLight dl = new DirectionalLight();
		dl.setColor(ColorRGBA.White);
		rootNode.addLight(dl);
		
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White);
		rootNode.addLight(al);
		
//		System.out.println(tankNode.getQuantity());
		for (Spatial spat : tankNode.getChildren()) {
			System.out.println(spat.getName());
		}
	}

	@Override
	public void simpleUpdate(float tpf) {
		Node cannon = (Node) tankNode.getChild("TankCannonGeom-ogremesh");
		Node cannonPart2 = (Node) cannon.getChild("CannonPart2Geom-ogremesh");
		Node cannonPart3 = (Node) cannonPart2.getChild("CannonPart3Geom-ogremesh");
		Node engine = (Node) tankNode.getChild("EngineGeom-ogremesh");
		
		engine.rotate(0, tpf*16, 0);
		
		if (rotateUp) {
			rotation += tpf/8;
			cannonPart2.rotate(-tpf/8, 0 , 0);
			cannonPart3.rotate(-tpf/8, 0 , 0);
			if (rotation > 0.3f) {
				rotateUp = false;
			}
		}
		else {
			rotation -= tpf/8;
			cannonPart2.rotate(tpf/8, 0 , 0);
			cannonPart3.rotate(tpf/8, 0 , 0);
			if (rotation < 0) {
				rotateUp = true;
			}
		}
		
	}
	
	/** 
	 * Creates a new tank
	 * @return 
	 */
	private Node createTank() {
		Node tankBodyNode = (Node) assetManager.loadModel("Models/HoverTank/TankBodyGeom.mesh.xml");
		Node tankCannonBaseNode = (Node) assetManager.loadModel("Models/HoverTank/TankCannonGeom.mesh.xml");
		Node tankCannonLinkNode = (Node) assetManager.loadModel("Models/HoverTank/CannonPart2Geom.mesh.xml");
		Node tankCannonMussleNode = (Node) assetManager.loadModel("Models/HoverTank/CannonPart3Geom.mesh.xml");
		Node tankEngineNode = (Node) assetManager.loadModel("Models/HoverTank/EngineGeom.mesh.xml");

		tankCannonLinkNode.attachChild(tankCannonMussleNode);
		tankCannonBaseNode.attachChild(tankCannonLinkNode);
		tankBodyNode.attachChild(tankEngineNode);
		tankBodyNode.attachChild(tankCannonBaseNode);
		
		tankCannonBaseNode.setLocalTranslation(0, 2.0985f, -2.14132f);
		tankCannonLinkNode.setLocalTranslation(0, 0.28472f, 2.08453f);
		tankCannonMussleNode.setLocalTranslation(0, -0.16652f, 1.50661f);
		tankEngineNode.setLocalTranslation(0, 0.32361f, -4.62786f);
		tankEngineNode.getChild(0).setLocalTranslation(0, 0, -0.08f);
		
		tankBodyNode.rotate(0, FastMath.PI, 0);
		
		ColorRGBA tankColor = ColorRGBA.Blue;
		
		Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		mat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/MetalGalvanized.jpg"));
		//mat.setTexture("NormalMap", assetManager.loadTexture("Textures/MetalGalvanized.jpg"));
		//mat.setTexture("SpecularMap", assetManager.loadTexture("Models/HoverTank/tank_specular.jpg"));
		mat.setBoolean("UseMaterialColors",true); 
		mat.setColor("Diffuse", ColorRGBA.White);
		mat.setColor("Ambient", tankColor.mult(0.5f));
		//mat.setColor("Specular", ColorRGBA.White);
		tankBodyNode.setMaterial(mat);
		
		return tankBodyNode;
	}
	
}
