package com.example.demo.screens;

import com.example.demo.powerups.ShieldImage;
import javafx.scene.Group;

/**
 * Represents the visual setup for Level Two in the game.
 * Handles the display of level-specific elements such as the shield image.
 */
public class LevelViewLevelTwo extends LevelView {

	// Constants for ShieldImage positioning
	private static final int SHIELD_X_POSITION = 1150;
	private static final int SHIELD_Y_POSITION = 500;

	// Root group for the scene
	private final Group root;

	// Shield image for Level Two
	private final ShieldImage shieldImage;

	/**
	 * Constructs a LevelViewLevelTwo object to handle Level Two-specific visuals.
	 *
	 * @param root             The root group to which all elements are added.
	 * @param heartsToDisplay  The number of hearts to display on the screen.
	 */
	public LevelViewLevelTwo(Group root, int heartsToDisplay) {
		super(root, heartsToDisplay); // Call the parent constructor
		this.root = root;
		this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION); // Initialize the shield image
		addImagesToRoot(); // Add shield to the root group
	}

	/**
	 * Adds all Level Two-specific images (e.g., shields) to the root group.
	 */
	private void addImagesToRoot() {
		root.getChildren().add(shieldImage);
	}

	/**
	 * Displays the shield on the screen.
	 */
	public void showShield() {
		shieldImage.showShield();
	}

	/**
	 * Hides the shield from the screen.
	 */
	public void hideShield() {
		shieldImage.hideShield();
	}
}
