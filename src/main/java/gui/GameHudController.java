package gui;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class GameHudController {

	@FXML
	public TextField numberOfInvader;
	@FXML
	public TextField invaderCreationReloadTimeout;
	@FXML
	public ProgressBar invaderRespawn;
	@FXML
	public TextField cursorScreenPosition;
	private Integer oldNumberOfInvaders;

	public void setNumberOfInvaders(Integer numberOfInvaders) {
		if (numberOfInvaders != null) {
			if (!numberOfInvaders.equals(oldNumberOfInvaders)) {
				oldNumberOfInvaders = numberOfInvaders;
				this.numberOfInvader.setText(numberOfInvaders.toString());
			}
		}
	}

	public void updateInvaderTimeFroCreation(Float timeout) {
		if (timeout != null) {
			this.invaderCreationReloadTimeout.setText(timeout.toString());
			this.invaderRespawn.setProgress(timeout/5);
		}
	}

	public void updateCursorPosition(String cursorPosition) {
		if (cursorScreenPosition != null) {
			this.cursorScreenPosition.setText(cursorPosition.toString());
		}
	}

	@FXML
	private void initialize() {
	}

}
