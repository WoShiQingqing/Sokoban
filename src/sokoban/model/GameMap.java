package sokoban.model;

import java.util.List;

// Basic map state for one Sokoban level
public class GameMap {

    private final Level sourceLevel;
    private final int width;
    private final int height;
    private final List<String> layoutRows;
    private int moveCount;
    private GameState gameState;

    public GameMap(Level level) {
        this.sourceLevel = level;
        this.width = level.getWidth();
        this.height = level.getHeight();
        // Keep the raw map text around because C will build logic from it
        this.layoutRows = level.getRows();
        this.gameState = GameState.READY;
    }

    public boolean movePlayer(Direction direction) {
        // TODO for gameplay logic: move the player, push boxes, and update state
        return false;
    }

    public void reset() {
        // TODO for gameplay logic: restore player and box positions too
        moveCount = 0;
        gameState = GameState.READY;
    }

    public boolean isCompleted() {
        return gameState == GameState.COMPLETED;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Level getSourceLevel() {
        return sourceLevel;
    }

    public List<String> getLayoutRows() {
        return layoutRows;
    }

    public void markPlaying() {
        gameState = GameState.PLAYING;
    }

    public void markCompleted() {
        gameState = GameState.COMPLETED;
    }

    public void incrementMoveCount() {
        moveCount++;
    }
}
