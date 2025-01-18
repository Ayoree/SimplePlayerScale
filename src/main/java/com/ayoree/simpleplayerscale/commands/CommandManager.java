package com.ayoree.simpleplayerscale.commands;

import com.ayoree.simpleplayerscale.SimplePlayerScale;
import com.ayoree.simpleplayerscale.Config;
import com.ayoree.simpleplayerscale.flags.PlayerScaleFlags;
import com.ayoree.simpleplayerscale.utility.*;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandManager implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (args.length > 0) {
            if (args[0].equals("reload")) {
                if (sender.hasPermission("playerscale.reload")) {
                    SimplePlayerScale.config.loadConfig();
                    MessageUtility.processMessage(Config.MSG_CONFIG_RELOAD, sender);
                } else {
                    MessageUtility.processMessage(Config.MSG_NO_PERMISSION, sender);
                }
            } else try {
                if (sender instanceof Player player) {
                    if (Bukkit.getPluginManager().getPlugin("ViaVersion")  != null) {
                        if (PlayerUtility.getPlayerProtocolVersion(player) < Objects.requireNonNull(ProtocolVersion.getClosest("1.20.5")).getVersion()) {
                            player.sendMessage(Config.MSG_OLD_VERSION);
                            return true;
                        }
                    }
                    Location loc = new com.sk89q.worldedit.util.Location(BukkitAdapter.adapt(player.getWorld()), player.getX(), player.getY(), player.getZ());
                    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    RegionQuery query = container.createQuery();
                    ApplicableRegionSet set = query.getApplicableRegions(loc);
                    for (ProtectedRegion region : set) {
                        StateFlag.State flag = region.getFlag(PlayerScaleFlags.playerScaleFlag);
                        if (flag != null && !StateFlag.test(flag))
                        {
                            player.sendMessage(Config.MSG_RG_FLAG);
                            return true;
                        }
                    }
                    double scale = Double.parseDouble(args[0]);
                    PlayerUtility playerUtility = new PlayerUtility(player);
                    scale = playerUtility.calculatePlayerScale(scale);
                    if (sender.hasPermission("playerscale.set")) {
                        MessageUtility.processMessage(Config.MSG_CHANGE, player, scale);
                        PlayerUtility.setScale(player, scale);
                    } else {
                        MessageUtility.processMessage(Config.MSG_NO_PERMISSION, sender);
                    }
                }
            } catch (Exception e) {
                MessageUtility.processMessage(Config.MSG_INCORRECT_ARGS, sender);
            }
        }
        else {
            MessageUtility.processMessage(Config.MSG_INCORRECT_ARGS, sender);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        ArrayList<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("playerscale.reload") && "reload".startsWith(args[0])) {
                suggestions.add("reload");
            }
            if (sender instanceof Player player) {
                PlayerUtility playerUtility = new PlayerUtility(player);
                double maxPlayerScale = playerUtility.getMaxPlayerScale();
                double minPlayerScale = playerUtility.getMinPlayerScale();
                for (int i = (int)(minPlayerScale * 10); i <= maxPlayerScale * 10; i++) {
                    suggestions.add(i / 10 + "." + i % 10);
                }
            }
            suggestions.removeIf((String suggestion) -> !suggestion.startsWith(args[0]));
        }
        return suggestions;
    }
}
