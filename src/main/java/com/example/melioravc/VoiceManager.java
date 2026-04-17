package com.example.melioravc;

import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class VoiceManager {
    private JavaPlugin plugin;
    private Map<UUID, PlayerVoiceSession> activeSessions;
    private Map<UUID, VoiceState> playerVoiceStates;

    public enum VoiceState {
        IDLE, TRANSMITTING, RECEIVING, MUTED, DEAFENED
    }

    public static class PlayerVoiceSession {
        public UUID playerId;
        public String playerName;
        public Location lastPosition;
        public boolean isMuted;
        public boolean isDeafened;
        public boolean isTransmitting;
        public long lastTransmissionTime;
        public int voiceRange;

        public PlayerVoiceSession(Player player) {
            this.playerId = player.getUniqueId();
            this.playerName = player.getName();
            this.lastPosition = player.getLocation();
            this.isMuted = false;
            this.isDeafened = false;
            this.isTransmitting = false;
            this.lastTransmissionTime = System.currentTimeMillis();
            this.voiceRange = 100;
        }
    }

    public VoiceManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.activeSessions = new HashMap<>();
        this.playerVoiceStates = new HashMap<>();
    }

    public void registerPlayer(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerVoiceSession session = new PlayerVoiceSession(player);
        activeSessions.put(playerId, session);
        playerVoiceStates.put(playerId, VoiceState.IDLE);
        plugin.getLogger().info("Registered player for voice: " + player.getName());
    }

    public void unregisterPlayer(Player player) {
        activeSessions.remove(player.getUniqueId());
        playerVoiceStates.remove(player.getUniqueId());
    }

    public void updatePlayerPosition(Player player) {
        PlayerVoiceSession session = activeSessions.get(player.getUniqueId());
        if (session != null) {
            session.lastPosition = player.getLocation();
        }
    }

    public Set<Player> getPlayersInRange(Player speaker, int range) {
        Set<Player> nearbyPlayers = new HashSet<>();
        Location speakerLocation = speaker.getLocation();

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.getUniqueId().equals(speaker.getUniqueId())) continue;
            if (!player.getWorld().equals(speaker.getWorld())) continue;

            double distance = speakerLocation.distance(player.getLocation());
            if (distance <= range) {
                nearbyPlayers.add(player);
            }
        }
        return nearbyPlayers;
    }

    public void startTransmission(Player player) {
        PlayerVoiceSession session = activeSessions.get(player.getUniqueId());
        if (session != null && !session.isMuted) {
            session.isTransmitting = true;
            playerVoiceStates.put(player.getUniqueId(), VoiceState.TRANSMITTING);
        }
    }

    public void endTransmission(Player player) {
        PlayerVoiceSession session = activeSessions.get(player.getUniqueId());
        if (session != null) {
            session.isTransmitting = false;
            playerVoiceStates.put(player.getUniqueId(), VoiceState.IDLE);
        }
    }

    public void mutePlayer(Player player) {
        PlayerVoiceSession session = activeSessions.get(player.getUniqueId());
        if (session != null) {
            session.isMuted = true;
            session.isTransmitting = false;
            playerVoiceStates.put(player.getUniqueId(), VoiceState.MUTED);
        }
    }

    public void unmutePlayer(Player player) {
        PlayerVoiceSession session = activeSessions.get(player.getUniqueId());
        if (session != null) {
            session.isMuted = false;
            playerVoiceStates.put(player.getUniqueId(), VoiceState.IDLE);
        }
    }

    public void deafenPlayer(Player player) {
        PlayerVoiceSession session = activeSessions.get(player.getUniqueId());
        if (session != null) {
            session.isDeafened = true;
            playerVoiceStates.put(player.getUniqueId(), VoiceState.DEAFENED);
        }
    }

    public void undeafenPlayer(Player player) {
        PlayerVoiceSession session = activeSessions.get(player.getUniqueId());
        if (session != null) {
            session.isDeafened = false;
            playerVoiceStates.put(player.getUniqueId(), VoiceState.IDLE);
        }
    }

    public boolean isTransmitting(UUID playerId) {
        PlayerVoiceSession session = activeSessions.get(playerId);
        return session != null && session.isTransmitting;
    }

    public boolean isMuted(UUID playerId) {
        PlayerVoiceSession session = activeSessions.get(playerId);
        return session != null && session.isMuted;
    }

    public boolean isDeafened(UUID playerId) {
        PlayerVoiceSession session = activeSessions.get(playerId);
        return session != null && session.isDeafened;
    }

    public VoiceState getVoiceState(UUID playerId) {
        return playerVoiceStates.getOrDefault(playerId, VoiceState.IDLE);
    }

    public PlayerVoiceSession getSession(UUID playerId) {
        return activeSessions.get(playerId);
    }

    public Map<UUID, PlayerVoiceSession> getActiveSessions() {
        return new HashMap<>(activeSessions);
    }

    public void shutdown() {
        activeSessions.clear();
        playerVoiceStates.clear();
    }
}