package tankgame;

import com.jme3.renderer.RenderManager;
import com.jme3.system.JmeContext;

/**
 *
 * @author MrIngelborn
 */
public class GameServer extends GameCommon {
	
	public static void main(String[] args) {
		GameServer server = new GameServer();
		server.start(JmeContext.Type.Headless);
	}

	@Override
	public void simpleInitApp() {
		super.simpleInitApp();
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
