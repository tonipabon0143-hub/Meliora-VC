package com.example.melioravc;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class GlobalGroupChatManager implements Listener {
    private JavaPlugin plugin;
    private PrivateGroupManager groupManager;
    private CharacterModeManager characterModeManager;
    private VoiceManager voiceManager;

    public GlobalGroupChatManager(JavaPlugin plugin, PrivateGroupManager groupManager,
                                 CharacterModeManager characterModeManager,
                                 VoiceManager voiceManager,
                                 NameplateManager nameplateManager) {
        this.plugin = plugin;
        this.groupManager = groupManager;
        this.characterModeManager = characterModeManager;
        this.voiceManager = voiceManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (message.trim().length() == 1) return;

        PrivateGroup group = groupManager.getPlayerCurrentGroup(player.getUniqueId());
        if (group == null || !group.isVoiceEnabled() || !group.isGlobalVoiceChat()) return;

        if (voiceManager.isMuted(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage("§c✗ You are muted and cannot send messages!");
            return;
        }

        event.setCancelled(true);
        CharacterMode mode = characterModeManager.getPlayerMode(player.getUniqueId());
        String tag = mode == CharacterMode.IC ? "§a[IC]" : "§c[OOC]";
        String prefix = "§b[" + group.getGroupName() + "]§r";
        String formatted = prefix + " " + player.getName() + " " + tag + "§r: " + message;

        for (UUID memberId : group.getMemberIds()) {
            Player member = plugin.getServer().getPlayer(memberId);
            if (member != null && member.isOnline()) member.sendMessage(formatted);
        }
        group.logAction(player.getName(), "said: " + message);
    }
}