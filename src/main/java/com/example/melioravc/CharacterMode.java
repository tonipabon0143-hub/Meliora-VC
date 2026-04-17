package com.example.melioravc;

public enum CharacterMode {
    IC("In-Character", "🎭"),
    OOC("Out-of-Character", "💬");

    private final String displayName;
    private final String emoji;

    CharacterMode(String displayName, String emoji) {
        this.displayName = displayName;
        this.emoji = emoji;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmoji() {
        return emoji;
    }
}