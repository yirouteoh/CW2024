package com.example.demo;

import javafx.scene.Group;

public class LevelView {

	private static final double HEART_DISPLAY_X_POSITION = 5;
	private static final double HEART_DISPLAY_Y_POSITION = 25;

	private final Group root;
	private final HeartDisplay heartDisplay;

	public LevelView(Group root, int heartsToDisplay) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
	}

	public void showHeartDisplay() {
		root.getChildren().add(heartDisplay.getContainer());
	}

	public void showWinImage(double screenWidth, double screenHeight, Runnable onRestart, Runnable onExitToMenu) {
		WinImage winScreen = new WinImage(screenWidth, screenHeight, onRestart, onExitToMenu);
		root.getChildren().add(winScreen);
	}

	public void showGameOverImage(double screenWidth, double screenHeight, Runnable onRestart, Runnable onExitToMenu) {
		GameOverImage gameOverImage = new GameOverImage(screenWidth, screenHeight, onRestart, onExitToMenu);
		root.getChildren().add(gameOverImage);
	}

	public void removeHearts(int heartsRemaining) {
		int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
		for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
			heartDisplay.removeHeart();
		}
	}
}
