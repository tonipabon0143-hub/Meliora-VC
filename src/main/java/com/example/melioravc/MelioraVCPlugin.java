package com.example.melioravc;

import com.example.melioravc.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;
import java.util.UUID;

public class MelioraVCPlugin extends JavaPlugin implements Listener {

    private VoiceManager voiceManager;
    private NameplateManager nameplateManager;
    private PrivateGroupManager groupManager;
    private CharacterModeManager characterModeManager;
    private ProximityChatManager proximityChatManager;
    private VoiceGroupSettingsManager voiceGroupSettingsManager;
    private GuiManager guiManager;
    private KeybindManager keybindManager;
    private AutoProximityChatManager autoProximityChatManager;
    private GlobalGroupChatManager globalGroupChatManager;
    private GroupCreationListener groupCreationListener;

    private BukkitTask playerUpdateTask;
    private BukkitTask nameplateUpdateTask;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        voiceManager = new VoiceManager(this);
        nameplateManager = new NameplateManager(this, voiceManager);
        characterModeManager = new CharacterModeManager(this);
        proximityChatManager = new ProximityChatManager(this);
        groupManager = new PrivateGroupManager(this, voiceManager);
        voiceGroupSettingsManager = new VoiceGroupSettingsManager(this);
        guiManager = new GuiManager(this, groupManager, voiceGroupSettingsManager, characterModeManager);
        keybindManager = new KeybindManager(this, guiManager, voiceManager, characterModeManager, proximityChatManager);

        globalGroupChatManager = new GlobalGroupChatManager(this, groupManager, characterModeManager, voiceManager, nameplateManager);
        autoProximityChatManager = new AutoProximityChatManager(this, voiceManager, characterModeManager, keybindManager, nameplateManager, groupManager);
        groupCreationListener = new GroupCreationListener(this, groupManager, guiManager);

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(keybindManager, this);
        getServer().getPluginManager().registerEvents(globalGroupChatManager, this);
        getServer().getPluginManager().registerEvents(autoProximityChatManager, this);
        getServer().getPluginManager().registerEvents(groupCreationListener, this);

        Objects.requireNonNull(getCommand("vc")).setExecutor(new VoiceCommand(this, guiManager, groupManager, characterModeManager));
        Objects.requireNonNull(getCommand("vcgroup")).setExecutor(new GroupCommand(this, groupManager, guiManager));
        Objects.requireNonNull(getCommand("vcprox")).setExecutor(new ProximityCommand(this, proximityChatManager, characterModeManager, keybindManager));
        Objects.requireNonNull(getCommand("vcchar")).setExecutor(new CharacterCommand(characterModeManager, nameplateManager));
        Objects.requireNonNull(getCommand("vchelp")).setExecutor(new HelpCommand());

        Objects.requireNonNull(getCommand("vcmute")).setExecutor(new AdminMuteCommand(this, voiceManager));
        Objects.requireNonNull(getCommand("vcdeafen")).setExecutor(new AdminDeafenCommand(this, voiceManager));
        Objects.requireNonNull(getCommand("vcstatus")).setExecutor(new AdminVoiceStatusCommand(this, voiceManager, nameplateManager, groupManager));

        startTasks();
        getLogger().info("Meliora VC enabled");
    }

    private void startTasks() {
        playerUpdateTask = getServer().getScheduler().runTaskTimer(this, () -> {
            for (Player p : getServer().getOnlinePlayers()) voiceManager.updatePlayerPosition(p);
        }, 0L, 20L);

        nameplateUpdateTask = getServer().getScheduler().runTaskTimer(this, () -> {
            for (Player p : getServer().getOnlinePlayers()) nameplateManager.updatePlayerNameplate(p);
        }, 0L, 40L);
    }

    @Override
    public void onDisable() {
        if (playerUpdateTask != null) playerUpdateTask.cancel();
        if (nameplateUpdateTask != null) nameplateUpdateTask.cancel();
        if (keybindManager != null) keybindManager.shutdown();
        if (voiceManager != null) voiceManager.shutdown();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();
        voiceManager.registerPlayer(p);
        nameplateManager.initializeNameplate(p);
        characterModeManager.loadPlayerMode(id);
        p.sendTitle("§b§lMeliora VC", "§aPress §eV §ato open the menu!");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        voiceManager.unregisterPlayer(p);
        nameplateManager.removeNameplate(p);
        groupManager.removePlayerFromGroup(p.getUniqueId(), null);
        keybindManager.removePlayer(p.getUniqueId());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        voiceManager.updatePlayerPosition(e.getPlayer());
    }

    public VoiceManager getVoiceManager() { return voiceManager; }
    public NameplateManager getNameplateManager() { return nameplateManager; }
    public PrivateGroupManager getGroupManager() { return groupManager; }
}