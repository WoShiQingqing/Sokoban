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

// Instructions scene
public class HowToPlayView {

    private final Scene scene;

    public HowToPlayView(MainApp mainApp) {
        Label title = new Label("How To Play");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        // Keep the instructions basic for now; E can polish the wording later
        Label instructions = new Label(String.join("\n",
            "Goal:",
            "Push every box onto a target tile.",
            "",
            "Controls:",
            "- Use WASD or Arrow Keys to move",
            "- Press R to reset the current level",
            "- Press Esc to return to the main menu",
            "",
            "Rules:",
            "- You can only push one box at a time",
            "- Boxes cannot move through walls or other boxes"));
        instructions.setWrapText(true);
        instructions.setStyle("-fx-font-size: 15px; -fx-text-fill: #1E293B;");

        Button backButton = new Button("Back");
        backButton.setPrefWidth(180);
        backButton.setOnAction(event -> mainApp.switchToMenu());

        VBox content = new VBox(20, title, instructions, backButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        content.setMaxWidth(500);

        BorderPane root = new BorderPane();
        root.setCenter(content);
        root.setStyle("-fx-background-color: #F8FAFC;");

        this.scene = new Scene(root, MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
    }

    public Scene getScene() {
        return scene;
    }
}
