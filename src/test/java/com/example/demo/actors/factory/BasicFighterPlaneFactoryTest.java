package com.example.demo.actors.factory;

import com.example.demo.actors.plane.BasicFighterPlane;
import com.example.demo.actors.plane.FighterPlane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.JavaFXInitializer;

class BasicFighterPlaneFactoryTest {

    private BasicFighterPlaneFactory factory;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        factory = new BasicFighterPlaneFactory();
    }

    @Test
    void createEnemy() {
        double screenWidth = 0;
        double enemyMaximumYPosition = 600.0;

        FighterPlane enemyPlane = factory.createEnemy(screenWidth, enemyMaximumYPosition);

        assertNotNull(enemyPlane, "The created enemy plane should not be null.");
        assertTrue(enemyPlane instanceof BasicFighterPlane, "The created plane should be an instance of BasicFighterPlane.");
        assertEquals(screenWidth, enemyPlane.getTranslateX(), "Enemy plane's X position should match the screen width.");
        assertTrue(enemyPlane.getTranslateY() >= 0 && enemyPlane.getTranslateY() <= enemyMaximumYPosition,
                "Enemy plane's Y position should be within the allowable range.");
    }
}
