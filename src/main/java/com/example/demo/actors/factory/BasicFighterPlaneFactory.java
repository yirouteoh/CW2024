package com.example.demo.actors.factory;

import com.example.demo.actors.plane.BasicFighterPlane;
import com.example.demo.actors.plane.FighterPlane;

import java.util.Random;

/**
 * Factory class for creating instances of {@link BasicFighterPlane}.
 * This factory generates enemy planes with randomized positions within specified screen boundaries.
 */
public class BasicFighterPlaneFactory implements FighterPlaneFactory {
    private final Random random = new Random();

    /**
     * Creates a new {@link FighterPlane} enemy at a random vertical position within the allowable range.
     *
     * @param screenWidth The width of the screen, used to determine the horizontal position of the enemy plane.
     * @param enemyMaximumYPosition The maximum allowable Y position for the enemy plane.
     * @return A new instance of {@link BasicFighterPlane} with randomized vertical positioning.
     */
    @Override
    public FighterPlane createEnemy(double screenWidth, double enemyMaximumYPosition) {
        double yPosition = random.nextDouble() * enemyMaximumYPosition;
        return new BasicFighterPlane(screenWidth, yPosition); // Replace with your actual concrete FighterPlane subclass
    }
}
