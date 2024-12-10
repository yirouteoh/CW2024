package com.example.demo.utils;

import javafx.scene.text.Text;

/**
 * Represents a display for tracking and showing the player's current kill count.
 * <p>
 * The display shows the current number of kills relative to the target kill count
 * required to advance in the game.
 * </p>
 */
public class KillCountDisplay {

    private int killCount; // Tracks the current kill count
    private final int targetKillCount; // Total kills required to advance
    private final Text display; // Text object to show the kill progress

    /**
     * Constructor to initialize the kill count display.
     *
     * @param x              The X-coordinate of the display on the screen.
     * @param y              The Y-coordinate of the display on the screen.
     * @param targetKillCount The total number of kills required to advance.
     */
    public KillCountDisplay(double x, double y, int targetKillCount) {
        this.killCount = 0;
        this.targetKillCount = targetKillCount;
        this.display = createDisplay(x, y);
        updateDisplay();
    }

    /**
     * Creates the Text object for displaying the kill count.
     *
     * @param x The X-coordinate of the display on the screen.
     * @param y The Y-coordinate of the display on the screen.
     * @return A styled Text object.
     */
    private Text createDisplay(double x, double y) {
        Text text = new Text();
        text.setX(x);
        text.setY(y);
        text.setStyle("-fx-font-size: 20px; -fx-fill: white;"); // Styling for visibility
        return text;
    }

    /**
     * Increments the kill count and updates the display.
     */
    public void incrementKillCount() {
        if (killCount < targetKillCount) {
            killCount++;
            updateDisplay();
        }
    }

    /**
     * Updates the display text to match the current kill count.
     */
    private void updateDisplay() {
        display.setText("Kills: " + killCount + "/" + targetKillCount);
    }

    /**
     * Gets the Text object for displaying the kill count.
     *
     * @return The Text object.
     */
    public Text getDisplay() {
        return display;
    }

    /**
     * Gets the current kill count.
     *
     * @return The current kill count.
     */
    public int getKillCount() {
        return killCount;
    }

}
