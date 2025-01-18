package com.ayoree.simpleplayerscale.utility;

import com.ayoree.simpleplayerscale.SimplePlayerScale;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class SavePlayerUtility {
    private final SimplePlayerScale plugin;
    private final Player player;

    public SavePlayerUtility(Player player, SimplePlayerScale plugin) {
        this.player = player;
        this.plugin = plugin;
    }

    public void save(double scale) {
        PlayerUtility playerUtility = new PlayerUtility(player);
        File playerDataFile = playerUtility.getPlayerDataFile();
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(playerDataFile);
        cfg.set("scale", scale);

        try {
            cfg.save(playerDataFile);
        } catch (Exception event) {
            plugin.getLogger().warning("Ошибка при сохранении файла дальности прорисовки для " + player.getName() + "\n" + event.getMessage());
        }
    }

    public void delete() {
        PlayerUtility playerUtility = new PlayerUtility(player);
        File playerDataFile = playerUtility.getPlayerDataFile();
        if (playerDataFile.exists()) {
            if (!playerDataFile.delete()) {
                plugin.getLogger().warning("Ошибка при удалении файла дальности прорисовки для " + player.getName());
            }
        }
    }
}
