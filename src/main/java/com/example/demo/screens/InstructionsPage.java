package com.example.demo.screens;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The {@code InstructionsPage} class is responsible for displaying an instructions overlay
 * that guides users on how to play the game and includes control settings.
 * This class encapsulates the creation and behavior of the instructions page.
 */
public class InstructionsPage {

    private final Stage stage;
    private final Image instructionsImage;
    private final Image settingsImage;

    /**
     * Constructs an {@code InstructionsPage} with the specified stage and images.
     *
     * @param stage             the primary {@link Stage} where the overlay will be displayed
     * @param instructionsImage the {@link Image} used for the "How to Play" page icon
     * @param settingsImage     the {@link Image} used for the "Control Keys" page icon
     */
    public InstructionsPage(Stage stage, Image instructionsImage, Image settingsImage) {
        this.stage = stage;
        this.instructionsImage = instructionsImage;
        this.settingsImage = settingsImage;
    }

    /**
     * Displays the instructions overlay on the current stage.
     * The overlay includes two pages: "How to Play" and "Control Keys",
     * along with navigation buttons to switch between the pages.
     */
    public void show() {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        overlay.setAlignment(Pos.CENTER);

        VBox container = new VBox(20);
        container.setStyle("-fx-background-color: #dff9fb; -fx-padding: 20; -fx-border-color: #badc58; -fx-border-width: 3; -fx-background-radius: 10;");
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(500);
        container.setMaxHeight(400);

        VBox howToPlayPage = createHowToPlayPage();
        VBox settingsPage = createSettingsPage();

        StackPane pages = new StackPane(howToPlayPage, settingsPage);
        settingsPage.setVisible(false);

        HBox firstPageButtons = createNavigationButtons(howToPlayPage, settingsPage, overlay, true);
        HBox secondPageButtons = createNavigationButtons(howToPlayPage, settingsPage, overlay, false);

        howToPlayPage.getChildren().add(firstPageButtons);
        settingsPage.getChildren().add(secondPageButtons);

        container.getChildren().addAll(pages);
        overlay.getChildren().add(container);

        StackPane root = (StackPane) stage.getScene().getRoot();
        if (!root.getChildren().contains(overlay)) {
            root.getChildren().add(overlay);
        }
        overlay.setVisible(true);
    }

    /**
     * Creates the "How to Play" page with instructions for playing the game.
     *
     * @return a {@link VBox} containing the "How to Play" page layout
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
     * Creates the "Control Keys" page with information about game controls.
     *
     * @return a {@link VBox} containing the "Control Keys" page layout
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
     * Creates navigation buttons for switching between the pages in the instructions overlay.
     *
     * @param howToPlayPage the "How to Play" page
     * @param settingsPage  the "Control Keys" page
     * @param overlay       the overlay to close when needed
     * @param isFirstPage   {@code true} if the buttons are for the first page, {@code false} otherwise
     * @return an {@link HBox} containing the navigation buttons
     */
    private HBox createNavigationButtons(VBox howToPlayPage, VBox settingsPage, StackPane overlay, boolean isFirstPage) {
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);

        if (isFirstPage) {
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
}
