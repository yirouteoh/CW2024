package com.example.demo.controller;

import javafx.application.Application;
import javafx.stage.Stage;
import com.example.demo.screens.MenuView;

public class Main extends Application {

	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;
	private static final String TITLE = "Sky Battle";

	@Override
	public void start(Stage stage) {
		stage.setTitle(TITLE);
		stage.setResizable(false);
		stage.setHeight(SCREEN_HEIGHT);
		stage.setWidth(SCREEN_WIDTH);

		Controller myController = new Controller(stage); // Create a controller instance
		MenuView menuView = new MenuView(stage, myController); // Pass it to MenuView
		menuView.showMenu(); // Show the main menu
	}

	public static void main(String[] args) {
		launch(args);
	}
}
