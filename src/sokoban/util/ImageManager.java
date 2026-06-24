package sokoban.util;

import javafx.scene.paint.Color;

// Central place for board colors
public final class ImageManager {

    private ImageManager() {
    }

    public static Color wallColor() {
        return Color.web("#334155");
    }

    public static Color floorColor() {
        return Color.web("#E7E5E4");
    }

    public static Color targetColor() {
        return Color.web("#FACC15");
    }

    public static Color boxColor() {
        return Color.web("#B45309");
    }

    public static Color playerColor() {
        return Color.web("#2563EB");
    }
}
