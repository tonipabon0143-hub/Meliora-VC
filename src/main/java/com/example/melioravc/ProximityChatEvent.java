package com.example.melioravc;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import java.util.Set;

public class ProximityChatEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player sender;
    private String message;
    private ProximityChatType type;
    private Set<Player> recipients;
    private CharacterMode characterMode;

    public enum ProximityChatType {
        WHISPER(25, "🤫"),
        PROXIMITY(100, "💬"),
        YELL(250, "📢");

        private final int range;
        private final String emoji;

        ProximityChatType(int range, String emoji) {
            this.range = range;
            this.emoji = emoji;
        }

        public int getRange() { return range; }
        public String getEmoji() { return emoji; }
        public String getName() { return name(); }
    }

    public ProximityChatEvent(Player sender, String message, ProximityChatType type,
                            Set<Player> recipients, CharacterMode characterMode) {
        this.sender = sender;
        this.message = message;
        this.type = type;
        this.recipients = recipients;
        this.characterMode = characterMode;
    }

    public Player getSender() { return sender; }
    public String getMessage() { return message; }
    public ProximityChatType getType() { return type; }
    public Set<Player> getRecipients() { return recipients; }
    public CharacterMode getCharacterMode() { return characterMode; }

    @Override
    public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}