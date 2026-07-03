package sokoban.model;
import java.util.ArrayList;
import java.util.List;

// Immutable level data loaded from text resources
public class Level {
    private final String name;
    private final String difficulty; //difficulty attribute
    private final List<String> rows;

    // Modified constructor to include the difficulty parameter
    public Level(String name, String difficulty, List<String> rows) {
        this.name = name;
        this.difficulty = difficulty;
        this.rows = List.copyOf(rows);
    }

    public String getName() {
        return name;
    }

    // getter for difficulty
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
        // New run or restart always starts from the original level template
        return new GameMap(this);
    }
    
    // Modified static factory method to include a default difficulty parameter (mainly for Fallback levels)
    public static Level of(String name, String difficulty, String... rows) {
        List<String> values = new ArrayList<>();
        for (String row : rows) {
            values.add(row);
        }
        return new Level(name, difficulty, values);
    }
}
