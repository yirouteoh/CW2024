package com.example.demo.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.example.demo.levels.LevelParent;

public class Controller implements PropertyChangeListener {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.levels.LevelOne";
	private final Stage stage;

	/**
	 * Constructor to initialize the Controller.
	 *
	 * @param stage The primary Stage for the game.
	 */
	public Controller(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Launches the game starting at level one.
	 */
	public void launchGame() {
		stage.show();
		try {
			goToLevel(LEVEL_ONE_CLASS_NAME);
		} catch (ReflectiveOperationException e) {
			showErrorAlert("Error launching game", e);
		}
	}

	/**
	 * Navigates to the specified level by dynamically loading its class.
	 *
	 * @param className The fully qualified class name of the level to load.
	 * @throws ReflectiveOperationException If an error occurs during class loading or instantiation.
	 */
	private void goToLevel(String className) throws ReflectiveOperationException {
		LevelParent level = instantiateLevel(className);
		level.addPropertyChangeListener(this);

		Scene scene = level.initializeScene();
		stage.setScene(scene);

		level.startGame(stage); // Pass the stage to the startGame method
	}


	/**
	 * Instantiates a level dynamically using reflection.
	 *
	 * @param className The fully qualified class name of the level to instantiate.
	 * @return An instance of the LevelParent class.
	 * @throws ReflectiveOperationException If an error occurs during instantiation.
	 */
	private LevelParent instantiateLevel(String className) throws ReflectiveOperationException {
		Class<?> levelClass = Class.forName(className);
		Constructor<?> constructor = levelClass.getConstructor(double.class, double.class);
		return (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
	}

	/**
	 * Handles property change events to navigate to a new level.
	 *
	 * @param evt The PropertyChangeEvent containing the new level class name.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String nextLevelClass = (String) evt.getNewValue();
		try {
			goToLevel(nextLevelClass);
		} catch (ReflectiveOperationException e) {
			showErrorAlert("Error loading next level", e);
		}
	}

	/**
	 * Displays an error alert with the exception details.
	 *
	 * @param headerText The header text for the alert.
	 * @param exception  The exception to display details about.
	 */
	private void showErrorAlert(String headerText, Exception exception) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(headerText);
		alert.setContentText(exception.getMessage());
		alert.show();
		exception.printStackTrace(); // Log stack trace for debugging
	}
}
