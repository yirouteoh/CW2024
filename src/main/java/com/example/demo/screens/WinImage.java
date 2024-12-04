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
	private static final String CONFETTI_IMAGE_NAME = "/com/example/demo/images/confetti.png";

	private final double screenWidth;
	private final double screenHeight;

	/**
	 * Constructor for the WinImage class.
	 *
	 * @param screenWidth   The width of the screen.
	 * @param screenHeight  The height of the screen.
	 * @param onRestart     Action to perform when the restart button is clicked.
	 * @param onExitToMenu  Action to perform when the exit button is clicked.
	 */
	public WinImage(double screenWidth, double screenHeight, Runnable onRestart, Runnable onExitToMenu) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		// Add elements to the Group
		this.getChildren().addAll(
				createBackgroundOverlay(),
				createWinImage(),
				createButtonContainer(onRestart, onExitToMenu),
				createAndAnimateConfetti()
		);
	}

	/**
	 * Creates a gradient background overlay.
	 *
	 * @return A Rectangle with gradient fill.
	 */
	private Rectangle createBackgroundOverlay() {
		Rectangle overlay = new Rectangle(screenWidth, screenHeight);
		overlay.setFill(new LinearGradient(
				0, 0, 1, 1, true, null,
				new Stop(0, Color.LIGHTSKYBLUE.deriveColor(0, 1, 1, 0.5)), // Start color with 50% opacity
				new Stop(1, Color.LIGHTGOLDENRODYELLOW.deriveColor(0, 1, 1, 0.5)) // End color with 50% opacity
		));
		return overlay;
	}

	/**
	 * Creates the "You Win" image and positions it on the screen.
	 *
	 * @return An ImageView displaying the "You Win" image.
	 */
	private ImageView createWinImage() {
		ImageView winImage = new ImageView();
		try {
			winImage.setImage(loadImage(WIN_IMAGE_NAME));
			winImage.setFitWidth(400); // Adjust width as needed
			winImage.setFitHeight(200); // Adjust height as needed
			winImage.setTranslateX((screenWidth - 400) / 2); // Center horizontally
			winImage.setTranslateY((screenHeight / 3) - 50); // Move 50 pixels higher
		} catch (IllegalArgumentException e) {
			System.err.println("Win image not found: " + WIN_IMAGE_NAME);
		}
		return winImage;
	}

	/**
	 * Creates a container with restart and exit buttons.
	 *
	 * @param onRestart    Action to perform when the restart button is clicked.
	 * @param onExitToMenu Action to perform when the exit button is clicked.
	 * @return A VBox containing the buttons.
	 */
	private VBox createButtonContainer(Runnable onRestart, Runnable onExitToMenu) {
		VBox buttonContainer = new VBox(20); // Vertical box with 20px spacing
		buttonContainer.setTranslateX((screenWidth - 200) / 2); // Centered horizontally
		buttonContainer.setTranslateY((screenHeight / 3) + 150); // Below the win image

		// Add buttons to the container
		buttonContainer.getChildren().addAll(
				createImageButton(RESTART_IMAGE_NAME, "Restart", onRestart),
				createImageButton(EXIT_IMAGE_NAME, "Exit", onExitToMenu)
		);

		return buttonContainer;
	}

	/**
	 * Creates a button with an image and text.
	 *
	 * @param imagePath     The path to the button image.
	 * @param text          The text to display on the button.
	 * @param onClickAction The action to perform when the button is clicked.
	 * @return A Button with the specified image and text.
	 */
	private Button createImageButton(String imagePath, String text, Runnable onClickAction) {
		ImageView imageView = new ImageView();
		try {
			imageView.setImage(loadImage(imagePath));
			imageView.setFitWidth(30); // Set icon width
			imageView.setFitHeight(30); // Set icon height
		} catch (IllegalArgumentException e) {
			System.err.println("Image not found: " + imagePath);
		}

		Button button = new Button(text, imageView);
		button.setStyle(
				"-fx-font-size: 18px; -fx-background-color: #F7F7F7; -fx-text-fill: #333333; " +
						"-fx-border-color: #333333; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 10;"
		);
		button.setPrefWidth(200); // Set button width for consistency
		button.setOnAction(event -> onClickAction.run());

		return button;
	}

	/**
	 * Creates and animates the confetti effect.
	 *
	 * @return An ImageView displaying the confetti animation.
	 */
	private ImageView createAndAnimateConfetti() {
		ImageView confetti = new ImageView();
		try {
			confetti.setImage(loadImage(CONFETTI_IMAGE_NAME));
			confetti.setFitWidth(screenWidth);
			confetti.setFitHeight(350); // Adjust height as needed
			confetti.setLayoutX(0); // Ensure it starts from the left
			confetti.setLayoutY(-150); // Start slightly above the screen

			// Start falling animation
			startConfettiAnimation(confetti);
		} catch (IllegalArgumentException e) {
			System.err.println("Confetti image not found: " + CONFETTI_IMAGE_NAME);
		}
		return confetti;
	}

	/**
	 * Starts the animation for the confetti.
	 *
	 * @param confetti The ImageView containing the confetti image.
	 */
	private void startConfettiAnimation(ImageView confetti) {
		TranslateTransition confettiAnimation = new TranslateTransition(Duration.seconds(1.5), confetti);
		confettiAnimation.setFromY(confetti.getLayoutY());
		confettiAnimation.setToY(50); // Ensure it falls into the visible screen area
		confettiAnimation.setCycleCount(1); // Play only once
		confettiAnimation.play();
	}

	/**
	 * Loads an image from the specified path.
	 *
	 * @param imagePath The path to the image resource.
	 * @return The loaded Image object.
	 * @throws IllegalArgumentException if the image cannot be loaded.
	 */
	private Image loadImage(String imagePath) {
		java.net.URL imageUrl = getClass().getResource(imagePath);
		if (imageUrl == null) {
			throw new IllegalArgumentException("Image not found: " + imagePath);
		}
		return new Image(imageUrl.toExternalForm());
	}
}
