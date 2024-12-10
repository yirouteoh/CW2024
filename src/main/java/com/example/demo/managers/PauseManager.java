package com.example.demo.managers;

import com.example.demo.levels.LevelParent;
import com.example.demo.levels.LevelThree;
import com.example.demo.sounds.SoundManager;
import com.example.demo.screens.PauseScreen;
import com.example.demo.screens.MenuView;

import javafx.scene.Group;
import javafx.stage.Stage;

/**
 * Manages the game's pause functionality, including pausing, resuming, restarting, and returning to the main menu.
 * <p>
 * The {@link PauseManager} handles the interactions between the game's state, sound, and visual elements during a paused state.
 * </p>
 */
public class PauseManager {

    private final GameLoopManager gameLoopManager;
    private final SoundManager soundManager;
    private final Group root;

    /**
     * Constructs a {@link PauseManager} instance.
     *
     * @param gameLoopManager The {@link GameLoopManager} controlling the game's loop.
     * @param soundManager    The {@link SoundManager} for managing game sounds.
     * @param root            The root {@link Group} for the game's visual elements.
     */
    public PauseManager(GameLoopManager gameLoopManager, SoundManager soundManager, Group root)
    {
        this.gameLoopManager = gameLoopManager;
        this.soundManager = soundManager;
        this.root = root;
    }

    /**
     * Pauses the game and shows the pause screen.
     *
     * @param levelParent The current LevelParent for callbacks like restart or return to menu.
     */
    public void showPauseScreen(LevelParent levelParent) {
        if (levelParent == null) {
            throw new IllegalArgumentException("LevelParent cannot be null when showing the pause screen.");
        }

        if (root.getScene() == null) {
            throw new IllegalStateException("The root group must be added to a Scene before showing the pause screen.");
        }

        gameLoopManager.pause();
        soundManager.pauseBackgroundMusic();
        GameStateManager.getInstance().changeState(GameStateManager.GameState.PAUSED);

        // Check if levelParent is LevelThree and pause "Final Boss Message"
        if (levelParent instanceof LevelThree levelThree) {
            levelThree.pauseFinalBossMessage();
        }

        PauseScreen pauseScreen = new PauseScreen(
                root,
                () -> {
                    resumeGame(levelParent);
                    if (levelParent instanceof LevelThree levelThree) {
                        levelThree.resumeFinalBossMessage();
                    }
                },
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
        GameStateManager.getInstance().changeState(GameStateManager.GameState.PLAYING);
        soundManager.resumeBackgroundMusic();
    }

    /**
     * Restarts the game by resetting state and loading the initial level.
     *
     * @param levelParent The current LevelParent for restart logic.
     */
    public void restartGame(LevelParent levelParent) {
        try {
            if (levelParent == null) {
                throw new IllegalArgumentException("LevelParent is null. Cannot restart the game.");
            }

            GameStateManager.getInstance().changeState(GameStateManager.GameState.PLAYING);
            gameLoopManager.stop();
            soundManager.stopBackgroundMusic();

            com.example.demo.controller.Controller controller = new com.example.demo.controller.Controller(
                    (Stage) levelParent.getScene().getWindow()
            );
            controller.launchGame(); // Restart the game from Level One
        } catch (Exception e) {
            if (levelParent != null) {
                levelParent.showErrorDialog("Error restarting the game: " + e.getMessage());
            } else {
                System.err.println("Error restarting the game: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /**
     * Returns to the main menu by stopping the game and loading the menu view.
     *
     * @param levelParent The current LevelParent for context.
     */
    public void returnToMainMenu(LevelParent levelParent) {
        if (levelParent == null) {
            throw new IllegalArgumentException("LevelParent cannot be null when returning to the main menu.");
        }

        GameStateManager.getInstance().changeState(GameStateManager.GameState.PAUSED);
        gameLoopManager.stop();

        Stage stage = (Stage) levelParent.getScene().getWindow();
        MenuView menuView = new MenuView(stage, new com.example.demo.controller.Controller(stage));
        menuView.showMenu();
    }

}
