package com.example.melioravc.command;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        p.sendMessage("§bMeliora VC Help");
        p.sendMessage("§ePress V in chat to open menu");
        p.sendMessage("§eG = groups, I = IC, O = OOC, M = mute, N = deafen, Y = yell");
        p.sendMessage("§eCrouch to whisper");
        return true;
    }
}