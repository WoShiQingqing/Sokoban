package sokoban.controller;

import java.util.List;

import sokoban.MainApp;
import sokoban.model.GameMap;
import sokoban.model.Level;

// Coordinates the current level and basic game page actions
public class GameController {

    private final MainApp mainApp;
    private final List<Level> levels;
    private int currentLevelIndex;
    private GameMap gameMap;

    public GameController(MainApp mainApp, List<Level> levels, int levelIndex) {
        this.mainApp = mainApp;
        this.levels = levels;
        this.currentLevelIndex = levelIndex;
        // Build the current level scaffold right away
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
        // Move straight to the next level without changing the public API later
        currentLevelIndex++;
        gameMap = levels.get(currentLevelIndex).createGameMap();
        return true;
    }

    public void restartCurrentLevel() {
        gameMap = levels.get(currentLevelIndex).createGameMap();
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
