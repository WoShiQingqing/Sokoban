package sokoban.entity;

import sokoban.model.Position;

// Target represents the destination where a box should be pushed
public class Target extends GameObject {

    public Target(Position position) {
        super(position);
    }

    @Override
    public String getSymbol() {
        return ".";
    }

    @Override
    public boolean isPassable() {
        return true;
    }

    @Override
    public boolean isTarget() {
        return true;
    }
}
