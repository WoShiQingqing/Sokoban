package sokoban.entity;

import sokoban.model.Position;

// Base class for all objects shown on the Sokoban board
public abstract class GameObject {

    private Position position;

    protected GameObject(Position position) {
        this.position = position;
    }

    // Return the current position of this object
    public Position getPosition() {
        return position;
    }

    // Update the position of this object
    public void setPosition(Position position) {
        this.position = position;
    }

    // Return the row position of this object
    public int getRow() {
        return position.getRow();
    }

    // Return the column position of this object
    public int getColumn() {
        return position.getColumn();
    }

    // Return the symbol used to represent this object
    public abstract String getSymbol();

    // Keep getLabel for compatibility with other parts of the program
    public String getLabel() {
        return getSymbol();
    }

    // Whether the player can walk through this object
    public boolean isPassable() {
        return false;
    }

    // Whether this object can be pushed
    public boolean isPushable() {
        return false;
    }

    // Whether this object is a target point
    public boolean isTarget() {
        return false;
    }
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
