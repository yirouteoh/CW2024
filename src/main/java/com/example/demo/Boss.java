package com.example.demo;

import java.util.*;
import javafx.scene.effect.Glow;

public class Boss extends FighterPlane {

	private static final String IMAGE_NAME = "bossplane.png";
	private static final double INITIAL_X_POSITION = 1000.0;
	private static final double INITIAL_Y_POSITION = 400;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
	private static final double BOSS_FIRE_RATE = .03;
	private static final double BOSS_SHIELD_PROBABILITY = .009;
	private static final int IMAGE_HEIGHT = 300;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int HEALTH = 30;
	private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
	private static final int ZERO = 0;
	private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
	private static final int Y_POSITION_UPPER_BOUND = -100;
	private static final int Y_POSITION_LOWER_BOUND = 475;
	private static final int MAX_FRAMES_WITH_SHIELD = 100; // Reduced shield duration
	private static final int SHIELD_COOLDOWN = 300; // Frames before shield can activate again
	private final List<Integer> movePattern;
	private boolean isShielded;
	private int consecutiveMovesInSameDirection;
	private int indexOfCurrentMove;
	private int framesWithShieldActivated;
	private int framesSinceShieldDeactivated; // Tracks frames since shield was last deactivated

	public Boss() {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
		movePattern = new ArrayList<>();
		consecutiveMovesInSameDirection = 0;
		indexOfCurrentMove = 0;
		framesWithShieldActivated = 0;
		framesSinceShieldDeactivated = SHIELD_COOLDOWN; // Start with the ability to activate shield
		isShielded = false;
		initializeMovePattern();
	}

	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		moveVertically(getNextMove());
		double currentPosition = getLayoutY() + getTranslateY();
		if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
			setTranslateY(initialTranslateY);
		}
	}

	@Override
	public void updateActor() {
		updatePosition();
		updateShield();

		if (isShielded) {
			setEffect(new Glow(0.8)); // Add glow effect
		} else {
			setEffect(null); // Remove effect when shield is deactivated
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
		}
	}

	public javafx.geometry.Bounds getCustomHitbox() {
		// Get the default bounds of the Boss
		javafx.geometry.Bounds originalBounds = super.getBoundsInParent();

		// Adjust the bounds to make the hitbox more precise
		double paddingX = 80; // Horizontal padding
		double paddingY = 80; // Vertical padding
		return new javafx.geometry.BoundingBox(
				originalBounds.getMinX() + paddingX, // Adjust left boundary
				originalBounds.getMinY() + paddingY, // Adjust top boundary
				originalBounds.getWidth() - 2 * paddingX, // Adjust width
				originalBounds.getHeight() - 2 * paddingY // Adjust height
		);
	}

	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			movePattern.add(VERTICAL_VELOCITY);
			movePattern.add(-VERTICAL_VELOCITY);
			movePattern.add(ZERO);
		}
		Collections.shuffle(movePattern);
	}

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
				framesSinceShieldDeactivated = 0; // Reset cooldown
			}
		}
	}

	private int getNextMove() {
		int currentMove = movePattern.get(indexOfCurrentMove);
		consecutiveMovesInSameDirection++;
		if (consecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
			Collections.shuffle(movePattern);
			consecutiveMovesInSameDirection = 0;
			indexOfCurrentMove++;
		}
		if (indexOfCurrentMove == movePattern.size()) {
			indexOfCurrentMove = 0;
		}
		return currentMove;
	}

	private boolean bossFiresInCurrentFrame() {
		return Math.random() < BOSS_FIRE_RATE;
	}

	private double getProjectileInitialPosition() {
		return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
	}

	private boolean shieldShouldBeActivated() {
		return Math.random() < BOSS_SHIELD_PROBABILITY;
	}

	private boolean shieldExhausted() {
		return framesWithShieldActivated >= MAX_FRAMES_WITH_SHIELD;
	}

	private void activateShield() {
		isShielded = true;
		framesWithShieldActivated = 0;
	}

	private void deactivateShield() {
		isShielded = false;
		framesWithShieldActivated = 0;
		framesSinceShieldDeactivated = 0; // Start counting frames since shield deactivation
	}
}
