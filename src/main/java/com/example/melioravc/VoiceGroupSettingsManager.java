package com.example.melioravc;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;

public class VoiceGroupSettingsManager {
    private JavaPlugin plugin;
    private Map<String, Object> settings;

    public VoiceGroupSettingsManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.settings = new HashMap<>();
    }

    public void setSetting(String key, Object value) {
        settings.put(key, value);
    }

    public Object getSetting(String key) {
        return settings.get(key);
    }
}