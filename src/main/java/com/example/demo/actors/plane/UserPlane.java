package com.example.demo.actors.plane;

import com.example.demo.levels.LevelParent;
import com.example.demo.actors.projectile.UserProjectile;
import com.example.demo.actors.ActiveActorDestructible;

import java.util.ArrayList;
import java.util.List;

public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "userplane.png";
	private static final double Y_UPPER_BOUND = -40;
	private static final double Y_LOWER_BOUND = 600.0;
	private static final double X_LEFT_BOUND = 0.0;
	private static final double X_RIGHT_BOUND = 800.0; // Adjust for screen width
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 150;
	private static final int VERTICAL_VELOCITY = 5; // Reduced for smoother motion
	private int velocityMultiplier;
	private int horizontalVelocityMultiplier;
	private int numberOfKills;

	private int spreadshotCount = 0; // Counter for spreadshot power-ups

	private LevelParent levelParent; // Reference to the LevelParent

	public UserPlane(int initialHealth) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		velocityMultiplier = 0;
		horizontalVelocityMultiplier = 0;
	}

	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		double initialTranslateX = getTranslateX();

		// Move vertically
		if (velocityMultiplier != 0) {
			this.moveVertically(VERTICAL_VELOCITY * velocityMultiplier);
			double newPositionY = getLayoutY() + getTranslateY();
			if (newPositionY < Y_UPPER_BOUND || newPositionY > Y_LOWER_BOUND) {
				this.setTranslateY(initialTranslateY); // Revert if out of bounds
			}
		}

		// Move horizontally
		if (horizontalVelocityMultiplier != 0) {
			this.moveHorizontally(VERTICAL_VELOCITY * horizontalVelocityMultiplier);
			double newPositionX = getLayoutX() + getTranslateX();
			if (newPositionX < X_LEFT_BOUND || newPositionX > X_RIGHT_BOUND) {
				this.setTranslateX(initialTranslateX); // Revert if out of bounds
			}
		}
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

	@Override
	public ActiveActorDestructible fireProjectile() {

		double currentX = getLayoutX() + getTranslateX();
		double currentY = getLayoutY() + getTranslateY();

		if (spreadshotCount > 0) {
			// Create spreadshot projectiles
			ActiveActorDestructible leftProjectile = new UserProjectile(currentX + 100, currentY - 30);
			ActiveActorDestructible leftmidProjectile = new UserProjectile(currentX + 100, currentY - 15);
			ActiveActorDestructible centerProjectile = new UserProjectile(currentX + 100, currentY);
			ActiveActorDestructible rightmidProjectile = new UserProjectile(currentX + 100, currentY + 15);
			ActiveActorDestructible rightProjectile = new UserProjectile(currentX + 100, currentY + 30);

			// Add all projectiles to the scene via LevelParent
			levelParent.addProjectile(leftProjectile);
			levelParent.addProjectile(leftmidProjectile);
			levelParent.addProjectile(centerProjectile);
			levelParent.addProjectile(rightmidProjectile);
			levelParent.addProjectile(rightProjectile);


			spreadshotCount--; // Decrease the spreadshot count after firing
			return centerProjectile; // Return the center projectile for compatibility
		} else {
			// Single shot
			ActiveActorDestructible projectile = new UserProjectile(currentX + 100, currentY);
			levelParent.addProjectile(projectile); // Add to LevelParent
			return projectile;
		}
	}

	public void activateOneTimeSpreadshot() {
		spreadshotCount++; // Increment the spreadshot count when power-up is collected
	}

	public boolean isSpreadshotActive() {
		return spreadshotCount > 0;
	}

	public List<ActiveActorDestructible> getSpreadshotProjectiles() {
		List<ActiveActorDestructible> projectiles = new ArrayList<>();

		double currentX = getLayoutX() + getTranslateX();
		double currentY = getLayoutY() + getTranslateY();

		// Create spreadshot projectiles
		projectiles.add(new UserProjectile(currentX + 100, currentY - 10)); // Left projectile
		projectiles.add(new UserProjectile(currentX + 100, currentY));      // Center projectile
		projectiles.add(new UserProjectile(currentX + 100, currentY + 10)); // Right projectile

		return projectiles;
	}

	public void setLevelParent(LevelParent levelParent) {
		this.levelParent = levelParent;
	}



	public void moveUp() {
		velocityMultiplier = -2;
	}

	public void moveDown() {
		velocityMultiplier = 2;
	}

	public void moveLeft() {
		horizontalVelocityMultiplier = -2;
	}

	public void moveRight() {
		horizontalVelocityMultiplier = 2;
	}

	public void stop() {
		velocityMultiplier = 0;
	}

	public void stopHorizontal() {
		horizontalVelocityMultiplier = 0;
	}

	public int getNumberOfKills() {
		return numberOfKills;
	}

	public void incrementKillCount() {
		numberOfKills++;
	}
}
