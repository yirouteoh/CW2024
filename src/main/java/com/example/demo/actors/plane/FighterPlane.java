package com.example.demo.actors.plane;

import com.example.demo.actors.ActiveActorDestructible;

/**
 * Represents a fighter plane in the game that can take damage and fire projectiles.
 * Extends ActiveActorDestructible to provide destructible behavior.
 */
public abstract class FighterPlane extends ActiveActorDestructible {

	private int health;
	private final int maxHealth;

	/**
	 * Constructs a FighterPlane with the specified parameters.
	 *
	 * @param imageName    the name of the image file
	 * @param imageHeight  the height of the image
	 * @param initialXPos  the initial X position
	 * @param initialYPos  the initial Y position
	 * @param health       the initial health of the plane
	 */
	public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.health = health;
		this.maxHealth = health; // Set maxHealth equal to the initial health
	}

	/**
	 * Fires a projectile from the fighter plane.
	 * Subclasses must implement the specific behavior for firing projectiles.
	 *
	 * @return a new instance of ActiveActorDestructible representing the projectile
	 */
	public abstract ActiveActorDestructible fireProjectile();

	/**
	 * Reduces the plane's health by one when it takes damage.
	 * If the health reaches zero, the plane is destroyed.
	 */
	@Override
	public void takeDamage() {
		health--;
		if (isHealthAtZero()) {
			this.destroy();
		}
	}

	/**
	 * Calculates the X position for the projectile.
	 *
	 * @param xPositionOffset the offset to apply to the X position
	 * @return the X position for the projectile
	 */
	protected double getProjectileXPosition(double xPositionOffset) {
		return getLayoutX() + getTranslateX() + xPositionOffset;
	}

	/**
	 * Calculates the Y position for the projectile.
	 *
	 * @param yPositionOffset the offset to apply to the Y position
	 * @return the Y position for the projectile
	 */
	protected double getProjectileYPosition(double yPositionOffset) {
		return getLayoutY() + getTranslateY() + yPositionOffset;
	}

	/**
	 * Checks if the fighter plane's health has reached zero.
	 *
	 * @return true if health is zero, false otherwise
	 */
	private boolean isHealthAtZero() {
		return health <= 0;
	}

	/**
	 * Gets the current health of the plane.
	 *
	 * @return the current health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Gets the maximum health of the plane.
	 *
	 * @return the maximum health
	 */
	public int getMaxHealth() {
		return maxHealth;
	}
}
