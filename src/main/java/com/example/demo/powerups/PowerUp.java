package com.example.demo.powerups;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.plane.UserPlane;

/**
 * Represents a power-up in the game.
 * <p>
 * Power-ups are collectible items that grant special abilities or effects to the user's plane.
 * They fall from the top of the screen and are removed if they move off-screen or are collected.
 * </p>
 */
public class PowerUp extends ActiveActorDestructible {

    private static final int IMAGE_HEIGHT = 40; // Fixed height for power-up images
    private static final double FALL_SPEED = 3.0; // Speed at which power-up falls
    private static final double SCREEN_HEIGHT = 600; // Screen height boundary for removal
    private static final String SPREADSHOT_IMAGE_NAME = "spreadshot.png"; // Image name for spreadshot power-up

    /**
     * Constructor for PowerUp.
     *
     * @param imageName The image name for the power-up.
     * @param initialX  The initial X-coordinate.
     * @param initialY  The initial Y-coordinate.
     */
    public PowerUp(String imageName, double initialX, double initialY) {
        super(imageName, IMAGE_HEIGHT, initialX, initialY);
    }

    /**
     * Updates the actor's behavior. Moves the power-up downwards
     * and destroys it if it moves off-screen.
     */
    @Override
    public void updateActor() {
        moveVertically(FALL_SPEED);
        if (isOutOfBounds()) {
            destroy();
        }
    }

    /**
     * Updates the position of the power-up. Moves it downwards.
     */
    @Override
    public void updatePosition() {
        moveVertically(FALL_SPEED);
    }

    /**
     * Handles the power-up taking damage. By default, power-ups are destroyed when taking damage.
     */
    @Override
    public void takeDamage() {
        destroy();
    }

    /**
     * Activates the power-up's effect when collected by the user.
     *
     * @param user The user plane collecting the power-up.
     */
    public void activate(UserPlane user) {
        if (isSpreadshotPowerUp()) {
            activateSpreadshot(user);
        } else {
            activateDefaultPowerUp();
        }
        destroy(); // Remove power-up after it's collected
    }

    /**
     * Checks if the power-up is out of bounds (off the screen).
     *
     * @return True if the power-up is out of bounds, false otherwise.
     */
    private boolean isOutOfBounds() {
        return getTranslateY() > SCREEN_HEIGHT;
    }

    /**
     * Checks if the power-up is a spreadshot power-up.
     *
     * @return True if the power-up is a spreadshot, false otherwise.
     */
    private boolean isSpreadshotPowerUp() {
        return getImage().equals(SPREADSHOT_IMAGE_NAME);
    }

    /**
     * Activates the spreadshot power-up effect.
     *
     * @param user The user plane collecting the power-up.
     */
    private void activateSpreadshot(UserPlane user) {
        user.activateOneTimeSpreadshot();
    }

    /**
     * Activates the default power-up effect.
     */
    private void activateDefaultPowerUp() {
        // Add any other default power-up behavior if necessary
    }
}
