package com.example.demo.utils;

import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class HealthBar {
    private final Rectangle healthBar;
    private final Rectangle healthBarBackground;
    private final double width;
    private final double height;
    private final Text healthLabel;

    public HealthBar(double width, double height) {
        this.width = width;
        this.height = height;

        // Create a background for the health bar
        healthBarBackground = new Rectangle(width, height, Color.color(0.2, 0.2, 0.2, 0.8));
        healthBarBackground.setArcWidth(5);
        healthBarBackground.setArcHeight(5);
        healthBarBackground.setStroke(Color.BLACK);
        healthBarBackground.setStrokeWidth(2);

        // Create the health bar itself
        healthBar = new Rectangle(width, height);
        healthBar.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.LIME), new Stop(1, Color.RED)));
        healthBar.setArcWidth(5);
        healthBar.setArcHeight(5);
        healthBar.setEffect(new Glow(0.3));

        // Create a label for numeric health display
        healthLabel = new Text();
        healthLabel.setFill(Color.WHITE);
        healthLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
    }

    public Rectangle getHealthBar() {
        return healthBar;
    }

    public Rectangle getHealthBarBackground() {
        return healthBarBackground;
    }

    public Text getHealthLabel() {
        return healthLabel;
    }

    public void updatePosition(double x, double y) {
        healthBar.setX(x + 10);
        healthBar.setY(y - 20);
        healthBarBackground.setX(x + 10);
        healthBarBackground.setY(y - 20);
        healthLabel.setX(x + 10 + width / 2 - healthLabel.getLayoutBounds().getWidth() / 2);
        healthLabel.setY(y - 30);
    }

    public void updateHealth(double healthPercentage, int currentHealth, int maxHealth) {
        healthBar.setWidth(width * healthPercentage);
        healthLabel.setText(currentHealth + "/" + maxHealth);
    }

    public void animateHealth(double healthPercentage) {
        javafx.animation.Timeline timeline = new javafx.animation.Timeline();
        javafx.animation.KeyValue keyValue = new javafx.animation.KeyValue(
                healthBar.widthProperty(), width * healthPercentage
        );
        javafx.animation.KeyFrame keyFrame = new javafx.animation.KeyFrame(
                javafx.util.Duration.millis(300), keyValue
        );
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}
