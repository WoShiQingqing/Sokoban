package sokoban.view;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sokoban.MainApp;
import sokoban.controller.GameController;
import sokoban.model.Direction;
import sokoban.model.GameMap;
import sokoban.model.Level;

// Core game page: Replaced the text preview with a real 2D board rendering
public class GameView {

    private final GameController controller;
    private final BorderPane root;
    private final Label levelLabel;
    private final Label statusLabel;
    private final Label stepCountLabel; // Added: To display the step count
    private final GridPane boardGrid;   // Added: The real game board, replacing the original TextArea
    private final Scene scene;
    private final Button nextButton;    // Extracted as a member variable to easily change its state after level completion

    public GameView(MainApp mainApp, List<Level> levels, int levelIndex) {
        // Keep Teammate A's controller wiring
        this.controller = new GameController(mainApp, levels, levelIndex);
        this.root = new BorderPane();
        this.levelLabel = new Label();
        this.statusLabel = new Label();
        this.stepCountLabel = new Label();
        this.boardGrid = new GridPane();
        this.nextButton = new Button("Next Level");

        configureLayout();
        setupKeyListener(); // Add keyboard listener
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
        stepCountLabel.setStyle("-fx-text-fill: #334155; -fx-font-size: 14px;");

        VBox header = new VBox(8, title, levelLabel, statusLabel, stepCountLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(24, 20, 12, 20));

        // Set board style
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setStyle("-fx-background-color: #333333; -fx-padding: 10; -fx-background-radius: 8;");

        // Replaced levelPreviewArea with boardGrid
        VBox center = new VBox(16, boardGrid); 
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(20));

        Button reloadButton = new Button("Reload Level");
        reloadButton.setOnAction(event -> {
            controller.restartCurrentLevel();
            refresh();
            requestFocusForGame();
        });

        nextButton.setDisable(!controller.hasNextLevel());
        nextButton.setOnAction(event -> {
            if (controller.openNextLevel()) {
                // Teammate A's Controller handles the page transition, no extra action needed here
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

    private void setupKeyListener() {
        root.setOnKeyPressed(event -> {
            GameMap gameMap = controller.getGameMap();
            // If the level is completed, stop responding to movement keys
            if (gameMap != null && gameMap.isCompleted()) {
                return;
            }

            KeyCode code = event.getCode();
            switch (code) {
                case UP:
                case W:
                    controller.move(Direction.UP);
                    break;
                case DOWN:
                case S:
                    controller.move(Direction.DOWN);
                    break;
                case LEFT:
                case A:
                    controller.move(Direction.LEFT);
                    break;
                case RIGHT:
                case D:
                    controller.move(Direction.RIGHT);
                    break;
                case R:
                    controller.restartCurrentLevel(); // Shortcut R to restart
                    break;
                case ESCAPE:
                    controller.goBackToMenu(); // Shortcut ESC to return
                    return; // Exit directly after returning to the main menu to avoid refresh errors
                default:
                    return; 
            }
            refresh(); // Refresh the UI after each move
        });
    }

    private void refresh() {
        GameMap gameMap = controller.getGameMap();
        if (gameMap == null) return;

        // Update top labels
        levelLabel.setText("Level " + controller.getCurrentLevelNumber() + " / " + controller.getTotalLevels()
            + " - " + controller.getLevelName());
        stepCountLabel.setText("Steps: " + gameMap.getStepCount());

        if (gameMap.isCompleted()) {
            statusLabel.setText("Status: COMPLETED!");
            statusLabel.setStyle("-fx-text-fill: #16A34A; -fx-font-size: 16px; -fx-font-weight: bold;");
            // Highlight the next level button upon completion (if available)
            if (controller.hasNextLevel()) {
                nextButton.setStyle("-fx-background-color: #10B981; -fx-text-fill: white;");
            }
        } else {
            statusLabel.setText("Status: Playing");
            statusLabel.setStyle("-fx-text-fill: #334155; -fx-font-size: 14px;");
            nextButton.setStyle(""); // Restore default style
        }

        // Clear and redraw the board
        boardGrid.getChildren().clear();
        
        int rows = gameMap.getRows();
        int cols = gameMap.getCols();
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char symbol = gameMap.getSymbolAt(r, c); 
                StackPane cellNode = createCellNode(symbol);
                boardGrid.add(cellNode, c, r);
            }
        }
    }

    // Draw the corresponding blocks/shapes using JavaFX nodes based on object symbols
    private StackPane createCellNode(char symbol) {
        StackPane cell = new StackPane();
        cell.setPrefSize(40, 40);
        
        // Base floor
        Rectangle floor = new Rectangle(40, 40, Color.web("#CBD5E1")); 
        floor.setStroke(Color.web("#94A3B8"));
        cell.getChildren().add(floor);
        
        switch (symbol) {
            case '#': // Wall
                Rectangle wall = new Rectangle(40, 40, Color.web("#475569"));
                cell.getChildren().add(wall);
                break;
            case '.': // Target
                Circle target = new Circle(6, Color.web("#EF4444"));
                cell.getChildren().add(target);
                break;
            case '$': // Box
                Rectangle box = new Rectangle(30, 30, Color.web("#D97706"));
                box.setArcWidth(5); box.setArcHeight(5); // Rounded corners for the box
                cell.getChildren().add(box);
                break;
            case '*': // Box already on the target
                Rectangle doneBox = new Rectangle(30, 30, Color.web("#10B981")); // Green indicates it's in place
                doneBox.setArcWidth(5); doneBox.setArcHeight(5);
                cell.getChildren().add(doneBox);
                break;
            case '@': // Player
                Circle player = new Circle(14, Color.web("#3B82F6")); 
                cell.getChildren().add(player);
                break;
            case '+': // Player standing on the target
                Circle targetSpot = new Circle(6, Color.web("#EF4444"));
                Circle playerOnTarget = new Circle(14, Color.web("#3B82F6"));
                playerOnTarget.setOpacity(0.8);
                cell.getChildren().addAll(targetSpot, playerOnTarget);
                break;
            case ' ': // Empty floor
            default:
                break;
        }
        return cell;
    }
}
