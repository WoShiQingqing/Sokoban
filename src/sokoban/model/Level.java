package sokoban.model;

import java.util.ArrayList;
import java.util.List;

// Immutable level data loaded from a text file or fallback definition
public class Level {

    private final String name;
    private final String difficulty;
    private final List<String> rows;

    public Level(String name, String difficulty, List<String> rows) {
        this.name = name;
        this.difficulty = difficulty;
        this.rows = List.copyOf(rows);
    }

    public String getName() {
        return name;
    }

    public String getDifficulty() {
        return difficulty;
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
        // Build a fresh map each time so restart does not reuse old positions
        return new GameMap(this);
    }

    public static Level of(String name, String difficulty, String... rows) {
        List<String> values = new ArrayList<>();
        for (String row : rows) {
            values.add(row);
        }
        return new Level(name, difficulty, values);
    }
}
