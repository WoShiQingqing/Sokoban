package sokoban.entity;

import sokoban.model.Position;

public class Player extends GameObject {

    public Player(Position position) {
        super(position);
    }

    @Override
    public String getLabel() {
        return "P";
    }
}
