package sokoban.model;

import sokoban.entity.Box;
import sokoban.entity.Player;
import sokoban.entity.Target;
import sokoban.entity.Wall;

import java.util.ArrayList;
import java.util.List;

// Basic map state for one Sokoban level
public class GameMap {

    private final Level sourceLevel;
    private final int width;
    private final int height;
    private final List<String> layoutRows;
    private int moveCount;
    private GameState gameState;

    // 游戏实体集合
    private List<Wall> walls;
    private List<Target> targets;
    private List<Box> boxes;
    private Player player;

    // Initial snapshot, used for resetting to restore the starting state
    private Position playerStartPos;
    private List<Position> boxStartPositions;

    public GameMap(Level level) {
        this.sourceLevel = level;
        this.width = level.getWidth();
        this.height = level.getHeight();
        this.layoutRows = level.getRows();
        this.gameState = GameState.READY;

        // Initialize the entity container and parse the map
        walls = new ArrayList<>();
        targets = new ArrayList<>();
        boxes = new ArrayList<>();
        boxStartPositions = new ArrayList<>();
        parseLevelLayout();
        saveInitialState();
    }

    // Core: Parse level text # . $ @ * +
    private void parseLevelLayout() {
        for (int row = 0; row < layoutRows.size(); row++) {
            String line = layoutRows.get(row);
            for (int col = 0; col < line.length(); col++) {
                char symbol = line.charAt(col);
                Position pos = new Position(row, col);
                switch (symbol) {
                    case '#' -> walls.add(new Wall(pos));
                    case '@' -> player = new Player(pos);
                    case '$' -> boxes.add(new Box(pos));
                    case '.' -> targets.add(new Target(pos));
                    case '*' -> { // The box is at the target point
                        targets.add(new Target(pos));
                        boxes.add(new Box(pos));
                    }
                    case '+' -> { // The player is at the target point
                        targets.add(new Target(pos));
                        player = new Player(pos);
                    }
                }
            }
        }
    }

    // Save the initial starting coordinates and restore them upon reset
    private void saveInitialState() {
        playerStartPos = new Position(player.getRow(), player.getColumn());
        boxStartPositions.clear();
        for (Box b : boxes) {
            boxStartPositions.add(new Position(b.getRow(), b.getColumn()));
        }
    }

    // Main logic for player movement; returning true means the movement is valid
    public boolean movePlayer(Direction direction) {
        markPlaying();
        Position playerNextPos = player.getPosition().translate(direction);
        // 1. A wall is ahead, movement is blocked
        if (isWallAt(playerNextPos)) {
            return false;
        }

        // 2. Determine whether there is a box ahead
        Box hitBox = getBoxAtPosition(playerNextPos);
        if (hitBox != null) {
            // Next coordinate of the box
            Position boxNextPos = playerNextPos.translate(direction);
            // If there is a wall or another box in front of the box → it cannot be pushed
            if (isWallAt(boxNextPos) || getBoxAtPosition(boxNextPos) != null) {
                return false;
            }
            // Push the box
            hitBox.setPosition(boxNextPos);
        }

        // 3. Execute player movement and add 1 to step count
        player.setPosition(playerNextPos);
        incrementMoveCount();

        // 4. Detect whether the level is cleared after moving
        if (isCompleted()) {
            markCompleted();
        }
        return true;
    }

    // Reset the level to its initial state
    public void reset() {
        moveCount = 0;
        gameState = GameState.READY;
        // Restore the player's initial position
        player.setPosition(playerStartPos);
        // Restore the initial coordinates of all boxes
        for (int i = 0; i < boxes.size(); i++) {
            boxes.get(i).setPosition(boxStartPositions.get(i));
        }
    }

    // Determine whether all boxes are at the target points → Clear the level
    public boolean isCompleted() {
        int matchCount = 0;
        List<Position> targetPositions = targets.stream()
                .map(t -> t.getPosition())
                .toList();
        for (Box box : boxes) {
            if (targetPositions.contains(box.getPosition())) {
                matchCount++;
            }
        }
        return matchCount == targets.size();
    }

    // Assistant: Is this coordinate a wall?
    public boolean isWallAt(Position pos) {
        for (Wall w : walls) {
            if (w.getPosition().equals(pos)) {
                return true;
            }
        }
        return false;
    }

    // Helper: Retrieve the crate at the specified coordinates; return null if none exists
    public Box getBoxAtPosition(Position pos) {
        for (Box b : boxes) {
            if (b.getPosition().equals(pos)) {
                return b;
            }
        }
        return null;
    }

    // External getter for rendering by GameView
    public List<Wall> getWalls() {
        return walls;
    }

    public List<Target> getTargets() {
        return targets;
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public Player getPlayer() {
        return player;
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
