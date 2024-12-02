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
	private static final int VERTICAL_VELOCITY = 5;

	private int velocityMultiplier = 0;  // Controls vertical movement
	private int horizontalVelocityMultiplier = 0;  // Controls horizontal movement
	private int spreadshotCount = 0;  // Counter for spreadshot power-ups
	private LevelParent levelParent; // Reference to the LevelParent for managing projectiles

	/**
	 * Constructs a UserPlane with the specified initial health.
	 *
	 * @param initialHealth the initial health of the user plane
	 */
	public UserPlane(int initialHealth) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
	}

	/**
	 * Updates the position of the user plane based on velocity multipliers.
	 * Ensures the plane does not move outside the defined screen boundaries.
	 */
	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		double initialTranslateX = getTranslateX();

		// Move vertically
		if (velocityMultiplier != 0) {
			moveVertically(VERTICAL_VELOCITY * velocityMultiplier);
			double newPositionY = getLayoutY() + getTranslateY();
			if (newPositionY < Y_UPPER_BOUND || newPositionY > Y_LOWER_BOUND) {
				setTranslateY(initialTranslateY); // Revert if out of bounds
			}
		}

		// Move horizontally
		if (horizontalVelocityMultiplier != 0) {
			moveHorizontally(VERTICAL_VELOCITY * horizontalVelocityMultiplier);
			double newPositionX = getLayoutX() + getTranslateX();
			if (newPositionX < X_LEFT_BOUND || newPositionX > X_RIGHT_BOUND) {
				setTranslateX(initialTranslateX); // Revert if out of bounds
			}
		}
	}

	/**
	 * Updates the actor's state, including its position.
	 */
	@Override
	public void updateActor() {
		updatePosition();
	}

	/**
	 * Fires a projectile from the user plane.
	 * If the spreadshot power-up is active, multiple projectiles are fired simultaneously.
	 * Otherwise, a single projectile is fired.
	 *
	 * @return the fired projectile (center projectile if spreadshot is active)
	 */
	@Override
	public ActiveActorDestructible fireProjectile() {
		double currentX = getLayoutX() + getTranslateX();
		double currentY = getLayoutY() + getTranslateY();

		if (spreadshotCount > 0) {
			// Create and add spreadshot projectiles
			List<ActiveActorDestructible> spreadshotProjectiles = createSpreadshotProjectiles(currentX, currentY);
			spreadshotProjectiles.forEach(levelParent::addProjectile);

			spreadshotCount--; // Decrease the spreadshot count after firing
			return spreadshotProjectiles.get(2); // Return the center projectile for compatibility
		} else {
			// Create and add a single projectile
			ActiveActorDestructible projectile = new UserProjectile(currentX + 100, currentY);
			levelParent.addProjectile(projectile);
			return projectile;
		}
	}

	/**
	 * Activates the spreadshot power-up, allowing the plane to fire multiple projectiles at once.
	 */
	public void activateOneTimeSpreadshot() {
		spreadshotCount++;
	}



	/**
	 * Creates a list of spreadshot projectiles.
	 *
	 * @param currentX the X position of the plane
	 * @param currentY the Y position of the plane
	 * @return a list of spreadshot projectiles
	 */
	private List<ActiveActorDestructible> createSpreadshotProjectiles(double currentX, double currentY) {
		List<ActiveActorDestructible> projectiles = new ArrayList<>();
		projectiles.add(new UserProjectile(currentX + 100, currentY - 30)); // Left
		projectiles.add(new UserProjectile(currentX + 100, currentY - 15)); // Left-mid
		projectiles.add(new UserProjectile(currentX + 100, currentY));      // Center
		projectiles.add(new UserProjectile(currentX + 100, currentY + 15)); // Right-mid
		projectiles.add(new UserProjectile(currentX + 100, currentY + 30)); // Right
		return projectiles;
	}

	/**
	 * Sets the parent level for managing projectiles.
	 *
	 * @param levelParent the LevelParent instance
	 */
	public void setLevelParent(LevelParent levelParent) {
		this.levelParent = levelParent;
	}

	// Movement controls

	/**
	 * Moves the user plane upward.
	 */
	public void moveUp() {
		velocityMultiplier = -2;
	}

	/**
	 * Moves the user plane downward.
	 */
	public void moveDown() {
		velocityMultiplier = 2;
	}

	/**
	 * Moves the user plane to the left.
	 */
	public void moveLeft() {
		horizontalVelocityMultiplier = -2;
	}

	/**
	 * Moves the user plane to the right.
	 */
	public void moveRight() {
		horizontalVelocityMultiplier = 2;
	}

	/**
	 * Stops the vertical movement of the user plane.
	 */
	public void stop() {
		velocityMultiplier = 0;
	}

	/**
	 * Stops the horizontal movement of the user plane.
	 */
	public void stopHorizontal() {
		horizontalVelocityMultiplier = 0;
	}

}
