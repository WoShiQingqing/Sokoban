package sokoban.entity;

import sokoban.model.Position;

// Box represents the object that can be pushed by the player
public class Box extends GameObject {

    public Box(Position position) {
        super(position);
    }

    @Override
    public String getSymbol() {
        return "$";
    }

    @Override
    public boolean isPushable() {
        return true;
    }
}
