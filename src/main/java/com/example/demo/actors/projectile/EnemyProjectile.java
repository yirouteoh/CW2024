package com.example.demo.actors.projectile;

/**
 * Represents a projectile fired by enemy planes in the game.
 * <p>
 * The EnemyProjectile moves horizontally to the left at a constant speed.
 * It uses the `enemyFire.png` image and has a fixed size and velocity.
 * </p>
 */
public class EnemyProjectile extends Projectile {

	private static final String IMAGE_NAME = "enemyFire.png"; // Name of the projectile image
	private static final int IMAGE_HEIGHT = 50;              // Height of the projectile image
	private static final int HORIZONTAL_VELOCITY = -10;      // Speed of horizontal movement

	/**
	 * Constructs an EnemyProjectile with the specified initial position.
	 *
	 * @param initialXPos the initial X position of the projectile
	 * @param initialYPos the initial Y position of the projectile
	 */
	public EnemyProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
	}

	/**
	 * Updates the position of the EnemyProjectile by moving it horizontally to the left.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	/**
	 * Updates the state of the EnemyProjectile, including its position.
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
