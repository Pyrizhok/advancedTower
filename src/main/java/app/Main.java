package app;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.debug.BulletDebugAppState;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.jvpichowski.jme3.states.ESBulletState;
import com.simsilica.es.EntityData;
import com.simsilica.es.base.DefaultEntityData;
import system.*;

public class Main extends SimpleApplication {

	public static void main(String[] args) {
		EntityData entityData = new DefaultEntityData();
		Main app = new Main(entityData);
		app.setShowSettings(false);
		AppSettings settings = new AppSettings(true);
		settings.put("Width", 1280);
		settings.put("Height", 720);
		settings.put("Title", "AdvancedTower");
		settings.put("VSync", true);
		settings.put("Samples", 4);
		app.setSettings(settings);
		app.start();
	}

	public Main(EntityData entityData) {
		super(new VisualAppState(),
				new ExplosionAppState(),
				new EntityDataState(entityData),
				new InvadersAIAppState(),
				new GameAppState(),
				new GateSystem(),
				new CollisionAppState(),
				new BulletAppState(),
				new DecayAppState(),
				new GuiAppState(),
				new ControlAppState(),
				new ESBulletState(entityData)
		);

	}

	@Override
	public void simpleInitApp() {

		ESBulletState esBulletState = stateManager.getState(ESBulletState.class);
		esBulletState.onInitialize(() -> {
			//Add Debug State to debug physics
			//As you see there are getters for physics space and so on.
			BulletDebugAppState debugAppState = new BulletDebugAppState(esBulletState.getPhysicsSpace());
			getStateManager().attach(debugAppState);
		});
	}

	@Override
	public void simpleUpdate(float tpf) {
	}

	@Override
	public void simpleRender(RenderManager rm) {
	}
}