package com.example.demo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBar {
    private final Rectangle healthBar;
    private final Rectangle healthBarBackground;
    private final double width;
    private final double height;

    public HealthBar(double width, double height) {
        this.width = width;
        this.height = height;

        // Create a background for the health bar
        healthBarBackground = new Rectangle(width, height, Color.DARKGRAY);

        // Create the health bar itself
        healthBar = new Rectangle(width, height, Color.RED);
    }

    public Rectangle getHealthBar() {
        return healthBar;
    }

    public Rectangle getHealthBarBackground() {
        return healthBarBackground;
    }

    // Update the health bar position
    public void updatePosition(double x, double y) {
        healthBar.setX(x);
        healthBar.setY(y);
        healthBarBackground.setX(x);
        healthBarBackground.setY(y);
    }

    // Update the health bar's size based on the current health percentage
    public void updateHealth(double healthPercentage) {
        healthBar.setWidth(width * healthPercentage);
    }
}
