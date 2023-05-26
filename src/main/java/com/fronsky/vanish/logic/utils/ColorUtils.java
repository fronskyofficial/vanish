package com.fronsky.vanish.logic.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public class ColorUtils {
    /**
     * Converts RGB values to a Color object.
     *
     * @param r the red component (0-255).
     * @param g the green component (0-255).
     * @param b the blue component (0-255).
     * @return a Color object representing the RGB values.
     */
    public static Color rgbToColor(int r, int g, int b) {
        return Color.fromRGB(r, g, b);
    }

    /**
     * Converts a Color object to RGB values.
     *
     * @param color the Color object to convert.
     * @return an integer array containing the RGB values in the order [red, green, blue].
     */
    public static int[] colorToRGB(Color color) {
        return new int[]{color.getRed(), color.getGreen(), color.getBlue()};
    }

    /**
     * Applies color formatting to the given message.
     *
     * @param message the message to be colorized.
     * @return the colorized message as a string.
     */
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Removes color formatting from the given message.
     *
     * @param message the message to be decolorized.
     * @return the decolorized message as a string.
     */
    public static String decolorize(String message) {
        return ChatColor.stripColor(message);
    }
}
