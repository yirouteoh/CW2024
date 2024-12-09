package com.example.demo.screens;

import com.example.demo.sounds.SoundManager;
import com.example.demo.levels.LevelParent;
import com.example.demo.levels.LevelOne;
import com.example.demo.levels.LevelTwo;
import com.example.demo.levels.LevelThree;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class AudioControlPanel extends HBox {

    private final SoundManager soundManager;
    private final Image musicIcon;
    private final Image unmuteMusicIcon;
    private final Image soundIcon;
    private final Image unmuteSoundIcon;
    private final LevelParent currentLevel; // Add this field

    public AudioControlPanel(LevelParent currentLevel) {
        this.soundManager = SoundManager.getInstance();
        this.currentLevel = currentLevel; // Set the current level

        // Load images
        this.musicIcon = new Image(MenuView.getResourceOrThrow("/com/example/demo/images/music.png").toExternalForm());
        this.unmuteMusicIcon = new Image(MenuView.getResourceOrThrow("/com/example/demo/images/unmutemusic.png").toExternalForm());
        this.soundIcon = new Image(MenuView.getResourceOrThrow("/com/example/demo/images/speaker.png").toExternalForm());
        this.unmuteSoundIcon = new Image(MenuView.getResourceOrThrow("/com/example/demo/images/unmuteeffect.png").toExternalForm());

        // Initialize buttons
        // Declare as local variables inside the constructor
        Button muteBackgroundMusicButton = createMuteBackgroundMusicButton();
        Button muteSoundEffectsButton = createMuteSoundEffectsButton();

        // Add buttons to the panel
        this.setSpacing(10); // Adjust spacing
        this.getChildren().addAll(muteBackgroundMusicButton, muteSoundEffectsButton);
    }

    private Button createMuteBackgroundMusicButton() {
        ImageView icon = new ImageView(soundManager.isBackgroundMusicMuted() ? unmuteMusicIcon : musicIcon);
        icon.setFitHeight(30);
        icon.setFitWidth(30);

        Button button = new Button("", icon);
        button.setOnAction(e -> {
            String currentMusicFile = getCurrentLevelMusic(); // Retrieve the current level's music
            if (soundManager.isBackgroundMusicMuted()) {
                soundManager.unmuteBackgroundMusic(currentMusicFile);
                icon.setImage(musicIcon);
            } else {
                soundManager.muteBackgroundMusic();
                icon.setImage(unmuteMusicIcon);
            }
            button.getScene().getRoot().requestFocus();
        });

        return button;
    }

    private String getCurrentLevelMusic() {
        if (currentLevel instanceof LevelOne) return SoundManager.LEVEL_ONE_MUSIC;
        if (currentLevel instanceof LevelTwo) return SoundManager.LEVEL_TWO_MUSIC;
        if (currentLevel instanceof LevelThree) return SoundManager.LEVEL_THREE_MUSIC;
        return SoundManager.MENU_MUSIC;
    }


    private Button createMuteSoundEffectsButton() {
        ImageView icon = new ImageView(soundManager.isSoundEffectsMuted() ? unmuteSoundIcon : soundIcon);
        icon.setFitHeight(30);
        icon.setFitWidth(30);

        Button button = new Button("", icon);
        button.setOnAction(e -> {
            if (soundManager.isSoundEffectsMuted()) {
                soundManager.unmuteSoundEffects();
                icon.setImage(soundIcon);
            } else {
                soundManager.muteSoundEffects();
                icon.setImage(unmuteSoundIcon);
            }
            // Regain focus for the game after clicking the button
            button.getScene().getRoot().requestFocus();
        });

        return button;
    }

}