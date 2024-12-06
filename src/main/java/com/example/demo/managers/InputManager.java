package com.example.demo.managers;

import com.example.demo.actors.plane.UserPlane;
import com.example.demo.levels.LevelParent;
import com.example.demo.sounds.SoundManager;

import javafx.scene.Scene;

public class InputManager {
    private final UserPlane user;
    private final LevelParent levelParent;
    private final SoundManager soundManager;

    public InputManager(UserPlane user, LevelParent levelParent, SoundManager soundManager) {
        this.user = user;
        this.levelParent = levelParent;
        this.soundManager = soundManager;
    }

    public void initializeInputHandlers(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (levelParent.isCountdownInProgress() || !levelParent.getGameStateManager().isState(GameStateManager.GameState.PLAYING)) {
                return; // Ignore input if not playing
            }

            switch (event.getCode()) {
                case UP -> user.moveUp();
                case DOWN -> user.moveDown();
                case LEFT -> user.moveLeft();
                case RIGHT -> user.moveRight();
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
            if (levelParent.isCountdownInProgress()) {
                return; // Ignore input during countdown
            }

            switch (event.getCode()) {
                case UP, DOWN -> user.stop(); // Stop vertical movement
                case LEFT, RIGHT -> user.stopHorizontal(); // Stop horizontal movement
            }
        });
    }
}
