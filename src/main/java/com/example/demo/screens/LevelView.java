package com.example.demo.screens;

import com.example.demo.utils.HeartDisplay;
import javafx.scene.Group;

/**
 * Represents the base visual setup for a game level.
 * Provides functionalities for managing and displaying the heart (health) UI for the player.
 */
public class LevelView {

	// Constants for the heart display positioning
	private static final double HEART_DISPLAY_X_POSITION = 5;
	private static final double HEART_DISPLAY_Y_POSITION = 25;

	// Root group for the scene
	private final Group root;

	// Heart display for showing player health
	private final HeartDisplay heartDisplay;

	/**
	 * Constructs a LevelView with a specified number of hearts for display.
	 *
	 * @param root             The root group to which all elements are added.
	 * @param heartsToDisplay  The initial number of hearts to display on the screen.
	 */
	public LevelView(Group root, int heartsToDisplay) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
	}

	/**
	 * Adds the heart display container to the root group, making it visible on the screen.
	 */
	public void showHeartDisplay() {
		root.getChildren().add(heartDisplay.getContainer());
	}

	/**
	 * Updates the heart display by removing hearts to reflect the player's remaining health.
	 *
	 * @param heartsRemaining The number of hearts the player has left.
	 */
	public void removeHearts(int heartsRemaining) {
		int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
		for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
			heartDisplay.removeHeart();
		}
	}
}
