package com.example.demo.managers;

import com.example.demo.actors.plane.UserPlane;
import com.example.demo.levels.LevelParent;

import com.example.demo.sounds.SoundManager;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class InputManager {
    private final UserPlane user;
    private final LevelParent levelParent;
    private final SoundManager soundManager;

    private final Set<KeyCode> pressedKeys = new HashSet<>(); // Key State Tracker

    public InputManager(UserPlane user, LevelParent levelParent, SoundManager soundManager) {
        this.user = user;
        this.levelParent = levelParent;
        this.soundManager = soundManager;
    }

    public void initializeInputHandlers(Scene scene) {
        scene.setOnKeyPressed(event -> {
            pressedKeys.add(event.getCode()); // Add key to tracker
            if (levelParent.isCountdownInProgress() || !levelParent.getGameStateManager().isState(GameStateManager.GameState.PLAYING)) {
                return; // Ignore input if not playing
            }

            switch (event.getCode()) {
                case SPACE -> {
                    levelParent.fireProjectile(); // Fire projectile
                    soundManager.playShootSound(); // Play shooting sound effect
                }
                case ESCAPE -> {
                    if (!levelParent.getGameStateManager().isGameOver() && !levelParent.getGameStateManager().isWin() && !levelParent.getGameLoopManager().isPaused()) {
                        levelParent.getGameLoopManager().pause();
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
        if (levelParent.isCountdownInProgress() || !levelParent.getGameStateManager().isState(GameStateManager.GameState.PLAYING)) {
            return; // Ignore input if not playing
        }

        // Reset movement
        user.stopVerticalMovement();
        user.stopHorizontalMovement();

        // Process movement keys
        if (pressedKeys.contains(KeyCode.UP)) {
            user.setVerticalVelocity(-2); // Move up
        }
        if (pressedKeys.contains(KeyCode.DOWN)) {
            user.setVerticalVelocity(2); // Move down
        }
        if (pressedKeys.contains(KeyCode.LEFT)) {
            user.setHorizontalVelocity(-2); // Move left
        }
        if (pressedKeys.contains(KeyCode.RIGHT)) {
            user.setHorizontalVelocity(2); // Move right
        }
    }
}
