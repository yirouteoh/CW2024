package com.example.demo.utils;

import javafx.scene.layout.HBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.JavaFXInitializer;

class HeartDisplayTest {

    private HeartDisplay heartDisplay;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        heartDisplay = new HeartDisplay(100, 200, 5); // Initialize with 5 hearts
    }

    @Test
    void removeHeart() {
        HBox container = heartDisplay.getContainer();
        assertEquals(5, container.getChildren().size(), "Initial number of hearts should be 5.");

        heartDisplay.removeHeart();
        assertEquals(4, container.getChildren().size(), "Number of hearts should decrease by 1 after removal.");

        for (int i = 0; i < 4; i++) {
            heartDisplay.removeHeart();
        }

        assertEquals(0, container.getChildren().size(), "All hearts should be removed.");
    }

    @Test
    void getContainer() {
        HBox container = heartDisplay.getContainer();
        assertNotNull(container, "Heart container should not be null.");
        assertEquals(5, container.getChildren().size(), "Container should initially contain 5 hearts.");
    }
}
