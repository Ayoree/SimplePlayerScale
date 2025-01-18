package com.ayoree.simpleplayerscale.utility;

import com.ayoree.simpleplayerscale.SimplePlayerScale;
import com.ayoree.simpleplayerscale.Config;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class PlayerUtility {

    private final Player player;
    private static final Map<String, Double> PlayerDataHandlerMap = new HashMap<>();
    private static final ClampUtility clampUtility = new ClampUtility();

    public PlayerUtility(Player player) {
        this.player = player;
    }

    public double getPlayerScale() {
        double scale  = PlayerDataHandlerMap.getOrDefault(player.getName().toLowerCase(), 1.0);
        return clampUtility.clampScale(scale);
    }

    public static double getPlayerScale(Player player) {
        AttributeInstance scale = player.getAttribute(Attribute.GENERIC_SCALE);
        if (scale != null) {
            return scale.getBaseValue();
        }
        return 1.0;
    }

    public double getMaxPlayerScale() {
        return getMaxPlayerScale(true);
    }

    public double getMinPlayerScale() {
        return getMinPlayerScale(true);
    }

    public double calculatePlayerScale(double scale) {
        scale = clampUtility.clampScale(scale);
        scale = Math.min(scale, getMaxPlayerScale(false));
        scale = Math.max(scale, getMinPlayerScale(false));
        return scale;
    }

    private double getMaxPlayerScale(boolean clamp) {
        double scale = 1.0;
        for(String group : getPlayerGroups()) {
            ScaleClass groupScale = Config.groupScales.get(group);
            scale = groupScale != null ? Math.max(scale, groupScale.max) : scale;
        }
        if (clamp) return clampUtility.clampScale(scale);
        else return scale;
    }

    private double getMinPlayerScale(boolean clamp) {
        double scale = 1.0;
        for(String group : getPlayerGroups()) {
            ScaleClass groupScale = Config.groupScales.get(group);
            scale = groupScale != null ? Math.min(scale, groupScale.min) : scale;
        }
        if (clamp) return clampUtility.clampScale(scale);
        else return scale;
    }

    public File getPlayerDataFile() {
        File dataFolder = SimplePlayerScale.getPlugin(SimplePlayerScale.class).getDataFolder();
        return new File(dataFolder, "players/" + player.getName().toLowerCase() + ".yml");
    }

    private List<String> getPlayerGroups() {
        List<String> lst = new ArrayList<>();
        Config.groupScales.forEach((String group, ScaleClass scale) ->  {
            if (player.hasPermission("group." + group)) {
                lst.add(group);
            }
        });
        return lst;
    }

    public static void setScale(Player player, double newScale) {
        AttributeInstance scale = player.getAttribute(Attribute.GENERIC_SCALE);
        if (scale == null) return;
        scale.setBaseValue(newScale);
        save(player, newScale);
    }

    public static void save(Player player, double scale) {
        PlayerDataHandlerMap.put(player.getName().toLowerCase(), scale);
    }

    public static void remove(Player player) {
        PlayerDataHandlerMap.remove(player.getName().toLowerCase());
    }

    public static int getPlayerProtocolVersion(Player player) {
        UserConnection userConnection = Via.getManager().getConnectionManager().getConnectedClient(player.getUniqueId());
        if (userConnection == null) {
            return 0;
        }
        return userConnection.getProtocolInfo().protocolVersion().getVersion();
    }
}
