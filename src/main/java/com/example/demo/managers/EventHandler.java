package com.example.demo.managers;

import com.example.demo.sounds.SoundManager;
import com.example.demo.levels.LevelParent;
import com.example.demo.screens.GameOverImage;
import com.example.demo.screens.WinImage;

/**
 * Handles events related to winning and losing the game.
 */
public class EventHandler {

    private final GameStateManager gameStateManager;
    private final SoundManager soundManager;
    private final PauseManager pauseManager;
    private final LevelParent levelParent;

    public EventHandler(GameStateManager gameStateManager, SoundManager soundManager, PauseManager pauseManager, LevelParent levelParent) {
        this.gameStateManager = gameStateManager;
        this.soundManager = soundManager;
        this.pauseManager = pauseManager;
        this.levelParent = levelParent;
    }

    /**
     * Handles the logic for when the player wins the game.
     */
    public void handleWin() {
        if (!gameStateManager.isWin()) { // Prevent duplicate calls
            levelParent.getGameLoopManager().stop();
            soundManager.stopBackgroundMusic();
            soundManager.playBackgroundMusic(SoundManager.WIN_GAME_MUSIC);
            gameStateManager.changeState(GameStateManager.GameState.WIN);

            WinImage winScreen = new WinImage(
                    levelParent.getScreenWidth(),
                    levelParent.getScreenHeight(),
                    () -> pauseManager.restartGame(levelParent), // Use PauseManager for restarting
                    () -> pauseManager.returnToMainMenu(levelParent) // Use PauseManager for returning to menu
            );
            levelParent.getRoot().getChildren().add(winScreen);
        }
    }

    /**
     * Handles the logic for when the player loses the game.
     */
    public void handleLose() {
        if (!gameStateManager.isGameOver()) { // Prevent duplicate calls
            levelParent.getGameLoopManager().stop();
            soundManager.stopBackgroundMusic();
            soundManager.playBackgroundMusic(SoundManager.GAME_OVER_MUSIC);
            gameStateManager.changeState(GameStateManager.GameState.GAME_OVER);

            GameOverImage gameOverScreen = new GameOverImage(
                    levelParent.getScreenWidth(),
                    levelParent.getScreenHeight(),
                    () -> pauseManager.restartGame(levelParent), // Use PauseManager for restarting
                    () -> pauseManager.returnToMainMenu(levelParent) // Use PauseManager for returning to menu
            );
            levelParent.getRoot().getChildren().add(gameOverScreen);
        }
    }
}
