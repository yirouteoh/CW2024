package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WinImage extends ImageView {

	private static final String IMAGE_NAME = "/com/example/demo/images/youwin.png";
	private static final int HEIGHT = 500;
	private static final int WIDTH = 600;

	public WinImage(double xPosition, double yPosition) {
		super();
		java.net.URL imageUrl = getClass().getResource(IMAGE_NAME);
		if (imageUrl == null) {
			System.err.println("Resource not found: " + IMAGE_NAME);
			// Optionally, set a default or placeholder image if the resource is not found
			// this.setImage(new Image("path_to_default_placeholder_image"));
		} else {
			this.setImage(new Image(imageUrl.toExternalForm()));
		}
		this.setVisible(false);
		this.setFitHeight(HEIGHT);
		this.setFitWidth(WIDTH);
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);
	}

	public void showWinImage() {
		this.setVisible(true);
	}
}
