package com.ayoree.simpleplayerscale;

import com.ayoree.simpleplayerscale.utility.ScaleClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

public class Config {

    private final Plugin plugin;
    protected final File configFile;

    public Config(SimplePlayerScale plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
    }

    public static String PREFIX = "§f[§lP§r§7layer§f§lS§r§7cale§f] §r";
    public static String MSG_CHANGE = "§aВы установили ваш размер на {scale}";
    public static String MSG_INCORRECT_ARGS = "§cОшибка синтаксиса§e - используйте §l/pscale <число>";
    public static String MSG_CONFIG_RELOAD = "§aКонфиг успешно перезагружен";
    public static String MSG_NO_PERMISSION = "§cУ вас нет прав для использования данной команды";
    public static String MSG_RG_FLAG = "§cВ данном регионе запрещено изменение размера персонажа";
    public static String MSG_OLD_VERSION = "§cРазмер персонажа можно менять на версии 1.20.5 и выше";

    public static Map<String, ScaleClass> groupScales = new HashMap<>();

    public void loadConfig() {
        plugin.saveDefaultConfig();
        loadAll();
    }

    private void loadAll() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        PREFIX = config.getString("prefix", PREFIX);
        MSG_CHANGE = config.getString("scale-change-msg", MSG_CHANGE);
        MSG_INCORRECT_ARGS = config.getString("incorrect-args-msg", MSG_INCORRECT_ARGS);
        MSG_CONFIG_RELOAD = config.getString("reload-config-msg", MSG_CONFIG_RELOAD);
        MSG_NO_PERMISSION = config.getString("no-permission-msg", MSG_NO_PERMISSION);
        MSG_RG_FLAG = config.getString("rg-flag-msg", MSG_RG_FLAG);

        groupScales.clear();
        ConfigurationSection groupsSection = config.getConfigurationSection("scales");
        if (groupsSection != null) {
            for (String group : groupsSection.getKeys(false)) {
                ScaleClass scale = new ScaleClass();
                scale.min = config.getDouble("scales." + group + ".min", 1.0);
                scale.max = config.getDouble("scales." + group + ".max", 1.0);
                groupScales.put(
                        group,
                        scale
                );
            }
        }
    }
}
