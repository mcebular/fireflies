package net.iamsilver.fireflies.drawable;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class Colors {

    private static Color from255(int r, int g, int b, int a) {
        return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public static Color withAlpha(Color color, int alpha) {
        return new Color(color.r, color.g, color.b, alpha / 255f);
    }

    public static Color withAlpha(Color color, float alpha) {
        return new Color(color.r, color.g, color.b, alpha);
    }

    public static final Color BACKGROUND = from255(20, 20, 40, 255);
    public static final Color LIGHT_DEFAULT = from255(255, 255, 210, 255);

    public static final Color[] lightColors = {
            Color.valueOf("#ef5350"), // Red
            // Color.valueOf("#ec407a"),
            Color.valueOf("#ab47bc"), // Purple
            // Color.valueOf("#7e57c2"),
            // Color.valueOf("#5c6bc0"),
            Color.valueOf("#42a5f5"), // Blue
            //Color.valueOf("#29b6f6"),
            Color.valueOf("#26c6da"), // Cyan
            // Color.valueOf("#26a69a"),
            Color.valueOf("#66bb6a"), // Green
            // Color.valueOf("#9ccc65"),
            // Color.valueOf("#d4e157"), // Lime
            Color.valueOf("#ffee58"), // Yellow
            // Color.valueOf("#ffca28"),
            Color.valueOf("#ffa726"), // Orange
            Color.valueOf("#ff7043"), // Deep Orange
            Color.valueOf("#ffffff"), // White
    };

    public static Color randomLightColor() {
        Random r = new Random();
        return lightColors[r.nextInt(lightColors.length)].cpy();
    }

}
