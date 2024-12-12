package com.example.demo.utils;

import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.JavaFXInitializer;

class KillCountDisplayTest {

    private KillCountDisplay killCountDisplay;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        killCountDisplay = new KillCountDisplay(100, 200, 10); // Initialize with sample coordinates and target count
    }

    @Test
    void incrementKillCount() {
        killCountDisplay.incrementKillCount();
        assertEquals(1, killCountDisplay.getKillCount(), "Kill count should increment by 1.");

        for (int i = 0; i < 9; i++) {
            killCountDisplay.incrementKillCount();
        }
        assertEquals(10, killCountDisplay.getKillCount(), "Kill count should not exceed the target.");
    }

    @Test
    void getDisplay() {
        Text display = killCountDisplay.getDisplay();
        assertNotNull(display, "Display text object should not be null.");
        assertEquals("Kills: 0/10", display.getText(), "Initial display text should be 'Kills: 0/10'.");

        killCountDisplay.incrementKillCount();
        assertEquals("Kills: 1/10", display.getText(), "Display text should update after increment.");
    }

    @Test
    void getKillCount() {
        assertEquals(0, killCountDisplay.getKillCount(), "Initial kill count should be 0.");
        killCountDisplay.incrementKillCount();
        assertEquals(1, killCountDisplay.getKillCount(), "Kill count should increment correctly.");
    }
}
