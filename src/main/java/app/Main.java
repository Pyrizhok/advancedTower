package app;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import system.*;

public class Main extends SimpleApplication {

	public static void main(String[] args) {
		Main app = new Main();
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

	public Main() {
		super(new VisualAppState(),
				new ExplosionAppState(),
				new EntityDataState(),
				new InvadersAIAppState(),
				new GameAppState(),
				new CollisionAppState(),
				new BulletAppState(),
				new DecayAppState(),
				new GuiAppState(),
				new ControlAppState()
		);

	}

	@Override
	public void simpleInitApp() {
	}

	@Override
	public void simpleUpdate(float tpf) {
	}

	@Override
	public void simpleRender(RenderManager rm) {
	}
}