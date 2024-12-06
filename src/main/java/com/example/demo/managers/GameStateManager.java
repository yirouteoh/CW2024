package com.example.demo.managers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GameStateManager {

    public enum GameState {
        PLAYING,
        PAUSED,
        GAME_OVER,
        WIN
    }

    private GameState currentState;
    private final PropertyChangeSupport support;

    /**
     * Constructs a GameStateManager with the initial state set to PLAYING.
     */
    public GameStateManager() {
        this.currentState = GameState.PLAYING;
        this.support = new PropertyChangeSupport(this);
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
}
