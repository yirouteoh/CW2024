package com.example.demo.actors.projectile;

/**
 * Represents a projectile fired by the player's plane in the game.
 * <p>
 * The UserProjectile moves horizontally to the right at a constant speed.
 * It uses the `userfire.png` image and has a fixed size and velocity.
 * </p>
 */
public class UserProjectile extends Projectile {

	private static final String IMAGE_NAME = "userfire.png"; // Name of the projectile image
	private static final int IMAGE_HEIGHT = 125;            // Height of the projectile image
	private static final int HORIZONTAL_VELOCITY = 15;      // Speed of horizontal movement

	/**
	 * Constructs a UserProjectile with the specified initial position.
	 *
	 * @param initialXPos the initial X position of the projectile
	 * @param initialYPos the initial Y position of the projectile
	 */
	public UserProjectile(double initialXPos, double initialYPos) {
		super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
	}

	/**
	 * Updates the position of the UserProjectile by moving it horizontally to the right.
	 */
	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	/**
	 * Updates the state of the UserProjectile, including its position.
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
