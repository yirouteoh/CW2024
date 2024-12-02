package com.example.demo.actors;

/**
 * Represents an entity in the game that can take damage and be destroyed.
 * Classes implementing this interface must provide logic for taking damage and being destroyed.
 */
public interface Destructible {

	/**
	 * Handles the logic for when the object takes damage.
	 * Implementing classes should define how damage is applied.
	 */
	void takeDamage();

	/**
	 * Handles the logic for destroying the object.
	 * Implementing classes should define what happens when the object is destroyed.
	 */
	void destroy();
}
