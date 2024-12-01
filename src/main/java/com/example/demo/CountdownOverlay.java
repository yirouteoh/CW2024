package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
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
     * Displays a countdown (3-2-1-GO) before the game starts.
     *
     * @param onCountdownComplete Runnable to execute when the countdown ends.
     */
    public void showCountdown(Runnable onCountdownComplete) {
        Text countdownText = new Text("3");
        countdownText.setStyle("-fx-font-size: 80px; -fx-font-weight: bold; -fx-fill: white;");
        countdownText.setX(screenWidth / 2 - 40); // Center horizontally
        countdownText.setY(screenHeight / 2);    // Center vertically
        root.getChildren().add(countdownText);

        Timeline countdownTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> countdownText.setText("3")),
                new KeyFrame(Duration.seconds(1), e -> countdownText.setText("2")),
                new KeyFrame(Duration.seconds(2), e -> countdownText.setText("1")),
                new KeyFrame(Duration.seconds(3), e -> {
                    countdownText.setText("GO!");
                    countdownText.setStyle("-fx-font-size: 100px; -fx-fill: yellow;");
                }),
                new KeyFrame(Duration.seconds(4), e -> {
                    root.getChildren().remove(countdownText);
                    onCountdownComplete.run();
                })
        );
        countdownTimeline.play();
    }
}
