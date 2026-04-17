package com.example.melioravc.command;

import com.example.melioravc.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class VoiceCommand implements CommandExecutor {
    private final MelioraVCPlugin plugin;
    private final GuiManager guiManager;

    public VoiceCommand(MelioraVCPlugin plugin, GuiManager guiManager,
                        PrivateGroupManager groupManager, CharacterModeManager characterModeManager) {
        this.plugin = plugin;
        this.guiManager = guiManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            guiManager.openMainMenu(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            player.performCommand("vchelp");
            return true;
        }
        if (args[0].equalsIgnoreCase("status")) {
            player.sendMessage("§bMeliora VC§7 | Status: " + plugin.getVoiceManager().getVoiceState(player.getUniqueId()));
            return true;
        }
        guiManager.openMainMenu(player);
        return true;
    }
}