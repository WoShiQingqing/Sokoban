package sokoban.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sokoban.MainApp;
import sokoban.controller.MenuController;

// Main menu scene
public class MenuView {

    private final Scene scene;

    public MenuView(MainApp mainApp) {
        // The menu only handles button clicks; routing stays in the controller
        MenuController controller = new MenuController(mainApp);

        Label title = new Label("Sokoban Puzzle Game");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 34));

        Button startButton = createMainButton("Start Game");
        startButton.setOnAction(event -> controller.openLevelSelect());

        Button howToPlayButton = createMainButton("How To Play");
        howToPlayButton.setOnAction(event -> controller.openHowToPlay());

        Button exitButton = createMainButton("Exit");
        exitButton.setOnAction(event -> controller.exitGame());

        VBox content = new VBox(18, title, startButton, howToPlayButton, exitButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        BorderPane root = new BorderPane();
        root.setCenter(content);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #F8FAFC, #DBEAFE);");

        this.scene = new Scene(root, MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
    }

    public Scene getScene() {
        return scene;
    }

    private Button createMainButton(String text) {
        // Keep button styling in one place so D can tweak it fast
        Button button = new Button(text);
        button.setPrefWidth(240);
        button.setPrefHeight(48);
        button.setStyle(
            "-fx-background-color: #1D4ED8;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 15px;"
                + "-fx-background-radius: 8px;");
        return button;
    }
}
