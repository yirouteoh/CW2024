package com.example.demo.actors.factory;

import com.example.demo.actors.plane.FighterPlane;

/**
 * Interface for factories that create instances of {@link FighterPlane}.
 * Defines the contract for creating enemy fighter planes with specified screen and position constraints.
 */
public interface FighterPlaneFactory {
    /**
     * Creates a new instance of {@link FighterPlane}.
     *
     * @param screenWidth The width of the screen, used to determine the horizontal position of the enemy plane.
     * @param enemyMaximumYPosition The maximum allowable Y position for the enemy plane.
     * @return A new {@link FighterPlane} instance configured based on the specified constraints.
     */
    FighterPlane createEnemy(double screenWidth, double enemyMaximumYPosition);
}
