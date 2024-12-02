package com.example.demo.actors.projectile;

import com.example.demo.actors.ActiveActorDestructible;

/**
 * Abstract base class for all types of projectiles in the game.
 * <p>
 * Projectiles are destructible actors that have specific behaviors
 * such as movement (`updatePosition()`) and interactions (e.g., destruction on collision).
 * This class provides a common structure for projectiles fired by
 * the player or enemies.
 * </p>
 */
public abstract class Projectile extends ActiveActorDestructible {

	/**
	 * Constructs a projectile with the specified image, size, and initial position.
	 *
	 * @param imageName   the name of the image resource for the projectile
	 * @param imageHeight the height of the projectile image
	 * @param initialXPos the initial X position of the projectile
	 * @param initialYPos the initial Y position of the projectile
	 */
	public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
	}

	/**
	 * Destroys the projectile immediately when it takes damage.
	 * <p>
	 * This implementation ensures that projectiles are removed
	 * from the game as soon as they interact with a target or an obstacle.
	 * </p>
	 */
	@Override
	public void takeDamage() {
		this.destroy(); // Mark the projectile as destroyed
	}

	/**
	 * Abstract method for updating the projectile's position.
	 * <p>
	 * Subclasses must implement this method to define specific
	 * movement behavior (e.g., linear movement, acceleration).
	 * </p>
	 */
	@Override
	public abstract void updatePosition();
}
