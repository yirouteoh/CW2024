package com.example.demo;

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
		double currentX = getLayoutX() + getTranslateX(); // Get the current X position of the plane
		double currentY = getLayoutY() + getTranslateY(); // Get the current Y position of the plane
		return new UserProjectile(
				currentX + 110, // Fire from the front of the plane
				currentY + 20 // Adjust for vertical alignment
		);
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
