package sokoban.entity;

import sokoban.model.Position;

public class Box extends GameObject {

    public Box(Position position) {
        super(position);
    }

    @Override
    public String getLabel() {
        return "B";
    }
}
