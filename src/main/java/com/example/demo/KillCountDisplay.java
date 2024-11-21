package com.example.demo;

import javafx.scene.text.Text;

public class KillCountDisplay {
    private int killCount;       // Tracks the current kill count
    private final int targetKillCount; // Total kills required to advance
    private final Text display; // Text object to show the kill progress

    /**
     * Constructor to initialize the kill count display.
     *
     * @param x The X-coordinate of the display on the screen.
     * @param y The Y-coordinate of the display on the screen.
     * @param targetKillCount The total number of kills required to advance.
     */
    public KillCountDisplay(double x, double y, int targetKillCount) {
        this.killCount = 0; // Start with 0 kills
        this.targetKillCount = targetKillCount;
        this.display = new Text("Kills: 0/" + targetKillCount);
        this.display.setX(x);
        this.display.setY(y);
        this.display.setStyle("-fx-font-size: 20px; -fx-fill: white;"); // Styling for visibility
    }

    /**
     * Increment the kill count and update the display.
     */
    public void incrementKillCount() {
        if (killCount < targetKillCount) {
            this.killCount++;
            updateDisplay();
        }
    }

    /**
     * Reset the kill count back to 0 and update the display.
     */
    public void resetKillCount() {
        this.killCount = 0;
        updateDisplay();
    }

    /**
     * Update the display text to match the current kill count.
     */
    private void updateDisplay() {
        this.display.setText("Kills: " + this.killCount + "/" + targetKillCount);
    }

    /**
     * Get the Text object for displaying the kill count.
     *
     * @return The Text object.
     */
    public Text getDisplay() {
        return this.display;
    }

    /**
     * Get the current kill count.
     *
     * @return The current kill count.
     */
    public int getKillCount() {
        return this.killCount;
    }

    /**
     * Check if the user has reached the target kill count.
     *
     * @return True if the user has reached the target, false otherwise.
     */
    public boolean hasReachedTarget() {
        return this.killCount >= this.targetKillCount;
    }
}
