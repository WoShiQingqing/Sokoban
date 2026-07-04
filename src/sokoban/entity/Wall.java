package sokoban.entity;

import sokoban.model.Position;

// Wall represents a blocked area on the game board
public class Wall extends GameObject {

    public Wall(Position position) {
        super(position);
    }

    @Override
    public String getSymbol() {
        return "#";
    }

    @Override
    public boolean isPassable() {
        return false;
    }
}
