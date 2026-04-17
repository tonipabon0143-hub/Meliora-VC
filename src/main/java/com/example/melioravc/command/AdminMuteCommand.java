package com.example.melioravc.command;

import com.example.melioravc.*;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class AdminMuteCommand implements CommandExecutor {
    private final MelioraVCPlugin plugin;
    private final VoiceManager voiceManager;

    public AdminMuteCommand(MelioraVCPlugin plugin, VoiceManager voiceManager) {
        this.plugin = plugin;
        this.voiceManager = voiceManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && !((Player) sender).isOp()) {
            sender.sendMessage("§cOP only");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /vcmute <player>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found");
            return true;
        }
        if (voiceManager.isMuted(target.getUniqueId())) {
            voiceManager.unmutePlayer(target);
            sender.sendMessage("§aUnmuted " + target.getName());
            target.sendMessage("§aYou were unmuted by an admin");
        } else {
            voiceManager.mutePlayer(target);
            sender.sendMessage("§cMuted " + target.getName());
            target.sendMessage("§cYou were muted by an admin");
        }
        return true;
    }
}