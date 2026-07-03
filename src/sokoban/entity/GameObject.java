package sokoban.entity;

import sokoban.model.Position;

// Base type for objects represented on the board
public abstract class GameObject {

    private final Position position;

    protected GameObject(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public abstract String getLabel();
}
// Newly added: Coordinate modification, required for box pushing and level resetting
    public void setPosition(Position position) {
        this.position = position;
    }

    // Quickly get rows and columns to simplify GameMap code
    public int getRow() {
        return position.getRow();
    }
    public int getColumn() {
        return position.getColumn();
    }

    public abstract String getLabel();
}
