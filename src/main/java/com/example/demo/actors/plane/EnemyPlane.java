package com.example.demo.actors.plane;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.factory.ProjectileFactory;


/**
 * Represents an enemy plane in the game.
 * The enemy plane moves horizontally and can fire projectiles at a randomized rate.
 */
public class EnemyPlane extends FighterPlane {

	private static final String IMAGE_NAME = "enemyplane.png";
	private static final int IMAGE_HEIGHT = 150;
	private static final int HORIZONTAL_VELOCITY = -6; // Movement speed to the left
	private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
	private static final int INITIAL_HEALTH = 1; // Default health for enemy planes
	private static final double FIRE_RATE = 0.01; // Probability of firing a projectile per update

	/**
	 * Constructs an EnemyPlane with the specified initial position.
	 *
	 * @param initialXPos the initial X position of the plane
	 * @param initialYPos the initial Y position of the plane
	 */
	public EnemyPlane(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
	}

	/**
	 * Updates the position of the enemy plane by moving it horizontally.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	/**
	 * Fires a projectile from the enemy plane at a randomized rate.
	 * The projectile is created with an offset based on the plane's position.
	 *
	 * @return a new EnemyProjectile if fired, or null if not fired
	 */
	@Override
	public ActiveActorDestructible fireProjectile() {
		if (Math.random() < FIRE_RATE) { // Keep the fire rate logic
			double randomOffsetX = -100 + Math.random() * 20; // Randomize X offset
			double randomOffsetY = -30 + Math.random() * 60;  // Randomize Y offset
			double projectileX = getProjectileXPosition(randomOffsetX);
			double projectileY = getProjectileYPosition(randomOffsetY);
			return ProjectileFactory.createProjectile(ProjectileFactory.ProjectileType.ENEMY, projectileX, projectileY);
		}
		return null; // No projectile fired
	}


	/**
	 * Updates the enemy plane's state, including position and potential firing of projectiles.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}
}
