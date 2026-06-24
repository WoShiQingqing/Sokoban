package sokoban.view;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sokoban.MainApp;
import sokoban.controller.LevelSelectController;
import sokoban.model.Level;

// Level selection scene
public class LevelSelectView {

    private final Scene scene;

    public LevelSelectView(MainApp mainApp, List<Level> levels) {
        // This page only displays choices; the controller owns the jump
        LevelSelectController controller = new LevelSelectController(mainApp);

        Label title = new Label("Select A Level");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        FlowPane levelButtons = new FlowPane();
        levelButtons.setAlignment(Pos.CENTER);
        levelButtons.setHgap(16);
        levelButtons.setVgap(16);

        for (int index = 0; index < levels.size(); index++) {
            int levelIndex = index;
            Level level = levels.get(index);
            // Bind the level name and index here so the click handler stays tiny
            Button button = new Button((index + 1) + ". " + level.getName());
            button.setPrefSize(200, 60);
            button.setStyle(
                "-fx-background-color: #E2E8F0;"
                    + "-fx-font-size: 14px;"
                    + "-fx-background-radius: 10px;");
            button.setOnAction(event -> controller.openLevel(levelIndex));
            levelButtons.getChildren().add(button);
        }

        Button backButton = new Button("Back");
        backButton.setPrefWidth(180);
        backButton.setOnAction(event -> controller.goBack());

        VBox content = new VBox(24, title, levelButtons, backButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        BorderPane root = new BorderPane();
        root.setCenter(content);
        root.setStyle("-fx-background-color: #EFF6FF;");

        this.scene = new Scene(root, MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
    }

    public Scene getScene() {
        return scene;
    }
}
