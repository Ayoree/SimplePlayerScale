package com.ayoree.simpleplayerscale.events;

import com.ayoree.simpleplayerscale.SimplePlayerScale;
import com.ayoree.simpleplayerscale.utility.*;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.Objects;

public class JoinLeaveEvent implements Listener {

    private final SimplePlayerScale plugin;

    public JoinLeaveEvent(SimplePlayerScale plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        double scale;

        PlayerUtility playerUtility = new PlayerUtility(e.getPlayer());
        File playerDataFile = playerUtility.getPlayerDataFile();

        if (playerDataFile.exists()) {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(playerDataFile);
            scale = cfg.getDouble("scale", playerUtility.getMaxPlayerScale());
            Player player = e.getPlayer();
            if (Bukkit.getPluginManager().getPlugin("ViaVersion")  != null) {
                if (PlayerUtility.getPlayerProtocolVersion(player) < Objects.requireNonNull(ProtocolVersion.getClosest("1.20.5")).getVersion()) {
                    return;
                }
            }
            PlayerUtility.setScale(player, scale);
        }
        else {
            scale = 1.0;
            PlayerUtility.save(e.getPlayer(), scale);
        }
    }


    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PlayerUtility playerUtility = new PlayerUtility(player);
        SavePlayerUtility saveUtility = new SavePlayerUtility(player, plugin);
        double scale = playerUtility.getPlayerScale();

        if (scale == 1.0) {
            saveUtility.delete();
        }
        else {
            saveUtility.save(scale);
        }

        PlayerUtility.remove(player);
    }
}
