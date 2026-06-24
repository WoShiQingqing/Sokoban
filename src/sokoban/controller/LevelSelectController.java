package sokoban.controller;

import sokoban.MainApp;

// Handles level selection actions
public class LevelSelectController {

    private final MainApp mainApp;

    public LevelSelectController(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void openLevel(int levelIndex) {
        // Pass the clicked level index straight to MainApp
        mainApp.switchToGame(levelIndex);
    }

    public void goBack() {
        mainApp.switchToMenu();
    }
}
