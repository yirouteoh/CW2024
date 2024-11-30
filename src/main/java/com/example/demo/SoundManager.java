package com.example.demo;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundManager {
    private static SoundManager instance; // Singleton instance
    private MediaPlayer mediaPlayer; // For background music
    private AudioClip shootingSound; // For short sound effects
    private final double volume = 0.5; // Default volume (50%)

    // Audio file paths
    public static final String MENU_MUSIC = "/com/example/demo/audios/menumusic.mp3";
    public static final String LEVEL_ONE_MUSIC = "/com/example/demo/audios/levelonemusic.mp3";
    public static final String LEVEL_TWO_MUSIC = "/com/example/demo/audios/leveltwomusic.mp3";
    public static final String LEVEL_THREE_MUSIC = "/com/example/demo/audios/levelthreemusic.mp3";
    public static final String SHOOT_SOUND = "/com/example/demo/audios/shootmusic.mp3";

    private SoundManager() { }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
            instance.initializeSounds(); // Initialize all sounds
        }
        return instance;
    }

    private void initializeSounds() {
        try {
            // Load shooting sound
            URL shootResource = getClass().getResource(SHOOT_SOUND);
            if (shootResource == null) {
                throw new IllegalArgumentException("Audio file not found: " + SHOOT_SOUND);
            }
            shootingSound = new AudioClip(shootResource.toExternalForm());
            shootingSound.setVolume(volume); // Set initial volume for sound effects
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playBackgroundMusic(String audioFilePath) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }

            URL resource = getClass().getResource(audioFilePath);
            if (resource == null) {
                throw new IllegalArgumentException("Audio file not found: " + audioFilePath);
            }

            Media media = new Media(resource.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop background music
            mediaPlayer.setVolume(volume); // Set initial volume for background music
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playShootSound() {
        if (shootingSound != null) {
            shootingSound.play();
        }
    }

    public void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void pauseBackgroundMusic() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        }
    }

    public void resumeBackgroundMusic() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            mediaPlayer.play();
        }
    }


}
