package com.example.demo.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a visual display of hearts used to indicate the player's health.
 * <p>
 * The class manages a container of heart icons, allowing hearts to be dynamically
 * added or removed based on the player's current health.
 * </p>
 */
public class HeartDisplay {

	private static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png";
	private static final int HEART_HEIGHT = 50;
	private static final int INDEX_OF_FIRST_ITEM = 0;

	private final HBox container;
	private final double containerXPosition;
	private final double containerYPosition;
	private final int totalHearts;

	/**
	 * Constructor for HeartDisplay.
	 *
	 * @param xPosition      The x-coordinate of the heart container.
	 * @param yPosition      The y-coordinate of the heart container.
	 * @param numberOfHearts The total number of hearts to display.
	 */
	public HeartDisplay(double xPosition, double yPosition, int numberOfHearts) {
		this.containerXPosition = xPosition;
		this.containerYPosition = yPosition;
		this.totalHearts = numberOfHearts;
		this.container = createContainer();
		addHeartsToContainer();
	}

	/**
	 * Initializes the heart container with the correct position.
	 *
	 * @return The initialized HBox container.
	 */
	private HBox createContainer() {
		HBox heartContainer = new HBox();
		heartContainer.setLayoutX(containerXPosition);
		heartContainer.setLayoutY(containerYPosition);
		return heartContainer;
	}

	/**
	 * Adds the specified number of hearts to the container.
	 */
	private void addHeartsToContainer() {
		for (int i = 0; i < totalHearts; i++) {
			ImageView heart = createHeartImageView();
			if (heart != null) {
				container.getChildren().add(heart);
			}
		}
	}

	/**
	 * Creates an ImageView for the heart icon.
	 *
	 * @return The ImageView for the heart, or null if the image resource is not found.
	 */
	private ImageView createHeartImageView() {
		java.net.URL imageUrl = getClass().getResource(HEART_IMAGE_NAME);
		if (imageUrl == null) {
			System.err.println("Heart image resource not found: " + HEART_IMAGE_NAME);
			return null; // Return null to skip adding the heart
		}

		ImageView heart = new ImageView(new Image(imageUrl.toExternalForm()));
		heart.setFitHeight(HEART_HEIGHT);
		heart.setPreserveRatio(true);
		return heart;
	}

	/**
	 * Removes one heart from the container, if available.
	 */
	public void removeHeart() {
		if (!container.getChildren().isEmpty()) {
			container.getChildren().remove(INDEX_OF_FIRST_ITEM);
		}
	}

	/**
	 * Gets the heart container.
	 *
	 * @return The HBox containing the hearts.
	 */
	public HBox getContainer() {
		return container;
	}
}
