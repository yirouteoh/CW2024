package com.example.demo.actors.plane;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.projectile.EnemyProjectile;

/**
 * Represents a basic enemy fighter plane in the game.
 * This plane has predefined behavior for movement and firing projectiles.
 */
public class BasicFighterPlane extends FighterPlane {
    /**
     * Constructs a new {@link BasicFighterPlane} instance with the specified initial position.
     *
     * @param initialXPos The initial X-coordinate of the plane.
     * @param initialYPos The initial Y-coordinate of the plane.
     */
    public BasicFighterPlane(double initialXPos, double initialYPos) {
        super("enemyplane.png", 50, initialXPos, initialYPos, 3); // Replace with your actual image name and properties
    }

    /**
     * Fires a projectile from the plane.
     *
     * @return A new {@link ActiveActorDestructible} instance representing the projectile fired.
     */
    @Override
    public ActiveActorDestructible fireProjectile() {
        return new EnemyProjectile(getProjectileXPosition(10), getProjectileYPosition(20)); // Customize offsets as needed
    }

    /**
     * Updates the state of the plane, including its position and any other necessary behaviors.
     * This method is called on each game update cycle.
     */
    @Override
    public void updateActor() {
        // Additional actor-specific updates can go here
        updatePosition();
    }

    /**
     * Updates the position of the plane by moving it horizontally.
     * This implementation moves the plane leftward at a constant speed.
     */

    @Override
    public void updatePosition() {
        moveHorizontally(-3); // Example: Move leftward at a constant speed of 3 units
    }
}
