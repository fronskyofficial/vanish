package com.fronsky.vanish.logic.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IGUI {
    /**
     * Creates and returns an instance of the Inventory GUI.
     *
     * @return The Inventory GUI instance.
     */
    Inventory createGUI();

    /**
     * Opens the GUI for the specified player.
     *
     * @param player The player for whom the GUI should be opened.
     */
    void openGUI(Player player);

    /**
     * Handles a click event on the GUI slot.
     *
     * @param player The player for whom the GUI click action should be for.
     * @param slot The slot that was clicked.
     */

    void onGUIClick(Player player, int slot);
}
