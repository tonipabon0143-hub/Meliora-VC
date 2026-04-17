package com.example.melioravc;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class PrivateGroupManager {
    private JavaPlugin plugin;
    private Map<UUID, PrivateGroup> groups;
    private Map<UUID, UUID> playerGroupMapping;
    private VoiceManager voiceManager;
    private Path groupsDirectory;

    public PrivateGroupManager(JavaPlugin plugin, VoiceManager voiceManager) {
        this.plugin = plugin;
        this.voiceManager = voiceManager;
        this.groups = new HashMap<>();
        this.playerGroupMapping = new HashMap<>();
        this.groupsDirectory = Paths.get(plugin.getDataFolder().toString(), "groups");
        try {
            Files.createDirectories(groupsDirectory);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to create groups directory");
        }
    }

    public PrivateGroup createGroup(UUID ownerId, String groupName) {
        if (groupName.isEmpty() || groupName.length() > 32) return null;
        UUID groupId = UUID.randomUUID();
        PrivateGroup group = new PrivateGroup(groupId, groupName, ownerId);
        groups.put(groupId, group);
        playerGroupMapping.put(ownerId, groupId);
        return group;
    }

    public PrivateGroup getGroup(UUID groupId) { return groups.get(groupId); }

    public List<PrivateGroup> getPlayerGroups(UUID playerId) {
        return groups.values().stream()
            .filter(g -> g.isMember(playerId))
            .collect(Collectors.toList());
    }

    public PrivateGroup getPlayerCurrentGroup(UUID playerId) {
        UUID groupId = playerGroupMapping.get(playerId);
        return groupId != null ? groups.get(groupId) : null;
    }

    public boolean addPlayerToGroup(UUID playerId, UUID groupId) {
        PrivateGroup group = groups.get(groupId);
        if (group == null) return false;

        UUID previousGroupId = playerGroupMapping.get(playerId);
        if (previousGroupId != null && !previousGroupId.equals(groupId)) {
            PrivateGroup previousGroup = groups.get(previousGroupId);
            if (previousGroup != null) previousGroup.removeMember(playerId);
        }

        if (group.addMember(playerId)) {
            playerGroupMapping.put(playerId, groupId);
            return true;
        }
        return false;
    }

    public boolean removePlayerFromGroup(UUID playerId, UUID groupId) {
        PrivateGroup group = groups.get(groupId);
        if (group == null) return false;

        if (group.removeMember(playerId)) {
            playerGroupMapping.remove(playerId);
            return true;
        }
        return false;
    }

    public boolean invitePlayerToGroup(UUID playerId, UUID invitedPlayerId, UUID groupId) {
        PrivateGroup group = groups.get(groupId);
        if (group == null || !group.isMember(playerId)) return false;
        return group.invitePlayer(invitedPlayerId);
    }

    public boolean deleteGroup(UUID playerId, UUID groupId) {
        PrivateGroup group = groups.get(groupId);
        if (group == null || !group.getOwnerId().equals(playerId)) return false;

        for (UUID memberId : group.getMemberIds()) {
            playerGroupMapping.remove(memberId);
        }
        groups.remove(groupId);
        return true;
    }

    public Map<UUID, PrivateGroup> getAllGroups() { return new HashMap<>(groups); }
}