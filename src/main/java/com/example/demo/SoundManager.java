package com.example.demo;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundManager {
    private static SoundManager instance; // Singleton instance
    private MediaPlayer mediaPlayer; // For background music
    private AudioClip shootingSound; // For shooting sound effect
    private AudioClip crashSound; // For crash sound effect
    private final double volume = 0.5; // Default volume (50%)

    // Audio file paths
    public static final String MENU_MUSIC = "/com/example/demo/audios/menumusic.mp3";
    public static final String LEVEL_ONE_MUSIC = "/com/example/demo/audios/levelonemusic.mp3";
    public static final String LEVEL_TWO_MUSIC = "/com/example/demo/audios/leveltwomusic.mp3";
    public static final String LEVEL_THREE_MUSIC = "/com/example/demo/audios/levelthreemusic.mp3";
    public static final String GAME_OVER_MUSIC = "/com/example/demo/audios/gameovermusic.mp3";
    public static final String WIN_GAME_MUSIC = "/com/example/demo/audios/wingamemusic.mp3";
    public static final String SHOOT_SOUND = "/com/example/demo/audios/shootmusic.mp3";
    public static final String CRASH_SOUND = "/com/example/demo/audios/crashmusic.mp3";

    // Private constructor for Singleton
    private SoundManager() {
        initializeSounds(); // Load and initialize sounds
    }

    // Singleton instance accessor
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    // Preload sounds for quick playback
    private void initializeSounds() {
        try {
            // Load shooting sound
            URL shootResource = getClass().getResource(SHOOT_SOUND);
            if (shootResource != null) {
                shootingSound = new AudioClip(shootResource.toExternalForm());
                shootingSound.setVolume(volume);
            } else {
                throw new IllegalArgumentException("Audio file not found: " + SHOOT_SOUND);
            }

            // Load crash sound
            URL crashResource = getClass().getResource(CRASH_SOUND);
            if (crashResource != null) {
                crashSound = new AudioClip(crashResource.toExternalForm());
                crashSound.setVolume(volume);
            } else {
                throw new IllegalArgumentException("Audio file not found: " + CRASH_SOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Play background music
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
            mediaPlayer.setVolume(volume);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Stop background music
    public void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    // Pause background music
    public void pauseBackgroundMusic() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        }
    }

    // Resume background music
    public void resumeBackgroundMusic() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            mediaPlayer.play();
        }
    }

    // Play shooting sound
    public void playShootSound() {
        if (shootingSound != null) {
            shootingSound.play();
        } else {
            System.err.println("Shooting sound not initialized!");
        }
    }

    // Play crash sound
    public void playCrashSound() {
        if (crashSound != null) {
            crashSound.play();
        } else {
            System.err.println("Crash sound not initialized!");
        }
    }
}
