package com.example.demo.actors;

/**
 * Represents an actor in the game that is both active and destructible.
 * Extends the functionality of ActiveActor and implements the Destructible interface.
 */
public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

	private boolean isDestroyed;

	/**
	 * Constructs an ActiveActorDestructible with the specified image and position.
	 *
	 * @param imageName    the name of the image file
	 * @param imageHeight  the height of the image
	 * @param initialXPos  the initial X position
	 * @param initialYPos  the initial Y position
	 */
	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.isDestroyed = false;
	}

	/**
	 * Updates the position of the actor.
	 * Subclasses must define their specific logic.
	 */
	@Override
	public abstract void updatePosition();

	/**
	 * Updates the actor's state.
	 * Subclasses must implement their specific behavior for updating the actor.
	 */
	public abstract void updateActor();

	/**
	 * Handles logic when the actor takes damage.
	 * Subclasses must define their specific behavior for damage handling.
	 */
	@Override
	public abstract void takeDamage();

	/**
	 * Destroys the actor by marking it as destroyed.
	 */
	@Override
	public void destroy() {
		setDestroyed(true);
	}

	/**
	 * Sets the destroyed state of the actor.
	 *
	 * @param isDestroyed true if the actor is destroyed, false otherwise
	 */
	protected void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	/**
	 * Checks if the actor is destroyed.
	 *
	 * @return true if the actor is destroyed, false otherwise
	 */
	public boolean isDestroyed() {
		return isDestroyed;
	}
}
