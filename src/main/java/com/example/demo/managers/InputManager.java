package com.example.demo.managers;

import com.example.demo.actors.plane.UserPlane;
import com.example.demo.levels.LevelParent;

import com.example.demo.sounds.SoundManager;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages user input for controlling the game.
 * <p>
 * This class tracks key presses, processes input in the game loop,
 * and executes corresponding actions, such as movement, firing projectiles, and pausing the game.
 * </p>
 */
public class InputManager {

    private final UserPlane user;
    private final LevelParent levelParent;
    private final SoundManager soundManager;
    private static final int MOVEMENT_SPEED = 2;

    private final Set<KeyCode> pressedKeys = new HashSet<>(); // Key State Tracker

    /**
     * Constructs an {@link InputManager} instance.
     *
     * @param user        The user's plane that responds to movement and actions.
     * @param levelParent The current game level being played.
     */
    public InputManager(UserPlane user, LevelParent levelParent) {
        this.user = user;
        this.levelParent = levelParent;
        this.soundManager = SoundManager.getInstance();
    }

    /**
     * Initializes input handlers for the specified scene.
     * <p>
     * This method sets up listeners for key presses and releases, handling specific actions like
     * firing projectiles and pausing the game.
     * </p>
     *
     * @param scene The {@link Scene} to attach input handlers to.
     */
    public void initializeInputHandlers(Scene scene) {
        scene.setOnKeyPressed(event -> {
            pressedKeys.add(event.getCode()); // Add key to tracker
            if (levelParent.isCountdownInProgress() || !GameStateManager.getInstance().isState(GameStateManager.GameState.PLAYING)) {
                return;
            }

            switch (event.getCode()) {
                case SPACE -> {
                    levelParent.fireProjectile(); // Fire projectile
                    soundManager.playShootSound(); // Play shooting sound effect
                }
                case ESCAPE -> {
                    GameStateManager gameStateManager = GameStateManager.getInstance();
                    if (!gameStateManager.isGameOver() && !gameStateManager.isWin() && !GameLoopManager.getInstance().isPaused()) {
                        GameLoopManager.getInstance().pause();
                        soundManager.pauseBackgroundMusic();
                        levelParent.showPauseScreen();
                    }
                }
            }
        });

        scene.setOnKeyReleased(event -> {
            pressedKeys.remove(event.getCode()); // Remove key from tracker
        });
    }

    /**
     * Continuously processes input based on the current key states.
     * This method should be called in the game loop.
     */
    public void processInput() {
        if (levelParent.isCountdownInProgress() || !GameStateManager.getInstance().isState(GameStateManager.GameState.PLAYING)) {
            return; // Ignore input if not playing
        }

        // Reset movement
        user.stopVerticalMovement();
        user.stopHorizontalMovement();

        // Process movement keys
        if (pressedKeys.contains(KeyCode.UP)) {
            user.setVerticalVelocity(-MOVEMENT_SPEED); // Move up
        }
        if (pressedKeys.contains(KeyCode.DOWN)) {
            user.setVerticalVelocity(MOVEMENT_SPEED); // Move down
        }
        if (pressedKeys.contains(KeyCode.LEFT)) {
            user.setHorizontalVelocity(-MOVEMENT_SPEED); // Move left
        }
        if (pressedKeys.contains(KeyCode.RIGHT)) {
            user.setHorizontalVelocity(MOVEMENT_SPEED); // Move right
        }
    }

}
