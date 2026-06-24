package sokoban.entity;

import sokoban.model.Position;

public class Floor extends GameObject {

    public Floor(Position position) {
        super(position);
    }

    @Override
    public String getLabel() {
        return " ";
    }
}
