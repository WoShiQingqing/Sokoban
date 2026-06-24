package sokoban.model;

import java.util.Objects;

// Immutable row and column pair used by the game grid
public final class Position {

    private final int row;
    private final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Position translate(Direction direction) {
        // Keep coordinate shifts here instead of hand-writing them elsewhere
        return new Position(row + direction.rowDelta(), column + direction.columnDelta());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Position position)) {
            return false;
        }
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
