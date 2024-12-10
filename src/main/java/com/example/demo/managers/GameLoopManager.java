package com.example.demo.managers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Manages the game loop using a {@link Timeline}.
 * <p>
 * This class provides methods to start, stop, pause, and resume the game loop,
 * allowing for precise control over game state transitions.
 * </p>
 */
public class GameLoopManager {

    private static GameLoopManager instance; // Singleton instance
    private Timeline timeline;
    private boolean paused = false; // Track whether the game loop is paused

    /**
     * Private constructor to enforce Singleton pattern.
     */
    private GameLoopManager() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE); // Loop indefinitely
    }

    /**
     * Returns the singleton instance of GameLoopManager.
     *
     * @return The singleton instance of GameLoopManager.
     */
    public static synchronized GameLoopManager getInstance() {
        if (instance == null) {
            instance = new GameLoopManager();
        }
        return instance;
    }

    /**
     * Initializes the game loop with the specified frame duration and update task.
     *
     * @param frameDuration The duration of each frame in milliseconds.
     * @param updateTask    The task to run on each frame.
     */
    public void initialize(Duration frameDuration, Runnable updateTask) {
        timeline.getKeyFrames().clear(); // Clear any existing frames
        KeyFrame keyFrame = new KeyFrame(frameDuration, e -> updateTask.run());
        timeline.getKeyFrames().add(keyFrame);
    }

    /**
     * Starts the game loop.
     * Resets the paused state and begins playing the timeline.
     */
    public void start() {
        paused = false;
        timeline.play();
    }

    /**
     * Stops the game loop.
     * Resets the paused state and stops the timeline.
     */
    public void stop() {
        paused = false;
        timeline.stop();
    }

    /**
     * Pauses the game loop.
     * Sets the paused state and pauses the timeline.
     */
    public void pause() {
        paused = true;
        timeline.pause();
    }

    /**
     * Resumes the game loop if it was paused.
     * Resets the paused state and resumes the timeline.
     */
    public void resume() {
        if (paused) {
            paused = false;
            timeline.play();
        }
    }

    /**
     * Checks if the game loop is currently paused.
     *
     * @return true if the game loop is paused, false otherwise.
     */
    public boolean isPaused() {
        return paused;
    }
}
