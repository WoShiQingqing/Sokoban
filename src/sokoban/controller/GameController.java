package sokoban.controller;

import java.util.List;
import sokoban.MainApp;
import sokoban.model.Direction;
import sokoban.model.GameMap;
import sokoban.model.Level;

public class GameController {

    private final MainApp mainApp;
    private final List<Level> levels;
    private int currentLevelIndex;
    private GameMap gameMap;

    public GameController(MainApp mainApp, List<Level> levels, int levelIndex) {
        this.mainApp = mainApp;
        this.levels = levels;
        this.currentLevelIndex = levelIndex;
        this.gameMap = levels.get(levelIndex).createGameMap();
    }

    public void resetLevel() {
        gameMap.reset();
    }

    public void goBackToMenu() {
        mainApp.switchToMenu();
    }

    public boolean hasNextLevel() {
        return currentLevelIndex + 1 < levels.size();
    }

    public boolean openNextLevel() {
        if (!hasNextLevel()) {
            return false;
        }
        currentLevelIndex++;
        gameMap = levels.get(currentLevelIndex).createGameMap();
        return true;
    }

    public void restartCurrentLevel() {
        gameMap = levels.get(currentLevelIndex).createGameMap();
    }

    public void move(Direction direction) {
        if (gameMap != null) {
            gameMap.movePlayer(direction);
        }
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public String getLevelName() {
        return levels.get(currentLevelIndex).getName();
    }

    public int getCurrentLevelNumber() {
        return currentLevelIndex + 1;
    }

    public int getTotalLevels() {
        return levels.size();
    }
}
