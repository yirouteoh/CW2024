package com.example.demo.powerups;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents a visual shield effect in the game.
 * <p>
 * The shield is displayed as an image when active and hidden when inactive. It is typically
 * used to indicate a temporary invulnerability effect for a plane or actor.
 * </p>
 */
public class ShieldImage extends ImageView {
	
	private static final String IMAGE_NAME = "/com/example/demo/images/shield.png";
	private static final int SHIELD_SIZE = 200;

	/**
	 * Constructs a {@link ShieldImage} instance and sets its initial position and visibility.
	 *
	 * @param xPosition The initial X position of the shield.
	 * @param yPosition The initial Y position of the shield.
	 */
	public ShieldImage(double xPosition, double yPosition) {
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);

		// Safely load the shield image
		java.net.URL resource = getClass().getResource(IMAGE_NAME);
		if (resource != null) {
			this.setImage(new Image(resource.toExternalForm())); // Load the image if resource exists
		} else {
			System.err.println("Warning: Shield image resource not found at " + IMAGE_NAME);
		}

		this.setVisible(false); // Initially hidden
		this.setFitHeight(SHIELD_SIZE);
		this.setFitWidth(SHIELD_SIZE);
	}

	/**
	 * Displays the shield image, making it visible in the game scene.
	 */
	public void showShield() {
		this.setVisible(true);
	}

	/**
	 * Hides the shield image, making it invisible in the game scene.
	 */
	public void hideShield() {
		this.setVisible(false);
	}



}
