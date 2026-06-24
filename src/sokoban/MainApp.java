package sokoban;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sokoban.model.Level;
import sokoban.util.LevelLoader;
import sokoban.view.GameView;
import sokoban.view.HowToPlayView;
import sokoban.view.LevelSelectView;
import sokoban.view.MenuView;

// Main entry point and scene switcher
public class MainApp extends Application {

    public static final int WINDOW_WIDTH = 900;
    public static final int WINDOW_HEIGHT = 700;

    private Stage primaryStage;
    private List<Level> levels;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        // Load levels once here so page switches stay simple
        this.levels = LevelLoader.loadLevels();
        this.primaryStage.setTitle("Sokoban Puzzle Game");
        this.primaryStage.setMinWidth(WINDOW_WIDTH);
        this.primaryStage.setMinHeight(WINDOW_HEIGHT);
        // Start from the menu so the app flow stays predictable
        switchToMenu();
        this.primaryStage.show();
    }

    public void switchToMenu() {
        // The menu is the default landing page
        MenuView menuView = new MenuView(this);
        setScene(menuView.getScene());
    }

    public void switchToLevelSelect() {
        // Level select needs the full level list for button labels
        LevelSelectView levelSelectView = new LevelSelectView(this, levels);
        setScene(levelSelectView.getScene());
    }

    public void switchToHowToPlay() {
        HowToPlayView howToPlayView = new HowToPlayView(this);
        setScene(howToPlayView.getScene());
    }

    public void switchToGame(int levelIndex) {
        // The game page only needs the chosen level index
        GameView gameView = new GameView(this, levels, levelIndex);
        setScene(gameView.getScene());
        gameView.requestFocusForGame();
    }

    public List<Level> getLevels() {
        return levels;
    }

    private void setScene(Scene scene) {
        // Keep the actual page switch in one place
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
