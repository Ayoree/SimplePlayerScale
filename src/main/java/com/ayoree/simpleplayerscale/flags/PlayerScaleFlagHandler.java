package com.ayoree.simpleplayerscale.flags;

import com.ayoree.simpleplayerscale.Config;
import com.ayoree.simpleplayerscale.utility.PlayerUtility;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.session.handler.FlagValueChangeHandler;
import com.sk89q.worldguard.session.handler.Handler;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;


import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlayerScaleFlagHandler extends FlagValueChangeHandler<StateFlag.State>
{
    public static Factory FACTORY(Plugin plugin)
    {
        return new Factory(plugin);
    }

    @Override
    protected void onInitialValue(LocalPlayer localPlayer, ApplicableRegionSet applicableRegionSet, StateFlag.State state) {

    }

    @Override
    protected boolean onSetValue(LocalPlayer localPlayer, Location location, Location location1, ApplicableRegionSet applicableRegionSet, StateFlag.State state, StateFlag.State t1, MoveType moveType) {
        if (state != null && !StateFlag.test(state))
        {
            Player player = BukkitAdapter.adapt(localPlayer);
            if (player != null) {
                if (PlayerUtility.getPlayerScale(player) != 1.0) {
                    PlayerUtility.setScale(player, 1.0);
                    player.sendMessage(Config.MSG_RG_FLAG);
                    PlayerUtility.remove(player);
                }
            }
        }
        return true;
    }

    @Override
    protected boolean onAbsentValue(LocalPlayer localPlayer, Location location, Location location1, ApplicableRegionSet applicableRegionSet, StateFlag.State state, MoveType moveType) {
        return true;
    }

    public static class Factory extends Handler.Factory<PlayerScaleFlagHandler>
    {
        private final Plugin plugin;

        public Factory(Plugin plugin)
        {
            this.plugin = plugin;
        }

        @Override
        public PlayerScaleFlagHandler create(Session session)
        {
            return new PlayerScaleFlagHandler(this.plugin, session);
        }
    }

    protected PlayerScaleFlagHandler(Plugin plugin, Session session)
    {
        super(session, PlayerScaleFlags.playerScaleFlag);
    }
}