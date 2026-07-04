package sokoban.entity;

import sokoban.model.Position;

// Player represents the current position controlled by the user
public class Player extends GameObject {

    public Player(Position position) {
        super(position);
    }

    @Override
    public String getSymbol() {
        return "@";
    }

    public void moveTo(Position newPosition) {
        setPosition(newPosition);
    }
}
