package system;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3x.jfx.FXMLHud;
import com.jme3x.jfx.GuiManager;
import com.jme3x.jfx.cursor.ICursorDisplayProvider;
import com.jme3x.jfx.cursor.proton.ProtonCursorProvider;
import javafx.scene.paint.Color;

public class GuiAppState extends BaseAppState {

	private GuiManager guiManager;
	private FXMLHud gameHUD;


	@Override
	protected void initialize(Application app) {
		Node guiNode = ((SimpleApplication) app).getGuiNode();
		boolean ifFullScreen = false;
		AssetManager assetManager = app.getAssetManager();

		ICursorDisplayProvider cursorProvider = new ProtonCursorProvider(app
				, assetManager
				, app.getInputManager());

		GuiManager guiManager = new GuiManager(guiNode
				, assetManager
				, app
				, ifFullScreen
				, cursorProvider);
		this.guiManager = guiManager;

		gameHUD = new FXMLHud("assets/Interfaces/GameHudView.fxml");
		guiManager.attachHudAsync(gameHUD);
		guiManager.getjmeFXContainer()
				.getScene()
				.setFill(new Color(0, 0, 0, 0));

	}

	@Override
	protected void cleanup(Application app) {
		guiManager.detachHudAsync(gameHUD);
	}

	@Override
	protected void onEnable() {

	}

	@Override
	protected void onDisable() {

	}
}
