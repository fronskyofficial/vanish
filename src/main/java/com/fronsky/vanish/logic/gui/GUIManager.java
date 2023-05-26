package com.fronsky.vanish.logic.gui;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIManager {
    private final Map<UUID, IGUI> guis = new HashMap<>();

    public void addGUI(UUID id, IGUI gui) {
        gui.createGUI();
        guis.put(id, gui);
    }

    public void openGUI(UUID id, Player player) {
        if (guis.containsKey(id)) {
            guis.get(id).openGUI(player);
        }
    }

    public void onGUIClick(UUID id, Player player, int slot) {
        if (guis.containsKey(id)) {
            guis.get(id).onGUIClick(player, slot);
        }
    }

    public void removeGUI(UUID id) {
        guis.remove(id);
    }
}
