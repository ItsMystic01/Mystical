package mys.serone.mystical.handlers;

import org.bukkit.ChatColor;

import java.awt.*;

public class Gradient {
    /**
     * @param hexColor1 : Starting hex color
     * @param hexColor2 : Ending hex color
     * @param steps : Number of steps from starting to ending hex color
     * @param currentStep : Current step at
     * @return String : Gradient Color of the String
     */
    public static String gradientColor(String hexColor1, String hexColor2, int steps, int currentStep) {

        Color color1 = Color.decode(hexColor1);
        Color color2 = Color.decode(hexColor2);
        float[] rgb1 = color1.getRGBComponents(null);
        float[] rgb2 = color2.getRGBComponents(null);
        float[] result = new float[3];

        for (int i = 0; i < 3; i++) {
            result[i] = rgb1[i] + (rgb2[i] - rgb1[i]) * currentStep / (float) steps;
        }

        Color color = new Color(result[0], result[1], result[2]);

        return String.format("#%06x", color.getRGB() & 0xFFFFFF);
    }

    /**
     * @param name : Text to gradient
     * @param startingHexColor : HEX Color Code
     * @param endingHexColor : HEX Color Code
     * @param bold : true/false to the bold text
     * @return StringBuilder displayName
     */
    public static StringBuilder displayName(String name, String startingHexColor, String endingHexColor, boolean bold) {

        StringBuilder displayName = new StringBuilder();
        int nameLength = name.length();

        for (int i = 0; i < nameLength; i++) {
            String hexColor = gradientColor(startingHexColor, endingHexColor, nameLength - 1, i);
            char c = name.charAt(i);
            displayName.append(net.md_5.bungee.api.ChatColor.of(hexColor));

            if (bold) { displayName.append(ChatColor.BOLD); }
            displayName.append(c);
        }

        return displayName;
    }
}
