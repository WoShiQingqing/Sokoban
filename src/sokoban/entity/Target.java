package sokoban.entity;

import sokoban.model.Position;

public class Target extends GameObject {

    public Target(Position position) {
        super(position);
    }

    @Override
    public String getLabel() {
        return ".";
    }
}
