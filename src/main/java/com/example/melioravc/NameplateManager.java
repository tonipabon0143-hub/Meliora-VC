package com.example.melioravc;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class NameplateManager {
    private JavaPlugin plugin;
    private Map<UUID, NameplateData> playerNameplates;
    private VoiceManager voiceManager;

    public enum VoiceStatus {
        IDLE("✓", ChatColor.GREEN),
        SPEAKING("🎤", ChatColor.RED),
        MUTED("🔇", ChatColor.DARK_RED),
        DEAFENED("🔉", ChatColor.DARK_GRAY);

        private final String symbol;
        private final ChatColor color;

        VoiceStatus(String symbol, ChatColor color) {
            this.symbol = symbol;
            this.color = color;
        }

        public String getSymbol() { return symbol; }
        public ChatColor getColor() { return color; }
    }

    public static class NameplateData {
        public UUID playerId;
        public String playerName;
        public CharacterMode characterMode;
        public VoiceStatus voiceStatus;
        public boolean isTransmitting;

        public NameplateData(UUID playerId, String playerName) {
            this.playerId = playerId;
            this.playerName = playerName;
            this.characterMode = CharacterMode.IC;
            this.voiceStatus = VoiceStatus.IDLE;
            this.isTransmitting = false;
        }
    }

    public NameplateManager(JavaPlugin plugin, VoiceManager voiceManager) {
        this.plugin = plugin;
        this.voiceManager = voiceManager;
        this.playerNameplates = new HashMap<>();
    }

    public void initializeNameplate(Player player) {
        UUID playerId = player.getUniqueId();
        NameplateData data = new NameplateData(playerId, player.getName());
        playerNameplates.put(playerId, data);
        updatePlayerNameplate(player);
    }

    public void updatePlayerNameplate(Player player) {
        UUID playerId = player.getUniqueId();
        NameplateData data = playerNameplates.get(playerId);
        if (data == null) {
            initializeNameplate(player);
            data = playerNameplates.get(playerId);
        }
        String displayName = buildDisplayName(data);
        player.setDisplayName(displayName);
        player.setPlayerListName(displayName);
    }

    private String buildDisplayName(NameplateData data) {
        StringBuilder sb = new StringBuilder();
        if (data.characterMode == CharacterMode.IC) {
            sb.append(ChatColor.GREEN).append("[IC] ");
        } else {
            sb.append(ChatColor.YELLOW).append("[OOC] ");
        }
        sb.append(ChatColor.RESET).append(ChatColor.WHITE).append(data.playerName);
        sb.append(" ").append(data.voiceStatus.getColor()).append(data.voiceStatus.getSymbol());
        if (data.voiceStatus == VoiceStatus.MUTED) {
            sb.append(ChatColor.DARK_RED).append(" MUTED");
        } else if (data.voiceStatus == VoiceStatus.DEAFENED) {
            sb.append(ChatColor.DARK_GRAY).append(" DEAFENED");
        } else if (data.isTransmitting) {
            sb.append(ChatColor.RED).append(" ♪");
        }
        return sb.toString();
    }

    public void updateCharacterMode(Player player, CharacterMode mode) {
        UUID playerId = player.getUniqueId();
        NameplateData data = playerNameplates.get(playerId);
        if (data != null) {
            data.characterMode = mode;
            updatePlayerNameplate(player);
        }
    }

    public void updateTransmissionStatus(Player player, boolean transmitting) {
        UUID playerId = player.getUniqueId();
        NameplateData data = playerNameplates.get(playerId);
        if (data != null) {
            data.isTransmitting = transmitting;
            if (transmitting) {
                data.voiceStatus = VoiceStatus.SPEAKING;
            } else if (data.voiceStatus == VoiceStatus.SPEAKING) {
                data.voiceStatus = VoiceStatus.IDLE;
            }
            updatePlayerNameplate(player);
        }
    }

    public NameplateData getNameplateData(UUID playerId) {
        return playerNameplates.get(playerId);
    }

    public void removeNameplate(Player player) {
        playerNameplates.remove(player.getUniqueId());
    }

    public void updateAllNameplates() {
        plugin.getServer().getOnlinePlayers().forEach(this::updatePlayerNameplate);
    }
}