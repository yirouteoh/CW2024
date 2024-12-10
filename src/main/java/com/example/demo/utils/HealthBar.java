package com.example.demo.utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Represents a health bar that visually displays the health status of an entity in the game.
 * <p>
 * The health bar consists of a background, a colored bar indicating current health,
 * and a label displaying the numeric health values.
 * </p>
 */
public class HealthBar {

    private final Rectangle healthBar; // The current health bar
    private final Rectangle healthBarBackground; // The background for the health bar
    private final double width; // The width of the health bar
    private final Text healthLabel; // Label to display health as text

    /**
     * Constructor for HealthBar.
     *
     * @param width  The width of the health bar.
     * @param height The height of the health bar (used locally in the constructor).
     */
    public HealthBar(double width, double height) {
        this.width = width;

        this.healthBarBackground = createHealthBarBackground(width, height);
        this.healthBar = createHealthBar(width, height);
        this.healthLabel = createHealthLabel();
    }

    /**
     * Creates the background for the health bar.
     *
     * @param width  The width of the background.
     * @param height The height of the background.
     * @return A Rectangle representing the background.
     */
    private Rectangle createHealthBarBackground(double width, double height) {
        Rectangle background = new Rectangle(width, height, Color.color(0.2, 0.2, 0.2, 0.8));
        background.setArcWidth(5);
        background.setArcHeight(5);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(2);
        return background;
    }

    /**
     * Creates the health bar rectangle.
     *
     * @param width  The initial width of the health bar.
     * @param height The height of the health bar.
     * @return A Rectangle representing the health bar.
     */
    private Rectangle createHealthBar(double width, double height) {
        Rectangle bar = new Rectangle(width, height);
        bar.setFill(new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.LIME), new Stop(1, Color.RED)
        ));
        bar.setArcWidth(5);
        bar.setArcHeight(5);
        bar.setEffect(new Glow(0.3));
        return bar;
    }

    /**
     * Creates the label to display the numeric health.
     *
     * @return A Text object for the health label.
     */
    private Text createHealthLabel() {
        Text label = new Text();
        label.setFill(Color.WHITE);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        return label;
    }

    /**
     * Updates the position of the health bar and label.
     *
     * @param x The x-coordinate of the health bar.
     * @param y The y-coordinate of the health bar.
     */
    public void updatePosition(double x, double y) {
        double offsetX = x + 10;
        double offsetY = y - 20;

        healthBar.setX(offsetX);
        healthBar.setY(offsetY);

        healthBarBackground.setX(offsetX);
        healthBarBackground.setY(offsetY);

        healthLabel.setX(offsetX + width / 2 - healthLabel.getLayoutBounds().getWidth() / 2);
        healthLabel.setY(offsetY - 10);
    }

    /**
     * Updates the health bar's width and label based on the current health.
     *
     * @param healthPercentage The current health as a percentage (0.0 - 1.0).
     * @param currentHealth    The current health value.
     * @param maxHealth        The maximum health value.
     */
    public void updateHealth(double healthPercentage, int currentHealth, int maxHealth) {
        healthBar.setWidth(width * healthPercentage);
        healthLabel.setText(currentHealth + "/" + maxHealth);
    }

    /**
     * Animates the health bar's width to the new health percentage.
     *
     * @param healthPercentage The new health as a percentage (0.0 - 1.0).
     */
    public void animateHealth(double healthPercentage) {
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(healthBar.widthProperty(), width * healthPercentage);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(300), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    // Getters for the components

    /**
     * Retrieves the current health bar rectangle.
     *
     * @return The {@link Rectangle} representing the health bar.
     */
    public Rectangle getHealthBar() {
        return healthBar;
    }

    /**
     * Retrieves the background rectangle of the health bar.
     *
     * @return The {@link Rectangle} representing the health bar background.
     */
    public Rectangle getHealthBarBackground() {
        return healthBarBackground;
    }
}
