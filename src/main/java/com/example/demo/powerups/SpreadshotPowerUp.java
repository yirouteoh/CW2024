package com.example.demo.powerups;

import com.example.demo.actors.plane.UserPlane;

/**
 * Represents a spreadshot power-up in the game.
 * <p>
 * When collected by the user's plane, this power-up enables a spreadshot effect
 * for the next projectile fired by the user. The power-up is removed after activation.
 * </p>
 */
public class SpreadshotPowerUp extends PowerUp {

    private static final String SPREADSHOT_IMAGE = "spreadshot.png"; // Image name for the spreadshot power-up

    /**
     * Constructor for SpreadshotPowerUp.
     *
     * @param initialX The initial X-coordinate of the power-up.
     * @param initialY The initial Y-coordinate of the power-up.
     */
    public SpreadshotPowerUp(double initialX, double initialY) {
        super(SPREADSHOT_IMAGE, initialX, initialY);
    }

    /**
     * Activates the spreadshot effect when collected by the user.
     *
     * @param user The user plane collecting the power-up.
     */
    @Override
    public void activate(UserPlane user) {
        user.activateOneTimeSpreadshot(); // Enable spreadshot for the next shot
        destroy(); // Remove the power-up after activation
    }
}
