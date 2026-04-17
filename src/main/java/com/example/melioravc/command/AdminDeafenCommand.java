package com.example.melioravc.command;

import com.example.melioravc.*;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class AdminDeafenCommand implements CommandExecutor {
    private final VoiceManager voiceManager;

    public AdminDeafenCommand(MelioraVCPlugin plugin, VoiceManager voiceManager) {
        this.voiceManager = voiceManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && !((Player) sender).isOp()) {
            sender.sendMessage("§cOP only");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /vcdeafen <player>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found");
            return true;
        }
        if (voiceManager.isDeafened(target.getUniqueId())) {
            voiceManager.undeafenPlayer(target);
            sender.sendMessage("§aUndeafened " + target.getName());
        } else {
            voiceManager.deafenPlayer(target);
            sender.sendMessage("§cDeafened " + target.getName());
        }
        return true;
    }
}