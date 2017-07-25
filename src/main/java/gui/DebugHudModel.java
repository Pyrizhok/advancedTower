package gui;

import javafx.beans.property.IntegerProperty;

public class DebugHudModel {

	private final IntegerProperty numberOfInvaders;

	public DebugHudModel(IntegerProperty numberOfInvaders) {
		this.numberOfInvaders = numberOfInvaders;
	}
}
