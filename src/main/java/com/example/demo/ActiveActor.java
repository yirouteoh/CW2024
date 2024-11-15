package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class ActiveActor extends ImageView {

	private static final String IMAGE_LOCATION = "/com/example/demo/images/";

	public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super();
		java.net.URL imageUrl = getClass().getResource(IMAGE_LOCATION + imageName);
		if (imageUrl == null) {
			System.err.println("Image resource not found: " + IMAGE_LOCATION + imageName);
			// Optionally, set a default or placeholder image if the resource is not found.
			this.setImage(new Image("path_to_default_placeholder_image"));
		} else {
			this.setImage(new Image(imageUrl.toExternalForm()));
		}
		this.setLayoutX(initialXPos);
		this.setLayoutY(initialYPos);
		this.setFitHeight(imageHeight);
		this.setPreserveRatio(true);
	}

	public abstract void updatePosition();

	protected void moveHorizontally(double horizontalMove) {
		this.setTranslateX(getTranslateX() + horizontalMove);
	}

	protected void moveVertically(double verticalMove) {
		this.setTranslateY(getTranslateY() + verticalMove);
	}
}
