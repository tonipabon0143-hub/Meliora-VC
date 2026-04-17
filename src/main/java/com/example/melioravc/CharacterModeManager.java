package com.example.melioravc;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CharacterModeManager {
    private JavaPlugin plugin;
    private Map<UUID, CharacterMode> playerModes;
    private Path modesDirectory;

    public CharacterModeManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.playerModes = new HashMap<>();
        this.modesDirectory = Paths.get(plugin.getDataFolder().toString(), "modes");
        try {
            Files.createDirectories(modesDirectory);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to create modes directory");
        }
    }

    public void setPlayerMode(UUID playerId, CharacterMode mode) {
        playerModes.put(playerId, mode);
    }

    public CharacterMode getPlayerMode(UUID playerId) {
        return playerModes.getOrDefault(playerId, CharacterMode.IC);
    }

    public void loadPlayerMode(UUID playerId) {
        Path filePath = modesDirectory.resolve(playerId + ".txt");
        try {
            if (Files.exists(filePath)) {
                String content = new String(Files.readAllBytes(filePath)).trim();
                CharacterMode mode = CharacterMode.valueOf(content);
                playerModes.put(playerId, mode);
            }
        } catch (Exception e) {
            playerModes.put(playerId, CharacterMode.IC);
        }
    }

    public void savePlayerMode(UUID playerId, CharacterMode mode) {
        Path filePath = modesDirectory.resolve(playerId + ".txt");
        try {
            Files.write(filePath, mode.name().getBytes());
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save player mode for " + playerId);
        }
    }
}