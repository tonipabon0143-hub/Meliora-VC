package com.example.melioravc.command;

import com.example.melioravc.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class CharacterCommand implements CommandExecutor {
    private final CharacterModeManager characterModeManager;
    private final NameplateManager nameplateManager;

    public CharacterCommand(CharacterModeManager characterModeManager, NameplateManager nameplateManager) {
        this.characterModeManager = characterModeManager;
        this.nameplateManager = nameplateManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        if (args.length == 0) {
            p.sendMessage("§eCurrent: " + characterModeManager.getPlayerMode(p.getUniqueId()));
            return true;
        }
        if (args[0].equalsIgnoreCase("ic")) {
            characterModeManager.setPlayerMode(p.getUniqueId(), CharacterMode.IC);
            nameplateManager.updateCharacterMode(p, CharacterMode.IC);
        } else if (args[0].equalsIgnoreCase("ooc")) {
            characterModeManager.setPlayerMode(p.getUniqueId(), CharacterMode.OOC);
            nameplateManager.updateCharacterMode(p, CharacterMode.OOC);
        }
        return true;
    }
}