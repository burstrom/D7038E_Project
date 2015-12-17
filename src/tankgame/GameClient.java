package tankgame;

import com.jme3.renderer.RenderManager;
import tankgame.client.KeyBindings;

/**
 *
 * @author MrIngelborn
 */
public class GameClient extends GameCommon {
	private KeyBindings keyBindings = new KeyBindings(inputManager);
	
	public static void main(String[] args) {
		GameClient gameClient = new GameClient();
		gameClient.start();
	}

	@Override
	public void simpleInitApp() {
		super.simpleInitApp();
		keyBindings.init();
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
	}

	@Override
	public void simpleRender(RenderManager rm) {
		super.simpleRender(rm);
	}
	
}
