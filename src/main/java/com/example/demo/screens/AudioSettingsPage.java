package com.example.demo.screens;

import com.example.demo.sounds.SoundManager;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import java.net.URL;

/**
 * The {@code AudioSettingsPage} class handles the display and functionality of the
 * audio settings overlay, allowing users to mute/unmute background music and sound effects.
 */
public class AudioSettingsPage {

    private final Stage stage;
    private final SoundManager soundManager;
    private final Image unmuteMusicImage;
    private final Image unmuteSoundImage;

    /**
     * Constructs an {@code AudioSettingsPage} with the specified parameters.
     *
     * @param stage            the {@link Stage} where the overlay will be displayed
     * @param soundManager     the {@link SoundManager} instance for controlling audio settings
     * @param unmuteMusicImage the {@link Image} for the "Unmute Music" icon
     * @param unmuteSoundImage the {@link Image} for the "Unmute Sound Effects" icon
     */
    public AudioSettingsPage(Stage stage, SoundManager soundManager, Image unmuteMusicImage, Image unmuteSoundImage) {
        this.stage = stage;
        this.soundManager = soundManager;
        this.unmuteMusicImage = unmuteMusicImage;
        this.unmuteSoundImage = unmuteSoundImage;
    }

    /**
     * Displays the audio settings overlay, allowing users to toggle audio settings.
     */
    public void show() {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);"); // Semi-transparent darker background
        overlay.setAlignment(Pos.CENTER);

        VBox container = new VBox(20); // Adjust spacing for a balanced look
        container.setStyle("-fx-background-color: #ffffff; -fx-padding: 30; -fx-border-color: #3498db; -fx-border-width: 3; -fx-border-radius: 15; -fx-background-radius: 15;");
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(500);
        container.setMaxHeight(300); // Refined size

        // Title Text
        Text title = new Text("Audio Settings");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #34495e; -fx-font-family: 'Verdana';");

        // Mute Background Music Button with Icon
        ImageView musicIcon = new ImageView(soundManager.isBackgroundMusicMuted() ? unmuteMusicImage : new Image(getResourceOrThrow("/com/example/demo/images/music.png").toExternalForm()));
        musicIcon.setFitHeight(30);
        musicIcon.setFitWidth(30);

        Button muteBackgroundMusicButton = new Button(
                soundManager.isBackgroundMusicMuted() ? "Unmute Background Music" : "Mute Background Music",
                musicIcon
        );
        muteBackgroundMusicButton.setStyle("-fx-background-color: #74b9ff; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 15; -fx-background-radius: 15;");
        muteBackgroundMusicButton.setOnAction(e -> toggleBackgroundMusic(muteBackgroundMusicButton, musicIcon));
        addHoverEffect(muteBackgroundMusicButton);

        // Mute Sound Effects Button with Icon
        ImageView speakerIcon = new ImageView(soundManager.isSoundEffectsMuted() ? unmuteSoundImage : new Image(getResourceOrThrow("/com/example/demo/images/speaker.png").toExternalForm()));
        speakerIcon.setFitHeight(30);
        speakerIcon.setFitWidth(30);

        Button muteSoundEffectsButton = new Button(
                soundManager.isSoundEffectsMuted() ? "Unmute Sound Effects" : "Mute Sound Effects",
                speakerIcon
        );
        muteSoundEffectsButton.setStyle("-fx-background-color: #ff69b4; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 15; -fx-background-radius: 15;");
        muteSoundEffectsButton.setOnAction(e -> toggleSoundEffects(muteSoundEffectsButton, speakerIcon));
        addHoverEffect(muteSoundEffectsButton);

        // Close Button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 15; -fx-background-radius: 15;");
        closeButton.setOnAction(e -> closeOverlay(overlay));
        addHoverEffect(closeButton);

        // Add elements to the container
        container.getChildren().addAll(title, muteBackgroundMusicButton, muteSoundEffectsButton, closeButton);
        overlay.getChildren().add(container);

        // Add overlay to the root
        StackPane root = (StackPane) stage.getScene().getRoot();
        if (!root.getChildren().contains(overlay)) {
            root.getChildren().add(overlay);
        }
        overlay.setVisible(true);
    }

    /**
     * Toggles the background music state between muted and unmuted.
     *
     * @param button    the button to update the text and icon
     * @param musicIcon the icon to update based on the music state
     */
    private void toggleBackgroundMusic(Button button, ImageView musicIcon) {
        if (soundManager.isBackgroundMusicMuted()) {
            soundManager.unmuteBackgroundMusic(SoundManager.MENU_MUSIC);
            button.setText("Mute Background Music");
            musicIcon.setImage(new Image(getResourceOrThrow("/com/example/demo/images/music.png").toExternalForm()));
            soundManager.playBackgroundMusic(SoundManager.MENU_MUSIC);
        } else {
            soundManager.muteBackgroundMusic();
            button.setText("Unmute Background Music");
            musicIcon.setImage(unmuteMusicImage);
            soundManager.stopBackgroundMusic();
        }
    }

    /**
     * Toggles the sound effects state between muted and unmuted.
     *
     * @param button      the button to update the text and icon
     * @param speakerIcon the icon to update based on the sound effects state
     */
    private void toggleSoundEffects(Button button, ImageView speakerIcon) {
        if (soundManager.isSoundEffectsMuted()) {
            soundManager.unmuteSoundEffects();
            button.setText("Mute Sound Effects");
            speakerIcon.setImage(new Image(getResourceOrThrow("/com/example/demo/images/speaker.png").toExternalForm()));
        } else {
            soundManager.muteSoundEffects();
            button.setText("Unmute Sound Effects");
            speakerIcon.setImage(unmuteSoundImage);
        }
    }

    /**
     * Closes the overlay and removes it from the root pane.
     *
     * @param overlay the overlay to close
     */
    private void closeOverlay(StackPane overlay) {
        overlay.setVisible(false);
        StackPane root = (StackPane) stage.getScene().getRoot();
        root.getChildren().remove(overlay);
    }

    private void addHoverEffect(Button button) {
        // Shorten the animation duration to make it more responsive
        Duration hoverDuration = Duration.millis(100);

        // Scale up transition for hover in
        ScaleTransition scaleUp = new ScaleTransition(hoverDuration, button);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);

        // Scale down transition for hover out
        ScaleTransition scaleDown = new ScaleTransition(hoverDuration, button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // Set hover event handlers
        button.setOnMouseEntered(e -> {
            scaleDown.stop(); // Ensure the "scale down" animation stops immediately
            scaleUp.playFromStart(); // Start the "scale up" animation
        });

        button.setOnMouseExited(e -> {
            scaleUp.stop(); // Ensure the "scale up" animation stops immediately
            scaleDown.playFromStart(); // Start the "scale down" animation
        });
    }

    /**
     * Retrieves a resource or throws an exception if not found.
     *
     * @param resourcePath the resource path
     * @return the resource URL
     */
    private URL getResourceOrThrow(String resourcePath) {
        URL resourceUrl = getClass().getResource(resourcePath);
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        return resourceUrl;
    }
}
