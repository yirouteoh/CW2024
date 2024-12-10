package com.example.demo.controller;

import javafx.application.Application;
import javafx.stage.Stage;
import com.example.demo.screens.MenuView;

/**
 * The entry point of the Sky Battle game.
 * <p>
 * This class initializes the JavaFX application, sets up the primary stage, and displays the main menu.
 * </p>
 */
public class Main extends Application {

	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;
	private static final String TITLE = "Sky Battle";

	/**
	 * Entry point for the JavaFX application.
	 *
	 * @param stage The primary stage for the application.
	 */
	@Override
	public void start(Stage stage) {
		configureStage(stage);
		initializeAndShowMenu(stage);
	}

	/**
	 * Configures the primary stage with title, dimensions, and other settings.
	 *
	 * @param stage The primary stage to configure.
	 */
	private void configureStage(Stage stage) {
		stage.setTitle(TITLE);
		stage.setResizable(false);
		stage.setWidth(SCREEN_WIDTH);
		stage.setHeight(SCREEN_HEIGHT);
	}

	/**
	 * Initializes the controller and menu, and displays the main menu.
	 *
	 * @param stage The primary stage.
	 */
	private void initializeAndShowMenu(Stage stage) {
		Controller controller = new Controller(stage); // Create the controller
		MenuView menuView = new MenuView(stage, controller); // Pass the controller to the menu
		menuView.showMenu(); // Display the main menu
	}

	/**
	 * Main method to launch the application.
	 *
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
