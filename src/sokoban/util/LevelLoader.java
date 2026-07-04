package sokoban.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import sokoban.model.Level;

// Loads level metadata and map rows from resources/levels
public final class LevelLoader {

    private static final Path LEVEL_DIRECTORY = Path.of("resources", "levels");

    private LevelLoader() {
    }

    public static List<Level> loadLevels() {
        if (Files.isDirectory(LEVEL_DIRECTORY)) {
            try (Stream<Path> levelFiles = Files.list(LEVEL_DIRECTORY)) {
                List<Level> levels = levelFiles
                    .filter(path -> path.getFileName().toString().endsWith(".txt"))
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .map(LevelLoader::readLevelFile)
                    .toList();
                if (!levels.isEmpty()) {
                    return levels;
                }
            } catch (IOException exception) {
                // Keep the app runnable even when local level files cannot be read
            }
        }
        return defaultLevels();
    }

    private static Level readLevelFile(Path path) {
        try {
            List<String> lines = Files.readAllLines(path);
            String levelName = stripExtension(path.getFileName().toString());
            String difficulty = "Unknown";

            int startIndex = 0;
            while (startIndex < lines.size()) {
                String currentLine = lines.get(startIndex).trim();
                if (currentLine.startsWith("name=")) {
                    levelName = currentLine.substring("name=".length()).trim();
                    startIndex++;
                } else if (currentLine.startsWith("difficulty=")) {
                    difficulty = currentLine.substring("difficulty=".length()).trim();
                    startIndex++;
                } else if (currentLine.isEmpty()) {
                    startIndex++;
                } else {
                    break;
                }
            }

            List<String> rows = new ArrayList<>();
            for (int index = startIndex; index < lines.size(); index++) {
                String line = lines.get(index);
                if (!line.isEmpty()) {
                    rows.add(line);
                }
            }
            return new Level(levelName, difficulty, rows);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read level file: " + path, exception);
        }
    }

    private static List<Level> defaultLevels() {
        // These fallback levels keep the app usable before external files are ready
        return List.of(
            Level.of("Warm Up", "Easy",
                "#######",
                "#     #",
                "# .$  #",
                "#  @  #",
                "#     #",
                "#######"),
            Level.of("Corner Push", "Medium",
                "########",
                "#  .   #",
                "#  $   #",
                "#  $@  #",
                "#      #",
                "########"),
            Level.of("Two Targets", "Hard",
                "########",
                "#   .  #",
                "# $$   #",
                "#  @ . #",
                "#      #",
                "########"));
    }

    private static String stripExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex >= 0 ? filename.substring(0, dotIndex) : filename;
    }
}
