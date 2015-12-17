package tankgame;

import com.jme3.app.SimpleApplication;
import tankgame.geoms.TankNode;
import tankgame.settings.Constants;

/**
 *
 * @author MrIngelborn
 */
public abstract class GameCommon extends SimpleApplication {
	private TankNode[] tankNodes;

	@Override
	public void simpleInitApp() {
		tankNodes = new TankNode[Constants.MAX_PLAYERS];
	}
	
}
