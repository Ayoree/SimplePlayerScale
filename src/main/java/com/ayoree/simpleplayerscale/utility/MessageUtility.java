package com.ayoree.simpleplayerscale.utility;

import com.ayoree.simpleplayerscale.Config;
import com.ayoree.simpleplayerscale.SimplePlayerScale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageUtility {
    public static void processMessage(@NotNull String msg, CommandSender commandSender) {
        SimplePlayerScale plugin = SimplePlayerScale.getPlugin(SimplePlayerScale.class);
        msg = Config.PREFIX + msg;
        if (!(commandSender instanceof Player)) {
            msg = msg.replaceAll("§.", "");
            plugin.getLogger().info(msg);
        }
        else {
            commandSender.sendMessage(msg);
        }
    }

    public static void processMessage(@NotNull String msg, Player target, double amount) {
        processMessage(replacePlaceholders(msg, amount), target);
    }

    private static String replacePlaceholders(@NotNull String msg, double amount) {
        msg = msg.replace("{scale}", String.valueOf(amount));
        return msg;
    }
}
