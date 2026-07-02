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
import sokoban.model.Direction; // 依赖 C 同学的 Direction
import sokoban.model.GameMap;
import sokoban.model.Level;

// 游戏核心页面：已将文本预览替换为真实的 2D 棋盘渲染
public class GameView {

    private final GameController controller;
    private final BorderPane root;
    private final Label levelLabel;
    private final Label statusLabel;
    private final Label stepCountLabel; // 新增：用于显示步数
    private final GridPane boardGrid;   // 新增：真正的棋盘，替换了原先的 TextArea
    private final Scene scene;
    private final Button nextButton;    // 提出来作为成员变量，方便通关后改变状态

    public GameView(MainApp mainApp, List<Level> levels, int levelIndex) {
        // 保持 A 同学的控制器接线方式
        this.controller = new GameController(mainApp, levels, levelIndex);
        this.root = new BorderPane();
        this.levelLabel = new Label();
        this.statusLabel = new Label();
        this.stepCountLabel = new Label();
        this.boardGrid = new GridPane();
        this.nextButton = new Button("Next Level");

        configureLayout();
        setupKeyListener(); // 添加键盘监听
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

        // 设置棋盘样式
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setStyle("-fx-background-color: #333333; -fx-padding: 10; -fx-background-radius: 8;");

        VBox center = new VBox(16, boardGrid); // 用 boardGrid 替换了 levelPreviewArea
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
                // A 同学的 Controller 会处理页面跳转，这里不需要额外操作
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
            // 如果已经通关，不再响应方向键移动
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
                    controller.restartCurrentLevel(); // 快捷键 R 重置
                    break;
                case ESCAPE:
                    controller.goBackToMenu(); // 快捷键 ESC 返回
                    return; // 返回主菜单后直接退出，避免调用 refresh 报错
                default:
                    return; // 按下其他无关按键直接返回
            }
            refresh(); // 每次移动后刷新界面
        });
    }

    private void refresh() {
        GameMap gameMap = controller.getGameMap();
        if (gameMap == null) return;

        // 更新顶部标签
        levelLabel.setText("Level " + controller.getCurrentLevelNumber() + " / " + controller.getTotalLevels()
            + " - " + controller.getLevelName());
        stepCountLabel.setText("Steps: " + gameMap.getStepCount());

        if (gameMap.isCompleted()) {
            statusLabel.setText("Status: COMPLETED!");
            statusLabel.setStyle("-fx-text-fill: #16A34A; -fx-font-size: 16px; -fx-font-weight: bold;");
            // 通关时，高亮下一关按钮（如果有的话）
            if (controller.hasNextLevel()) {
                nextButton.setStyle("-fx-background-color: #10B981; -fx-text-fill: white;");
            }
        } else {
            statusLabel.setText("Status: Playing");
            statusLabel.setStyle("-fx-text-fill: #334155; -fx-font-size: 14px;");
            nextButton.setStyle(""); // 恢复默认样式
        }

        // 清空并重新绘制棋盘
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

    // 根据不同对象的符号，使用 JavaFX 节点画出对应的方块/图形
    private StackPane createCellNode(char symbol) {
        StackPane cell = new StackPane();
        cell.setPrefSize(40, 40);
        
        // 基础地板
        Rectangle floor = new Rectangle(40, 40, Color.web("#CBD5E1")); 
        floor.setStroke(Color.web("#94A3B8"));
        cell.getChildren().add(floor);
        
        switch (symbol) {
            case '#': // 墙
                Rectangle wall = new Rectangle(40, 40, Color.web("#475569"));
                cell.getChildren().add(wall);
                break;
            case '.': // 目标点
                Circle target = new Circle(6, Color.web("#EF4444"));
                cell.getChildren().add(target);
                break;
            case '$': // 箱子
                Rectangle box = new Rectangle(30, 30, Color.web("#D97706"));
                box.setArcWidth(5); box.setArcHeight(5); // 圆角箱子
                cell.getChildren().add(box);
                break;
            case '*': // 已经在目标点上的箱子
                Rectangle doneBox = new Rectangle(30, 30, Color.web("#10B981")); // 绿色代表到位
                doneBox.setArcWidth(5); doneBox.setArcHeight(5);
                cell.getChildren().add(doneBox);
                break;
            case '@': // 玩家
                Circle player = new Circle(14, Color.web("#3B82F6")); 
                cell.getChildren().add(player);
                break;
            case '+': // 站在目标点上的玩家
                Circle targetSpot = new Circle(6, Color.web("#EF4444"));
                Circle playerOnTarget = new Circle(14, Color.web("#3B82F6"));
                playerOnTarget.setOpacity(0.8);
                cell.getChildren().addAll(targetSpot, playerOnTarget);
                break;
            case ' ': // 纯地板
            default:
                break;
        }
        return cell;
    }
}
