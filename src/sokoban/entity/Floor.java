package sokoban.entity;

import sokoban.model.Position;

// Floor represents an empty space where the player can move
public class Floor extends GameObject {

    public Floor(Position position) {
        super(position);
    }

    @Override
    public String getSymbol() {
        return " ";
    }

    @Override
    public boolean isPassable() {
        return true;
    }
}
