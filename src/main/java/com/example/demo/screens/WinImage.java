package com.example.demo.screens;

import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class WinImage extends Group {

	private static final String RESTART_IMAGE_NAME = "/com/example/demo/images/restart.png";
	private static final String EXIT_IMAGE_NAME = "/com/example/demo/images/exit.jpg";
	private static final String WIN_IMAGE_NAME = "/com/example/demo/images/youwin.png";

	public WinImage(double screenWidth, double screenHeight, Runnable onRestart, Runnable onExitToMenu) {
		// Transparent gradient background overlay
		Rectangle overlay = new Rectangle(screenWidth, screenHeight);
		overlay.setFill(new LinearGradient(
				0, 0, 1, 1, true, null,
				new Stop(0, Color.LIGHTSKYBLUE.deriveColor(0, 1, 1, 0.5)), // Start color with 50% opacity
				new Stop(1, Color.LIGHTGOLDENRODYELLOW.deriveColor(0, 1, 1, 0.5)) // End color with 50% opacity
		));

		// Replace "YOU WIN!" text with an image
		ImageView winImage = new ImageView();
		java.net.URL winImageUrl = getClass().getResource(WIN_IMAGE_NAME);
		if (winImageUrl != null) {
			winImage.setImage(new Image(winImageUrl.toExternalForm()));
			winImage.setFitWidth(400); // Adjust width as needed
			winImage.setFitHeight(200); // Adjust height as needed
			winImage.setTranslateX((screenWidth - 400) / 2); // Center horizontally
			winImage.setTranslateY((screenHeight / 3) - 50); // Move 50 pixels higher
		} else {
			System.err.println("Win image not found: " + WIN_IMAGE_NAME);
		}

		// Confetti
		ImageView confetti = createConfetti(screenWidth, screenHeight);

		// Buttons container
		VBox buttonContainer = new VBox(20); // Vertical box with 20px spacing
		buttonContainer.setTranslateX((screenWidth - 200) / 2); // Centered horizontally
		buttonContainer.setTranslateY((screenHeight / 3) + 150); // Below the win image

		// Restart Button
		Button restartButton = createImageButton(RESTART_IMAGE_NAME, "Restart", onRestart);

		// Exit Button
		Button exitButton = createImageButton(EXIT_IMAGE_NAME, "Exit", onExitToMenu);

		// Add buttons to the container
		buttonContainer.getChildren().addAll(restartButton, exitButton);

		// Add elements to the Group
		this.getChildren().addAll(overlay, winImage, buttonContainer, confetti);

		// Start confetti animation
		startConfettiAnimation(confetti);
	}

	private Button createImageButton(String imagePath, String text, Runnable onClickAction) {
		// Load the image
		ImageView imageView = new ImageView();
		java.net.URL imageUrl = getClass().getResource(imagePath);
		if (imageUrl != null) {
			imageView.setImage(new Image(imageUrl.toExternalForm()));
			imageView.setFitWidth(30); // Set icon width
			imageView.setFitHeight(30); // Set icon height
		} else {
			System.err.println("Image not found: " + imagePath);
		}

		// Create a button with text and image
		Button button = new Button(text, imageView);
		button.setStyle(
				"-fx-font-size: 18px; -fx-background-color: #F7F7F7; -fx-text-fill: #333333; " +
						"-fx-border-color: #333333; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 10;"
		);
		button.setPrefWidth(200); // Set button width for consistency
		button.setOnAction(event -> onClickAction.run());

		return button;
	}

	private ImageView createConfetti(double screenWidth, double screenHeight) {
		// Load confetti image
		Image confettiImage = new Image(getClass().getResource("/com/example/demo/images/confetti.png").toExternalForm());
		ImageView confetti = new ImageView(confettiImage);

		// Set initial size and position
		confetti.setFitWidth(screenWidth);
		confetti.setFitHeight(350); // Adjust height as needed
		confetti.setLayoutX(0); // Ensure it starts from the left
		confetti.setLayoutY(-150); // Start slightly above the screen

		return confetti;
	}

	private void startConfettiAnimation(ImageView confetti) {
		// Create falling animation for confetti
		TranslateTransition confettiAnimation = new TranslateTransition(Duration.seconds(1.5), confetti); // Reduced duration
		confettiAnimation.setFromY(confetti.getLayoutY());
		confettiAnimation.setToY(50); // Ensure it falls into the visible screen area
		confettiAnimation.setCycleCount(1); // Play only once
		confettiAnimation.play();
	}
}
