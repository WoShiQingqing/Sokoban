package sokoban.view;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

// Main game page that draws the board and sends player input to the controller
public class GameView {
    private final GameController controller;
    private final BorderPane root;
    private final Label levelLabel;
    private final Label statusLabel;
    private final Label stepCountLabel; 
    private final GridPane boardGrid;   
    private final Scene scene;
    private final Button nextButton;    
    
    public GameView(MainApp mainApp, List<Level> levels, int levelIndex) {
        this.controller = new GameController(mainApp, levels, levelIndex);
        this.root = new BorderPane();
        this.levelLabel = new Label();
        this.statusLabel = new Label();
        this.stepCountLabel = new Label();
        this.boardGrid = new GridPane();
        this.nextButton = new Button("Next Level");
        
        configureLayout();
        setupKeyListener();
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
        
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setStyle("-fx-background-color: #333333; -fx-padding: 10; -fx-background-radius: 8;");
        
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
        nextButton.setOnAction(event -> openNextLevel());
        
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
            KeyCode code = event.getCode();
            boolean moved = false;

            switch (code) {
                case UP:
                case W:
                    moved = controller.move(Direction.UP);
                    break;
                case DOWN:
                case S:
                    moved = controller.move(Direction.DOWN);
                    break;
                case LEFT:
                case A:
                    moved = controller.move(Direction.LEFT);
                    break;
                case RIGHT:
                case D:
                    moved = controller.move(Direction.RIGHT);
                    break;
                case R:
                    controller.restartCurrentLevel();
                    break;
                case ESCAPE:
                    controller.goBackToMenu();
                    return;
                default:
                    return;
            }

            refresh();

            GameMap gameMap = controller.getGameMap();
            if (moved && gameMap != null && gameMap.isCompleted()) {
                showLevelCompletedAlert();
            }
        });
    }

    // Show a simple completion dialog after the player solves the current level
    private void showLevelCompletedAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Level Completed!");
        alert.setHeaderText("Congratulations!");
        alert.setContentText("You have successfully completed this level!");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                requestFocusForGame();
            }
        });
    }

    private void openNextLevel() {
        if (controller.openNextLevel()) {
            refresh();
            requestFocusForGame();
        }
    }

    private void refresh() {
        GameMap gameMap = controller.getGameMap();
        if (gameMap == null) return;
        
        levelLabel.setText("Level " + controller.getCurrentLevelNumber() + " / " + controller.getTotalLevels()
            + " - " + controller.getLevelName());
        
        stepCountLabel.setText("Steps: " + gameMap.getMoveCount());
        
        if (gameMap.isCompleted()) {
            statusLabel.setText("Status: COMPLETED!");
            statusLabel.setStyle("-fx-text-fill: #16A34A; -fx-font-size: 16px; -fx-font-weight: bold;");
            if (controller.hasNextLevel()) {
                nextButton.setStyle("-fx-background-color: #10B981; -fx-text-fill: white;");
            }
        } else {
            statusLabel.setText("Status: Playing");
            statusLabel.setStyle("-fx-text-fill: #334155; -fx-font-size: 14px;");
            nextButton.setStyle("");
        }
        nextButton.setDisable(!(gameMap.isCompleted() && controller.hasNextLevel()));
        
        boardGrid.getChildren().clear();
        
        int rows = gameMap.getHeight();
        int cols = gameMap.getWidth();
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char symbol = ' ';
                sokoban.model.Position pos = new sokoban.model.Position(r, c);
                
                if (gameMap.isWallAt(pos)) {
                    symbol = '#';
                } else if (gameMap.getBoxAtPosition(pos) != null) {
                    boolean onTarget = false;
                    for (sokoban.entity.Target t : gameMap.getTargets()) {
                        if (t.getPosition().equals(pos)) {
                            onTarget = true;
                            break;
                        }
                    }
                    symbol = onTarget ? '*' : '$';
                } else if (gameMap.getPlayer() != null && gameMap.getPlayer().getPosition().equals(pos)) {
                    boolean onTarget = false;
                    for (sokoban.entity.Target t : gameMap.getTargets()) {
                        if (t.getPosition().equals(pos)) {
                            onTarget = true;
                            break;
                        }
                    }
                    symbol = onTarget ? '+' : '@';
                } else {
                    for (sokoban.entity.Target t : gameMap.getTargets()) {
                        if (t.getPosition().equals(pos)) {
                            symbol = '.';
                            break;
                        }
                    }
                }
                
                StackPane cellNode = createCellNode(symbol);
                boardGrid.add(cellNode, c, r);
            }
        }
    }
    
    private StackPane createCellNode(char symbol) {
        StackPane cell = new StackPane();
        cell.setPrefSize(40, 40);
        
        Rectangle floor = new Rectangle(40, 40, Color.web("#CBD5E1"));
        floor.setStroke(Color.web("#94A3B8"));
        cell.getChildren().add(floor);
        
        switch (symbol) {
            case '#':
                Rectangle wall = new Rectangle(40, 40, Color.web("#475569"));
                cell.getChildren().add(wall);
                break;
            case '.':
                Circle target = new Circle(6, Color.web("#EF4444"));
                cell.getChildren().add(target);
                break;
            case '$':
                Rectangle box = new Rectangle(30, 30, Color.web("#D97706"));
                box.setArcWidth(5);
                box.setArcHeight(5);
                cell.getChildren().add(box);
                break;
            case '*':
                Rectangle doneBox = new Rectangle(30, 30, Color.web("#10B981"));
                doneBox.setArcWidth(5);
                doneBox.setArcHeight(5);
                cell.getChildren().add(doneBox);
                break;
            case '@':
                Circle player = new Circle(14, Color.web("#3B82F6"));
                cell.getChildren().add(player);
                break;
            case '+':
                Circle targetSpot = new Circle(6, Color.web("#EF4444"));
                Circle playerOnTarget = new Circle(14, Color.web("#3B82F6"));
                playerOnTarget.setOpacity(0.8);
                cell.getChildren().addAll(targetSpot, playerOnTarget);
                break;
            case ' ':
            default:
                break;
        }
        return cell;
    }
}
