package com.example.demo.powerups;

import com.example.demo.actors.plane.UserPlane;

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
