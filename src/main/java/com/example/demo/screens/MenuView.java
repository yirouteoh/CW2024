package com.example.demo.screens;

import com.example.demo.sounds.SoundManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;

public class MenuView {

    private final Stage stage;
    private final com.example.demo.controller.Controller controller;
    private final SoundManager soundManager;
    private final Image instructionsImage;
    private final Image settingsImage;


    private boolean isMuted = false; // Tracks mute state

    // Constructor
    public MenuView(Stage stage, com.example.demo.controller.Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.soundManager = SoundManager.getInstance();

        // Preload images
        this.instructionsImage = new Image(getResourceOrThrow("/com/example/demo/images/instructions.png").toExternalForm());
        this.settingsImage = new Image(getResourceOrThrow("/com/example/demo/images/settings.png").toExternalForm());
    }

    /**
     * Displays the main menu with background music and layout.
     */
    public void showMenu() {
        soundManager.playBackgroundMusic(SoundManager.MENU_MUSIC);

        StackPane root = createRootPane();
        VBox menuPanel = createMenuPanel();

        root.getChildren().add(menuPanel);

        Scene menuScene = new Scene(root, 800, 600);
        menuScene.getStylesheets().add(getResourceOrThrow("/style.css").toExternalForm());

        stage.setScene(menuScene);
        stage.setTitle("Sky Battle Menu");
        stage.show();
    }

    /**
     * Creates the root pane with a background image.
     *
     * @return StackPane with background.
     */
    private StackPane createRootPane() {
        StackPane root = new StackPane();
        Image backgroundImage = new Image(getResourceOrThrow("/com/example/demo/images/background1.jpeg").toExternalForm());

        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true)
        );

        root.setBackground(new Background(background));
        return root;
    }

    /**
     * Creates the menu panel with title and buttons.
     *
     * @return VBox containing menu elements.
     */
    private VBox createMenuPanel() {
        StackPane title = createTitle();

        Button playButton = createStyledButton("Play", e -> launchGame());
        Button instructionsButton = createStyledButton("Instructions", e -> showInstructionsOverlay());
        Button muteButton = createStyledButton("Mute", e -> toggleMute((Button) e.getSource()));
        Button exitButton = createStyledButton("Exit", e -> System.exit(0));

        VBox panel = new VBox(20, title, playButton, instructionsButton, muteButton, exitButton);
        panel.setAlignment(Pos.CENTER);
        panel.getStyleClass().add("menu-panel");
        panel.setPadding(new javafx.geometry.Insets(20));

        return panel;
    }

    /**
     * Creates the title bar for the menu.
     *
     * @return StackPane containing the title.
     */
    private StackPane createTitle() {
        Rectangle titleBackground = new Rectangle(700, 180, Color.GREEN);
        titleBackground.setArcWidth(20);
        titleBackground.setArcHeight(20);

        Text titleText = new Text("SKY BATTLE");
        titleText.getStyleClass().add("banner-text");

        return new StackPane(titleBackground, titleText);
    }

    /**
     * Creates a styled button with a given text and action.
     *
     * @param text Button text.
     * @param action Event handler for the button.
     * @return Button instance.
     */
    private Button createStyledButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button(text);
        button.getStyleClass().add("menu-button");
        button.setOnAction(action);
        return button;
    }

    /**
     * Toggles the mute state of the background music.
     *
     * @param muteButton The mute button instance.
     */
    private void toggleMute(Button muteButton) {
        isMuted = !isMuted;
        if (isMuted) {
            soundManager.stopBackgroundMusic();
            muteButton.setText("Unmute");
        } else {
            soundManager.playBackgroundMusic(SoundManager.MENU_MUSIC);
            muteButton.setText("Mute");
        }
    }

    /**
     * Launches the game by notifying the controller.
     */
    private void launchGame() {
        try {
            soundManager.stopBackgroundMusic();
            controller.launchGame();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Displays the instructions overlay.
     */
    private void showInstructionsOverlay() {
        // Create overlay only once and reuse it
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        overlay.setAlignment(Pos.CENTER);

        VBox container = new VBox(20);
        container.setStyle("-fx-background-color: #dff9fb; -fx-padding: 20; -fx-border-color: #badc58; -fx-border-width: 3; -fx-background-radius: 10;");
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(500);
        container.setMaxHeight(400);

        // Create the pages
        VBox howToPlayPage = createHowToPlayPage();
        VBox settingsPage = createSettingsPage();

        // StackPane for toggling between pages
        StackPane pages = new StackPane(howToPlayPage, settingsPage);
        settingsPage.setVisible(false);

        // Create navigation buttons
        HBox firstPageButtons = createNavigationButtons(howToPlayPage, settingsPage, overlay, true);
        HBox secondPageButtons = createNavigationButtons(howToPlayPage, settingsPage, overlay, false);

        // Add navigation buttons to each page
        howToPlayPage.getChildren().add(firstPageButtons);
        settingsPage.getChildren().add(secondPageButtons);

        // Add pages to the container
        container.getChildren().addAll(pages);
        overlay.getChildren().add(container);

        // Add overlay to the root pane and toggle visibility
        StackPane root = (StackPane) stage.getScene().getRoot();
        if (!root.getChildren().contains(overlay)) {
            root.getChildren().add(overlay);
        }
        overlay.setVisible(true);
    }


    /**
     * Creates the "How to Play" page content.
     *
     * @return VBox containing the content.
     */
    private VBox createHowToPlayPage() {
        VBox page = new VBox(15);
        page.setAlignment(Pos.CENTER);

        ImageView icon = new ImageView(instructionsImage);
        icon.setFitHeight(100);
        icon.setFitWidth(100);

        Text text = new Text("""
        How to Play Sky Battle

        1. Use arrow keys to navigate your plane.
        2. Press space to shoot.
        3. Destroy enemy planes and avoid obstacles.
        4. Collect power-ups at level 3 to spread shot!
        5. Survive and progress through levels to win!
        """);
        text.setStyle("-fx-font-size: 16px; -fx-text-fill: #30336b;");
        text.setWrappingWidth(450);

        page.getChildren().addAll(icon, text);
        return page;
    }


    /**
     * Creates the settings page content.
     *
     * @return VBox containing the content.
     */
    private VBox createSettingsPage() {
        VBox page = new VBox(15);
        page.setAlignment(Pos.CENTER);

        ImageView icon = new ImageView(settingsImage);
        icon.setFitHeight(100);
        icon.setFitWidth(100);

        Text text = new Text("""
        Control Keys

        1. Arrow Keys: Navigate your plane.
        2. Spacebar: Shoot bullets.
        3. ESC: Pause the game.
        """);
        text.setStyle("-fx-font-size: 16px; -fx-text-fill: #30336b;");
        text.setWrappingWidth(450);

        page.getChildren().addAll(icon, text);
        return page;
    }


    /**
     * Creates navigation buttons for the instruction overlay.
     *
     * @param howToPlayPage The "How to Play" page.
     * @param settingsPage The settings page.
     * @param overlay The overlay to close.
     * @return HBox containing navigation buttons.
     */
    private HBox createNavigationButtons(VBox howToPlayPage, VBox settingsPage, StackPane overlay, boolean isFirstPage) {
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);

        if (isFirstPage) {
            // For the first page: "Next" and "Close"
            Button nextButton = new Button("Next");
            nextButton.setStyle("-fx-background-color: #74b9ff; -fx-text-fill: white; -fx-font-size: 14px;");
            nextButton.setOnAction(e -> {
                howToPlayPage.setVisible(false);
                settingsPage.setVisible(true);
            });

            Button closeButton = new Button("Close");
            closeButton.setStyle("-fx-background-color: #ffbe76; -fx-text-fill: white; -fx-font-size: 14px;");
            closeButton.setOnAction(e -> {
                overlay.setVisible(false);
                StackPane root = (StackPane) stage.getScene().getRoot();
                root.getChildren().remove(overlay);
            });

            buttons.getChildren().addAll(nextButton, closeButton);

        } else {
            // For the second page: "Previous" and "Close"
            Button prevButton = new Button("Previous");
            prevButton.setStyle("-fx-background-color: #74b9ff; -fx-text-fill: white; -fx-font-size: 14px;");
            prevButton.setOnAction(e -> {
                settingsPage.setVisible(false);
                howToPlayPage.setVisible(true);
            });

            Button closeButton = new Button("Close");
            closeButton.setStyle("-fx-background-color: #ffbe76; -fx-text-fill: white; -fx-font-size: 14px;");
            closeButton.setOnAction(e -> {
                overlay.setVisible(false);
                StackPane root = (StackPane) stage.getScene().getRoot();
                root.getChildren().remove(overlay);
            });

            buttons.getChildren().addAll(prevButton, closeButton);
        }

        return buttons;
    }


    /**
     * Retrieves a resource or throws an exception if not found.
     *
     * @param resourcePath The resource path.
     * @return The resource URL.
     */
    private URL getResourceOrThrow(String resourcePath) {
        URL resourceUrl = getClass().getResource(resourcePath);
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        return resourceUrl;
    }
}
