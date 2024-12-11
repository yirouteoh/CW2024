package com.example.demo.managers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameStateManagerTest {

    @Test
    void testSingletonInstance() {
        GameStateManager instance1 = GameStateManager.getInstance();
        GameStateManager instance2 = GameStateManager.getInstance();
        assertSame(instance1, instance2, "GameStateManager should follow the singleton pattern");
    }

    @Test
    void testChangeState() {
        GameStateManager manager = GameStateManager.getInstance();
        manager.changeState(GameStateManager.GameState.PAUSED);
        assertTrue(manager.isState(GameStateManager.GameState.PAUSED), "State should change to PAUSED");
    }

    @Test
    void testIsGameOver() {
        GameStateManager manager = GameStateManager.getInstance();
        manager.changeState(GameStateManager.GameState.GAME_OVER);
        assertTrue(manager.isGameOver(), "State should be GAME_OVER");
        assertFalse(manager.isWin(), "State should not be WIN");
    }

    @Test
    void testIsWin() {
        GameStateManager manager = GameStateManager.getInstance();
        manager.changeState(GameStateManager.GameState.WIN);
        assertTrue(manager.isWin(), "State should be WIN");
        assertFalse(manager.isGameOver(), "State should not be GAME_OVER");
    }

    @Test
    void testNullStateChange() {
        GameStateManager manager = GameStateManager.getInstance();
        assertThrows(IllegalArgumentException.class, () -> manager.changeState(null), "Changing state to null should throw IllegalArgumentException");
    }
}
