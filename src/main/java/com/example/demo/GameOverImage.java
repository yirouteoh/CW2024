package com.example.demo;

import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GameOverImage extends Group {

	private static final String RESTART_IMAGE_NAME = "/com/example/demo/images/restart.png";
	private static final String EXIT_IMAGE_NAME = "/com/example/demo/images/exit.jpg";

	public GameOverImage(double screenWidth, double screenHeight, Runnable onRestart, Runnable onExitToMenu) {
		// Dimmed background overlay
		Rectangle overlay = new Rectangle(screenWidth, screenHeight);
		overlay.setFill(Color.BLACK);
		overlay.setOpacity(0.7); // Semi-transparent black

		// "Game Over" text
		Text gameOverText = new Text("GAME OVER");
		gameOverText.setFont(Font.font("Arial", 80));
		gameOverText.setFill(Color.RED);
		gameOverText.setTranslateY(screenHeight / 3); // Centered vertically
		gameOverText.setTranslateX((screenWidth - gameOverText.getLayoutBounds().getWidth()) / 2); // Centered horizontally

		// Motivational text
		Text motivationalText = new Text("Don’t give up! Try again and show them who's the boss!");
		motivationalText.setFont(Font.font("Arial", 20));
		motivationalText.setFill(Color.WHITE);
		motivationalText.setTranslateY(screenHeight / 2.5); // Position below "Game Over" text
		motivationalText.setTranslateX((screenWidth - motivationalText.getLayoutBounds().getWidth()) / 2); // Centered horizontally

		// Buttons container
		VBox buttonContainer = new VBox(20); // Vertical box with 20px spacing
		buttonContainer.setTranslateX((screenWidth - 200) / 2); // Centered horizontally
		buttonContainer.setTranslateY(screenHeight / 2); // Below the motivational text

		// Restart Button
		Button restartButton = createImageButton(RESTART_IMAGE_NAME, "Restart", onRestart);

		// Exit Button
		Button exitButton = createImageButton(EXIT_IMAGE_NAME, "Exit", onExitToMenu);

		// Add buttons to the container
		buttonContainer.getChildren().addAll(restartButton, exitButton);

		// Fade-in effect for the "Game Over" text
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), gameOverText);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
		fadeTransition.play();

		// Add all elements to the Group
		this.getChildren().addAll(overlay, gameOverText, motivationalText, buttonContainer);
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
				"-fx-font-size: 18px; -fx-background-color: #FFFFFF; -fx-text-fill: #000000; " +
						"-fx-border-color: #000000; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 10;"
		);
		button.setPrefWidth(200); // Set button width for consistency
		button.setOnAction(event -> onClickAction.run());

		return button;
	}
}
