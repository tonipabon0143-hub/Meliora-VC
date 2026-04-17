package com.example.melioravc;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GroupCreationListener implements Listener {
    private JavaPlugin plugin;
    private PrivateGroupManager groupManager;
    private GuiManager guiManager;

    public GroupCreationListener(JavaPlugin plugin, PrivateGroupManager groupManager, GuiManager guiManager) {
        this.plugin = plugin;
        this.groupManager = groupManager;
        this.guiManager = guiManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().trim();

        // If player is in the create-group GUI, treat their next chat as group name
        if (player.getOpenInventory() != null && "§b§lCreate New Group".equals(player.getOpenInventory().getTitle())) {
            event.setCancelled(true);
            if (message.length() < 1 || message.length() > 32) {
                player.sendMessage("§c✗ Group name must be 1-32 characters");
                return;
            }
            PrivateGroup group = groupManager.createGroup(player.getUniqueId(), message);
            if (group == null) {
                player.sendMessage("§c✗ Failed to create group");
                return;
            }
            player.sendMessage("§a✓ Group created: §f" + message);
            plugin.getServer().getScheduler().runTask(plugin, () -> guiManager.openGroupsMenu(player));
        }
    }
}