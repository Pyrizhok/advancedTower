package gui;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class GameHudController {

	private Integer oldNumberOfInvaders;

	@FXML
	public void setNumberOfInvaders(Integer numberOfInvaders) {
		if (numberOfInvaders != null) {
			if (!numberOfInvaders.equals(oldNumberOfInvaders)) {
				oldNumberOfInvaders = numberOfInvaders;
				this.numberOfInvader.setText(numberOfInvaders.toString());
				System.out.println("number of invaders has to be updated to :" + numberOfInvaders);
			}
		}
	}

	@FXML
	public void updateInvaderTimeFroCreation(Float timeout) {
		if (timeout != null) {
			this.invaderCreationReloadTimeout.setText(timeout.toString());
			this.invaderRespawn.setProgress(timeout/5);
		}
	}

	@FXML
	public TextField numberOfInvader;

	@FXML
	public TextField invaderCreationReloadTimeout;

	@FXML
	public ProgressBar invaderRespawn;

	@FXML
	private void initialize() {
	}

}
