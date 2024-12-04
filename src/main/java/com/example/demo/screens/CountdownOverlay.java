package com.example.demo.screens;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Displays a countdown overlay (3-2-1-GO) before the game starts.
 * Enhances the visual experience with scaling and fading effects.
 */
public class CountdownOverlay {

    // Root of the current scene
    private final Group root;

    // Dimensions of the screen
    private final double screenWidth;
    private final double screenHeight;

    /**
     * Constructs a CountdownOverlay with the specified scene root and screen dimensions.
     *
     * @param root         The root group of the current scene.
     * @param screenWidth  The width of the screen.
     * @param screenHeight The height of the screen.
     */
    public CountdownOverlay(Group root, double screenWidth, double screenHeight) {
        this.root = root;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /**
     * Displays a visually enhanced countdown (3-2-1-GO) before the game starts.
     *
     * @param onCountdownComplete Runnable to execute when the countdown ends.
     */
    public void showCountdown(Runnable onCountdownComplete) {
        // Create a semi-transparent background overlay
        Rectangle overlay = createOverlay();
        root.getChildren().add(overlay);

        // Create and position countdown text
        Text countdownText = createCountdownText();
        root.getChildren().add(countdownText);

        // Create and play the countdown timeline
        createAndPlayCountdownTimeline(countdownText, overlay, onCountdownComplete);
    }

    /**
     * Creates a semi-transparent overlay for the countdown background.
     *
     * @return The created overlay.
     */
    private Rectangle createOverlay() {
        Rectangle overlay = new Rectangle(screenWidth, screenHeight, Color.BLACK);
        overlay.setOpacity(0.5);
        return overlay;
    }

    /**
     * Creates the initial countdown text.
     *
     * @return The created countdown text.
     */
    private Text createCountdownText() {
        Text countdownText = new Text("3");
        countdownText.setStyle("-fx-font-size: 100px; -fx-font-weight: bold; -fx-fill: white;");
        countdownText.setX(screenWidth / 2 - 30); // Dynamically calculate center
        countdownText.setY(screenHeight / 2);
        return countdownText;
    }

    /**
     * Creates and plays the countdown timeline.
     *
     * @param countdownText      The countdown text to animate.
     * @param overlay            The overlay to remove after the countdown.
     * @param onCountdownComplete Runnable to execute when the countdown ends.
     */
    private void createAndPlayCountdownTimeline(Text countdownText, Rectangle overlay, Runnable onCountdownComplete) {
        Timeline countdownTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> animateText(countdownText, "3")),
                new KeyFrame(Duration.seconds(1), e -> animateText(countdownText, "2")),
                new KeyFrame(Duration.seconds(2), e -> animateText(countdownText, "1")),
                new KeyFrame(Duration.seconds(3), e -> {
                    countdownText.setText("GO!");
                    countdownText.setStyle("-fx-font-size: 120px; -fx-fill: yellow;");
                    animateText(countdownText, "GO!");
                    onCountdownComplete.run();
                }),
                new KeyFrame(Duration.seconds(3.5), e -> {
                    root.getChildren().remove(countdownText);
                    root.getChildren().remove(overlay);
                })
        );
        countdownTimeline.play();
    }

    /**
     * Animates the countdown text with a scaling and fading effect.
     *
     * @param text    The text to animate.
     * @param newText The new text to display.
     */
    private void animateText(Text text, String newText) {
        text.setText(newText);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), text);
        scaleTransition.setFromX(0.5);
        scaleTransition.setFromY(0.5);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.play();

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), text);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }
}
