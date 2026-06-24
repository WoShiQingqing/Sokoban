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
