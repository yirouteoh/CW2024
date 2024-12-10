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

import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import java.util.logging.Logger;


import java.net.URL;

public class MenuView {

    private final Stage stage;
    private final com.example.demo.controller.Controller controller;
    private final SoundManager soundManager;
    private final Image instructionsImage;
    private final Image settingsImage;
    private final Image unmuteMusicImage;
    private final Image unmuteSoundImage;
    private static final Logger logger = Logger.getLogger(MenuView.class.getName());

    // Constructor
    public MenuView(Stage stage, com.example.demo.controller.Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.soundManager = SoundManager.getInstance();

        // Preload images
        this.instructionsImage = new Image(getResourceOrThrow("/com/example/demo/images/instructions.png").toExternalForm());
        this.settingsImage = new Image(getResourceOrThrow("/com/example/demo/images/settings.png").toExternalForm());
        // In the constructor
        this.unmuteMusicImage = new Image(getResourceOrThrow("/com/example/demo/images/unmutemusic.png").toExternalForm());
        this.unmuteSoundImage = new Image(getResourceOrThrow("/com/example/demo/images/unmuteeffect.png").toExternalForm());
    }

    /**
     * Displays the main menu with background music and layout.
     */
    public void showMenu() {
        soundManager.playBackgroundMusic(SoundManager.MENU_MUSIC);

        // Create root pane and menu panel
        StackPane root = createRootPane();
        VBox menuPanel = createMenuPanel();

        // Create the exit button
        Button exitButton = createExitButton();

        // Align and add the exit button to the root
        StackPane.setAlignment(exitButton, Pos.TOP_LEFT);
        StackPane.setMargin(exitButton, new javafx.geometry.Insets(10)); // Margin from top-left corner

        // Add menu panel and exit button to the root
        root.getChildren().addAll(menuPanel, exitButton);

        // Set up the scene
        Scene menuScene = new Scene(root, 800, 600);
        menuScene.getStylesheets().add(getResourceOrThrow("/style.css").toExternalForm());

        stage.setScene(menuScene);
        stage.setTitle("Sky Battle Menu");
        stage.show();
    }


    /**
     * Creates an exit button and attaches a close action.
     *
     * @return Button configured as the exit button.
     */
    private Button createExitButton() {
        // Load the exit icon
        ImageView exitIcon = new ImageView(new Image(getResourceOrThrow("/com/example/demo/images/cross.png").toExternalForm()));
        exitIcon.setFitHeight(60);
        exitIcon.setFitWidth(60);

        // Create the button and set the icon
        Button exitButton = new Button();
        exitButton.setGraphic(exitIcon);
        exitButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        // Add smooth scaling effects with animations
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), exitButton);
        scaleUp.setToX(1.2); // Scale to 120%
        scaleUp.setToY(1.2);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), exitButton);
        scaleDown.setToX(1.0); // Scale back to normal size
        scaleDown.setToY(1.0);

        exitButton.setOnMouseEntered(e -> scaleUp.playFromStart());
        exitButton.setOnMouseExited(e -> scaleDown.playFromStart());

        // Set button action
        exitButton.setOnAction(e -> {
            soundManager.stopBackgroundMusic();
            stage.close();
        });

        return exitButton;
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
        Button audioSettingsButton = createStyledButton("Audio Settings", e -> showAudioSettingsOverlay());
        Button exitButton = createStyledButton("Exit", e -> System.exit(0));

        VBox panel = new VBox(20, title, playButton, instructionsButton, audioSettingsButton, exitButton);
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
     * @return Button instance with hover effects.
     */
    private Button createStyledButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button(text);
        button.getStyleClass().add("menu-button");
        button.setOnAction(action);

        // Create ScaleTransitions for hover effect
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), button);
        scaleUp.setToX(1.2); // Scale to 120%
        scaleUp.setToY(1.2);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), button);
        scaleDown.setToX(1.0); // Scale back to original size
        scaleDown.setToY(1.0);

        // Add hover event handlers
        button.setOnMouseEntered(e -> {
            scaleDown.stop(); // Stop any ongoing scaleDown animation
            scaleUp.playFromStart(); // Play scaleUp animation immediately
        });

        button.setOnMouseExited(e -> {
            scaleUp.stop(); // Stop any ongoing scaleUp animation
            scaleDown.playFromStart(); // Play scaleDown animation immediately
        });

        return button;
    }


    /**
     * Launches the game by notifying the controller.
     */
    private void launchGame() {
        try {
            soundManager.stopBackgroundMusic();
            controller.launchGame();
        } catch (Exception ex) {
            logger.severe("Error launching game: " + ex.getMessage());
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

    private void showAudioSettingsOverlay() {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);"); // Semi-transparent darker background
        overlay.setAlignment(Pos.CENTER);

        VBox container = new VBox(20); // Adjust spacing for a balanced look
        container.setStyle("-fx-background-color: #ffffff; -fx-padding: 30; -fx-border-color: #3498db; -fx-border-width: 3; -fx-border-radius: 15; -fx-background-radius: 15;");
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(500);
        container.setMaxHeight(300); // Refined size

        // Title Text
        Text title = new Text("Audio Settings");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: #34495e; -fx-font-family: 'Verdana';");

        // Mute Background Music Button with Icon
        ImageView musicIcon = new ImageView(soundManager.isBackgroundMusicMuted() ? unmuteMusicImage : new Image(getResourceOrThrow("/com/example/demo/images/music.png").toExternalForm()));
        musicIcon.setFitHeight(30);
        musicIcon.setFitWidth(30);

        Button muteBackgroundMusicButton = new Button(
                soundManager.isBackgroundMusicMuted() ? "Unmute Background Music" : "Mute Background Music",
                musicIcon
        );

        muteBackgroundMusicButton.setOnMouseEntered(e -> {
            muteBackgroundMusicButton.setScaleX(1.1);
            muteBackgroundMusicButton.setScaleY(1.1);
        });
        muteBackgroundMusicButton.setOnMouseExited(e -> {
            muteBackgroundMusicButton.setScaleX(1.0);
            muteBackgroundMusicButton.setScaleY(1.0);
        });
        muteBackgroundMusicButton.setStyle("-fx-background-color: #74b9ff; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 15; -fx-background-radius: 15;");
        muteBackgroundMusicButton.setOnAction(e -> {
            if (soundManager.isBackgroundMusicMuted()) {
                soundManager.unmuteBackgroundMusic(SoundManager.MENU_MUSIC);
                muteBackgroundMusicButton.setText("Mute Background Music");
                musicIcon.setImage(new Image(getResourceOrThrow("/com/example/demo/images/music.png").toExternalForm()));
                soundManager.playBackgroundMusic(SoundManager.MENU_MUSIC);
            } else {
                soundManager.muteBackgroundMusic();
                muteBackgroundMusicButton.setText("Unmute Background Music");
                musicIcon.setImage(unmuteMusicImage);
                soundManager.stopBackgroundMusic();
            }
        });

        // Mute Sound Effects Button with Icon
        ImageView speakerIcon = new ImageView(soundManager.isSoundEffectsMuted() ? unmuteSoundImage : new Image(getResourceOrThrow("/com/example/demo/images/speaker.png").toExternalForm()));
        speakerIcon.setFitHeight(30);
        speakerIcon.setFitWidth(30);

        Button muteSoundEffectsButton = new Button(
                soundManager.isSoundEffectsMuted() ? "Unmute Sound Effects" : "Mute Sound Effects",
                speakerIcon
        );

        muteSoundEffectsButton.setOnMouseEntered(e -> {
            muteSoundEffectsButton.setScaleX(1.1);
            muteSoundEffectsButton.setScaleY(1.1);
        });
        muteSoundEffectsButton.setOnMouseExited(e -> {
            muteSoundEffectsButton.setScaleX(1.0);
            muteSoundEffectsButton.setScaleY(1.0);
        });
        muteSoundEffectsButton.setStyle("-fx-background-color: #ff69b4; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 15; -fx-background-radius: 15;");
        muteSoundEffectsButton.setOnAction(e -> {
            if (soundManager.isSoundEffectsMuted()) {
                soundManager.unmuteSoundEffects();
                muteSoundEffectsButton.setText("Mute Sound Effects");
                speakerIcon.setImage(new Image(getResourceOrThrow("/com/example/demo/images/speaker.png").toExternalForm()));
            } else {
                soundManager.muteSoundEffects();
                muteSoundEffectsButton.setText("Unmute Sound Effects");
                speakerIcon.setImage(unmuteSoundImage);
            }
        });

        // Close Button
        Button closeButton = new Button("Close");
        closeButton.setOnMouseEntered(e -> {
            closeButton.setScaleX(1.1);
            closeButton.setScaleY(1.1);
        });
        closeButton.setOnMouseExited(e -> {
            closeButton.setScaleX(1.0);
            closeButton.setScaleY(1.0);
        });
        closeButton.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 15; -fx-background-radius: 15;");
        closeButton.setOnAction(e -> overlay.setVisible(false));

        // Add elements to the container
        container.getChildren().addAll(title, muteBackgroundMusicButton, muteSoundEffectsButton, closeButton);
        overlay.getChildren().add(container);

        // Add overlay to the root
        StackPane root = (StackPane) stage.getScene().getRoot();
        if (!root.getChildren().contains(overlay)) {
            root.getChildren().add(overlay);
        }
        overlay.setVisible(true);
    }

    /**
     * Retrieves a resource or throws an exception if not found.
     *
     * @param resourcePath The resource path.
     * @return The resource URL.
     */
    public static URL getResourceOrThrow(String resourcePath) {
        URL resourceUrl = MenuView.class.getResource(resourcePath);
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        return resourceUrl;
    }
}
