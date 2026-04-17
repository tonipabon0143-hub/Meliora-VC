package com.example.melioravc;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;

public class ProximityChatManager implements Listener {
    private JavaPlugin plugin;

    public ProximityChatManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }
}