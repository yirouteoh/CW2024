package com.example.demo;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CountdownOverlay {

    private final Group root; // Root of the current scene
    private final double screenWidth;
    private final double screenHeight;

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
        Rectangle overlay = new Rectangle(screenWidth, screenHeight, Color.BLACK);
        overlay.setOpacity(0.5);
        root.getChildren().add(overlay);

        // Create countdown text
        Text countdownText = new Text("3");
        countdownText.setStyle("-fx-font-size: 100px; -fx-font-weight: bold; -fx-fill: white;");
        countdownText.setX(screenWidth / 2 - 30); // Dynamically calculate center
        countdownText.setY(screenHeight / 2);
        root.getChildren().add(countdownText);

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
     * Animates the countdown text with a scaling effect.
     *
     * @param text Text to animate.
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
