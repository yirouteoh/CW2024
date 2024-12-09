package com.example.demo.actors.plane;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.projectile.EnemyProjectile;

public class BasicFighterPlane extends FighterPlane {

    public BasicFighterPlane(double initialXPos, double initialYPos) {
        super("enemyplane.png", 50, initialXPos, initialYPos, 3); // Replace with your actual image name and properties
    }

    @Override
    public ActiveActorDestructible fireProjectile() {
        return new EnemyProjectile(getProjectileXPosition(10), getProjectileYPosition(20)); // Customize offsets as needed
    }

    @Override
    public void updateActor() {
        // Additional actor-specific updates can go here
        updatePosition();
    }

    @Override
    public void updatePosition() {
        moveHorizontally(-3); // Example: Move leftward at a constant speed of 3 units
    }
}
