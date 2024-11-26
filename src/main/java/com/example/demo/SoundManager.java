package com.example.demo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundManager {
    private static SoundManager instance; // Singleton instance
    private MediaPlayer mediaPlayer;

    // Audio file path for menu music
    public static final String MENU_MUSIC = "/com/example/demo/audios/menumusic.mp3";
    // Audio file path for level one music
    public static final String LEVEL_ONE_MUSIC = "/com/example/demo/audios/levelonemusic.mp3";
    public static final String LEVEL_TWO_MUSIC = "/com/example/demo/audios/leveltwomusic.mp3";
    public static final String LEVEL_THREE_MUSIC = "/com/example/demo/audios/levelthreemusic.mp3";

    // Private constructor to enforce Singleton pattern
    private SoundManager() { }

    // Public method to get the Singleton instance
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playBackgroundMusic(String audioFilePath) {
        try {
            // Stop any existing media player
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }

            // Locate the audio file
            URL resource = getClass().getResource(audioFilePath);
            if (resource == null) {
                throw new IllegalArgumentException("Audio file not found: " + audioFilePath);
            }
            // Create Media and MediaPlayer
            Media media = new Media(resource.toExternalForm());
            mediaPlayer = new MediaPlayer(media);

            // Configure MediaPlayer settings
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
            mediaPlayer.setVolume(0.5); // Set volume (0.0 to 1.0)

            // Play the music
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to stop background music
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
