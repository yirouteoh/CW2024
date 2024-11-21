package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameOverImage extends ImageView {

	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";

	public GameOverImage(double screenWidth, double screenHeight) {
		super();
		java.net.URL imageUrl = getClass().getResource(IMAGE_NAME);
		if (imageUrl == null) {
			System.err.println("Resource not found: " + IMAGE_NAME);
		} else {
			this.setImage(new Image(imageUrl.toExternalForm()));
		}

		// Scale the image to fit within the screen while preserving its aspect ratio
		setPreserveRatio(true);

		// Set width relative to screen size (adjustable percentage)
		double targetWidth = screenWidth * 0.6; // Set to 60% of the screen width
		setFitWidth(targetWidth);

		// Dynamically calculate height
		double targetHeight = getImage().getHeight() * (targetWidth / getImage().getWidth());
		setFitHeight(targetHeight);

		// Center the image
		double centerX = (screenWidth - targetWidth) / 2;
		double centerY = (screenHeight - targetHeight) / 2;

		// Move the image slightly upward for better centering
		double verticalAdjustment = -50; // Adjust this value to move up
		setLayoutX(centerX);
		setLayoutY(centerY + verticalAdjustment);
	}
}
