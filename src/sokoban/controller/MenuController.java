package sokoban.controller;

import javafx.application.Platform;
import sokoban.MainApp;

// Handles main menu actions
public class MenuController {

    private final MainApp mainApp;

    public MenuController(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void openLevelSelect() {
        // Going into the game starts from level select
        mainApp.switchToLevelSelect();
    }

    public void openHowToPlay() {
        mainApp.switchToHowToPlay();
    }

    public void exitGame() {
        Platform.exit();
    }
}
