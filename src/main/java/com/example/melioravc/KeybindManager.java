package com.example.melioravc;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.util.*;

public class KeybindManager implements Listener {
    private JavaPlugin plugin;
    private GuiManager guiManager;
    private VoiceManager voiceManager;
    private CharacterModeManager characterModeManager;
    private Set<UUID> crouchingPlayers;
    private Set<UUID> yellingPlayers;

    public KeybindManager(JavaPlugin plugin, GuiManager guiManager, VoiceManager voiceManager,
                         CharacterModeManager characterModeManager, ProximityChatManager proximityChatManager) {
        this.plugin = plugin;
        this.guiManager = guiManager;
        this.voiceManager = voiceManager;
        this.characterModeManager = characterModeManager;
        this.crouchingPlayers = Collections.synchronizedSet(new HashSet<>());
        this.yellingPlayers = Collections.synchronizedSet(new HashSet<>());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage().trim();
        if (message.length() == 1) {
            char key = message.toLowerCase().charAt(0);
            event.setCancelled(true);
            Player player = event.getPlayer();

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                switch (key) {
                    case 'v':
                        guiManager.openMainMenu(player);
                        break;
                    case 'g':
                        guiManager.openGroupsMenu(player);
                        break;
                    case 'i':
                        characterModeManager.setPlayerMode(player.getUniqueId(), CharacterMode.IC);
                        player.sendActionBar("§a🎭 §fCharacter Mode: IC");
                        break;
                    case 'o':
                        characterModeManager.setPlayerMode(player.getUniqueId(), CharacterMode.OOC);
                        player.sendActionBar("§c💬 §fCharacter Mode: OOC");
                        break;
                    case 'm':
                        if (voiceManager.isMuted(player.getUniqueId())) {
                            voiceManager.unmutePlayer(player);
                            player.sendActionBar("§a✓ §fMicrophone Unmuted");
                        } else {
                            voiceManager.mutePlayer(player);
                            player.sendActionBar("§c✗ §fMicrophone Muted");
                        }
                        break;
                    case 'n':
                        if (voiceManager.isDeafened(player.getUniqueId())) {
                            voiceManager.undeafenPlayer(player);
                            player.sendActionBar("§a✓ §fDeafened Disabled");
                        } else {
                            voiceManager.deafenPlayer(player);
                            player.sendActionBar("§c✗ §fDeafened Enabled");
                        }
                        break;
                    case 'y':
                        if (yellingPlayers.contains(player.getUniqueId())) {
                            yellingPlayers.remove(player.getUniqueId());
                            player.sendActionBar("§a💬 §fProximity Mode Active");
                        } else {
                            yellingPlayers.add(player.getUniqueId());
                            player.sendActionBar("§6📢 §fYell Mode Active");
                        }
                        break;
                }
            });
        }
    }

    public boolean isWhisperMode(UUID playerId) {
        return crouchingPlayers.contains(playerId);
    }

    public boolean isYellMode(UUID playerId) {
        return yellingPlayers.contains(playerId);
    }

    public void shutdown() {
        crouchingPlayers.clear();
        yellingPlayers.clear();
    }

    public void removePlayer(UUID playerId) {
        crouchingPlayers.remove(playerId);
        yellingPlayers.remove(playerId);
    }
}