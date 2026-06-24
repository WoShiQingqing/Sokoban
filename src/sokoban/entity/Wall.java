package sokoban.entity;

import sokoban.model.Position;

public class Wall extends GameObject {

    public Wall(Position position) {
        super(position);
    }

    @Override
    public String getLabel() {
        return "#";
    }
}
