package com.example.demo.actors.plane;

import java.util.*;

import com.example.demo.actors.projectile.BossProjectile;
import com.example.demo.utils.HealthBar;
import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.powerups.ShieldImage;
import javafx.geometry.Bounds;
import javafx.geometry.BoundingBox;
import javafx.scene.effect.Glow;
import javafx.scene.shape.Rectangle;

/**
 * Represents the boss enemy in the game.
 * <p>
 * The boss is a challenging enemy with high health, a shield mechanic, a custom hitbox,
 * and a health bar displayed above it. It follows a randomized movement pattern and fires
 * projectiles at a consistent rate. The boss can activate a shield temporarily, preventing
 * damage, and has a cooldown period before the shield can be reactivated.
 * </p>
 */
public class Boss extends FighterPlane {

	private static final String IMAGE_NAME = "bossPlane.png";
	private static final double INITIAL_X_POSITION = 1000.0;
	private static final double INITIAL_Y_POSITION = 400.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
	private static final double BOSS_FIRE_RATE = 0.03;
	private static final double BOSS_SHIELD_PROBABILITY = 0.009;
	private static final int IMAGE_HEIGHT = 300;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HEALTH = 30;
	private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
	private static final int ZERO = 0;
	private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
	private static final int Y_POSITION_UPPER_BOUND = -100;
	private static final int Y_POSITION_LOWER_BOUND = 475;
	private static final int MAX_FRAMES_WITH_SHIELD = 100;
	private static final int SHIELD_COOLDOWN = 300;

	private final List<Integer> movePattern; // List of vertical movement patterns
	private final HealthBar healthBar;       // Displays the boss's health
	private final ShieldImage shieldImage;

	private boolean isShielded;              // Indicates if the boss is shielded
	private int consecutiveMovesInSameDirection; // Tracks consecutive frames in the same movement
	private int indexOfCurrentMove;          // Index of the current move in the pattern
	private int framesWithShieldActivated;   // Frames since shield activation
	private int framesSinceShieldDeactivated; // Frames since the shield was deactivated

	/**
	 * Constructs a Boss instance with predefined properties such as initial position, health,
	 * movement pattern, and shield mechanics.
	 */
	public Boss() {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);

		// Initialize movement variables
		movePattern = new ArrayList<>();
		consecutiveMovesInSameDirection = 0;
		indexOfCurrentMove = 0;
		framesWithShieldActivated = 0;
		framesSinceShieldDeactivated = SHIELD_COOLDOWN;
		isShielded = false;
		initializeMovePattern();

		// Initialize health bar
		healthBar = new HealthBar(200, 20); // Width: 200, Height: 20
		shieldImage = new ShieldImage(getLayoutX(), getLayoutY());

	}

	/**
	 * Checks if the boss is currently shielded.
	 *
	 * @return true if the shield is active, false otherwise
	 */
	public boolean isShielded() {
		return isShielded;
	}

	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		moveVertically(getNextMove());
		double currentPosition = getLayoutY() + getTranslateY();
		if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
			setTranslateY(initialTranslateY); // Revert if out of bounds
		}
	}

	@Override
	public void updateActor() {
		updatePosition();
		updateShield();

		// Update health bar position
		double healthBarX = getLayoutX() + getTranslateX();
		double healthBarY = getLayoutY() + getTranslateY() - 20; // Position above the boss
		healthBar.updatePosition(healthBarX, healthBarY);

		// Apply shield effect
		setEffect(isShielded ? new Glow(0.8) : null);

		if (isShielded) {
			shieldImage.showShield();
			shieldImage.setLayoutX(getLayoutX() + getTranslateX() - shieldImage.getFitWidth() / 2);
			shieldImage.setLayoutY(getLayoutY() + getTranslateY() - shieldImage.getFitHeight() / 2);
			shieldImage.toFront();
		} else {
			shieldImage.hideShield();
		}
	}

	@Override
	public ActiveActorDestructible fireProjectile() {
		return bossFiresInCurrentFrame() ? new BossProjectile(getProjectileInitialPosition()) : null;
	}

	@Override
	public void takeDamage() {
		if (!isShielded) {
			super.takeDamage();

			// Update health bar based on health percentage
			double healthPercentage = (double) getHealth() / getMaxHealth();
			healthBar.animateHealth(healthPercentage);
			healthBar.updateHealth(healthPercentage, getHealth(), getMaxHealth());
		}
	}

	/**
	 * Provides a custom hitbox for the boss with padding applied.
	 *
	 * @return a BoundingBox representing the custom hitbox
	 */
	public Bounds getCustomHitbox() {
		Bounds originalBounds = super.getBoundsInParent();

		double paddingX = 80;
		double paddingY = 80;
		return new BoundingBox(
				originalBounds.getMinX() + paddingX,
				originalBounds.getMinY() + paddingY,
				originalBounds.getWidth() - 2 * paddingX,
				originalBounds.getHeight() - 2 * paddingY
		);
	}

	/**
	 * Gets the background rectangle of the health bar.
	 *
	 * @return the background rectangle of the health bar
	 */
	public Rectangle getHealthBarBackground() {
		return healthBar.getHealthBarBackground();
	}

	/**
	 * Gets the foreground rectangle of the health bar (the actual health display).
	 *
	 * @return the foreground rectangle of the health bar
	 */
	public Rectangle getHealthBar() {
		return healthBar.getHealthBar();
	}


	// Private helper methods

	/**
	 * Initializes the movement pattern for the boss.
	 * <p>
	 * The pattern alternates between moving up, moving down, and staying still.
	 * Each pattern cycle consists of a predefined frequency of movements.
	 * The pattern is shuffled to introduce randomness.
	 * </p>
	 */
	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			movePattern.add(VERTICAL_VELOCITY);
			movePattern.add(-VERTICAL_VELOCITY);
			movePattern.add(ZERO);
		}
		Collections.shuffle(movePattern);
	}

	/**
	 * Updates the boss's shield status.
	 * <p>
	 * If the shield is currently active, the shield duration is incremented, and the shield
	 * is deactivated once the maximum duration is reached.
	 * If the shield is inactive, the cooldown period is incremented, and the shield is
	 * activated again if the cooldown has elapsed and the shield activation probability is met.
	 * </p>
	 */
	private void updateShield() {
		if (isShielded) {
			framesWithShieldActivated++;
			if (shieldExhausted()) {
				deactivateShield();
			}
		} else {
			framesSinceShieldDeactivated++;
			if (shieldShouldBeActivated() && framesSinceShieldDeactivated >= SHIELD_COOLDOWN) {
				activateShield();
			}
		}
	}

	/**
	 * Retrieves the next vertical movement in the boss's movement pattern.
	 * <p>
	 * If the boss has performed the same move for too many consecutive frames, the movement
	 * pattern is reshuffled to ensure variety. The movement index is also cycled to ensure
	 * it loops back to the start of the pattern.
	 * </p>
	 *
	 * @return the next vertical movement value (positive, negative, or zero)
	 */
	private int getNextMove() {
		int currentMove = movePattern.get(indexOfCurrentMove);
		consecutiveMovesInSameDirection++;
		if (consecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
			Collections.shuffle(movePattern); // Shuffle the movement pattern
			consecutiveMovesInSameDirection = 0;
			indexOfCurrentMove++;
		}
		if (indexOfCurrentMove == movePattern.size()) {
			indexOfCurrentMove = 0;  // Reset to the start of the pattern
		}
		return currentMove;
	}

	/**
	 * Determines whether the boss fires a projectile in the current frame.
	 * <p>
	 * The probability of firing is determined by the `BOSS_FIRE_RATE` constant. This method
	 * uses a random number generator to decide if the boss should fire in the current frame.
	 * </p>
	 *
	 * @return true if the boss fires a projectile, false otherwise
	 */
	private boolean bossFiresInCurrentFrame() {
		return Math.random() < BOSS_FIRE_RATE;
	}

	/**
	 * Calculates the initial Y position for the boss's projectile.
	 * <p>
	 * The position is offset from the boss's current position to ensure projectiles are
	 * fired from the correct location.
	 * </p>
	 *
	 * @return the Y position for the boss's projectile
	 */
	private double getProjectileInitialPosition() {
		return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
	}


	/**
	 * Determines whether the boss's shield should be activated in the current frame.
	 * <p>
	 * The probability of shield activation is determined by the `BOSS_SHIELD_PROBABILITY` constant.
	 * This method uses a random number generator to decide if the shield should be activated.
	 * </p>
	 *
	 * @return true if the shield should activate, false otherwise
	 */
	private boolean shieldShouldBeActivated() {
		return Math.random() < BOSS_SHIELD_PROBABILITY;
	}

	/**
	 * Checks if the boss's shield has been active for too long.
	 * <p>
	 * The shield's maximum duration is defined by the `MAX_FRAMES_WITH_SHIELD` constant.
	 * If the shield has been active for this many frames, it should be deactivated.
	 * </p>
	 *
	 * @return true if the shield's duration is exhausted, false otherwise
	 */
	private boolean shieldExhausted() {
		return framesWithShieldActivated >= MAX_FRAMES_WITH_SHIELD;
	}

	/**
	 * Activates the boss's shield.
	 * <p>
	 * Resets the shield activation timer and sets the `isShielded` flag to true.
	 * </p>
	 */
	private void activateShield() {
		isShielded = true;
		framesWithShieldActivated = 0; // Reset the shield duration
	}

	/**
	 * Deactivates the boss's shield.
	 * <p>
	 * Resets the shield deactivation timer and sets the `isShielded` flag to false.
	 * </p>
	 */
	private void deactivateShield() {
		isShielded = false;
		framesWithShieldActivated = 0;       // Reset the shield activation timer
		framesSinceShieldDeactivated = 0;   // Reset the cooldown timer
	}

	public ShieldImage getShieldImage() {
		return shieldImage;
	}
}
