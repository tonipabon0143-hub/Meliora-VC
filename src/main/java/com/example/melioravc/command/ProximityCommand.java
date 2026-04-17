package com.example.melioravc.command;

import com.example.melioravc.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class ProximityCommand implements CommandExecutor {
    public ProximityCommand(MelioraVCPlugin plugin, ProximityChatManager proximityChatManager,
                            CharacterModeManager characterModeManager, KeybindManager keybindManager) {}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            ((Player) sender).sendMessage("§eProximity chat is always active. Just type normally.");
        }
        return true;
    }
}