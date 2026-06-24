package sokoban.model;

import java.util.ArrayList;
import java.util.List;

// Immutable level data loaded from text resources
public class Level {

    private final String name;
    private final List<String> rows;

    public Level(String name, List<String> rows) {
        this.name = name;
        this.rows = List.copyOf(rows);
    }

    public String getName() {
        return name;
    }

    public List<String> getRows() {
        return rows;
    }

    public int getHeight() {
        return rows.size();
    }

    public int getWidth() {
        int width = 0;
        for (String row : rows) {
            width = Math.max(width, row.length());
        }
        return width;
    }

    public GameMap createGameMap() {
        // New run or restart always starts from the original level template
        return new GameMap(this);
    }

    public static Level of(String name, String... rows) {
        List<String> values = new ArrayList<>();
        for (String row : rows) {
            values.add(row);
        }
        return new Level(name, values);
    }
}
