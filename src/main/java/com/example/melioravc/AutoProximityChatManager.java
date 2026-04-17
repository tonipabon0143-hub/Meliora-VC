package com.example.melioravc;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Set;

public class AutoProximityChatManager implements Listener {
    private JavaPlugin plugin;
    private VoiceManager voiceManager;
    private CharacterModeManager characterModeManager;
    private KeybindManager keybindManager;
    private NameplateManager nameplateManager;
    private PrivateGroupManager groupManager;

    public AutoProximityChatManager(JavaPlugin plugin, VoiceManager voiceManager,
                                   CharacterModeManager characterModeManager,
                                   KeybindManager keybindManager,
                                   NameplateManager nameplateManager,
                                   PrivateGroupManager groupManager) {
        this.plugin = plugin;
        this.voiceManager = voiceManager;
        this.characterModeManager = characterModeManager;
        this.keybindManager = keybindManager;
        this.nameplateManager = nameplateManager;
        this.groupManager = groupManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        // Let keybind manager handle single-letter binds
        if (message.trim().length() == 1) return;

        // If player in a group with global chat, let GlobalGroupChatManager handle it
        PrivateGroup group = groupManager.getPlayerCurrentGroup(player.getUniqueId());
        if (group != null && group.isVoiceEnabled() && group.isGlobalVoiceChat()) return;

        if (voiceManager.isMuted(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage("§c✗ You are muted and cannot send messages!");
            return;
        }

        event.setCancelled(true);
        CharacterMode mode = characterModeManager.getPlayerMode(player.getUniqueId());

        ProximityChatEvent.ProximityChatType type = ProximityChatEvent.ProximityChatType.PROXIMITY;
        if (keybindManager.isYellMode(player.getUniqueId())) type = ProximityChatEvent.ProximityChatType.YELL;
        if (keybindManager.isWhisperMode(player.getUniqueId())) type = ProximityChatEvent.ProximityChatType.WHISPER;

        Set<Player> recipients = voiceManager.getPlayersInRange(player, type.getRange());
        ProximityChatEvent proxEvent = new ProximityChatEvent(player, message, type, recipients, mode);
        plugin.getServer().getScheduler().runTask(plugin, () -> plugin.getServer().getPluginManager().callEvent(proxEvent));

        String tag = mode == CharacterMode.IC ? "§a[IC]" : "§c[OOC]";
        String formatted = type.getEmoji() + " " + player.getName() + " " + tag + "§r: " + message;
        recipients.forEach(p -> p.sendMessage(formatted));
        player.sendMessage("§7[" + type.getName() + "]§r " + formatted);
    }
}