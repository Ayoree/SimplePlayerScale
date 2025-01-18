package com.ayoree.simpleplayerscale;

import com.ayoree.simpleplayerscale.commands.CommandManager;
import com.ayoree.simpleplayerscale.events.JoinLeaveEvent;
import com.ayoree.simpleplayerscale.flags.PlayerScaleFlagHandler;
import com.ayoree.simpleplayerscale.flags.PlayerScaleFlags;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.session.SessionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SimplePlayerScale extends JavaPlugin {
    public static Config config;
    public static boolean wgLoaded = false;

    @Override
    public void onLoad() {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null) {
            getLogger().info("WorldGuard не найден. Поддержка флага player-scale будет выключена.");
            wgLoaded = false;
        }
        else {
            getLogger().info("WorldGuard найден. Будет добавлен новый флаг player-scale.");
            wgLoaded = true;
            PlayerScaleFlags.initPlayerScaleFlag();
        }
    }

    @Override
    public void onEnable() {
        config = new Config(this);
        config.loadConfig();

        getServer().getPluginManager().registerEvents(new JoinLeaveEvent(this), this);

        CommandManager commandManager = new CommandManager();
        Objects.requireNonNull(getCommand("playerscale")).setExecutor(commandManager);
        Objects.requireNonNull(getCommand("playerscale")).setTabCompleter(commandManager);

        if (wgLoaded) {
            SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
            sessionManager.registerHandler(PlayerScaleFlagHandler.FACTORY(this), null);
        }

        getLogger().info("Плагин запущен.");
    }

    @Override
    public void onDisable() { getLogger().info("Плагин выключен."); }
}
