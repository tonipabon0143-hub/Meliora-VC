package com.example.melioravc.command;

import com.example.melioravc.*;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class AdminVoiceStatusCommand implements CommandExecutor {
    private final VoiceManager voiceManager;

    public AdminVoiceStatusCommand(MelioraVCPlugin plugin, VoiceManager voiceManager,
                                   NameplateManager nameplateManager, PrivateGroupManager groupManager) {
        this.voiceManager = voiceManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player && !((Player) sender).isOp()) {
            sender.sendMessage("§cOP only");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /vcstatus <player>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found");
            return true;
        }
        sender.sendMessage("§eVoice state: §f" + voiceManager.getVoiceState(target.getUniqueId()));
        sender.sendMessage("§eMuted: §f" + voiceManager.isMuted(target.getUniqueId()));
        sender.sendMessage("§eDeafened: §f" + voiceManager.isDeafened(target.getUniqueId()));
        return true;
    }
}