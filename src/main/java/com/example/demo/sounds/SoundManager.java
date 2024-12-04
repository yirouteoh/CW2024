package com.example.demo.sounds;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundManager {
    private static SoundManager instance; // Singleton instance
    private MediaPlayer mediaPlayer; // For background music
    private AudioClip shootingSound; // For shooting sound effect
    private AudioClip crashSound; // For crash sound effect
    private static final double DEFAULT_VOLUME = 0.5; // Default volume (50%)

    // Audio file paths
    public static final String MENU_MUSIC = "/com/example/demo/audios/menumusic.mp3";
    public static final String LEVEL_ONE_MUSIC = "/com/example/demo/audios/levelonemusic.mp3";
    public static final String LEVEL_TWO_MUSIC = "/com/example/demo/audios/leveltwomusic.mp3";
    public static final String LEVEL_THREE_MUSIC = "/com/example/demo/audios/levelthreemusic.mp3";
    public static final String GAME_OVER_MUSIC = "/com/example/demo/audios/gameovermusic.mp3";
    public static final String WIN_GAME_MUSIC = "/com/example/demo/audios/wingamemusic.mp3";
    public static final String SHOOT_SOUND = "/com/example/demo/audios/shootmusic.mp3";
    public static final String CRASH_SOUND = "/com/example/demo/audios/crashmusic.mp3";

    /**
     * Private constructor for Singleton.
     */
    private SoundManager() {
        initializeSoundEffects(); // Load and initialize sound effects
    }

    /**
     * Singleton instance accessor.
     *
     * @return Singleton instance of SoundManager.
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Initializes sound effects for quick playback.
     */
    private void initializeSoundEffects() {
        shootingSound = loadAudioClip(SHOOT_SOUND);
        crashSound = loadAudioClip(CRASH_SOUND);
    }

    /**
     * Plays background music from the specified file path.
     *
     * @param audioFilePath Path to the audio file.
     */
    public void playBackgroundMusic(String audioFilePath) {
        try {
            stopBackgroundMusic(); // Stop any currently playing music
            mediaPlayer = createMediaPlayer(audioFilePath);
            if (mediaPlayer != null) {
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop background music
                mediaPlayer.setVolume(DEFAULT_VOLUME);
                mediaPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("Error playing background music: " + e.getMessage());
        }
    }

    /**
     * Stops the currently playing background music.
     */
    public void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null; // Clear the reference
        }
    }

    /**
     * Pauses the currently playing background music.
     */
    public void pauseBackgroundMusic() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        }
    }

    /**
     * Resumes the paused background music.
     */
    public void resumeBackgroundMusic() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            mediaPlayer.play();
        }
    }

    /**
     * Plays the shooting sound effect.
     */
    public void playShootSound() {
        playAudioClip(shootingSound, "Shooting sound");
    }

    /**
     * Plays the crash sound effect.
     */
    public void playCrashSound() {
        playAudioClip(crashSound, "Crash sound");
    }

    /**
     * Loads an audio clip for sound effects.
     *
     * @param filePath Path to the audio file.
     * @return Loaded AudioClip, or null if loading fails.
     */
    private AudioClip loadAudioClip(String filePath) {
        try {
            URL resource = getClass().getResource(filePath);
            if (resource == null) {
                throw new IllegalArgumentException("Audio file not found: " + filePath);
            }
            AudioClip audioClip = new AudioClip(resource.toExternalForm());
            audioClip.setVolume(DEFAULT_VOLUME);
            return audioClip;
        } catch (Exception e) {
            System.err.println("Error loading audio clip: " + filePath + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates a MediaPlayer for playing background music.
     *
     * @param filePath Path to the audio file.
     * @return MediaPlayer instance, or null if loading fails.
     */
    private MediaPlayer createMediaPlayer(String filePath) {
        try {
            URL resource = getClass().getResource(filePath);
            if (resource == null) {
                throw new IllegalArgumentException("Audio file not found: " + filePath);
            }
            Media media = new Media(resource.toExternalForm());
            return new MediaPlayer(media);
        } catch (Exception e) {
            System.err.println("Error creating MediaPlayer: " + filePath + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Plays an AudioClip and handles potential null references.
     *
     * @param audioClip  The AudioClip to play.
     * @param soundName  Name of the sound (for logging).
     */
    private void playAudioClip(AudioClip audioClip, String soundName) {
        if (audioClip != null) {
            audioClip.play();
        } else {
            System.err.println(soundName + " not initialized!");
        }
    }
}
