package com.example.demo.screens;

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
import javafx.scene.Scene;
import javafx.util.Duration;

/**
 * Represents the "Game Over" screen displayed when the player loses the game.
 * <p>
 * This screen provides motivational text and options to restart the game or exit to the main menu.
 * </p>
 */
public class GameOverImage extends Group {

	private static final String RESTART_IMAGE_NAME = "/com/example/demo/images/restart.png";
	private static final String EXIT_IMAGE_NAME = "/com/example/demo/images/exit.jpg";

	private final double screenWidth;
	private final double screenHeight;

	/**
	 * Constructor for the GameOverImage class.
	 *
	 * @param screenWidth   The width of the screen.
	 * @param screenHeight  The height of the screen.
	 * @param onRestart     Action to perform on restart.
	 * @param onExitToMenu  Action to perform on exiting to the menu.
	 */
	public GameOverImage(double screenWidth, double screenHeight, Runnable onRestart, Runnable onExitToMenu) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		// Add all elements to the Group
		this.getChildren().addAll(
				createBackgroundOverlay(),
				createGameOverText(),
				createMotivationalText(),
				createButtonContainer(onRestart, onExitToMenu)
		);
	}

	/**
	 * Creates a semi-transparent background overlay.
	 *
	 * @return A Rectangle representing the overlay.
	 */
	private Rectangle createBackgroundOverlay() {
		Rectangle overlay = new Rectangle(screenWidth, screenHeight);
		overlay.setFill(Color.BLACK);
		overlay.setOpacity(0.7); // Semi-transparent black
		return overlay;
	}

	/**
	 * Creates the "Game Over" text with a fade-in effect.
	 *
	 * @return A Text node for the "Game Over" message.
	 */
	private Text createGameOverText() {
		Text gameOverText = new Text("GAME OVER");
		gameOverText.setFont(Font.font("Arial", 80));
		gameOverText.setFill(Color.RED);
		gameOverText.setTranslateY(screenHeight / 3); // Centered vertically
		gameOverText.setTranslateX((screenWidth - getTextWidth(gameOverText)) / 2); // Centered horizontally

		// Fade-in effect
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), gameOverText);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
		fadeTransition.play();

		return gameOverText;
	}

	/**
	 * Creates motivational text displayed below the "Game Over" text.
	 *
	 * @return A Text node for the motivational message.
	 */
	private Text createMotivationalText() {
		Text motivationalText = new Text("Don’t give up! Try again and show them who's the boss!");
		motivationalText.setFont(Font.font("Arial", 20));
		motivationalText.setFill(Color.WHITE);
		motivationalText.setTranslateY(screenHeight / 2.5); // Positioned below "Game Over" text
		motivationalText.setTranslateX((screenWidth - getTextWidth(motivationalText)) / 2); // Centered horizontally
		return motivationalText;
	}

	/**
	 * Creates a container with buttons for restarting and exiting to the menu.
	 *
	 * @param onRestart    Action to perform on restart.
	 * @param onExitToMenu Action to perform on exiting to the menu.
	 * @return A VBox containing the buttons.
	 */
	private VBox createButtonContainer(Runnable onRestart, Runnable onExitToMenu) {
		VBox buttonContainer = new VBox(20); // Vertical box with 20px spacing
		buttonContainer.setTranslateX((screenWidth - 200) / 2); // Centered horizontally
		buttonContainer.setTranslateY(screenHeight / 2); // Below the motivational text

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
		ImageView imageView = loadImageView(imagePath);
		Button button = new Button(text, imageView);

		// Button styling
		button.setStyle(
				"-fx-font-size: 18px; -fx-background-color: #FFFFFF; -fx-text-fill: #000000; " +
						"-fx-border-color: #000000; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 10;"
		);
		button.setPrefWidth(200); // Set button width for consistency
		button.setOnAction(event -> onClickAction.run());

		return button;
	}

	/**
	 * Loads an image into an ImageView.
	 *
	 * @param imagePath The path to the image resource.
	 * @return An ImageView containing the loaded image.
	 */
	private ImageView loadImageView(String imagePath) {
		ImageView imageView = new ImageView();
		try {
			Image image = new Image(getClass().getResource(imagePath).toExternalForm());
			imageView.setImage(image);
			imageView.setFitWidth(30); // Set icon width
			imageView.setFitHeight(30); // Set icon height
		} catch (NullPointerException | IllegalArgumentException e) {
			System.err.println("Image not found: " + imagePath);
		}
		return imageView;
	}

	/**
	 * Calculates the width of a Text node.
	 *
	 * @param text The Text node.
	 * @return The width of the Text node.
	 */
	private double getTextWidth(Text text) {
		new Scene(new Group(text)); // Assign a dummy scene to calculate layout bounds
		text.applyCss();
		return text.getLayoutBounds().getWidth();
	}
}
