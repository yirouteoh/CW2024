package com.example.demo.managers;

import com.example.demo.levels.LevelParent;
import com.example.demo.sounds.SoundManager;
import com.example.demo.screens.PauseScreen;
import com.example.demo.screens.MenuView;

import javafx.scene.Group;
import javafx.stage.Stage;

public class PauseManager {

    private final GameLoopManager gameLoopManager;
    private final GameStateManager gameStateManager;
    private final SoundManager soundManager;
    private final Group root;

    public PauseManager(GameLoopManager gameLoopManager, GameStateManager gameStateManager, SoundManager soundManager, Group root) {
        this.gameLoopManager = gameLoopManager;
        this.gameStateManager = gameStateManager;
        this.soundManager = soundManager;
        this.root = root;
    }

    /**
     * Pauses the game and shows the pause screen.
     *
     * @param levelParent The current LevelParent for callbacks like restart or return to menu.
     */
    public void showPauseScreen(LevelParent levelParent) {
        gameLoopManager.pause();
        soundManager.pauseBackgroundMusic();
        gameStateManager.changeState(GameStateManager.GameState.PAUSED);

        PauseScreen pauseScreen = new PauseScreen(
                root,
                () -> resumeGame(levelParent),
                () -> restartGame(levelParent),
                () -> returnToMainMenu(levelParent),
                soundManager
        );
        pauseScreen.show();
    }

    /**
     * Resumes the game from a paused state.
     *
     * @param levelParent The current LevelParent for state management.
     */
    private void resumeGame(LevelParent levelParent) {
        gameLoopManager.resume();
        gameStateManager.changeState(GameStateManager.GameState.PLAYING);
        soundManager.resumeBackgroundMusic();
    }

    /**
     * Restarts the game by resetting state and loading the initial level.
     *
     * @param levelParent The current LevelParent for restart logic.
     */
    public void restartGame(LevelParent levelParent) {
        try {
            gameStateManager.changeState(GameStateManager.GameState.PLAYING);
            gameLoopManager.stop();
            soundManager.stopBackgroundMusic();

            // Assuming Controller handles restarting
            com.example.demo.controller.Controller controller = new com.example.demo.controller.Controller((Stage) levelParent.getScene().getWindow());
            controller.launchGame(); // Restart the game from Level One
        } catch (Exception e) {
            levelParent.showErrorDialog("Error restarting the game: " + e.getMessage());
        }
    }

    /**
     * Returns to the main menu by stopping the game and loading the menu view.
     *
     * @param levelParent The current LevelParent for context.
     */
    public void returnToMainMenu(LevelParent levelParent) {
        gameStateManager.changeState(GameStateManager.GameState.PAUSED);
        gameLoopManager.stop();

        Stage stage = (Stage) levelParent.getScene().getWindow();
        MenuView menuView = new MenuView(stage, new com.example.demo.controller.Controller(stage));
        menuView.showMenu();
    }
}
