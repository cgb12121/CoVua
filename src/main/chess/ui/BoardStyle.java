package main.chess.ui;

import java.awt.Color;

public class BoardStyle {
    public static final Color[][] CLASSIC = {
            {Color.WHITE, Color.BLACK},
            {Color.BLACK, Color.WHITE}
    };

    public static final Color[][] BROWN = {
            {new Color(210, 180, 140), new Color(139, 69, 19)},
            {new Color(139, 69, 19), new Color(210, 180, 140)}
    };

    public static final Color[][] GRAY_WHITE = {
            {Color.LIGHT_GRAY, Color.WHITE},
            {Color.WHITE, Color.LIGHT_GRAY}
    };
}

