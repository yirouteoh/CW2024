package com.example.demo.actors;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * Represents an active, drawable actor in the game.
 * <p>
 * This abstract class provides the basic structure and functionality for game actors,
 * including image initialization, positioning, and movement logic. Subclasses must implement
 * specific behavior by overriding the {@link #updatePosition()} method.
 * </p>
 */
public abstract class ActiveActor extends ImageView {

	private static final String IMAGE_LOCATION = "/com/example/demo/images/";

	// Constructor
	public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super();
		initializeImage(imageName);
		setPosition(initialXPos, initialYPos);
		setSize(imageHeight);
	}

	/**
	 * Initializes the image for the actor.
	 * If the specified image is not found, a placeholder image is used.
	 *
	 * @param imageName the name of the image file
	 */
	private void initializeImage(String imageName) {
		java.net.URL imageUrl = getClass().getResource(IMAGE_LOCATION + imageName);
		if (imageUrl == null) {
			System.err.println("Image resource not found: " + IMAGE_LOCATION + imageName);
			this.setImage(new Image("path_to_default_placeholder_image"));
		} else {
			this.setImage(new Image(imageUrl.toExternalForm()));
		}
	}

	/**
	 * Sets the initial position of the actor.
	 *
	 * @param initialXPos the initial X position
	 * @param initialYPos the initial Y position
	 */
	private void setPosition(double initialXPos, double initialYPos) {
		this.setLayoutX(initialXPos);
		this.setLayoutY(initialYPos);
	}

	/**
	 * Sets the size of the actor while preserving the aspect ratio.
	 *
	 * @param imageHeight the desired height of the image
	 */
	private void setSize(int imageHeight) {
		this.setFitHeight(imageHeight);
		this.setPreserveRatio(true);
	}

	/**
	 * Abstract method to update the position of the actor.
	 * Subclasses must implement this method to define specific movement logic.
	 */
	public abstract void updatePosition();

	/**
	 * Moves the actor horizontally by a specified amount.
	 *
	 * @param horizontalMove the amount to move horizontally
	 */
	protected void moveHorizontally(double horizontalMove) {
		this.setTranslateX(getTranslateX() + horizontalMove);
	}

	/**
	 * Moves the actor vertically by a specified amount.
	 *
	 * @param verticalMove the amount to move vertically
	 */
	protected void moveVertically(double verticalMove) {
		this.setTranslateY(getTranslateY() + verticalMove);
	}
}
