package com.example.melioravc;

import java.util.*;

public class PrivateGroup {
    private UUID groupId;
    private String groupName;
    private UUID ownerId;
    private Set<UUID> memberIds;
    private Set<UUID> invitedPlayers;
    private List<String> groupLog;
    private int maxMembers;
    private boolean voiceEnabled;
    private boolean globalVoiceChat;

    public enum GroupRole {
        OWNER("Owner", 4),
        MODERATOR("Moderator", 3),
        MEMBER("Member", 1),
        GUEST("Guest", 0);

        private final String displayName;
        private final int permissionLevel;

        GroupRole(String displayName, int permissionLevel) {
            this.displayName = displayName;
            this.permissionLevel = permissionLevel;
        }

        public String getDisplayName() { return displayName; }
        public int getPermissionLevel() { return permissionLevel; }
    }

    public PrivateGroup(UUID groupId, String groupName, UUID ownerId) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.ownerId = ownerId;
        this.memberIds = new HashSet<>();
        this.memberIds.add(ownerId);
        this.invitedPlayers = new HashSet<>();
        this.groupLog = new ArrayList<>();
        this.maxMembers = 50;
        this.voiceEnabled = true;
        this.globalVoiceChat = true;
    }

    public UUID getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }
    public UUID getOwnerId() { return ownerId; }
    public Set<UUID> getMemberIds() { return new HashSet<>(memberIds); }
    public int getMemberCount() { return memberIds.size(); }
    public boolean isVoiceEnabled() { return voiceEnabled; }
    public boolean isGlobalVoiceChat() { return globalVoiceChat; }
    public Set<UUID> getInvitedPlayers() { return new HashSet<>(invitedPlayers); }

    public void setGroupName(String name) { this.groupName = name; }
    public void setVoiceEnabled(boolean enabled) { this.voiceEnabled = enabled; }
    public void setGlobalVoiceChat(boolean global) { this.globalVoiceChat = global; }

    public boolean addMember(UUID playerId) {
        if (memberIds.size() >= maxMembers) return false;
        return memberIds.add(playerId);
    }

    public boolean removeMember(UUID playerId) {
        if (playerId.equals(ownerId)) return false;
        return memberIds.remove(playerId);
    }

    public boolean invitePlayer(UUID playerId) {
        if (!memberIds.contains(playerId)) {
            invitedPlayers.add(playerId);
            return true;
        }
        return false;
    }

    public boolean isMember(UUID playerId) { return memberIds.contains(playerId); }
    public boolean isInvited(UUID playerId) { return invitedPlayers.contains(playerId); }

    public void removeInvitation(UUID playerId) { invitedPlayers.remove(playerId); }
    public void logAction(String playerId, String action) {
        groupLog.add("[" + new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date()) + "] " + playerId + " " + action);
        if (groupLog.size() > 100) groupLog.remove(0);
    }
}