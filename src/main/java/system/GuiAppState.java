package system;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.scene.Node;
import com.jme3x.jfx.FXMLHud;
import com.jme3x.jfx.FxPlatformExecutor;
import com.jme3x.jfx.GuiManager;
import com.jme3x.jfx.cursor.ICursorDisplayProvider;
import com.jme3x.jfx.cursor.proton.ProtonCursorProvider;
import gui.GameHudController;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class GuiAppState extends BaseAppState {

	private GuiManager guiManager;
	private FXMLHud gameHUD;
	private GameHudController controller;
	private InputManager inputManager;

	@Override
	protected void initialize(Application app) {
		Node guiNode = ((SimpleApplication) app).getGuiNode();
		boolean ifFullScreen = false;
		AssetManager assetManager = app.getAssetManager();

		inputManager = app.getInputManager();
		ICursorDisplayProvider cursorProvider = new ProtonCursorProvider(app
				, assetManager
				, inputManager);

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
//		Integer numberOfInvaders = getState(InvadersAIAppState.class).getNumberOfInvaders();
		controller = (GameHudController) gameHUD.getController();
//		controller.setNumberOfInvaders(new TextField(numberOfInvaders.toString()));
		System.out.println(inputManager.getCursorPosition());
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

	@Override
	public void update( float tpf ) {
		System.out.println(inputManager.getCursorPosition());
			if (controller == null) {
			controller = (GameHudController) gameHUD.getController();
		}
		if (controller!= null) {
			Integer numberOfInvaders = getState(InvadersAIAppState.class).getNumberOfInvaders();
			float timePassedFromCreation = getState(GateSystem.class).timePassedFromCreation;

			FxPlatformExecutor.runOnFxApplication(() -> {
				controller.updateInvaderTimeFroCreation(timePassedFromCreation);
				controller.setNumberOfInvaders(numberOfInvaders);
			});


		}
	}
}
