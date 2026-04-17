package com.example.melioravc.command;

import com.example.melioravc.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class GroupCommand implements CommandExecutor {
    private final GuiManager guiManager;

    public GroupCommand(MelioraVCPlugin plugin, PrivateGroupManager groupManager, GuiManager guiManager) {
        this.guiManager = guiManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        guiManager.openGroupsMenu((Player) sender);
        return true;
    }
}