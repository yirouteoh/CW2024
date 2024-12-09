package com.example.demo.actors.plane;

import com.example.demo.levels.LevelParent;
import com.example.demo.actors.projectile.UserProjectile;
import com.example.demo.actors.ActiveActorDestructible;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the user-controlled fighter plane in the game.
 * The plane can move vertically and horizontally, fire projectiles,
 * and collect power-ups such as spreadshots.
 */
public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "userplane.png";
	private static final double Y_UPPER_BOUND = -40;
	private static final double Y_LOWER_BOUND = 600.0;
	private static final double X_LEFT_BOUND = 0.0;
	private static final double X_RIGHT_BOUND = 800.0;
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 150;

	private static final double VELOCITY_SCALE = 6.0;
	private double verticalVelocity = 0; // Current vertical speed
	private double horizontalVelocity = 0; // Current horizontal speed
	private final double acceleration = 1.0; // Acceleration rate
	private final double deceleration = 0.5; // Deceleration rate
	private final double maxSpeed = 10.0; // Maximum speed

	private int spreadshotCount = 0;  // Counter for spreadshot power-ups
	private LevelParent levelParent; // Reference to the LevelParent for managing projectiles

	public UserPlane(int initialHealth) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
	}

	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		double initialTranslateX = getTranslateX();

		// Apply velocity to position
		moveVertically(verticalVelocity * VELOCITY_SCALE);
		moveHorizontally(horizontalVelocity * VELOCITY_SCALE);

		// Check bounds for vertical movement
		double newPositionY = getLayoutY() + getTranslateY();
		if (newPositionY < Y_UPPER_BOUND || newPositionY > Y_LOWER_BOUND) {
			setTranslateY(initialTranslateY); // Revert if out of bounds
		}

		// Check bounds for horizontal movement
		double newPositionX = getLayoutX() + getTranslateX();
		if (newPositionX < X_LEFT_BOUND || newPositionX > X_RIGHT_BOUND) {
			setTranslateX(initialTranslateX); // Revert if out of bounds
		}

		// Decelerate when no input
		applyDeceleration();
	}

	private void applyDeceleration() {
		// Vertical deceleration
		if (verticalVelocity > 0) {
			verticalVelocity = Math.max(0, verticalVelocity - deceleration);
		} else if (verticalVelocity < 0) {
			verticalVelocity = Math.min(0, verticalVelocity + deceleration);
		}

		// Horizontal deceleration
		if (horizontalVelocity > 0) {
			horizontalVelocity = Math.max(0, horizontalVelocity - deceleration);
		} else if (horizontalVelocity < 0) {
			horizontalVelocity = Math.min(0, horizontalVelocity + deceleration);
		}
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		double currentX = getLayoutX() + getTranslateX();
		double currentY = getLayoutY() + getTranslateY();

		if (spreadshotCount > 0) {
			List<ActiveActorDestructible> spreadshotProjectiles = createSpreadshotProjectiles(currentX, currentY);
			spreadshotProjectiles.forEach(levelParent::addProjectile);

			spreadshotCount--; // Decrease the spreadshot count after firing
			return spreadshotProjectiles.get(2); // Return the center projectile for compatibility
		} else {
			ActiveActorDestructible projectile = new UserProjectile(currentX + 100, currentY);
			levelParent.addProjectile(projectile);
			return projectile;
		}
	}

	public void activateOneTimeSpreadshot() {
		spreadshotCount++;
	}

	private List<ActiveActorDestructible> createSpreadshotProjectiles(double currentX, double currentY) {
		List<ActiveActorDestructible> projectiles = new ArrayList<>();
		projectiles.add(new UserProjectile(currentX + 100, currentY - 30)); // Left
		projectiles.add(new UserProjectile(currentX + 100, currentY - 15)); // Left-mid
		projectiles.add(new UserProjectile(currentX + 100, currentY));      // Center
		projectiles.add(new UserProjectile(currentX + 100, currentY + 15)); // Right-mid
		projectiles.add(new UserProjectile(currentX + 100, currentY + 30)); // Right
		return projectiles;
	}

	public void setLevelParent(LevelParent levelParent) {
		this.levelParent = levelParent;
	}

	public void setVerticalVelocity(int multiplier) {
		verticalVelocity += multiplier * acceleration;
		verticalVelocity = Math.max(-maxSpeed, Math.min(maxSpeed, verticalVelocity)); // Limit speed
	}

	public void setHorizontalVelocity(int multiplier) {
		horizontalVelocity += multiplier * acceleration;
		horizontalVelocity = Math.max(-maxSpeed, Math.min(maxSpeed, horizontalVelocity)); // Limit speed
	}

	public void stopVerticalMovement() {
		verticalVelocity = 0;
	}

	public void stopHorizontalMovement() {
		horizontalVelocity = 0;
	}
}
