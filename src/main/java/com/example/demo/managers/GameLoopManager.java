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

    private final Timeline timeline;
    private boolean paused = false; // Track whether the game loop is paused

    /**
     * Constructs a GameLoopManager with a specified frame duration and update task.
     *
     * @param frameDuration The duration of each frame in milliseconds.
     * @param updateTask    The task to run on each frame.
     */
    public GameLoopManager(Duration frameDuration, Runnable updateTask) {
        timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(frameDuration, e -> updateTask.run());
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE); // Loop indefinitely
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
