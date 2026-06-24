package sokoban.view;

import java.util.List;
import java.util.stream.Collectors;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sokoban.MainApp;
import sokoban.controller.GameController;
import sokoban.model.GameMap;
import sokoban.model.Level;

// Temporary game page used to connect scene switching with level data
public class GameView {

    private final GameController controller;
    private final BorderPane root;
    private final Label levelLabel;
    private final Label statusLabel;
    private final TextArea levelPreviewArea;
    private final Scene scene;

    public GameView(MainApp mainApp, List<Level> levels, int levelIndex) {
        // Keep this wiring simple so gameplay and UI teammates can replace the middle later
        this.controller = new GameController(mainApp, levels, levelIndex);
        this.root = new BorderPane();
        this.levelLabel = new Label();
        this.statusLabel = new Label();
        this.levelPreviewArea = new TextArea();

        configureLayout();
        refresh();

        this.scene = new Scene(root, MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
    }

    public Scene getScene() {
        return scene;
    }

    public void requestFocusForGame() {
        root.requestFocus();
    }

    private void configureLayout() {
        Label title = new Label("Sokoban");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        levelLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 18));
        statusLabel.setStyle("-fx-text-fill: #334155; -fx-font-size: 14px;");

        VBox header = new VBox(8, title, levelLabel, statusLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(24, 20, 12, 20));

        levelPreviewArea.setEditable(false);
        levelPreviewArea.setFocusTraversable(false);
        levelPreviewArea.setPrefRowCount(10);
        levelPreviewArea.setMaxWidth(420);
        // Show the raw level text until the real board renderer is added
        levelPreviewArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 14px;");

        VBox center = new VBox(16, levelPreviewArea);
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(20));

        Button reloadButton = new Button("Reload Level");
        reloadButton.setOnAction(event -> {
            controller.restartCurrentLevel();
            refresh();
            requestFocusForGame();
        });

        Button nextButton = new Button("Next Level");
        nextButton.setDisable(!controller.hasNextLevel());
        nextButton.setOnAction(event -> {
            if (controller.openNextLevel()) {
                refresh();
                requestFocusForGame();
            }
        });

        Button menuButton = new Button("Back To Menu");
        menuButton.setOnAction(event -> controller.goBackToMenu());

        HBox controls = new HBox(12, reloadButton, nextButton, menuButton);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(12, 20, 24, 20));

        root.setTop(header);
        root.setCenter(center);
        root.setBottom(controls);
        root.setStyle("-fx-background-color: #F8FAFC;");
        root.setFocusTraversable(true);
    }

    private void refresh() {
        GameMap gameMap = controller.getGameMap();
        levelLabel.setText("Level " + controller.getCurrentLevelNumber() + " / " + controller.getTotalLevels()
            + " - " + controller.getLevelName());
        statusLabel.setText("Status: " + gameMap.getGameState());
        levelPreviewArea.setText(gameMap.getLayoutRows().stream().collect(Collectors.joining("\n")));
    }
}
