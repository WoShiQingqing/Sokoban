package sokoban.model;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single, immutable level in the Sokoban game.
 * This class stores the essential data required to construct a game map,
 * including the level's name, difficulty setting, and layout structure.
 */
public class Level {
    private final String name;
    private final String difficulty; 
    private final List<String> rows;

    /**
     * Constructs a new Level instance.
     * 
     * @param name       The title of the level.
     * @param difficulty The difficulty classification (e.g., Easy, Medium, Hard).
     * @param rows       A list of strings representing the grid layout of the map.
     */
    public Level(String name, String difficulty, List<String> rows) {
        this.name = name;
        this.difficulty = difficulty;
        this.rows = List.copyOf(rows);
    }

    public String getName() {
        return name;
    }

    /**
     * Retrieves the difficulty rating of this level.
     * 
     * @return A string representing the difficulty (e.g., "Easy", "Medium", "Hard").
     */
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
    
    public static Level of(String name, String difficulty, String... rows) {
        List<String> values = new ArrayList<>();
        for (String row : rows) {
            values.add(row);
        }
        return new Level(name, difficulty, values);
    }
}
