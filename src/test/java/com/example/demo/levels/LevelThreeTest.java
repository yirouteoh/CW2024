package com.example.demo.levels;

import com.example.demo.JavaFXInitializer;
import javafx.application.Platform;
import javafx.animation.SequentialTransition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class LevelThreeTest {

    private LevelThree levelThree;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized once
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            double screenHeight = 800; // Example screen height
            double screenWidth = 600;  // Example screen width
            levelThree = new LevelThree(screenHeight, screenWidth);
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX thread to complete initialization
    }

    @Test
    void spawnEnemyUnits() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            int initialEnemyCount = levelThree.getCurrentNumberOfEnemies();
            levelThree.spawnEnemyUnits();
            int newEnemyCount = levelThree.getCurrentNumberOfEnemies();

            assertTrue(newEnemyCount >= initialEnemyCount, "New enemies should be spawned.");
            assertTrue(newEnemyCount <= 5, "Enemy count should not exceed the maximum allowed.");
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX thread to complete
    }

    @Test
    void pauseFinalBossMessage() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            levelThree.displayFinalBossMessage(); // Ensure the message is displayed
            levelThree.pauseFinalBossMessage();   // Pause the animation

            SequentialTransition timeline = levelThree.finalBossMessageTimeline;
            assertNotNull(timeline, "The final boss message timeline should be initialized.");
            assertEquals(javafx.animation.Animation.Status.PAUSED, timeline.getStatus(),
                    "The boss message animation should be paused.");
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX thread to complete
    }

    @Test
    void resumeFinalBossMessage() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            levelThree.displayFinalBossMessage(); // Ensure the message is displayed
            levelThree.pauseFinalBossMessage();   // Pause the animation first
            levelThree.resumeFinalBossMessage();  // Resume the animation

            SequentialTransition timeline = levelThree.finalBossMessageTimeline;
            assertNotNull(timeline, "The final boss message timeline should be initialized.");
            assertEquals(javafx.animation.Animation.Status.RUNNING, timeline.getStatus(),
                    "The boss message animation should be running after resuming.");
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX thread to complete
    }
}
