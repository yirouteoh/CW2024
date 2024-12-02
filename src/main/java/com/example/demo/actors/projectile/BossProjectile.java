package com.example.demo.actors.projectile;

/**
 * Represents a projectile fired by the boss enemy in the game.
 * <p>
 * The BossProjectile moves horizontally to the left at a constant speed.
 * It uses the `fireball.png` image and has a fixed size and velocity.
 * </p>
 */
public class BossProjectile extends Projectile {

	private static final String IMAGE_NAME = "fireball.png"; // Name of the projectile image
	private static final int IMAGE_HEIGHT = 75;             // Height of the projectile image
	private static final int HORIZONTAL_VELOCITY = -15;     // Speed of horizontal movement
	private static final int INITIAL_X_POSITION = 950;      // Starting X position of the projectile

	/**
	 * Constructs a BossProjectile at the specified Y position.
	 *
	 * @param initialYPos the initial Y position of the projectile
	 */
	public BossProjectile(double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, initialYPos);
	}

	/**
	 * Updates the position of the BossProjectile by moving it horizontally to the left.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	/**
	 * Updates the state of the BossProjectile, including its position.
	 * <p>
	 * This method is called on every frame to ensure the projectile moves
	 * according to its velocity.
	 * </p>
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}
}
