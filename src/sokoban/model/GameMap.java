package sokoban.model;

import java.util.ArrayList;
import java.util.List;

import sokoban.entity.Box;
import sokoban.entity.Player;
import sokoban.entity.Target;
import sokoban.entity.Wall;

// Stores the current objects and movement state for one Sokoban level
public class GameMap {

    private final Level sourceLevel;
    private final int width;
    private final int height;
    private final List<String> layoutRows;
    private int moveCount;
    private GameState gameState;

    private final List<Wall> walls;
    private final List<Target> targets;
    private final List<Box> boxes;
    private Player player;

    private Position playerStartPos;
    private final List<Position> boxStartPositions;

    public GameMap(Level level) {
        this.sourceLevel = level;
        this.width = level.getWidth();
        this.height = level.getHeight();
        this.layoutRows = level.getRows();
        this.gameState = GameState.READY;
        this.walls = new ArrayList<>();
        this.targets = new ArrayList<>();
        this.boxes = new ArrayList<>();
        this.boxStartPositions = new ArrayList<>();
        parseLevelLayout();
        saveInitialState();
    }

    private void parseLevelLayout() {
        for (int row = 0; row < layoutRows.size(); row++) {
            String line = layoutRows.get(row);
            for (int column = 0; column < line.length(); column++) {
                char symbol = line.charAt(column);
                Position position = new Position(row, column);
                switch (symbol) {
                    case '#' -> walls.add(new Wall(position));
                    case '@' -> player = new Player(position);
                    case '$' -> boxes.add(new Box(position));
                    case '.' -> targets.add(new Target(position));
                    case '*' -> {
                        targets.add(new Target(position));
                        boxes.add(new Box(position));
                    }
                    case '+' -> {
                        targets.add(new Target(position));
                        player = new Player(position);
                    }
                    default -> {
                        // Spaces are normal floor tiles and do not need stored objects
                    }
                }
            }
        }
        if (player == null) {
            throw new IllegalArgumentException("Level must contain a player start position");
        }
    }

    private void saveInitialState() {
        // Store initial positions so reset can rebuild the same level state
        playerStartPos = new Position(player.getRow(), player.getColumn());
        boxStartPositions.clear();
        for (Box box : boxes) {
            boxStartPositions.add(new Position(box.getRow(), box.getColumn()));
        }
    }

    public boolean movePlayer(Direction direction) {
        if (direction == null || gameState == GameState.COMPLETED) {
            return false;
        }

        Position playerNextPos = player.getPosition().translate(direction);
        if (isWallAt(playerNextPos)) {
            return false;
        }

        Box hitBox = getBoxAtPosition(playerNextPos);
        if (hitBox != null) {
            Position boxNextPos = playerNextPos.translate(direction);
            if (isWallAt(boxNextPos) || getBoxAtPosition(boxNextPos) != null) {
                return false;
            }
            hitBox.setPosition(boxNextPos);
        }

        player.setPosition(playerNextPos);
        incrementMoveCount();
        markPlaying();
        if (isCompleted()) {
            markCompleted();
        }
        return true;
    }

    public void reset() {
        moveCount = 0;
        gameState = GameState.READY;
        player.setPosition(playerStartPos);
        for (int index = 0; index < boxes.size(); index++) {
            boxes.get(index).setPosition(boxStartPositions.get(index));
        }
    }

    public boolean isCompleted() {
        if (boxes.size() != targets.size()) {
            return false;
        }

        List<Position> targetPositions = targets.stream()
            .map(Target::getPosition)
            .toList();
        for (Box box : boxes) {
            if (!targetPositions.contains(box.getPosition())) {
                return false;
            }
        }
        return true;
    }

    public boolean isWallAt(Position position) {
        for (Wall wall : walls) {
            if (wall.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    public Box getBoxAtPosition(Position position) {
        for (Box box : boxes) {
            if (box.getPosition().equals(position)) {
                return box;
            }
        }
        return null;
    }

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
