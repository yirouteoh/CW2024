package com.example.demo.managers;

import java.beans.PropertyChangeSupport;

/**
 * Manages the state of the game and notifies listeners of state changes.
 * <p>
 * This class uses the singleton pattern to ensure there is only one instance of the
 * {@link GameStateManager} in the application. It tracks the current game state
 * and provides methods to change or query the state.
 * </p>
 */
public class GameStateManager {

    private static GameStateManager instance;

    /**
     * Enum representing possible game states.
     */
    public enum GameState {
        PLAYING,
        PAUSED,
        GAME_OVER,
        WIN
    }

    /**
     * Constructs a {@link GameStateManager} with the initial state set to {@code PLAYING}.
     * This constructor is private to enforce the singleton pattern.
     */
    private GameState currentState;
    private final PropertyChangeSupport support;

    /**
     * Constructs a GameStateManager with the initial state set to PLAYING.
     */
    private GameStateManager() {
        this.currentState = GameState.PLAYING;
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Retrieves the singleton instance of the {@link GameStateManager}.
     *
     * @return The singleton instance of {@link GameStateManager}.
     */
    public static GameStateManager getInstance() {
        if (instance == null) {
            synchronized (GameStateManager.class) {
                if (instance == null) {
                    instance = new GameStateManager();
                }
            }
        }
        return instance;
    }

    /**
     * Changes the game state and notifies listeners if the state has changed.
     *
     * @param newState The new game state.
     * @throws IllegalArgumentException if the new state is null.
     */
    public void changeState(GameState newState) {
        if (newState == null) {
            throw new IllegalArgumentException("Game state cannot be null.");
        }
        GameState oldState = this.currentState;
        this.currentState = newState;

        // Notify listeners of the state change
        support.firePropertyChange("gameState", oldState, newState);
    }

    /**
     * Checks if the game is currently in a specific state.
     *
     * @param state The state to check.
     * @return True if the current state matches the given state, false otherwise.
     */
    public boolean isState(GameState state) {
        return this.currentState == state;
    }

    /**
     * Checks if the current game state is GAME_OVER.
     *
     * @return True if the current state is GAME_OVER, false otherwise.
     */
    public boolean isGameOver() {
        return this.currentState == GameState.GAME_OVER;
    }

    /**
     * Checks if the current game state is WIN.
     *
     * @return True if the current state is WIN, false otherwise.
     */
    public boolean isWin() {
        return this.currentState == GameState.WIN;
    }

    /**
     * Retrieves the current game state.
     *
     * @return The current game state.
     */
    public GameState getState() {
        return this.currentState;
    }

}
